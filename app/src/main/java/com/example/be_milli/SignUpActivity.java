package com.example.be_milli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity extends AppCompatActivity {
    EditText signName,signEmail,signPassword;
    FirebaseAuth mAuth;
    Button signConfirm,signGoogle,signFacebook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signName=findViewById(R.id.signNameText);
        signEmail=findViewById(R.id.signEmailText);
        signPassword=findViewById(R.id.signPassText);

        signConfirm=findViewById(R.id.buttonSignConfirm);
        signGoogle=findViewById(R.id.buttonSignGoogle);
        signFacebook=findViewById(R.id.buttonSignFacebook);

        mAuth=FirebaseAuth.getInstance();



        signConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=signEmail.getText().toString().trim();
                String password=signPassword.getText().toString().trim();
                String name=signName.getText().toString().trim();


                if (TextUtils.isEmpty(name)){
                    signName.setError("Name is required");
                    return;
                }

                else if (TextUtils.isEmpty(email)){
                    signEmail.setError("Email is required");
                    return;
                }

                else if (TextUtils.isEmpty(password)){
                    signPassword.setError("Password is required");
                    return;
                }

                if(password.length()<6){
                    signPassword.setError("Password must be more than 6 character");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                        Toast.makeText(SignUpActivity.this,"Registration done",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUpActivity.this,HomeActivity.class));
                        }
                        else{
                            Toast.makeText(SignUpActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }
}
