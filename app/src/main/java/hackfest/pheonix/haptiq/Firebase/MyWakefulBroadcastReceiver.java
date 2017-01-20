package hackfest.pheonix.haptiq.Firebase;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import hackfest.pheonix.haptiq.Firebase.CustomFirebaseMessagingService;

/**
 * Created by aanisha
 */

public class MyWakefulBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, CustomFirebaseMessagingService.class);
        startWakefulService(context, service);

    }
}
