package hk.edu.polyu.P2pMobileApp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ContactFragment extends Fragment {
	ListView contactListView;
	EditText editTextSearchContact;
	Button buttonSearchContact;
	MainActivity mainActivity;
	public int [] prgmImages={
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete,
				android.R.drawable.ic_menu_delete
				};
    public String [] prgmNameList={
    			"Let Us C",
    			"c++",
    			"JAVA",
    			"Jsp",
    			"Microsoft .Net",
    			"Android",
    			"PHP",
    			"Jquery",
    			"JavaScript"
    			};
    
	public ContactFragment(MainActivity mainActivity) {
			this.mainActivity = mainActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contact, container, false);
		final ContactListAdapter contactListAdapter = new ContactListAdapter(mainActivity);
		contactListAdapter.setList(prgmNameList, prgmImages);
		
		contactListView = (ListView) rootView.findViewById(R.id.contactListView);
		editTextSearchContact = (EditText)rootView.findViewById(R.id.editTextSearchContact);
		buttonSearchContact = (Button)rootView.findViewById(R.id.buttonSearchContact);
		
		buttonSearchContact.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(mainActivity, "Searching..", Toast.LENGTH_LONG).show();
                contactListAdapter.setList(new String[]{"new"}, new int[]{android.R.drawable.ic_menu_add});
                contactListView.setAdapter(contactListAdapter);
                contactListAdapter.notifyDataSetChanged();
            }
        });
		
        contactListView.setAdapter(contactListAdapter);
        contactListAdapter.notifyDataSetChanged();
		
		return rootView;
	}

}
