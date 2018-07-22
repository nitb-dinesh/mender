package com.app.handyman.mender.common.activity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.app.handyman.mender.R;

public class LearnMoreActivity extends AppCompatActivity {


    private TextView h1;
    private TextView t1, t2, t3, t4, t5, t6, t18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_more);

        t1 = (TextView) findViewById(R.id.t1);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t1.setTypeface(myCustomFont);

        t2 = (TextView) findViewById(R.id.t2);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t2.setTypeface(myCustomFont1);

        t3 = (TextView) findViewById(R.id.t3);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t3.setTypeface(myCustomFont2);

        t4 = (TextView) findViewById(R.id.t4);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t4.setTypeface(myCustomFont3);

        t5 = (TextView) findViewById(R.id.t5);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t5.setTypeface(myCustomFont4);

        t6 = (TextView) findViewById(R.id.t6);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t6.setTypeface(myCustomFont5);

        t18 = (TextView) findViewById(R.id.t18);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        t18.setTypeface(myCustomFont6);

        h1 = (TextView) findViewById(R.id.h1);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        h1.setTypeface(myCustomFont7);

    }
}
