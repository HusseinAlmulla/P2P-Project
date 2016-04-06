package hk.edu.polyu.P2pMobileApp;

import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import hk.edu.polyu.P2pMobileApp.json.Transaction;
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class MoneyTransferFragment extends Fragment implements OnItemSelectedListener, OnClickListener, AsyncTaskCallback {
	private static final String TAG = "MoneyTransferFragment";
	
	private ProgressDialog progress;
	
	MainActivity mainActivity;
	Context mContext;
	
	EditText editTextMoneyTransferAmount;
	Spinner spinnerCurrencyList;
	Spinner spinnerFriendList;
	EditText editTextMoneyTransferMessage;
	Button buttonMoneyTransfer;
	
	public MoneyTransferFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		this.mContext = mainActivity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_money_transfer, container, false);
		
		editTextMoneyTransferAmount = (EditText) rootView.findViewById(R.id.editTextMoneyTransferAmount);
		spinnerCurrencyList = (Spinner) rootView.findViewById(R.id.spinnerCurrencyList);
		spinnerFriendList = (Spinner) rootView.findViewById(R.id.spinnerFirendList);
		editTextMoneyTransferMessage = (EditText) rootView.findViewById(R.id.editTextMoneyTransferMessage);
		buttonMoneyTransfer = (Button) rootView.findViewById(R.id.buttonMoneyTransfer);
		
		ArrayAdapter<CharSequence>  currentListAdapter = new ArrayAdapter<CharSequence> (mainActivity, android.R.layout.simple_spinner_item, new String[] {"HKD", "CNY", "USD", "GBP", "EUR"});
		currentListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCurrencyList.setAdapter(currentListAdapter);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> friendListAdapter = new ArrayAdapter<CharSequence> (mainActivity, android.R.layout.simple_spinner_item, getSavedP2pContacts());
		// Specify the layout to use when the list of choices appears
		friendListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinnerFriendList.setAdapter(friendListAdapter);
		
		spinnerFriendList.setOnItemSelectedListener(this);
		buttonMoneyTransfer.setOnClickListener(this);
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		//Toast.makeText(mainActivity, "You Clicked "+v.getId(), Toast.LENGTH_LONG).show();
		if (v instanceof Button) {
			String sender = ((GlobalClass) this.getActivity().getApplication()).getMobilePhone();
			
			//check if any field is null or empty
			String currency = (String)spinnerCurrencyList.getSelectedItem();
			String amount = editTextMoneyTransferAmount.getText().toString();
			String recipient = (String)spinnerFriendList.getSelectedItem();
			String message = editTextMoneyTransferMessage.getText().toString();
			
			Log.d(TAG, "curreny: " + currency);
			Log.d(TAG, "amount: " + amount);
			Log.d(TAG, "recipient: " + recipient);
			Log.d(TAG, "message: " + message);
			
			if (sender!=null && !sender.equals("") && 
				currency!=null && !currency.equals("") && 
				amount!=null && !amount.equals("") && 
				recipient!=null && !recipient.equals("")
			) {
				// parse the phone number from recipient info 
				String delim = " tel: ";
				recipient = recipient.substring(recipient.indexOf(delim)+delim.length(), recipient.length());
				
				// all mandatory fields are ready, we are good to go
	        	progress = ProgressDialog.show(this.getActivity(), "Connecting", "Please wait...", true);
	        	
	        	//trigger network request
	        	new ConnectWebServiceTask(this.getActivity().getApplicationContext(), this).execute(
	        			getString(R.string.webservice_protocol),
	        			getString(R.string.webservice_url),
	        			getString(R.string.webservice_port),
	        			getString(R.string.webservice_send_money), 
	        			sender, 
	        			currency, 
	        			amount, 
	        			recipient, 
	        			message);
			}
			
			if (recipient==null) {
		if (v instanceof Button) {
			
			String recipientPhone = (String)spinnerFriendList.getSelectedItem();
			if (recipientPhone==null) {
				// no recipient found from P2P address book
	            // reminder user to create
	            new AlertDialog.Builder(this.getActivity())
					.setTitle("Error").setMessage("P2P address book is empty!\nPlease use the contact list feature to add recipients.").setCancelable(true)
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create().show();
			}
			
			ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				// for online only - immediately submit the transaction to server
				sendMoney();
			} else {
				Log.d(TAG, "device currently offline, saving transactions to local cache and delay the transfer until device comes online again");
				// for offline only - save transactions to local cache 
				cacheTransaction();
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		//Toast.makeText(mainActivity, "Selected "+parent.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		//Toast.makeText(mainActivity, "onNothingSelected", Toast.LENGTH_LONG).show();
	}

	protected String[] getSavedP2pContacts() {
		SharedPreferences prefs = mainActivity.getSharedPreferences(getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			Map<String,?> keys = prefs.getAll();
			
			if (keys!=null || !keys.isEmpty()) {
				ArrayList<String> tmp = new ArrayList<String>();
				
				for (Map.Entry<String, ?> entry : keys.entrySet()) {
					String phone = entry.getKey();
					String name = entry.getValue().toString();
					Log.d(TAG, "@@ phone: " + phone+ ", name: " + name);
					tmp.add(name + " tel: " + phone);
				}
				
				// convert the ArrayList to Array
				String[] p2pContacts = new String[tmp.size()];
				p2pContacts = tmp.toArray(p2pContacts);
				return p2pContacts;
			}
		}
		
		return null;
	}
	}

	protected String[] getSavedP2pContacts() {
		SharedPreferences prefs = mainActivity.getSharedPreferences(getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			Map<String,?> keys = prefs.getAll();
			
			if (keys!=null || !keys.isEmpty()) {
				ArrayList<String> tmp = new ArrayList<String>();
				
				for (Map.Entry<String, ?> entry : keys.entrySet()) {
					String phone = entry.getKey();
					String name = entry.getValue().toString();
					Log.d(TAG, "@@ phone: " + phone+ ", name: " + name);
					tmp.add(name + " tel: " + phone);
				}
				
				// convert the ArrayList to Array
				String[] p2pContacts = new String[tmp.size()];
				p2pContacts = tmp.toArray(p2pContacts);
				return p2pContacts;
			}
		}
		
		return null;
	}
	
	protected void sendMoney() {
		//Toast.makeText(mainActivity, "You Clicked "+v.getId(), Toast.LENGTH_LONG).show();
		String senderPhone = ((GlobalClass) this.getActivity().getApplication()).getMobilePhone();
		
		//check if any field is null or empty
		String currency = (String)spinnerCurrencyList.getSelectedItem();
		String amount = editTextMoneyTransferAmount.getText().toString();
		String recipientPhone = (String)spinnerFriendList.getSelectedItem();
		String message = editTextMoneyTransferMessage.getText().toString();
		
		Log.d(TAG, "curreny: " + currency);
		Log.d(TAG, "amount: " + amount);
		Log.d(TAG, "recipient: " + recipientPhone);
		Log.d(TAG, "message: " + message);
		
		if (senderPhone!=null && !senderPhone.equals("") && 
			currency!=null && !currency.equals("") && 
			amount!=null && !amount.equals("") && 
			recipientPhone!=null && !recipientPhone.equals("")
		) {
			// parse the phone number from recipient info 
			String delim = " tel: ";
			recipientPhone = recipientPhone.substring(recipientPhone.indexOf(delim)+delim.length(), recipientPhone.length());
			
			// all mandatory fields are ready, we are good to go
        	progress = ProgressDialog.show(this.getActivity(), "Connecting", "Please wait...", true);
        	
        	//trigger network request
        	new ConnectWebServiceTask(this.getActivity().getApplicationContext(), this).execute(
        			getString(R.string.webservice_protocol),
        			getString(R.string.webservice_url),
        			getString(R.string.webservice_port),
        			getString(R.string.webservice_send_money), 
        			senderPhone, 
        			currency, 
        			amount, 
        			recipientPhone, 
        			message);
        	
		} else {
    		// show error if any of the field is absent
            new AlertDialog.Builder(getActivity())
			.setTitle("Error").setMessage("Please input all the information!").setCancelable(true)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).create().show();
		}
	}

	@Override
	public void callback(Boolean result) {
		progress.dismiss();
		
		if (result) {
			// prepare the dialog message;
			String msg = "Transaction is processed, the recipient will be notified. You can reference this transaction from transaction history.";
			String msg = "Transaction is processed, the recipient will be notified.";
			
			// display successful
            new AlertDialog.Builder(this.getActivity())
				.setTitle("Successful").setMessage(msg).setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
						// reset all the fields
						spinnerCurrencyList.setSelection(0);
						editTextMoneyTransferAmount.setText("");
						spinnerFriendList.setSelection(0);
						editTextMoneyTransferMessage.setText("");
						spinnerCurrencyList.requestFocus();
					}
				}).create().show();
            
		} else {
            // display error
            new AlertDialog.Builder(this.getActivity())
				.setTitle("Error").setMessage("Transaction cannot be processed!").setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
		}
	}
	
	protected void cacheTransaction() {
		Log.d(TAG, "cacheTransaction");
		String senderPhone = ((GlobalClass) this.getActivity().getApplication()).getMobilePhone();
		
		//check if any field is null or empty
		String currency = (String)spinnerCurrencyList.getSelectedItem();
		String amount = editTextMoneyTransferAmount.getText().toString();
		String recipientPhone = (String)spinnerFriendList.getSelectedItem();
		String message = editTextMoneyTransferMessage.getText().toString();
		
		Log.d(TAG, "curreny: " + currency);
		Log.d(TAG, "amount: " + amount);
		Log.d(TAG, "recipient: " + recipientPhone);
		Log.d(TAG, "message: " + message);
		
		if (senderPhone!=null && !senderPhone.equals("") && 
			currency!=null && !currency.equals("") && 
			amount!=null && !amount.equals("") && 
			recipientPhone!=null && !recipientPhone.equals("")
		) {
			// parse the phone number from recipient info 
			String delim = " tel: ";
			recipientPhone = recipientPhone.substring(recipientPhone.indexOf(delim)+delim.length(), recipientPhone.length());
			
			SharedPreferences prefs = mContext.getSharedPreferences(getString(R.string.p2p_cached_transaction), Context.MODE_PRIVATE);
			
			int currentTxCount = 1;
			Map<String, ?> map = prefs.getAll();
			if (map==null || map.size()<=0) {
				currentTxCount = 1;
			} else {
				currentTxCount = map.size() + 1;
			}
			
			Transaction tx = new Transaction();
			tx.setSenderPhone(senderPhone);
			tx.setCurrency(currency);
			tx.setAmount(Double.parseDouble(amount));
			tx.setReceiverPhone(recipientPhone);
			tx.setMessage(message);
			
			String txJsonStr;
			try {
				txJsonStr = new ObjectMapper().writeValueAsString(tx);
				Log.d(TAG, "transaction string in json: " + txJsonStr);
				
				Editor editor = prefs.edit();
				editor.putString("TX" + currentTxCount, txJsonStr);
				Log.d(TAG, "total cached transaction: " + currentTxCount);
				editor.commit();
				
				// prepare the dialog message;
				String msg = "Your device is currently offline, the transaction is cached and will be processed once your device is connected to network.";
				
				// display successful
	            new AlertDialog.Builder(this.getActivity())
					.setTitle("Warning").setMessage(msg).setCancelable(true)
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
							// reset all the fields
							spinnerCurrencyList.setSelection(0);
							editTextMoneyTransferAmount.setText("");
							spinnerFriendList.setSelection(0);
							editTextMoneyTransferMessage.setText("");
							spinnerCurrencyList.requestFocus();
						}
					}).create().show();
				
			} catch (JsonProcessingException e) {
				Log.d(TAG, e.getMessage(), e);
				
	            // display error
	            new AlertDialog.Builder(this.getActivity())
					.setTitle("Error").setMessage("Transaction cannot be processed!").setCancelable(true)
					.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					}).create().show();
			}
			
			// reset all the fields
			spinnerCurrencyList.setSelection(0);
			editTextMoneyTransferAmount.setText("");
			spinnerFriendList.setSelection(0);
			editTextMoneyTransferMessage.setText("");
			spinnerCurrencyList.requestFocus();
			
		} else {
    		// show error if any of the field is absent
            new AlertDialog.Builder(getActivity())
			.setTitle("Error").setMessage("Please input all the information!").setCancelable(true)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			}).create().show();
		}
	}
}
