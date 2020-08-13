package com.example.be_milli;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    ImageView homeProfileImage,navProfileImage;
    RelativeLayout homeView;
    LinearLayout navView;
    Animation fromTop,fromBottom;
    TextView navName,navPlace;

    Button nav1,nav2,nav3,nav4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeProfileImage=(ImageView) findViewById(R.id.profile_image2);
        navProfileImage=(ImageView)findViewById(R.id.profile_image);
        homeView=(RelativeLayout)findViewById(R.id.homeRelativeLayout);
        navView=(LinearLayout) findViewById(R.id.homeLinearLayout);
        fromTop= AnimationUtils.loadAnimation(this,R.anim.fromtop);
        fromBottom=AnimationUtils.loadAnimation(this,R.anim.frombottom);

        navName=(TextView)findViewById(R.id.navNameTextView);
        navPlace=(TextView)findViewById(R.id.navPlaceTextView);

        nav1=(Button)findViewById(R.id.navMenuButton1);
        nav2=(Button)findViewById(R.id.navMenuButton2);
        nav3=(Button)findViewById(R.id.navMenuButton3);
        nav4=(Button)findViewById(R.id.navMenuButton4);



        homeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeView.animate().translationX(0);
                navView.animate().translationX(0);
                nav1.startAnimation(fromBottom);
                nav2.startAnimation(fromBottom);
                nav3.startAnimation(fromBottom);
                nav4.startAnimation(fromBottom);

                navProfileImage.startAnimation(fromTop);
                navName.startAnimation(fromTop);
                navPlace.startAnimation(fromTop);
            }
        });
       homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeView.animate().translationX(-1000);
                navView.animate().translationX(-1000);
            }
        });
    }

}