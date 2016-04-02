package hk.edu.polyu.P2pMobileApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import hk.edu.polyu.P2pMobileApp.gcm.GCMRegistrationService;
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class RegisterActivity extends Activity implements OnClickListener, AsyncTaskCallback {
	private static final String TAG = "RegisterActivity";
	
	private Button registerButton;
	
	private EditText userNameField;
	private EditText emailField;
	private EditText phoneField;
	private EditText accountField;
	
	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		registerButton = (Button) findViewById(R.id.buttonRegister);
		registerButton.setOnClickListener(this);
		
		userNameField = (EditText) findViewById(R.id.editTextRName);
		emailField = (EditText) findViewById(R.id.editTextREmail);
		phoneField = (EditText) findViewById(R.id.editTextRMobilePhone);
		accountField = (EditText) findViewById(R.id.editTextRBankAccount);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	        case R.id.buttonRegister:
	        	// disable the text field once the register button is clicked 
	        	userNameField.setEnabled(false);
	        	emailField.setEnabled(false);
	        	phoneField.setEnabled(false);
	        	accountField.setEnabled(false);
	        	
	        	registerUser();
	            break;
		}
	}
	
	protected void registerUser() {
    	String userName = userNameField.getText().toString();
    	Log.d(TAG, userName);
    	
    	String email = emailField.getText().toString();
    	Log.d(TAG, email);
    	
    	String phone = phoneField.getText().toString();
    	Log.d(TAG, phone);
    	
    	String account = accountField.getText().toString();
    	Log.d(TAG, account);
    	
    	String bank_code = "HSBC"; //default to HSBC
    	Log.d(TAG, bank_code);
    	
    	// check if any of the field is empty or null
    	if (null==userName || "".equals(userName.trim()) || 
    		null==emailField || "".equals(email.trim()) || 
    		null==phoneField || "".equals(phone.trim()) || 
    		null==accountField || "".equals(account.trim()) || 
    		null==GCMRegistrationService.DEVICE_TOKEN
    	) {
    		// show error if any of the field is absent
            new AlertDialog.Builder(this)
			.setTitle("Error").setMessage("Please input all the information!").setCancelable(true)
			.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
					// re-enable the text field if failure occur
		        	userNameField.setEnabled(true);
		        	emailField.setEnabled(true);
		        	phoneField.setEnabled(true);
		        	accountField.setEnabled(false);
				}
			}).create().show();
            
    	} else {
    		// all the fields are ready, we are good to go
        	progress = ProgressDialog.show(this, "Connecting", "Please wait...", true);
        	
        	//trigger network request
        	new ConnectWebServiceTask(getApplicationContext(), this).execute(
        			getString(R.string.webservice_protocol),
        			getString(R.string.webservice_url),
        			getString(R.string.webservice_port),
        			getString(R.string.webservice_create_user), 
        			userName,
        			email,
        			phone,
        			bank_code,
        			account,
        			GCMRegistrationService.DEVICE_TOKEN);
    	}
	}
	
	public void callback(Boolean result) {
		progress.dismiss();
		
		if (result) {
			// display successful
            new AlertDialog.Builder(this)
				.setTitle("Congratulations").setMessage("Account registration successful!").setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						startActivity(((GlobalClass) RegisterActivity.this.getApplication()).getMainActivity());
					}
				}).create().show();
            
		} else {
            // display error
            new AlertDialog.Builder(this)
				.setTitle("Error").setMessage("Account registration failure!").setCancelable(true)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
						// re-enable the text field if failure occur
			        	userNameField.setEnabled(true);
			        	emailField.setEnabled(true);
			        	phoneField.setEnabled(true);
			        	accountField.setEnabled(false);
					}
				}).create().show();
		}
	}
}
