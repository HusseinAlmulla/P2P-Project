package hk.edu.polyu.P2pMobileApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	protected static final String TAG = "MainActivity";
	
	// declare properties
	private String[] mNavigationDrawerItemTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 
		// for proper titles
		mTitle = mDrawerTitle = getTitle();

		// initialize properties
		mNavigationDrawerItemTitles = getResources().getStringArray(R.array.navigation_drawer_items_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		
		// list the drawer items
//		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[5];
//		drawerItem[0] = new ObjectDrawerItem(getResources().getString(R.string.title_profile));
//		drawerItem[1] = new ObjectDrawerItem(getResources().getString(R.string.title_contact_list));
//		drawerItem[2] = new ObjectDrawerItem(getResources().getString(R.string.title_transfer_money));
//		drawerItem[3] = new ObjectDrawerItem(getResources().getString(R.string.title_history));
//		drawerItem[4] = new ObjectDrawerItem(getResources().getString(R.string.title_sign_out));
		
		// only support contact list and transfer money feature
		ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[3];
		drawerItem[0] = new ObjectDrawerItem(getResources().getString(R.string.title_contact_list));
		drawerItem[1] = new ObjectDrawerItem(getResources().getString(R.string.title_transfer_money));
		drawerItem[2] = new ObjectDrawerItem(getResources().getString(R.string.title_sign_out));
		
		// Pass the folderData to our ListView adapter
		DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, drawerItem);

		// Set the adapter for the list view
		mDrawerList.setAdapter(adapter);

		// set the item click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// for app icon control for nav drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /*
										 * nav drawer icon to replace 'Up' caret
										 */
				R.string.drawer_open, /* "open drawer" description */
				R.string.drawer_close /* "close drawer" description */
		) {

			/**
			 * Called when a drawer has settled in a completely closed state.
			 */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			selectItem(1);
		}
		
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		Log.d(TAG, "is login? " + ((GlobalClass) this.getApplication()).isLoggedIn());
		if(!((GlobalClass) this.getApplication()).isLoggedIn()){
			startActivity(((GlobalClass) this.getApplication()).getLoginActivity());
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		if (isFinishing()) {
			((GlobalClass) this.getApplication()).setLoggedIn(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Sign out")
        .setMessage("Are you sure you want to sign out?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		((GlobalClass) getApplication()).setLoggedIn(false);
        		finish();
        	}

        })
        .setNegativeButton("No", null)
        .show();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// to change up caret
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	// navigation drawer click listener
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}

	}

	private void selectItem(int position) {
		// update the main content by replacing fragments

		Fragment fragment = null;

		switch (position) {
//		case 0:
//			fragment = new ProfileFragment(this);
//			break;
		case 0:
			fragment = new ContactFragment(this);
			break;
		case 1:
			fragment = new MoneyTransferFragment(this);
			break;
//		case 3:
//			fragment = new HistoryFragment(this);
//			break;
		case 2:
	        new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Sign out")
	        .setMessage("Are you sure you want to sign out?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
	        {
	        	@Override
	        	public void onClick(DialogInterface dialog, int which) {
	        		((GlobalClass) getApplication()).setLoggedIn(false);
	        		finish();
	        	}

	        })
	        .setNegativeButton("No", null)
	        .show();
			break;
			
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(mNavigationDrawerItemTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);

		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}
}
