package hk.edu.polyu.P2pMobileApp.json;

public class User {
	private int userOid;
	private String name;
	private String email;
	private String phone;
	private String bank;
	private String account;
	private String deviceToken;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public int getUserOid() {
		return userOid;
	}

	public void setUserOid(int userOid) {
		this.userOid = userOid;
	}
}
