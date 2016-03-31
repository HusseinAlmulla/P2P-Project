package hk.edu.polyu.P2pMobileApp;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

public class GlobalClass extends Application {
	private boolean loggedIn = false;
	private String userName;
	private String accountNumber;
	private String email;
	private String mobilePhone;
	private List<ObjectFriend> firendList;

	private Intent loginIntent;
	private Intent mainIntent;
	private Intent registerIntent;
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public List<ObjectFriend> getFirendList() {
		return firendList;
	}

	public void setFirendList(List<ObjectFriend> firendList) {
		this.firendList = firendList;
	}
	
	public Intent getLoginActivity(){
		if(loginIntent == null)
		{
			loginIntent = new Intent(this, LoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
		
		return loginIntent;
	}
	
	public Intent getMainActivity(){
		if(mainIntent == null)
		{
			mainIntent = new Intent(this, MainActivity.class);
			mainIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		// startActivity(mainIntent);
		}
		
		return mainIntent;
	}
	
	public Intent getRegisterActivity(){
		if(registerIntent == null)
		{
			registerIntent = new Intent(this, RegisterActivity.class);
			registerIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

		}
		return registerIntent;
	}
}
