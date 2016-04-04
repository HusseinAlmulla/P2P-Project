package edu.polyu.comp.service;

import java.io.IOException;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import edu.polyu.comp.constants.TransactionConstants;
import edu.polyu.comp.dao.TransactionDao;
import edu.polyu.comp.domain.Transaction;
import edu.polyu.comp.util.LoggerUtil;
import edu.polyu.comp.util.StringUtil;

public class TransactionService {
	
	private UserService userService;
	public UserService getUserService() {
		if(userService == null){
			userService = new UserService();
		}
		return userService;
	}
	
	private TransactionDao transactionDao;
	public TransactionDao getTransactionDao() {
		if(transactionDao == null){
			transactionDao = new TransactionDao();
		}
		return transactionDao;
	}
	
	public boolean sendMoney(Transaction transaction){
		boolean isTransactionRecorded = this.createTransaction(transaction);
		if (isTransactionRecorded) {
			return this.sendPushNotification(transaction);
		} else {
			return false;
		}		
	}
	
	public boolean createTransaction(Transaction transaction) {
		return getTransactionDao().createTransaction(transaction);
	}
	
	public boolean sendPushNotification(Transaction transaction) {
		String notificationToken = getUserService().findDeviceTokenByPhoneNumber(transaction.getReceiverPhone());
		Sender sender = new Sender(TransactionConstants.GCM_API_KEY);
		Message.Builder builder = new Message.Builder();
		builder.addData(TransactionConstants.DATA_KEY_TO, getRecipientDeviceToken(transaction));
		builder.addData(TransactionConstants.DATA_KEY_MSG, getPushNotificationMessage(transaction));
		Message msg = builder.build();
		LoggerUtil.info(this.getClass().getName(), "@@ GCM message: " + msg.toString());
		
		try {
			Result result = sender.send(msg, notificationToken, TransactionConstants.GCM_MAX_RETRIVES);
			if (StringUtil.isEmpty(result.getErrorCodeName())) {
				LoggerUtil.info(this.getClass().getName(), "GCM Notification is sent successfully");
				return true;
			}
			LoggerUtil.error(this.getClass().getName(), "Error occurred while sending push notification :" + result.getErrorCodeName());
		} catch (InvalidRequestException e) {
			e.printStackTrace();
			LoggerUtil.error(this.getClass().getName(), "Invalid Request");
		} catch (IOException e) {
			e.printStackTrace();
			LoggerUtil.error(this.getClass().getName(), "IO Exception");
		}
		return false;
	}
	
	public String getPushNotificationMessage(Transaction transaction){
		String sender = getUserService().findUserNameByPhoneNumber(transaction.getSenderPhone());
		LoggerUtil.info(this.getClass().getName(), "@@ sender name: " + sender);
		//sender = sender.split(sender)[0];
		String receiver = getUserService().findUserNameByPhoneNumber(transaction.getReceiverPhone());
		LoggerUtil.info(this.getClass().getName(), "@@ recipient name: " + receiver);
		//receiver = receiver.split(receiver)[0];	
		return "Hey " + receiver + ", you just receive a P2P transfer from " + sender + "! " + sender + " said: " + transaction.getMessage();		
	}
	
	public String getRecipientDeviceToken(Transaction transaction) {
		String deviceToken = getUserService().findDeviceTokenByPhoneNumber(transaction.getReceiverPhone());
		LoggerUtil.info(this.getClass().getName(), "@@ recipient device token: " + deviceToken);
		return deviceToken;
	}
}
