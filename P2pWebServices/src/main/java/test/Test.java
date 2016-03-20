package test;

import edu.polyu.comp.domain.Transaction;
import edu.polyu.comp.domain.User;
import edu.polyu.comp.service.TransactionService;
import edu.polyu.comp.service.UserService;

public class Test {

	public static void main(String[] args) {
		//testCreateUser();
		//testfindUserNameByPhoneNumber();	
		testCreateTransaction();
	}
	
	private static void testCreateUser() {
		User user = new User();
		user.setName("Becky Yeung");
		user.setEmail("becky.yeung@abc.com");
		user.setPhone("0987654321");
		user.setBank("HSBC");
		user.setAccount("12344545");
		user.setDeviceToken("1234345");
		UserService userService = new UserService();
		boolean isSuccess = userService.createUser(user);
		System.out.println("isSuccess = " + isSuccess);
	}
	
	private static void testCreateTransaction() {
		Transaction tx = new Transaction();
		tx.setSenderPhone("123456");
		tx.setReceiverPhone("0987654321");
		tx.setCurrency("HKD");
		tx.setAmount(new Double("100"));
		tx.setMessage("Japanese BBQ money");
		boolean isSuccess = new TransactionService().createTransaction(tx);		
		System.out.println("isSuccess = " + isSuccess);
	}

	private static void testfindUserNameByPhoneNumber() {
		UserService userService = new UserService();
		String userName = userService.findUserNameByPhoneNumber("0987654321");
		System.out.println("userName = " + userName);
	}

}
