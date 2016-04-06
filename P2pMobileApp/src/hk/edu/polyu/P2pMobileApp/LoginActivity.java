package hk.edu.polyu.P2pMobileApp;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import hk.edu.polyu.P2pMobileApp.gcm.GCMRegistrationService;
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class LoginActivity extends Activity implements OnClickListener, AsyncTaskCallback {
	private static final String TAG = "LoginActivity";
	
	private Button loginButton;
	private Button registerButton;
	private EditText nameEditText;
	private EditText mobilePhoneEditText;

	private CheckBox rememberMeCheckBox;
	private boolean rememberMe = false;
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.buttonlogin);
		registerButton = (Button) findViewById(R.id.buttonRegister);
		
		nameEditText = (EditText) findViewById(R.id.editTextName);
		mobilePhoneEditText = (EditText) findViewById(R.id.editTextMobilePhone);
		mobilePhoneEditText.setInputType(InputType.TYPE_CLASS_PHONE);
		mobilePhoneEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
		
		rememberMeCheckBox = (CheckBox) findViewById(R.id.checkBoxRememberLogin);
		rememberMeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
		       public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
					Log.d(TAG, "onCheckedChanged: " + isChecked);
					rememberMe = isChecked;
		       }
		});
		
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		
        if (checkPlayServices()) {
        	// TODO add necessary handling to UI for the asynchronous GCM registration process
        	Log.i(TAG, "start registration with GCM... ");
        	
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, GCMRegistrationService.class);
            startService(intent);
        }
	}

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
    	boolean result = false;
    	
    	try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
            Log.i(TAG, "check Google Play Service result code: " + resultCode);
            
            if (resultCode != ConnectionResult.SUCCESS) {
            	result = false;
            	
            	/* There can be many different scenarios where Google Play Services is not functioning. 
            	 * We may not want to import the Google Play Services library project in HSBC core
            	 * mobile app source code.
            	 * 
            	 * Instead we have can consider 2 possible outcomes:
            	 * - we do NOT handle any error, just proceed with app launch and ignore the device registration with push
            	 * - if the error code is due to an outdated version of Google Play Service, we may re-direct the user to 
            	 *   Google Play while we keep our app running in background (need DCE review!!)
            	 */
            	if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                	startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.google.android.gms")));            		
            	}
            	
            } else {
            	result = true;
            }
            
    	} catch (Throwable t) { // catch throwable instead of exception to improve the defensive mechanism
    		result = false;
    		Log.e(TAG, t.getMessage());
    	}
    	
    	return result;
    }
	
	@Override
	protected void onStart() {
		super.onStart();
		
        // check if is remember me already activated
        if (isRememberMeForNextLogon()) {
        	prefillLogon();
        } else {
        	rememberMeCheckBox.setChecked(false); // release the register checkbox
        	registerButton.setEnabled(true); // re-enabled the register button 
        }
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((GlobalClass) this.getApplication()).setLoggedIn(false);
	}
	
	@Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle("Closing Activity")
        .setMessage("Are you sure you want to close?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		finish();
        	}
        })
        .setNegativeButton("No", null)
        .show();
        
    }
	
	@Override
	public void onClick(View v) {
	    switch (v.getId()) {
	        case R.id.buttonlogin:
	        	// disable the text field once the login button is clicked
	        	nameEditText.setEnabled(false);
	        	mobilePhoneEditText.setEnabled(false);
	        	
	        	// check if remember me is currently selected
	        	if (rememberMe) {
	        		// check if is remember me is activated already
		            if (isRememberMeForNextLogon()) {
		    			Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
		    			
		        		((GlobalClass) this.getApplication()).setUserName(nameEditText.getText().toString());
		        		((GlobalClass) this.getApplication()).setMobilePhone(mobilePhoneEditText.getText().toString());
		            	((GlobalClass) this.getApplication()).setLoggedIn(true);
		            	
		            	// re-enable the text field if failure occur
		            	nameEditText.setEnabled(true);
		            	mobilePhoneEditText.setEnabled(true);
		            	nameEditText.setText("");
		            	mobilePhoneEditText.setText("");
		            	
		            	startActivity(((GlobalClass) this.getApplication()).getMainActivity());
		            	
		            } else {
		            	// otherwise, this logon instance will still need to authenticate over the work
		            	authenticateUser();
		            }
	        	} else {
	        		authenticateUser(); // authenticate over the network
	        	}
	        	
	            break;
	            
	        case R.id.buttonRegister:
	            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		        if (networkInfo != null && networkInfo.isConnected()) {
		        	startActivity(((GlobalClass) this.getApplication()).getRegisterActivity());
		        } else {
		            // display error
		            new AlertDialog.Builder(this)
						.setTitle("Error").setMessage("Network connection failure!").setCancelable(true)
						.setNeutralButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}).create().show();
		        }
				break;
				
	        default:
	        	break;
	    }   
	}
	
	protected void authenticateUser(){
		String userName = nameEditText.getText().toString();
		String phone = mobilePhoneEditText.getText().toString();
		
		// check if any of the field is empty or null
		if (null==userName || "".equals(userName.trim()) ||
			null==phone || "".equals(phone.trim())
		) {
    		// show error if any of the field is absent
            new AlertDialog.Builder(this)
			.setTitle("Error").setMessage("Please input all the information!").setCancelable(true)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
		        	// re-enable the text field if failure occur
		        	nameEditText.setEnabled(true);
		        	mobilePhoneEditText.setEnabled(true);
				}
			}).create().show();
            
		} else {
			// @TODO should also check if device token is been updated then propagated 
			// the change to server associating with the user account
			
    		// all the fields are ready, we are good to go
        	progress = ProgressDialog.show(this, "Connecting", "Please wait...", true);
        	
        	//trigger network request
        	new ConnectWebServiceTask(getApplicationContext(), this).execute(
        			getString(R.string.webservice_protocol),
        			getString(R.string.webservice_url),
        			getString(R.string.webservice_port),
        			getString(R.string.webservice_get_user), 
        			phone, 
        			userName);
		}
	}
	
	public void callback(Boolean result) {
		progress.dismiss();
		
		if (result) {
			// display successful
			Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
			
    		((GlobalClass) this.getApplication()).setUserName(nameEditText.getText().toString());
    		((GlobalClass) this.getApplication()).setMobilePhone(mobilePhoneEditText.getText().toString());
        	((GlobalClass) this.getApplication()).setLoggedIn(true);
        	
        	// effectuate the remember settings after login process
        	setRememberMeForNextLogon(rememberMe);
        	
        	// re-enable the text field if failure occur
        	nameEditText.setEnabled(true);
        	mobilePhoneEditText.setEnabled(true);
        	nameEditText.setText("");
        	mobilePhoneEditText.setText("");
        	
        	startActivity(((GlobalClass) this.getApplication()).getMainActivity());
        	
		} else {
            // display error
            new AlertDialog.Builder(this)
				.setTitle("Error").setMessage("Account login failure!").setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
			        	// re-enable the text field if failure occur
			        	nameEditText.setEnabled(true);
			        	mobilePhoneEditText.setEnabled(true);
					}
				}).create().show();
		}
	}
	
	protected void setRememberMeForNextLogon(boolean remember) {
		Log.d(TAG, "setRememberMeForNextLogon: " + remember);
		
		SharedPreferences prefs = getSharedPreferences(getString(R.string.p2p_express_logon), Context.MODE_PRIVATE);
		
		if (remember) {
			String userName = ((GlobalClass) this.getApplication()).getUserName().toString();
			String phone = ((GlobalClass) this.getApplication()).getMobilePhone().toString();
			Log.d(TAG, "logon name: " + userName);
			Log.d(TAG, "logon password: " + phone);
			
			Editor editor = prefs.edit();
			editor.putBoolean("rememberMe", true);
			editor.putString("logonName", userName);
			editor.putString("logonPassword", phone);
			editor.commit();
		} else {
			Editor editor = prefs.edit();
			editor.putBoolean("rememberMe", false);
			editor.putString("logonName", "");
			editor.putString("logonPassword", "");
			editor.commit();
		}
	}
	
	protected boolean isRememberMeForNextLogon() {
		SharedPreferences prefs = getSharedPreferences(getString(R.string.p2p_express_logon), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			boolean isRememberMeActivted = prefs.getBoolean("rememberMe", false);
			Log.d(TAG, "isRememberMeForNextLogon: " + isRememberMeActivted);
			return isRememberMeActivted;
		}
		
		Log.d(TAG, "isRememberMeForNextLogon: " + false);
		return false;
	}
	
	protected void prefillLogon() {
		Log.d(TAG, "prefillLogon...");
		SharedPreferences prefs = getSharedPreferences(getString(R.string.p2p_express_logon), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			String userName = prefs.getString("logonName", "");
			String phone = prefs.getString("logonPassword", "");
			Log.d(TAG, "logon name: " + userName);
			Log.d(TAG, "logon password: " + phone);
			
			if (userName!=null && !userName.equals("") && 
					phone!=null && !phone.equals("")
			) {
				// both logon username and password (phone) exist in storage update the UI
				nameEditText.setText(userName);
				mobilePhoneEditText.setText(phone);
				rememberMeCheckBox.setChecked(true);
				registerButton.setEnabled(false);
			}
		}
	}
}
