package com.example.be_milli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.awt.font.TextAttribute;
import java.util.Random;
import java.util.Set;

public class TicketActivity extends AppCompatActivity {
    TextView ticketNumberText;
    Button btnTicketGen;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        final TickerView tickerView = findViewById(R.id.textTicket);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());



        btnTicketGen=findViewById(R.id.buttonTicketGen);
        tickerView.setAnimationDuration(3000);
        tickerView.setAnimationInterpolator(new OvershootInterpolator());
        tickerView.setGravity(Gravity.START);
        tickerView.setText("0000000");



        btnTicketGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rn=new Random();
                //int ticketNumber=rn.nextInt((9999999-1000000)+1)+1000000;
                int ticketNumber=rn.nextInt(10);
                final String numberString=String.valueOf(ticketNumber);
                Query ticketQuery= FirebaseDatabase.getInstance().getReference().child("Ticket").orderByChild("ticketnumber").equalTo(numberString);

                ticketQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        System.out.println("sagor "+snapshot.getChildrenCount());
                       if (snapshot.getChildrenCount()>0){
                            Toast.makeText(TicketActivity.this,"Same Number",Toast.LENGTH_SHORT).show();

                        }
                        else{
                           tickerView.setText(numberString);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}