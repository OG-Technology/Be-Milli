package com.example.be_milli;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.be_milli.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TicketActivity extends AppCompatActivity {
    TextView ticketNumberText;
    Button btnTicketGen,btnTicketCon, buttonTicketPay;
    ElegantNumberButton btnTicketNum;
    TickerView tickerView;

    FirebaseAuth tAuth;
    FirebaseDatabase TicketDatabase;
    DatabaseReference TicketReference,userTicketReference;
    String ticketConfirm;

    TextView one,two,three,four,five;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        tickerView = findViewById(R.id.textTicket);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        tAuth=FirebaseAuth.getInstance();
        TicketDatabase=FirebaseDatabase.getInstance();
        TicketReference=TicketDatabase.getReference("Ticket");
        userTicketReference=TicketDatabase.getReference("UserTicket");
        final String userId=tAuth.getCurrentUser().getUid();
        final Map tickets=new HashMap();
        final Map userTickets=new HashMap();

        btnTicketGen = findViewById(R.id.buttonTicketGen);
        btnTicketCon=findViewById(R.id.buttonTicketCon);
        buttonTicketPay=findViewById(R.id.buttonTicketPay);

        tickerView.setAnimationDuration(3000);
        tickerView.setAnimationInterpolator(new OvershootInterpolator());
        tickerView.setGravity(Gravity.START);
        tickerView.setText("0000000");

        one=findViewById(R.id.textView5);
        two=findViewById(R.id.textView6);
        three=findViewById(R.id.textView7);
        four=findViewById(R.id.textView8);




        //final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<>();


        final ArrayAdapter adapter = new ArrayAdapter(TicketActivity.this, android.R.layout.simple_list_item_1, list);
        //listView.setAdapter(adapter);







        btnTicketGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ranGen();

            }
        });
        buttonTicketPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnTicketCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ticketConfirm=tickerView.getText();
                three.setText(ticketConfirm);

                DatabaseReference ticketKey=TicketReference.child("Ticket").push();
                String ticketPushId=ticketKey.getKey();

                tickets.put(ticketPushId +"/"+"ticket",ticketConfirm);

                // System.out.println(tickerView.getText());
                //list.add(tickerView.getText());
                //adapter.notifyDataSetChanged();
                TicketReference.updateChildren(tickets);
                btnTicketCon.setVisibility(View.INVISIBLE);
                btnTicketGen.setVisibility(View.VISIBLE);
                one.setVisibility(View.VISIBLE);
                two.setVisibility(View.VISIBLE);
                three.setVisibility(View.VISIBLE);
                four.setVisibility(View.VISIBLE);
                buttonTicketPay.setVisibility(View.VISIBLE);

            }
        });

        buttonTicketPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userTicketKey=userTicketReference.child("UserTicket").child(userId).push();
                String userTicketPushId=userTicketKey.getKey();
                if (!ticketConfirm.equals("0000000")) {
                    userTickets.put(userTicketPushId, ticketConfirm);
                    userTicketReference.child(userId).updateChildren(tickets);
                    ticketConfirm="0000000";
                    tickerView.setText("0000000");
                }
            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item

                // set item background

                // set item title

                // add to menu


                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_baseline_delete_24);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        /*listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        System.out.println("onMenuItemClick: clicked item " + index);
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });*/

    }
    private void ranGen(){
        Random rn = new Random();
        //int ticketNumber=rn.nextInt((9999999-1000000)+1)+1000000;
        int ticketNumber=rn.nextInt(20);
        final String numberString = String.valueOf(ticketNumber);

        System.out.println("numberString"+numberString);
        final Query ticketQuery = FirebaseDatabase.getInstance().getReference().child("Ticket").orderByChild("ticket").equalTo(numberString);
        ticketQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                System.out.println("Sagor " + snapshot.getChildrenCount());
                if (snapshot.getChildrenCount() > 0) {
                    //Toast.makeText(TicketActivity.this, "Same Number", Toast.LENGTH_SHORT).show();
                    ranGen();
                } else {
                    tickerView.setText(numberString);
                    btnTicketCon.setVisibility(View.VISIBLE);
                    btnTicketGen.setVisibility(View.INVISIBLE);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }
}