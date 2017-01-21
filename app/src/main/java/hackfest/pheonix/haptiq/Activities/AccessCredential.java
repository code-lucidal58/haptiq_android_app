package hackfest.pheonix.haptiq.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import hackfest.pheonix.haptiq.Constants;
import hackfest.pheonix.haptiq.Databases.UserCredentialsDB;
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
    String[] domain = {"Facebook", "Gmail", "Quora", "LinkedIn", "GitHub", "StackOverFlow", "Twitter", "Pinterest", "Instagram"};

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

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    password.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    password.setInputType(TYPE_TEXT_VARIATION_WEB_PASSWORD);
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
                    SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_IDS, Context.MODE_PRIVATE);
                    String encryptedPassword = null;
                    try {
                        encryptedPassword = AESCrypt.encrypt(sharedPreferences.getString(Constants.SECRET_KEY, ""), passwordString);
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }
                    userCredentialsDB.addUserCredential(
                            new UserCredential(usernameString, encryptedPassword, urlString));
                    Log.e("demo", userCredentialsDB.getAllCredentials().get(0).getUrl());
                }
            }
        });
    }
}
