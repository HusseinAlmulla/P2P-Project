package aaa;
import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class GCMServer {
	
	static String GCM_API_KEY = "AIzaSyAqXCvGLUmgAdOcKGy8U7Udr7HYujHjM9Y";
	//static String HTC_REG_ID = "APA91bG2J_HYhJZzcZ3Tp-nJ9oVQ3TeAPGretCqruye416vhC4i-3_p5f4W-Em3uVw6FcPdZVIiQxJO2Ce4S2ZicQBI-Kpep78OPeDkD2--PXb7ZYZn99N_0IEKnH1BktMRAChhdsENt";
	static String HTC_REG_ID = "APA91bFoUKqJafnKu-YcFgV8PbO3XHg7MOui8yOyeJHKCeQ_R8itEA6MVxbxAt5QG8nDyeCVVbnvshVSeasw5ycmBP9nsoM-ckP-MgiinO3dtgzeE4GZ_KdTh7Ns_BfOjWTyZL0feR6v";
	static String MESSAGE_KEY = "title";
	static String MESSAGE_VALUE = "hi!!!!";
	
	public static void main(String[] arg){
		try {
			
			
			Sender sender = new Sender(GCM_API_KEY);
	
	        ArrayList<String> devicesList = new ArrayList<String>();
	        devicesList.add(HTC_REG_ID);
	
	        Message message = new Message.Builder().timeToLive(30)
	                .delayWhileIdle(true).addData(MESSAGE_KEY, MESSAGE_VALUE).build();
	
	        MulticastResult result;
		
			result = sender.send(message, devicesList, 1);
			sender.send(message, devicesList, 1);
	        System.out.println(result.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}
