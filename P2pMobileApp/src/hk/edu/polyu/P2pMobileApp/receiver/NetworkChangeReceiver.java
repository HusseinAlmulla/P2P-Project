package hk.edu.polyu.P2pMobileApp.receiver;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import hk.edu.polyu.P2pMobileApp.R;
import hk.edu.polyu.P2pMobileApp.json.Transaction;
import hk.edu.polyu.P2pMobileApp.task.AsyncTaskCallback;
import hk.edu.polyu.P2pMobileApp.task.ConnectWebServiceTask;

public class NetworkChangeReceiver extends BroadcastReceiver implements AsyncTaskCallback {
	private static final String TAG = "NetworkChangeReceiver";
	
	protected static int ID = 999;
	
	protected Context mContext;
	
	@Override
	public void onReceive(final Context context, final Intent intent) {
		mContext = context;
		
		Log.d(TAG, "Network status change is detected");
		Toast.makeText(context, "network status change is detected", Toast.LENGTH_LONG).show();
		
		final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected()) {
			Log.d(TAG, "Network connection becomes available ");
			// check if any cached transaction and submit them to server
			
			loadCacheTransaction();
			
		} else {
			Log.d(TAG, "Network connection becomes un-available ");
		}
	}
	
	protected void loadCacheTransaction() {
		SharedPreferences prefs = mContext.getSharedPreferences(mContext.getString(R.string.p2p_cached_transaction), Context.MODE_PRIVATE);
		
		if (prefs != null) {
			Map<String,?> keys = prefs.getAll();
			
			if (keys!=null || !keys.isEmpty()) {
				// prepare the preference editor
				Editor editor = prefs.edit();
				
				for (Map.Entry<String, ?> entry : keys.entrySet()) {
					String tx_key = entry.getKey();
					String tx_data = entry.getValue().toString();
					Log.d(TAG, "@@ cached tx: " + tx_key + ", data: " + tx_data);
					
					try {
						Transaction tx = new ObjectMapper().readValue(tx_data, Transaction.class);
						
			        	//trigger network request
			        	new ConnectWebServiceTask(mContext, this).execute(
			        			mContext.getString(R.string.webservice_protocol),
			        			mContext.getString(R.string.webservice_url),
			        			mContext.getString(R.string.webservice_port),
			        			mContext.getString(R.string.webservice_send_money), 
			        			tx.getSenderPhone(), 
			        			tx.getCurrency(), 
			        			tx.getAmount().toString(), 
			        			tx.getReceiverPhone(), 
			        			tx.getMessage());
						
					} catch (Exception exc) {
						Log.e(TAG, exc.getMessage(), exc);
					}
				}
				
				// clear all the cached tx no matter successfully submitted to server or not 
				Log.d(TAG, "deleteing all tx from cache ...");
				editor.clear();
				editor.commit();
			}
			
		} else {
			Log.d(TAG, "no cached transaction found");
		}
	}

	@Override
	public void callback(Boolean result) {
		Log.d(TAG, "callback result: " + result);
		
		// send local notification
        NotificationManager mNotificationManager;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        
        String message = "";
        if (result) {
            mBuilder.setContentTitle("Successful");
            message = "Previous transaction is processed, the recipient will be notified.";
            mBuilder.setContentText(message);
        } else {
            mBuilder.setContentTitle("Error");
            message = "Previous transaction cannot be processed";
            mBuilder.setContentText(message); 
        }
        
		// Applying an expanded layout to a notification
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		// Sets a title for the Inbox in expanded layout
		inboxStyle.setBigContentTitle("P2P transfer received");
		
		String p2pMsg1 = message.substring(0, message.indexOf(",") + 1);
		String p2pMsg2 = message.substring(message.indexOf(",") + 2, message.indexOf(".") + 1);

		String[] events = new String[2];
		events[0] = p2pMsg1;
		events[1] = p2pMsg2;
		
		// Moves events into the expanded layout
		for (int i = 0; i < events.length; i++) {
			inboxStyle.addLine(events[i]);
		}
		
        // Moves the expanded layout object into the notification object.
		mBuilder.setStyle(inboxStyle);
        
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS);
        mBuilder.setLights(0xff00ff00, 100, 1500);
        
        //so the notification is automatically cancelled when the user clicks it in the panel.
        mBuilder.setContentIntent(PendingIntent.getActivity(mContext, 0, new Intent(), 0));
        mBuilder.setAutoCancel(true); 
        
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        // notificationID allows you to update the notification later  on
        mNotificationManager.notify(ID, mBuilder.build());
        
        ID--;
        if (ID == 0) {
        	ID = 999;
        }
        Log.d(TAG, "@@ next notification ID: " + ID);
	}
}
