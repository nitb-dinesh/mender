package com.app.handyman.mender.handyman.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.app.handyman.mender.R;

public class OngoingJobActivity extends AppCompatActivity {

    public Button btbegindriving;
    public Button btendjob;
    public Button btstartjob;
    public Button btpausejob;
    public Chronometer Drivetime;
    public Chronometer Labortime;
    public TextView tvtitle;
    public TextView tvdescription;
    public TextView tvdrivetime;
    public TextView tvlabortime;
    public TextView txtPhoneNum;
    public TextView txtLocation;
    private long lastPause;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_job);

        btbegindriving = (Button) findViewById(R.id.btbegindriving);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btbegindriving.setTypeface(myCustomFont);

        btpausejob = (Button) findViewById(R.id.btpausejob);
        Typeface myCustomFont1=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btpausejob.setTypeface(myCustomFont1);

        btendjob = (Button) findViewById(R.id.btendjob);
        Typeface myCustomFont2=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btendjob.setTypeface(myCustomFont2);

        btstartjob = (Button) findViewById(R.id.btstartjob);
        Typeface myCustomFont3=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btstartjob.setTypeface(myCustomFont3);

        Drivetime = (Chronometer) findViewById(R.id.Drivetime);
        Typeface myCustomFont4=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        Drivetime.setTypeface(myCustomFont4);

        Labortime = (Chronometer) findViewById(R.id.Labortime);
        Typeface myCustomFont5=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        Labortime.setTypeface(myCustomFont5);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        Typeface myCustomFont6=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvtitle.setTypeface(myCustomFont6);

        tvdescription = (TextView) findViewById(R.id.tvdescription);
        Typeface myCustomFont7=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvdescription.setTypeface(myCustomFont7);

        tvdrivetime = (TextView) findViewById(R.id.tvdrivetime);
        Typeface myCustomFont8=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvdrivetime.setTypeface(myCustomFont8);

        tvlabortime = (TextView) findViewById(R.id.tvlabortime);
        Typeface myCustomFont9=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvlabortime.setTypeface(myCustomFont9);

        txtPhoneNum = (TextView) findViewById(R.id.txtPhoneNum);
        Typeface myCustomFont10=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtPhoneNum.setTypeface(myCustomFont10);

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        Typeface myCustomFont11=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtLocation.setTypeface(myCustomFont11);

        btstartjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPause != 0) {
                    Labortime.setBase(Labortime.getBase() + SystemClock.elapsedRealtime() - lastPause);
                } else {
                    Labortime.setBase(SystemClock.elapsedRealtime());
                }
                Labortime.start();
                Drivetime.stop();
                btstartjob.setEnabled(false);
                btpausejob.setEnabled(true);

            }
        });

        btpausejob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastPause = SystemClock.elapsedRealtime();
                Drivetime.stop();
                Labortime.stop();
                btpausejob.setEnabled(false);
                btstartjob.setEnabled(true);
                btbegindriving.setEnabled(true);
            }
        });
        btendjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Labortime.stop();
                Labortime.setBase(SystemClock.elapsedRealtime());
                Drivetime.stop();
                Drivetime.setBase(SystemClock.elapsedRealtime());
                lastPause = 0;
                btstartjob.setEnabled(true);
                btpausejob.setEnabled(false);
                if (v == btendjob){
                startActivity(new Intent(OngoingJobActivity.this, EndJobActivity.class));
             }
            }
        });
        btbegindriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lastPause != 0) {
                    Drivetime.setBase(Drivetime.getBase() + SystemClock.elapsedRealtime() - lastPause);
                } else {
                    Drivetime.setBase(SystemClock.elapsedRealtime());
                }
                Drivetime.start();
                Labortime.stop();
                btbegindriving.setEnabled(false);
                btpausejob.setEnabled(true);
                btstartjob.setEnabled(true);
            }
        });
    }
}
