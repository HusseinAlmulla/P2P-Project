package hk.edu.polyu.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;
import hk.edu.polyu.moneytransfer.GlobalClass;
import hk.edu.polyu.moneytransfer.MainActivity;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
	
    public GcmMessageHandler() {
		super("GcmMessageHandler");
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		handler = new Handler();
	}
	@Override
	protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if(((GlobalClass) this.getApplication()).isLoggedIn()){
    		showToast("New Message!");
    	}else{
    		notify(extras.getString("title"));
    	}
        
       
       
       Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));
       
       GcmBroadcastReceiver.completeWakefulIntent(intent);

	}
	
	public void showToast(final String mes){
		handler.post(new Runnable() {
		    public void run() {
		    	Toast.makeText(getApplicationContext(), mes , Toast.LENGTH_LONG).show();
		    }
		});

	}
	
	private void notify(final String mes){
		handler.post(new Runnable() {
		    public void run() {
		    	int notID = 0;
		    	NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext());
	            //Title for Notification
	            notification.setContentTitle("New Message");
	            //Message in the Notification
	            // notification.setContentText("New Post on Android Notification.");
	            //Alert shown when Notification is received
	            notification.setTicker("New Message Alert!");
	            //Icon to be set on Notification
	            notification.setSmallIcon(android.R.drawable.ic_menu_more);
	            
	            notification.setAutoCancel(true);

	            //Creating new Stack Builder
	            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
	            stackBuilder.addParentStack(MainActivity.class);
	            
	            //Intent which is opened when notification is clicked
	            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
	            stackBuilder.addNextIntent(resultIntent);
	            
	            PendingIntent pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
	            notification.setContentIntent(pIntent);
	            
	            NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	            manager.notify(notID, notification.build());
		    }
		});
	
	}

	
}