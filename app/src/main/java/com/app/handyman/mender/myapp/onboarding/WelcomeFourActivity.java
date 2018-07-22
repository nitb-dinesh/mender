package com.app.handyman.mender.myapp.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.handyman.mender.common.activity.HomeActivity;
import com.app.handyman.mender.R;

public class WelcomeFourActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "Preferences";
    private static final String HAS_WATCHED_TUTORIAL = "HasWatchedTutorial";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_four);


        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        TextView mTitle = (TextView) findViewById(R.id.title);
        TextView mDescription = (TextView) findViewById(R.id.description);

        Button mNext = (Button) findViewById(R.id.next);

        mTitle.setTypeface(myCustomFont);
        mDescription.setTypeface(myCustomFont);
        mNext.setTypeface(myCustomFont);

        mNext.setText("Finish");

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putBoolean(HAS_WATCHED_TUTORIAL, true);
                editor.commit();

                Intent intent = new Intent(WelcomeFourActivity.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        VideoView view = (VideoView) findViewById(R.id.video_view);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.welcome_4;
        view.setVideoURI(Uri.parse(path));
        view.start();

    }

    @Override
    public void onBackPressed() {

    }

}
