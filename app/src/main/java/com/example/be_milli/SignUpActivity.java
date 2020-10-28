package com.example.be_milli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    EditText signName, signEmail, signPassword, signPhone;

    String userId;

    FirebaseDatabase userDatabase;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth mAuth;

    Button signConfirm;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);

        signName = findViewById(R.id.signNameText);
        signEmail = findViewById(R.id.signEmailText);
        signPassword = findViewById(R.id.signAddText);
        //signPhone = findViewById(R.id.signPhoneText);
        //signConfirmPass = findViewById(R.id.signConPassText);

        signConfirm = findViewById(R.id.buttonSignConfirm);


        signConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                userDatabase = FirebaseDatabase.getInstance();
                reference = userDatabase.getReference("User");

                final String email = signEmail.getText().toString().trim();
                final String password = signPassword.getText().toString().trim();
                final String name = signName.getText().toString().trim();
                // final String conPass = signConfirmPass.getText().toString().trim();
                final String phoneNumber = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

                if (TextUtils.isEmpty(name)) {
                    signName.setError("Name is required");
                    return;
                } else if (TextUtils.isEmpty(email)) {
                    signEmail.setError("Email is required");
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    signPassword.setError("Address is required");
                    return;
                }


                HelperClass helperClass = new HelperClass(name, email, password, phoneNumber);
                userId = mAuth.getCurrentUser().getUid();
                reference.child(userId).setValue(helperClass);
                Intent i = new Intent(SignUpActivity.this, Home1Activity.class);
                startActivity(i);
                finish();


            }
        });
    }



}

