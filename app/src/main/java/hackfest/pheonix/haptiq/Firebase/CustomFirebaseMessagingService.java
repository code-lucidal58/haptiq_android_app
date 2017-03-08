package hackfest.pheonix.haptiq.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hackfest.pheonix.haptiq.Activities.Fingerprint;
import hackfest.pheonix.haptiq.Activities.MainActivity;
import hackfest.pheonix.haptiq.Constants;
import hackfest.pheonix.haptiq.R;

/**
 * Created by aanisha
 */

public class CustomFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getData());
        Map<String,String> message = remoteMessage.getData();
        sendNotification(message);

        JSONObject jsonObject = getObjectFromMessage(message);

        Intent intent = new Intent(this, Fingerprint.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(jsonObject.optString("type").equals("browser-authentication")){
            intent.putExtra("type", "browser");
        }

        startActivity(intent);
    }

    private JSONObject getObjectFromMessage(Map<String, String> message){
        return new JSONObject(message);
    }


    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(Map<String,String> messageBody) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String chromeId = "", url = "";
        JSONObject jsonObject = getObjectFromMessage(messageBody);
        try {
            chromeId = jsonObject.optString(Constants.CHROME_EXTENSION_ID);
            url = jsonObject.optString(Constants.KEY_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (chromeId.equals(getSharedPreferences(Constants.PREF_IDS, MODE_PRIVATE).getString(Constants.CHROME_EXTENSION_ID, ""))) {

            getSharedPreferences(Constants.PREF_IDS,MODE_PRIVATE).edit().putString(Constants.TO_SEARCH_URL,url).apply();

            String notificationString = "Authenticate for "+url;
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(Constants.APP)
                    .setContentText(notificationString)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
