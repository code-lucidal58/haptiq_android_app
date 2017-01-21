package hackfest.pheonix.haptiq.Firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import hackfest.pheonix.haptiq.Constants;

/**
 * Created by aanisha
 */

public class CustomFirebaseInstance extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("demo", "Refreshed token: " + refreshedToken);

        getSharedPreferences(Constants.PREF_IDS,MODE_PRIVATE).edit().putString(Constants.FCM_ID,refreshedToken).apply();
    }
}
