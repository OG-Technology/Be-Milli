package com.example.be_milli;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Date;

import cn.iwgang.countdownview.CountdownView;

public class Home1Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SpaceNavigationView dropDownNav;
    ImageView proImg;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    long countDownToNewYear;
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home1);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        proImg=findViewById(R.id.profile_image2);


        dropDownNav=findViewById(R.id.space);
        dropDownNav.addSpaceItem(new SpaceItem("Notification", R.drawable.ntf_icon));
        dropDownNav.addSpaceItem(new SpaceItem("Message", R.drawable.msg_icon));



        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference().child("Time");
        final CountdownView countDown=findViewById(R.id.countdownView);

//countdown
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time=snapshot.getValue().toString();
                System.out.println(time);
                long targetDate=Long.parseLong(time);

                System.out.println(targetDate);
                Date now=new Date();

                long currentTime=now.getTime();
                System.out.println("Current Time"+currentTime);

                countDownToNewYear=targetDate-currentTime;
                System.out.println("to Time"+countDownToNewYear);
                countDown.start(countDownToNewYear);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        proImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        dropDownNav.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(Home1Activity.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Home1Activity.this, WinnerActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(Home1Activity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(Home1Activity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

}
    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.your_tickets:
                break;
            case R.id.buy_tickets:
        }

        return true;
    }
}

