package hackfest.pheonix.haptiq.FingerprintAuthentication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.util.HashMap;

import hackfest.pheonix.haptiq.Constants;
import hackfest.pheonix.haptiq.Databases.UserCredentialsDB;
import hackfest.pheonix.haptiq.Encryption;
import hackfest.pheonix.haptiq.Models.UserCredential;
import hackfest.pheonix.haptiq.R;

/**
 * Created by aanisha
 */

public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {


    private Context context;
    private FingerprintManager manager;
    FingerprintManager.CryptoObject cryptoObject;
    Socket socket;

    // Constructor
    public FingerprintHandler(Context mContext) {
        context = mContext;
    }


    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject, Socket socket) {
        this.manager = manager;
        this.cryptoObject = cryptoObject;
        this.socket=socket;
        CancellationSignal cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.update("Fingerprint Authentication error\n" + errString, false);
    }


    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.update("Fingerprint Authentication help\n" + helpString, false);
    }


    @Override
    public void onAuthenticationFailed() {
        this.update("Fingerprint Authentication failed.", false);
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("Fingerprint Authentication succeeded.", true);
    }


    private void update(String e, Boolean success){
        TextView textView = (TextView) ((Activity)context).findViewById(R.id.errorText);
        textView.setText(e);
        if(success){
            UserCredentialsDB userCredentialsDB = new UserCredentialsDB(context);
            UserCredential uc = userCredentialsDB.getCredential(context.getSharedPreferences(Constants.PREF_IDS,Context.MODE_PRIVATE)
                    .getString(Constants.TO_SEARCH_URL,""));
            String username = uc.getUsername();
            String encryptedPassword = uc.getPassword();
            String encryptedKey =context.getSharedPreferences(Constants.PREF_IDS,Context.MODE_PRIVATE)
                    .getString(Constants.SECRET_KEY,"");
            Log.e("demo",username);
            Log.e("demo",encryptedPassword);
            Log.e("demo",encryptedKey);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("chromeId", context.getSharedPreferences(Constants.PREF_IDS, Context.MODE_PRIVATE).getString(Constants.CHROME_EXTENSION_ID, ""));
                jsonObject.put("userId", username);
                jsonObject.put("password", encryptedPassword);
            }catch (Exception ce){
                ce.printStackTrace();
            }
            socket.emit("mobile-authenticated", jsonObject);
//            ((Activity) context).finish();
        }
//        else {
//            startAuth(manager,cryptoObject);
//        }
    }
}
