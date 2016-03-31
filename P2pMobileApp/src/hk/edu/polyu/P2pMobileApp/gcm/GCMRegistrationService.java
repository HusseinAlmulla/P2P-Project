package hk.edu.polyu.P2pMobileApp.gcm;

import java.io.IOException;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import hk.edu.polyu.P2pMobileApp.R;

public class GCMRegistrationService extends IntentService {

	private static final String TAG = "GCMRegistrationService";
	private static final String[] TOPICS = { "global" };

	public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
	public static final String REGISTRATION_COMPLETE = "registrationComplete";

	public static String DEVICE_TOKEN = null;
	
	public GCMRegistrationService() {
		super(TAG);
		Log.i(TAG, "construct GCMRegistrationService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Log.i(TAG, "onHandleIntent");

		try {
			// [START register_for_gcm]
			// Initially this call goes out to the network to retrieve the
			// token, subsequent calls
			// are local.
			// [START get_token]
			InstanceID instanceID = InstanceID.getInstance(this);
			String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
					GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
			// [END get_token]
			Log.i(TAG, "GCM Default Sender ID: " + getString(R.string.gcm_defaultSenderId));
			Log.i(TAG, "GCM Registration Token: " + token);

			GCMRegistrationService.DEVICE_TOKEN = token;
			
			// TODO: Implement this method to send any registration to your
			// app's servers.
			// sendRegistrationToHsbcAppServer(token);

			// Subscribe to topic channels
			subscribeTopics(token);

			// You should store a boolean that indicates whether the generated
			// token has been
			// sent to your server. If the boolean is false, send the token to
			// your server,
			// otherwise your server should have already received the token.
			sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, true).apply();
			// [END register_for_gcm]

		} catch (Exception e) {
			Log.e(TAG, "Failed to complete token refresh", e);
			// If an exception happens while fetching the new token or updating
			// our registration data
			// on a third-party server, this ensures that we'll attempt the
			// update at a later time.
			sharedPreferences.edit().putBoolean(SENT_TOKEN_TO_SERVER, false).apply();
		}

		// TODO Notify UI that registration has completed, so the progress
		// indicator can be hidden.
	}

	/**
	 * Subscribe to any GCM topics of interest, as defined by the TOPICS
	 * constant.
	 *
	 * @param token
	 *            GCM token
	 * @throws IOException
	 *             if unable to reach the GCM PubSub service
	 */
	// [START subscribe_topics]
	private void subscribeTopics(String token) throws IOException {
		GcmPubSub pubSub = GcmPubSub.getInstance(this);
		for (String topic : TOPICS) {
			pubSub.subscribe(token, "/topics/" + topic, null);
		}
	}
	// [END subscribe_topics]
}
