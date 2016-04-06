package hk.edu.polyu.P2pMobileApp.gcm;

import com.google.android.gms.gcm.GcmListenerService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import hk.edu.polyu.P2pMobileApp.MainActivity;
import hk.edu.polyu.P2pMobileApp.R;

public class GCMListenerService extends GcmListenerService {
	private static final String TAG = "GCMListenerService";
	
	protected static int ID = 10;
	
    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(message);
        // [END_EXCLUDE]
    }
    // [END receive_message]
	
    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
		// Just bring the app to the foreground if it is running already
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("P2P transfer received")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

		// Applying an expanded layout to a notification
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		// Sets a title for the Inbox in expanded layout
		inboxStyle.setBigContentTitle("P2P transfer received");

		String p2pMsg1 = message.substring(0, message.indexOf(",") + 1);
		String p2pMsg2 = message.substring(message.indexOf(",") + 2, message.indexOf("!") + 1);
		String p2pMsg3 = message.substring(message.indexOf("!") + 1, message.indexOf(":") + 1);
		String p2pMsg4 = message.substring(message.indexOf(":") + 1, message.length());

		String[] events = new String[2];
		events[0] = p2pMsg1;
		events[1] = p2pMsg2;
		if (!p2pMsg4.trim().equals("")) {
			inboxStyle.setSummaryText(p2pMsg3 + p2pMsg4);
		}
		
		// Moves events into the expanded layout
		for (int i = 0; i < events.length; i++) {
			inboxStyle.addLine(events[i]);
		}
		
        // Moves the expanded layout object into the notification object.
        notificationBuilder.setStyle(inboxStyle);
        
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID, notificationBuilder.build());
        
        ID--;
        if (ID == 1) {
        	ID = 10;
        }
        Log.d(TAG, "@@ next notification ID: " + ID);
    }
}
