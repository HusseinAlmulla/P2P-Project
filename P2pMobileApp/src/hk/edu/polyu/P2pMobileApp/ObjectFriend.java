package hk.edu.polyu.P2pMobileApp;

public class ObjectFriend {
	private String userName;
	private String accountNumber;
	
	public ObjectFriend(){
		this(null, null);
	}
	
	public ObjectFriend(String userName, String accountNumber){
		this.userName = userName;
		this.accountNumber = accountNumber;
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
	
	
}
