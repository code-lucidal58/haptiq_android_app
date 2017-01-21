package hackfest.pheonix.haptiq;

import android.support.v4.util.Pair;

import java.util.Random;

/**
 * Created by aanisha
 */

public class Encryption {

    private int[] evenAllowed = new int[]{2, 4, 6, 8, 0};
    private int[] oddAllowed = new int[]{1, 3, 5, 7, 9};

    private Pair<Packet, Packet> getSecurePackets(String userId, String encryptedPassword, String encryptedKey) {
        String even = "", odd = "";
        String eEven = "", eOdd = "";

        for (int i = 0; i < encryptedPassword.length(); i++) {
            if (i % 2 == 0) {
                even += encryptedPassword.charAt(i);
            } else {
                odd += encryptedPassword.charAt(i);
            }
        }

        for (int i = 0; i < encryptedKey.length(); i++) {
            if (i % 2 == 0) {
                eEven += encryptedKey.charAt(i);
            } else {
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

        Packet p1 = new Packet(userId, even, eOdd, "mobile-authentication");
        Packet p2 = new Packet(userId, odd, eEven, "mobile-authentication");

        return new Pair<>(p1, p2);

    }

    public class Packet {
        String userId, password, key, event;



        /*
        socket.emit(event, {userId: userId, password: password, key: key});


         */

        public Packet(String userId, String password, String key, String event) {
            this.userId = userId;
            this.password = password;
            this.key = key;
            this.event = event;
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
    }


}
