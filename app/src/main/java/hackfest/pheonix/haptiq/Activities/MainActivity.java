package hackfest.pheonix.haptiq.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import hackfest.pheonix.haptiq.R;

public class MainActivity extends AppCompatActivity {

    Button scanCode, addCredential;
    IntentIntegrator intentIntegrator;
    TextView qrResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanCode = (Button) findViewById(R.id.scanCode);
        addCredential= (Button) findViewById(R.id.addCredential);
        qrResult =(TextView) findViewById(R.id.qrResult);
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
