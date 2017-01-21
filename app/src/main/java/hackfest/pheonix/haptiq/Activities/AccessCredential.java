package hackfest.pheonix.haptiq.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import hackfest.pheonix.haptiq.R;

public class AccessCredential extends AppCompatActivity {

    EditText username, password;
    AutoCompleteTextView url;
    String[] domain={"Facebook","Gmail","Quora","LinkedIn","GitHub","StackOverFlow","Twitter","Pinterest","Instagram"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_credential);

        url = (AutoCompleteTextView) findViewById(R.id.url);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        url.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
