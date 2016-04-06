package hk.edu.polyu.P2pMobileApp;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class ContactFragment extends Fragment {
	private static final String TAG = "ContactFragment";
	
	private ContactListAdapter contactListAdapter;
	
	ListView contactListView;
	//EditText editTextSearchContact;
	Button buttonSearchContact;
	MainActivity mainActivity;
	
	private ProgressDialog progress;
	private ArrayList<String[]> localContacts;
	
	
	public ContactFragment(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
		
		// Fetch the user's P2P address book
		contactListAdapter = new ContactListAdapter(this);
		contactListAdapter.refreshP2pContact();
		
		contactListView = (ListView) rootView.findViewById(R.id.contactListView);
		//editTextSearchContact = (EditText)rootView.findViewById(R.id.editTextSearchContact);
		buttonSearchContact = (Button)rootView.findViewById(R.id.buttonSearchContact);
		
		buttonSearchContact.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
            	// fetch all contacts from local system address book
            	loadAddressBook();
            }
        });
		
        contactListView.setAdapter(contactListAdapter);
        contactListAdapter.notifyDataSetChanged();
		
		return rootView;
	}

	protected void loadAddressBook() {
    	progress = ProgressDialog.show(this.getActivity(), "Connecting", "Please wait...", true);
        
    	localContacts = new ArrayList<String []>();
    	
        Cursor contactsCursor = ContactFragment.this.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null,null);
        while (contactsCursor.moveToNext()) {
          String name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
          String phoneNumber = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
          Log.d(TAG, "name: " + name + ", phone: " + phoneNumber);
          localContacts.add(new String[] {name, phoneNumber});
        }
        contactsCursor.close();
        
    	// propagate the local contacts to UI
        contactListAdapter.setList(localContacts, android.R.drawable.ic_menu_add);
        contactListAdapter.notifyDataSetChanged();
        
        progress.dismiss();
	}
}
