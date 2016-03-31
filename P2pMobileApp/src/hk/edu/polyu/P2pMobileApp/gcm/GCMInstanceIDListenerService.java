package hk.edu.polyu.P2pMobileApp.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;

import android.content.Intent;
import android.util.Log;

public class GCMInstanceIDListenerService extends InstanceIDListenerService {
	private static final String TAG = "GCMInstanceIDListenerService";
	
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
    	Log.i(TAG, "GCM token refreshed");
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, GCMRegistrationService.class);
        startService(intent);
    }
    // [END refresh_token]
}
