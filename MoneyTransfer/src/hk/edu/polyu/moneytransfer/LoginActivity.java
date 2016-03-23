package hk.edu.polyu.moneytransfer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {

	private Button loginButton;
	private Button registerButton;
	private EditText emailEditText;
	private EditText mobilePhoneEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginButton = (Button) findViewById(R.id.buttonlogin);
		registerButton = (Button) findViewById(R.id.buttonRegister);
	
		emailEditText = (EditText) findViewById(R.id.editTextEmail);
		mobilePhoneEditText = (EditText) findViewById(R.id.editTextMobilePhone);
		
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		((GlobalClass) this.getApplication()).setLoggedIn(false);
			
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
	        case R.id.buttonlogin:
	        	
	        	if(validUser()){
	        		((GlobalClass) this.getApplication()).setEmail(emailEditText.getText().toString());
	        		((GlobalClass) this.getApplication()).setMobilePhone(mobilePhoneEditText.getText().toString());
		        	((GlobalClass) this.getApplication()).setLoggedIn(true);
		        	
		        	Intent intent = new Intent(this, MainActivity.class);
		        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		        	
					startActivity(intent);
	        	}
	            break;
	        case R.id.buttonRegister:
	        	
	        	Intent intent = new Intent(this, RegisterActivity.class);
	        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        	
				startActivity(intent);
				
				break;
	    }   
	}
	
	
	private boolean validUser(){
		boolean result = false;
		
		//TODO do something 
		// set result to true
		result = true;
		
		return result;
	}
}
