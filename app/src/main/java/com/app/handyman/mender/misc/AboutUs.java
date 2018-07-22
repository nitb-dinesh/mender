package com.app.handyman.mender.misc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.app.handyman.mender.R;


/**
 * Activity to give details about the app, so user knows what this app is about.
 */

public class AboutUs extends AppCompatActivity {

    private TextView h1;
    private TextView t1, t2, t3, t4, t5, t6, t18;

    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        Typeface myBoldFace = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");

        h1 = (TextView) findViewById(R.id.h1);

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        t6 = (TextView) findViewById(R.id.t6);
        t18 = (TextView) findViewById(R.id.t18);
        h1.setTypeface(myCustomFont);
    //    h2.setTypeface(myCustomFont);
    //    h3.setTypeface(myCustomFont);

        t1.setTypeface(myCustomFont);
        t2.setTypeface(myCustomFont);
        t3.setTypeface(myCustomFont);
        t4.setTypeface(myCustomFont);
        t5.setTypeface(myCustomFont);
        t6.setTypeface(myCustomFont);
        t18.setTypeface(myCustomFont);

    }
}
