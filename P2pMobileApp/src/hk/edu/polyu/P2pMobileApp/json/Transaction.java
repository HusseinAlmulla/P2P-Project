package hk.edu.polyu.P2pMobileApp.json;

public class Transaction {
	private int transactionOid;
	private String senderPhone;
	private String receiverPhone;
	private String currency;
	private Double amount;
	private String message;
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public int getTransactionOid() {
		return transactionOid;
	}

	public void setTransactionOid(int transactionOid) {
		this.transactionOid = transactionOid;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
