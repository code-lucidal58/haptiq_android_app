package hackfest.pheonix.haptiq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.net.URISyntaxException;

import hackfest.pheonix.haptiq.R;

public class MainActivity extends AppCompatActivity {

    Button scanCode, addCredential, fingerprint;
    IntentIntegrator intentIntegrator;
    TextView qrResult;
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mSocket = IO.socket("http://52.25.225.108");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        mSocket.emit("phone-join");
        mSocket.on("join-with", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("demo",args[0].toString()+" length "+args[0].toString().length());
            }
        });
        scanCode = (Button) findViewById(R.id.scanCode);
        addCredential = (Button) findViewById(R.id.addCredential);
        fingerprint = (Button) findViewById(R.id.fingerprint);
        qrResult = (TextView) findViewById(R.id.qrResult);
        intentIntegrator = new IntentIntegrator(this);
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intentIntegrator.initiateScan();
            }
        });

        addCredential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AccessCredential.class));
            }
        });

        fingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Fingerprint.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                qrResult.setText(result.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
