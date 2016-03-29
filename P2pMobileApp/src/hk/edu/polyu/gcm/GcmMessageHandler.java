package hk.edu.polyu.gcm;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;
import hk.edu.polyu.moneytransfer.GlobalClass;
import hk.edu.polyu.moneytransfer.MainActivity;
import hk.edu.polyu.moneytransfer.R;

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
	            notification.setContentTitle("Money Transfer");
	            //Message in the Notification
	            notification.setContentText("New message");
	            //Alert shown when Notification is received
	            notification.setTicker("New Message Alert!");
	            //Icon to be set on Notification
	            notification.setSmallIcon(R.drawable.ic_launcher);
	            notification.setPriority(Notification.PRIORITY_HIGH);
	            if (Build.VERSION.SDK_INT >= 21) notification.setVibrate(new long[0]);
	            
	            //Creating new Stack Builder
	            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
	            stackBuilder.addParentStack(MainActivity.class);
	            
	            //Intent which is opened when notification is clicked
	            Intent resultIntent = ((GlobalClass) getApplication()).getLoginActivity();
	            //new Intent(getApplicationContext(), MainActivity.class);
	            stackBuilder.addNextIntent(resultIntent);
	            
	            PendingIntent pIntent =  stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
	            notification.setContentIntent(pIntent);
	            
	            NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	            
	            Notification note = notification.build();
	            note.defaults |= Notification.DEFAULT_VIBRATE;
	            note.defaults |= Notification.DEFAULT_SOUND;
	            note.flags |= Notification.FLAG_ONLY_ALERT_ONCE | Notification.FLAG_AUTO_CANCEL;
	            // note.when = System.currentTimeMillis() + (1000 * 60 * 15);
	            manager.notify(notID, note);
		    }
		});
	
	}

	
}