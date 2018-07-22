package com.app.handyman.mender.myapp.onboarding;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.handyman.mender.R;


public class WelcomeOneAvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_one);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        TextView mTitle = (TextView) findViewById(R.id.title);
        TextView mDescription = (TextView) findViewById(R.id.description);


        Button mNext = (Button) findViewById(R.id.next);

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeOneAvtivity.this, WelcomeTwoActivity.class);
                startActivity(intent);

            }
        });

        mTitle.setTypeface(myCustomFont);
        mDescription.setTypeface(myCustomFont);
        mNext.setTypeface(myCustomFont);

        VideoView view = (VideoView) findViewById(R.id.video_view);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.welcome_1;
        view.setVideoURI(Uri.parse(path));
        view.start();



    }

    @Override
    public void onBackPressed() {

    }
}
