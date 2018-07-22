package com.app.handyman.mender.common.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.app.handyman.mender.R;


public class TermsOfUse extends AppCompatActivity {

    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11,t12, t13;

    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);

        // Custom Font
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("Terms of Use");


        // Typeface myBoldFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Bold.otf");

        t1 = (TextView) findViewById(R.id.t1);
        t2 = (TextView) findViewById(R.id.t2);
        t3 = (TextView) findViewById(R.id.t3);
        t4 = (TextView) findViewById(R.id.t4);
        t5 = (TextView) findViewById(R.id.t5);
        t6 = (TextView) findViewById(R.id.t6);
        t7 = (TextView) findViewById(R.id.t7);
        t8 = (TextView) findViewById(R.id.t8);
        t9 = (TextView) findViewById(R.id.t9);
        t10 = (TextView) findViewById(R.id.t10);
        t11 = (TextView) findViewById(R.id.t11);
        t12 = (TextView) findViewById(R.id.t12);
        t13 = (TextView) findViewById(R.id.t13);

        t1.setTypeface(myCustomFont);
        t2.setTypeface(myCustomFont);
        t3.setTypeface(myCustomFont);
        t4.setTypeface(myCustomFont);
        t5.setTypeface(myCustomFont);
        t6.setTypeface(myCustomFont);
        t7.setTypeface(myCustomFont);
        t8.setTypeface(myCustomFont);
        t9.setTypeface(myCustomFont);
        t10.setTypeface(myCustomFont);
        t11.setTypeface(myCustomFont);
        t12.setTypeface(myCustomFont);
        t13.setTypeface(myCustomFont);

    }
}
