package com.example.be_milli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private static int Auth_Request_Code = 7192;
    private List<AuthUI.IdpConfig> providers;
    FirebaseAuth mAuth;
    FirebaseUser user;

    Button getStarted;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

         getStarted=findViewById(R.id.getStartedBtn);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                if (user != null) {
                    Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
                    startActivity(i);

                }
                else{
                    login();
                }

            }
    });

}


    private void login() {

        providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("bd").build()
        );
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(WelcomeActivity.this, SignUpActivity.class);
            startActivity(i);

        } else {

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.logo)
                    .setTheme(R.style.LoginTheme)
                    .build(), Auth_Request_Code);
        }
    }
}