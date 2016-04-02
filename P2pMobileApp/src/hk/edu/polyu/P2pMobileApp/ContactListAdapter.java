package hk.edu.polyu.P2pMobileApp;

import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter {
	private static final String TAG = "ContactListAdapter";
	
	private ArrayList<String[]> mLocalContacts;
	private int mDrawableResId;
	
    Context context;
    int position;
    
    private static LayoutInflater inflater=null;
    
    public ContactListAdapter(MainActivity mainActivity) {
    	this(mainActivity, null, 0);
	}

    public ContactListAdapter(MainActivity mainActivity, ArrayList<String[]> localContacts, int drawableResId) {
        context=mainActivity;
        mLocalContacts = localContacts;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		holder.tv.setText(name);
		holder.img.setImageResource(mDrawableResId);
		holder.img.setId(mDrawableResId);
		
		holder.img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView img = (ImageView)v;
				if (img.getId() == android.R.drawable.ic_menu_add) {
					// add the record to user's P2P address book
					addP2pContact(((String [])mLocalContacts.get(position))[0], ((String [])mLocalContacts.get(position))[1]);
					
				} else {
					// delete the record from user's P2P address book
					deleteP2pContact(((String [])mLocalContacts.get(position))[1]);
				}
			}
		});
		
		return rowView;
	}
	
	protected void addP2pContact(String name, String phone) {
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		// phone is the key
		Log.d(TAG, "### saving P2P contact: " + phone + ": " + name);
		Editor editor = prefs.edit();
		editor.putString(phone, name);
		editor.commit();
		
        new AlertDialog.Builder(context)
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
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
		// phone is the key
		if (prefs != null) {
			String name = prefs.getString(phone, "");
			if (name!=null && name.length()>0) {
				Log.d(TAG, "### deleting P2P contact: " + phone + ": " + name);
				Editor editor = prefs.edit();
				editor.remove(phone);
				editor.commit();
				
		        new AlertDialog.Builder(context)
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
		SharedPreferences prefs = context.getSharedPreferences(context.getString(R.string.p2p_address_book), Context.MODE_PRIVATE);
		
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
}
