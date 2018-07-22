package com.app.handyman.mender.myapp.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.app.handyman.mender.R;
import com.app.handyman.mender.myapp.Titanic;
import com.app.handyman.mender.myapp.TitanicTextView;
import com.app.handyman.mender.common.activity.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    TitanicTextView tv;
    Button mSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tv = (TitanicTextView) findViewById(R.id.titanic_tv);
        mSkip = (Button) findViewById(R.id.skip);

        mSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.startActivity(mainIntent);

                //Finish splash activity so user cant go back to it.
                SplashActivity.this.finish();
                overridePendingTransition(0, R.anim.splash_fade_new);

            }
        });

        // start animation
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tv.setTypeface(myCustomFont2);
        new Titanic().start(tv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Create an intent that will start the main activity.
                Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
                SplashActivity.this.startActivity(mainIntent);

                //Finish splash activity so user cant go back to it.
                SplashActivity.this.finish();
                overridePendingTransition(0, R.anim.splash_fade_new);

                //Apply splash exit (fade out) and main entry (fade in) animation transitions.
                // overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
            }
        }, 6000);

//        auth = FirebaseAuth.getInstance();
//
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//
//
//                FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
//                if (u == null) {
//
//                    // start animation
//                    Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
//                    tv.setTypeface(myCustomFont2);
//                    new Titanic().start(tv);
//
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            //Create an intent that will start the main activity.
//                            Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
//                            SplashActivity.this.startActivity(mainIntent);
//
//                            //Finish splash activity so user cant go back to it.
//                            SplashActivity.this.finish();
//                            overridePendingTransition(0, R.anim.splash_fade_new);
//
//                            //Apply splash exit (fade out) and main entry (fade in) animation transitions.
//                            // overridePendingTransition(R.anim.mainfadein, R.anim.splashfadeout);
//                        }
//                    }, 6000);
//
//                } else {
//
//                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
//                    SplashActivity.this.startActivity(mainIntent);
//
//                }
//
//            }
//
//        };


    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        auth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (authStateListener != null) {
//            auth.removeAuthStateListener(authStateListener);
//        }
//
//    }


}
