package com.example.be_milli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText logInEmail,logInPassword;
    Button signUp,logIn;

    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUp= findViewById(R.id.signUpButton);
        logInEmail=findViewById(R.id.loginEmail);
        logInPassword=findViewById(R.id.loginPassword);
        logIn=findViewById(R.id.loginButton);
        mAuth=FirebaseAuth.getInstance();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email=logInEmail.getText().toString().trim();
                String Password=logInPassword.getText().toString().trim();

                if (TextUtils.isEmpty(Email)){
                    logInEmail.setError("Email is required");
                    return;
                }

                else if (TextUtils.isEmpty(Password)){
                    logInPassword.setError("Password is required");
                    return;
                }
                else if(!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){

                    mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login done",Toast.LENGTH_SHORT).show();


                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

            }
        });



        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
            }
        });
    }
}
