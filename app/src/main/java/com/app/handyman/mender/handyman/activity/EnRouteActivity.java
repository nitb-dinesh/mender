package com.app.handyman.mender.handyman.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.handyman.mender.R;

public class EnRouteActivity extends AppCompatActivity {

    private Button btnbegin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_en_route);

        btnbegin = (Button) findViewById(R.id.btnbegin);

        btnbegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (v == btnbegin) {
                // startActivity(new Intent(EnRouteActivity.this, NewJob.class));
            }
            }
        });
    }
}
