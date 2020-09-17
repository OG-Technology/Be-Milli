package com.example.be_milli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class TicketActivity extends AppCompatActivity {
    TextView ticketNumberText;
    Button btnTicketGen,btnTicketCon, buttonTicketPay;
    ElegantNumberButton btnTicketNum;


    FirebaseAuth tAuth;
    FirebaseDatabase TicketDatabase;
    DatabaseReference TicketReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        final TickerView tickerView = findViewById(R.id.textTicket);
        tickerView.setCharacterLists(TickerUtils.provideNumberList());

        tAuth=FirebaseAuth.getInstance();
        TicketDatabase=FirebaseDatabase.getInstance();
        TicketReference=TicketDatabase.getReference("Ticket");
        final String userId=tAuth.getCurrentUser().getUid();


        btnTicketGen = findViewById(R.id.buttonTicketGen);
        btnTicketCon=findViewById(R.id.buttonTicketCon);
        btnTicketNum = (ElegantNumberButton) findViewById(R.id.btnTicketNum);

        tickerView.setAnimationDuration(3000);
        tickerView.setAnimationInterpolator(new OvershootInterpolator());
        tickerView.setGravity(Gravity.START);
        tickerView.setText("0000000");

        final SwipeMenuListView listView = (SwipeMenuListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<>();


        final ArrayAdapter adapter = new ArrayAdapter(TicketActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);




        btnTicketNum.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = btnTicketNum.getNumber();
                Log.e("Num", num);
            }
        });


        btnTicketGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random rn = new Random();
                int ticketNumber=rn.nextInt((9999999-1000000)+1)+1000000;
                final String numberString = String.valueOf(ticketNumber);

                System.out.println("numberString"+numberString);
                final Query ticketQuery = FirebaseDatabase.getInstance().getReference().child("TicketNumber").orderByChild("ticketNumber").equalTo(numberString);
                ticketQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        System.out.println("sagor " + snapshot.getChildrenCount());
                        if (snapshot.getChildrenCount() > 0) {
                            Toast.makeText(TicketActivity.this, "Same Number", Toast.LENGTH_SHORT).show();

                        } else {
                            tickerView.setText(numberString);

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
            }
        });

        btnTicketCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String ticketConfirm=tickerView.getText();
                //HelperClass helperClass=new HelperClass(ticketConfirm);

                System.out.println(tickerView.getText());
                list.add(tickerView.getText());
                adapter.notifyDataSetChanged();
                TicketReference.child(userId).child("ticket").setValue(list);
            }
        });

        //buttonTicketPay.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {

        //    }
        //});


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

        listView.setMenuCreator(creator);

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
        });

    }
}