package hk.edu.polyu.moneytransfer;

import java.util.List;

import android.app.Application;

public class GlobalClass extends Application {
	private boolean loggedIn = false;
	private String userName;
	private String accountNumber;
	private String email;
	private String mobilePhone;
	private List<ObjectFriend> firendList;

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
}
