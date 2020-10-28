package com.example.be_milli;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    private static int Auth_Request_Code = 7192;
    private List<AuthUI.IdpConfig> providers;


    //Button getStarted;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


                if(FirebaseAuth.getInstance().getCurrentUser()!=null){
               startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
                finish();



            }
    }


public void handleLoginRegister(View view){

        providers = Arrays.asList(
                //new AuthUI.IdpConfig.GoogleBuilder().build(),
                //new AuthUI.IdpConfig.FacebookBuilder().build(),
               // new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().setDefaultCountryIso("bd").build()
        );

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                    .setLogo(R.drawable.logo)
                    .setTheme(R.style.LoginTheme)
                    .build(), Auth_Request_Code);


        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Auth_Request_Code){
            if(resultCode==RESULT_OK){
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG,"OnActivityResult: "+user.getPhoneNumber());
                if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){
                    Toast.makeText(this,"welcome new user",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,SignUpActivity.class);
                    startActivity(intent);
                    this.finish();

                }
                else{
                    //returning user
                    Toast.makeText(this,"Returning user",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(this,Home1Activity.class);
                    startActivity(intent);
                    this.finish();
                }
            }
            else{
                //sign in failed
            }
        }
    }
}
