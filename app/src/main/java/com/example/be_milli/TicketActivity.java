package com.example.be_milli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.awt.font.TextAttribute;
import java.util.Random;
import java.util.Set;

public class TicketActivity extends AppCompatActivity {
    TextView ticketNumberText;
    Button btnTicketGen;

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
                int ticketNumber=rn.nextInt((9999999-1000000)+1)+1000000;
                String numberString=String.valueOf(ticketNumber);
                tickerView.setText(numberString);
            }
        });
    }
}