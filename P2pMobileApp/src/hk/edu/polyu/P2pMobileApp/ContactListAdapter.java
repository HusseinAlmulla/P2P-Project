package hk.edu.polyu.P2pMobileApp;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class ContactListAdapter extends BaseAdapter implements AsyncTaskCallback {
	private static final String TAG = "ContactListAdapter";
	
	private ProgressDialog progress;
	
	private ArrayList<String[]> mLocalContacts;
	private int mDrawableResId;
	
    Context mContext;
    int position;
    
    private String tmpUsername;
    private String tmpPhone;
    
    private static LayoutInflater inflater = null;
    
    public ContactListAdapter(ContactFragment contactFragment) {
    	this(contactFragment, null, 0);
	}

    public ContactListAdapter(ContactFragment contactFragment, ArrayList<String[]> localContacts, int drawableResId) {
        mContext = contactFragment.getActivity();
        mLocalContacts = localContacts;
        inflater = ( LayoutInflater )mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public void setList(ArrayList<String[]> localContacts, int drawableResId) {
    	mLocalContacts = localContacts;
    	mDrawableResId = drawableResId;
    }
    
	@Override
	public int getCount() {
		return mLocalContacts.size();
	}

	@Override
	public Object getItem(int position) {
		this.position = position;
		return this.position;
	}

	@Override
	public long getItemId(int position) {
		this.position = position;
		return this.position;
	}
	
	public class Holder {
        TextView tv;
        ImageView img;
    }
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView;
		rowView = inflater.inflate(R.layout.contact_list_item, null);

		Holder holder = new Holder();
		holder.tv = (TextView) rowView.findViewById(R.id.textView1);
		holder.img = (ImageView) rowView.findViewById(R.id.imageView1);

		String name = ((String [])mLocalContacts.get(position))[0];
		
		// support display name AND phone number in contact list selection
		String phone = ((String [])mLocalContacts.get(position))[1];
		holder.tv.setText(Html.fromHtml(name + "<br><small>tel: " + phone + "</small>"));
		
		holder.img.setImageResource(mDrawableResId);
		holder.img.setId(mDrawableResId);
		
		holder.img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView img = (ImageView)v;
				if (img.getId() == android.R.drawable.ic_menu_add) {
					
		            ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
			        if (networkInfo != null && networkInfo.isConnected()) {
						// for online only - validate the contact before adding the record to user's P2P address book
						validateP2pContact(((String [])mLocalContacts.get(position))[0], ((String [])mLocalContacts.get(position))[1]);
			        } else {
			        	Log.d(TAG, "device currently offline, proceed adding contact to P2P address book without validating with server");
						// for offline only - add the record to user's P2P address book
						addP2pContact(((String [])mLocalContacts.get(position))[0], ((String [])mLocalContacts.get(position))[1]);
			        }
					
				} else {
					// delete the record from user's P2P address book
					deleteP2pContact(((String [])mLocalContacts.get(position))[1]);
				}
			}
		});
		
		return rowView;
	}
	
	protected void addP2pContact(String name, String phone) {
		SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		// phone is the key
		Log.d(TAG, "### saving P2P contact: " + phone + ": " + name);
		Editor editor = prefs.edit();
		editor.putString(phone, name);
		editor.commit();
		
        new AlertDialog.Builder(mContext)
			.setTitle("New P2P contact added").setMessage(name + "\n" + phone).setCancelable(true)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					refreshP2pContact();
				}
			}).create().show();
	}
	
	protected void deleteP2pContact(String phone) {
		SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		// phone is the key
		if (prefs != null) {
			String name = prefs.getString(phone, "");
			if (name!=null && name.length()>0) {
				Log.d(TAG, "### deleting P2P contact: " + phone + ": " + name);
				Editor editor = prefs.edit();
				editor.remove(phone);
				editor.commit();
				
		        new AlertDialog.Builder(mContext)
				.setTitle("P2P contact is deleted").setMessage(name + "\n" + phone).setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						refreshP2pContact();
					}
				}).create().show();
			}
		}
	}
	
	protected void refreshP2pContact() {
		SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			Map<String,?> keys = prefs.getAll();
			
			if (keys!=null || !keys.isEmpty()) {
				ArrayList<String[]> p2pContacts = new ArrayList<String[]>();
				
				for (Map.Entry<String, ?> entry : keys.entrySet()) {
					String phone = entry.getKey();
					String name = entry.getValue().toString();
					Log.d(TAG, "@@ phone: " + phone+ ", name: " + name);
					p2pContacts.add(new String[] {name, phone});
				}
				
				// propagate the refreshed P2P contacts to UI
				setList(p2pContacts, android.R.drawable.ic_menu_delete);
				notifyDataSetChanged();
			}
		}
	}

	protected void validateP2pContact(String name, String phone) {
		// validate if the contact is already a registered user
    	progress = ProgressDialog.show(mContext, "Connecting", "Please wait...", true);
    	
    	tmpUsername = name;
    	tmpPhone = phone;
    	
    	//trigger network request
    	new ConnectWebServiceTask(mContext, this).execute(
    			mContext.getString(R.string.webservice_protocol),
    			mContext.getString(R.string.webservice_url),
    			mContext.getString(R.string.webservice_port),
    			mContext.getString(R.string.webservice_get_user), 
    			phone, 
    			""); // we only validate the phone number if the recipient exists
	}
	
	@Override
	public void callback(Boolean result) {
		progress.dismiss();
		
		if (result) {
			// the contact is a registered user, can be added to local P2P contact
			addP2pContact(tmpUsername, tmpPhone);
		} else {
            // display error
            new AlertDialog.Builder(mContext)
				.setTitle("Error").setMessage(tmpUsername + " is not a registered P2P user!").setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
		}
		
		// reset the tmp variable
		tmpUsername = "";
		tmpPhone = "";
	}
}
