package hk.edu.polyu.P2pMobileApp;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class MoneyTransferFragment extends Fragment implements OnItemSelectedListener, OnClickListener, AsyncTaskCallback {
	private static final String TAG = "MoneyTransferFragment";
	
	private ProgressDialog progress;
	
	MainActivity mainActivity;
	
	EditText editTextMoneyTransferAmount;
	Spinner spinnerCurrencyList;
	Spinner spinnerFriendList;
	EditText editTextMoneyTransferMessage;
	Button buttonMoneyTransfer;
	
	public MoneyTransferFragment(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
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

	@Override
	public void callback(Boolean result) {
		progress.dismiss();
		
		if (result) {
			// prepare the dialog message;
			String msg = "Transaction is processed, the recipient will be notified. You can reference this transaction from transaction history.";
			
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
	
}
