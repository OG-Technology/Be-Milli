package com.example.be_milli;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
//import java.util.Date;

import org.apache.commons.net.time.TimeTCPClient;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import cn.iwgang.countdownview.CountdownView;

public class Home1Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    SpaceNavigationView dropDownNav;
    ImageView proImg;
    FirebaseDatabase mDatabase;
    DatabaseReference mReference;
    FirebaseUser user;
    TextView userName,headerName;
    Button buyTicket;

    String dateStr;
    String[] Date;
    String CurrentDate;
    long CurrentTime;
    int Month;
    String CurrentTime2;
    String DateAndTime;
    String Datw;
    Context context;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mLister;

    String userId;
    long countDownToNewYear;

    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mLister);
    }

    @Override
    protected void onStop() {

        if (mLister != null) mFirebaseAuth.removeAuthStateListener(mLister);
        super.onStop();
    }
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        init();
        setContentView(R.layout.activity_home1);
        buyTicket=findViewById(R.id.button2);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        proImg=findViewById(R.id.profile_image2);
        userName=findViewById(R.id.nameTextView2);
            View headerView=navigationView.getHeaderView(0);
            headerName=headerView.findViewById(R.id.navNameTextView);

        dropDownNav=findViewById(R.id.space);
        dropDownNav.addSpaceItem(new SpaceItem("Notification", R.drawable.ntf_icon));
        dropDownNav.addSpaceItem(new SpaceItem("Message", R.drawable.msg_icon));

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            userId = user.getUid();
        }
        mDatabase= FirebaseDatabase.getInstance();
        mReference=mDatabase.getReference();
        final CountdownView countDown=findViewById(R.id.countdownView);

        if(isNetworkAvailable()){new GoogleTime().execute();}else
        {
            Toast.makeText(context, "Sorry no Internet", Toast.LENGTH_SHORT).show();
        }



//countdown
        mReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String time=snapshot.child("Time").getValue().toString();
                System.out.println(time);
                long targetDate=Long.parseLong(time);
                Date now=new Date();

                long currentTime=now.getTime();
                countDownToNewYear=targetDate-currentTime;


                countDown.start(countDownToNewYear);

                String nameUser=snapshot.child("User").child(userId).child("name").getValue().toString();
                userName.setText(nameUser);
                headerName.setText(nameUser);


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

        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home1Activity.this, TicketActivity.class);
                startActivity(i);
            }
        });

}
    private boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    class GoogleTime extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(new HttpGet("https://google.com/"));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){

                    dateStr = response.getFirstHeader("Date").getValue();
                    Date  = dateStr.split(" ");
                    int counter = 0 ;
                    for(String a : Date){
                        counter = counter + 1;

                        Log.e("Time - " + counter  , a.trim() );

                    }


                    CurrentDate = Date[1]+"/"+Month+"/" + Date[3];
                    //  String dateFormat = Date[0]
                    String[] Timee = Date[4].split(":");
                    int hour = Integer.parseInt(Timee[0]);

                    Log.e("Timeee", hour + " -- " + Timee[1] + " --" + Timee[2]);
                    int seconds = Integer.parseInt(Timee[2]);
                    int minutes = Integer.parseInt(Timee[1]);
                    CurrentTime = seconds + minutes + hour;


                    CurrentTime2 = Date[3]+"-" + Month +"-" + Date[1] +" " + hour+":" +Timee[1]+":"+Timee[2];


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    java.util.Date myDate = simpleDateFormat.parse(CurrentTime2);





                    Date date1 = null;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        date1 =myDate;
                    }catch (Exception e){

                    }
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Dhaka"));
                    //        System.out.println(sdf.format(date));
                    Log.e("@@@Date: ",String.valueOf(sdf.format(date1)));
                    DateAndTime = String.valueOf(sdf.format(date1));

                    Date date = sdf.parse(DateAndTime);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    int Da = calendar.get(Calendar.DATE);
                    int Mm = calendar.get(Calendar.MONTH);
                    int yy = calendar.get(Calendar.YEAR);


                    Datw = Da +""+Mm+""+yy;









                    // Here I do something with the Date String

                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            }catch (ClientProtocolException e) {
                Log.d("Response", e.getMessage());
            }catch (IOException e) {
                Log.d("Response", e.getMessage());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(Home1Activity.this, "Time " + CurrentDate+ "Time :" + DateAndTime +"?"+ Datw    , Toast.LENGTH_SHORT).show();
            Log.e("Date", CurrentDate);
            Log.e("DateTime",CurrentTime+"");


        }


    }

    private void init() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mLister = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(Home1Activity.this, "your id=" + user.getUid(), Toast.LENGTH_SHORT).show();
                } else {

                    Intent i = new Intent(Home1Activity.this, WelcomeActivity.class);
                    startActivity(i);

                }

            }
        };


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

