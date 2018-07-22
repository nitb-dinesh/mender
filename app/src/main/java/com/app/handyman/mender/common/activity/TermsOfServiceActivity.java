package com.app.handyman.mender.common.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.app.handyman.mender.R;

public class TermsOfServiceActivity extends AppCompatActivity {
    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11,t12;
    private TextView t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24,t25;
    private TextView t26, t27, t28, t29, t30, t31, t32, t33, t34, t35, t36,t37, t38;

    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);

        // Custom Font
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("Terms Of Service");

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
        t14 = (TextView) findViewById(R.id.t14);
        t15 = (TextView) findViewById(R.id.t15);
        t16 = (TextView) findViewById(R.id.t16);
        t17 = (TextView) findViewById(R.id.t17);
        t18 = (TextView) findViewById(R.id.t18);
        t19 = (TextView) findViewById(R.id.t19);
        t20 = (TextView) findViewById(R.id.t20);
        t21 = (TextView) findViewById(R.id.t21);
        t22 = (TextView) findViewById(R.id.t22);
        t23 = (TextView) findViewById(R.id.t23);
        t24 = (TextView) findViewById(R.id.t24);

        t25 = (TextView) findViewById(R.id.t25);
        t26 = (TextView) findViewById(R.id.t26);
        t27 = (TextView) findViewById(R.id.t27);
        t28 = (TextView) findViewById(R.id.t28);
        t29 = (TextView) findViewById(R.id.t29);
        t30 = (TextView) findViewById(R.id.t30);
        t31 = (TextView) findViewById(R.id.t31);
        t32 = (TextView) findViewById(R.id.t32);
        t33 = (TextView) findViewById(R.id.t33);
        t34 = (TextView) findViewById(R.id.t34);
        t35 = (TextView) findViewById(R.id.t35);
        t36 = (TextView) findViewById(R.id.t36);
        t37 = (TextView) findViewById(R.id.t37);
        t38 = (TextView) findViewById(R.id.t38);

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
        t14.setTypeface(myCustomFont);
        t15.setTypeface(myCustomFont);
        t16.setTypeface(myCustomFont);
        t17.setTypeface(myCustomFont);
        t18.setTypeface(myCustomFont);
        t19.setTypeface(myCustomFont);
        t20.setTypeface(myCustomFont);
        t21.setTypeface(myCustomFont);
        t22.setTypeface(myCustomFont);
        t23.setTypeface(myCustomFont);
        t24.setTypeface(myCustomFont);

        t25.setTypeface(myCustomFont);
        t26.setTypeface(myCustomFont);
        t27.setTypeface(myCustomFont);
        t28.setTypeface(myCustomFont);
        t29.setTypeface(myCustomFont);
        t30.setTypeface(myCustomFont);
        t31.setTypeface(myCustomFont);
        t32.setTypeface(myCustomFont);
        t33.setTypeface(myCustomFont);
        t34.setTypeface(myCustomFont);
        t35.setTypeface(myCustomFont);
        t36.setTypeface(myCustomFont);
        t37.setTypeface(myCustomFont);
        t38.setTypeface(myCustomFont);



    }
}
