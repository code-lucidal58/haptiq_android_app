package hackfest.pheonix.haptiq;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.util.Pair;

import com.scottyab.aescrypt.AESCrypt;

import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.util.Random;

/**
 * Created by aanisha
 */

public class Encryption {

    private static int[] evenAllowed = new int[]{2,4,6,8,0};
    private static int[] oddAllowed = new int[]{1,3,5,7,9};

    private static final String SECRET="!#bbdkQE3749&(DN";

    public static String getEncryptedPassword(Context context, String password){
        try{
            String key = decryptKey(context);
            return AESCrypt.encrypt(key, password);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return  null;
    }

    public static String getEncryptedKey(String key){
        try{
            return AESCrypt.encrypt(SECRET, key);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptKey(Context context) throws GeneralSecurityException{
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_IDS, Context.MODE_PRIVATE);
        String key = sharedPreferences.getString(Constants.SECRET_KEY, "");
        return AESCrypt.decrypt(SECRET, key);
    }

//    public static Pair<Packet, Packet> encryptPassword(Context context, String userId, String password){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_IDS, Context.MODE_PRIVATE);
//        String encryptedPassword = null;
//
//        encryptedPassword = getEncryptedPassword(context, password);
//
//        String encryptedKey = null;
//
//        encryptedKey = sharedPreferences.getString(Constants.SECRET_KEY, "");
//
//        return getSecurePackets(userId, encryptedPassword, encryptedKey);
//    }

    public static Pair<Packet, Packet> getSecurePackets(String userId, String encryptedPassword, String encryptedKey, String chromeId){
        String even="", odd="";
        String eEven="", eOdd="";

        for(int i=0;i<encryptedPassword.length();i++){
            if(i%2==0){
                even += encryptedPassword.charAt(i);
            }else{
                odd += encryptedPassword.charAt(i);
            }
        }

        for(int i=0;i<encryptedKey.length();i++){
            if(i%2==0){
                eEven += encryptedKey.charAt(i);
            }else{
                eOdd += encryptedKey.charAt(i);
            }
        }

        Random random = new Random();
        int pe = random.nextInt(evenAllowed.length);
        int po = random.nextInt(oddAllowed.length);
        int ke = random.nextInt(evenAllowed.length);
        int ko = random.nextInt(oddAllowed.length);

        even = evenAllowed[pe] + even;
        odd = oddAllowed[po] + odd;
        eEven = evenAllowed[ke] + eEven;
        eOdd = oddAllowed[ko] + eOdd;

        Packet p1 = new Packet(userId, even, eOdd, "mobile-authentication", chromeId);
        Packet p2 = new Packet(userId, odd, eEven, "mobile-authentication", chromeId);

        return new Pair<>(p1,p2);

    }

    public static class Packet{
        String userId, password, key, event, chromeId;
        /*
        socket.emit(packet.event, packet.getPayload());
         */

        public Packet(String userId, String password, String key, String event, String chromeId) {
            this.userId = userId;
            this.password = password;
            this.key = key;
            this.event = event;
            this.chromeId = chromeId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public JSONObject getPayload(){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userId", this.userId);
                jsonObject.put("password", this.password);
                jsonObject.put("key", this.key);
                jsonObject.put("chromeId", this.chromeId);
            }catch (Exception e){
                e.printStackTrace();
            }
            return jsonObject;
        }
    }


}
