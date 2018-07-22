package com.app.handyman.mender.handyman.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;

public class DeclinedActivity extends AppCompatActivity {

    public RadioButton radioButton;
    public RadioButton radioButton2;
    public RadioButton radioButton3;
    public RadioButton radioButton4;
    public RadioButton radioButton5;
    public TextView tvtitle;
    public TextView tvsubtitle;
    public TextView tvdescrption;
    public Button button7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declined);

        radioButton = (RadioButton) findViewById(R.id.radioButton);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        radioButton.setTypeface(myCustomFont1);

        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        radioButton2.setTypeface(myCustomFont2);

        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        radioButton3.setTypeface(myCustomFont3);

        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        radioButton4.setTypeface(myCustomFont4);

        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        radioButton5.setTypeface(myCustomFont5);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        tvtitle.setTypeface(myCustomFont8);

        tvsubtitle = (TextView) findViewById(R.id.tvsubtitle);
        Typeface myCustomFont10 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        tvsubtitle.setTypeface(myCustomFont10);

        tvdescrption = (TextView) findViewById(R.id.tvdescription);
        Typeface myCustomFont9 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        tvdescrption.setTypeface(myCustomFont9);

        button7 = (Button) findViewById(R.id.button7);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(),  "fonts/Quicksand Book.otf");
        button7.setTypeface(myCustomFont7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button7) {
                    startActivity(new Intent(DeclinedActivity.this, HomeActivity.class));
            }



    }
});
    }
}
