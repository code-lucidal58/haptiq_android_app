package hackfest.pheonix.haptiq.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hackfest.pheonix.haptiq.Databases.UserCredentialsDB;
import hackfest.pheonix.haptiq.Encryption;
import hackfest.pheonix.haptiq.Models.UserCredential;
import hackfest.pheonix.haptiq.R;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
import static android.text.InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD;

public class AccessCredential extends AppCompatActivity {

    EditText username, password;
    AutoCompleteTextView url;
    CheckBox showPassword;
    Button submit;
    UserCredentialsDB userCredentialsDB;
    ArrayList<String> domain = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_credential);

        userCredentialsDB = new UserCredentialsDB(this);
        url = (AutoCompleteTextView) findViewById(R.id.urlEditText);
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        showPassword = (CheckBox) findViewById(R.id.showPassword);
        submit = (Button) findViewById(R.id.submit);

        domain.add("Facebook");
        domain.add("Gmail");
        domain.add("Quora");
        domain.add("LinkedIn");
        domain.add("Github");
        domain.add("StackOverflow");
        domain.add("Twitter");
        domain.add("Pinterest");
        domain.add("Instagram");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, domain);
        url.setAdapter(arrayAdapter);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_WEB_PASSWORD);
                }
            }
        });

        url.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){

                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlString = url.getText().toString();
                String passwordString = password.getText().toString();
                String usernameString = username.getText().toString();

                if (urlString.isEmpty() || passwordString.isEmpty() || usernameString.isEmpty()) {
                    Toast.makeText(AccessCredential.this, "Please fill all details", Toast.LENGTH_SHORT).show();
                } else {
                    if(userCredentialsDB.addUserCredential(
                            new UserCredential(usernameString,
                                    passwordString, urlString))){
                        Log.e("demo", userCredentialsDB.getAllCredentials().get(0).getUrl());

                        Toast.makeText(AccessCredential.this, "Credentials for "+url+" is saved!",Toast.LENGTH_SHORT).show();
                        url.setText("");
                        password.setText("");
                        username.setText("");
                    } else {
                        Toast.makeText(AccessCredential.this,"Credentials already exist for "+urlString,Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public class getImage extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }
}
