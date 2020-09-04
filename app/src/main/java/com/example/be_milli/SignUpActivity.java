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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText signName,signEmail,signPassword,signConfirmPass;

    String userId;
    FirebaseAuth mAuth;
    FirebaseDatabase userDatabase;
    DatabaseReference reference;

    Button signConfirm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signName=findViewById(R.id.signNameText);
        signEmail=findViewById(R.id.signEmailText);
        signPassword=findViewById(R.id.signPassText);
        signConfirmPass=findViewById(R.id.signConPassText);

        signConfirm=findViewById(R.id.buttonSignConfirm);




        signConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth=FirebaseAuth.getInstance();
                userDatabase=FirebaseDatabase.getInstance();
                reference=userDatabase.getReference("User");

                final String email=signEmail.getText().toString().trim();
                final String password=signPassword.getText().toString().trim();
                final String name=signName.getText().toString().trim();
                final String conPass=signConfirmPass.getText().toString().trim();
                final String phoneNumber=getIntent().getStringExtra("phoneNumber");

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

                else if(password.length()<6){
                    signPassword.setError("Password must be more than 6 character");
                    return;
                }
                else if(!password.equals(conPass)){
                    signConfirmPass.setError("Password not matching");
                    return;
                }



                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            HelperClass helperClass=new HelperClass(name,email,password,phoneNumber);
                            userId=mAuth.getCurrentUser().getUid();
                            reference.child(userId).setValue(helperClass);
                            Toast.makeText(SignUpActivity.this,"Registration done",Toast.LENGTH_SHORT).show();
                            new Intent(SignUpActivity.this, Home1Activity.class);


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
