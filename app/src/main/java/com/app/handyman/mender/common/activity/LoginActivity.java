package com.app.handyman.mender.common.activity;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.AppUtils;
import com.app.handyman.mender.common.utils.DataManager;
import com.app.handyman.mender.common.utils.Helper;
import com.app.handyman.mender.handyman.activity.ApplyAsHandymanActivity;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity to login existing users back into the app!
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String TAG = LoginActivity.class.getSimpleName();

    // Widgets
    private Button sign_up_instead, apply;
    private Button mLoginActivity;
    private EditText mEmail;
    private EditText mPassword;
    private TextView learn_more;
    private TextView title;
    private TextView subtitle;
    private TextView mForgotPassword;
    // private ProgressWheel mProgresWheel;

    // Firebase Authentication
    private FirebaseAuth auth;

    //Icon Manager
    Icon_Manager icon_manager;

    private void signUp(){
        Intent intent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent);
    }

    private void applyAsHandyman(){
        Intent intent = new Intent(LoginActivity.this, ApplyAsHandymanActivity.class);
        startActivity(intent);
    }


    private void login(){
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        SharedPreferences sp=getSharedPreferences("Login", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("email", email);
        ed.apply();

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(LoginActivity.this, "Empty Data", Toast.LENGTH_SHORT).show();
            // mProgresWheel.stopSpinning();

        } else {

            DataManager.getInstance().showProgressMessage(LoginActivity.this);

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                  //  DataManager.getInstance().hideProgressMessage();

                    if (task.isSuccessful()) {
                        DataManager.getInstance().hideProgressMessage();
                        // User Created take him to SplashActivity
                        // Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                        // mProgresWheel.stopSpinning();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();


                    } else {
                        DataManager.getInstance().hideProgressMessage();

                        // mProgresWheel.stopSpinning();
                        Toast.makeText(LoginActivity.this, "Incorrect Username or Password", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
    }


    private void forgotPassword(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.setContentView(R.layout.dialog_forgot_password);
        dialog.setTitle("Enter Registered Email Address");

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        final EditText mEmail = (EditText) dialog.findViewById(R.id.enter_email);
        final Button mForgotPassword = (Button) dialog.findViewById(R.id.forgot_password);
        final TextView learn_more = (TextView) dialog.findViewById(R.id.learn_more);
        mEmail.setTypeface(myCustomFont);
        mForgotPassword.setTypeface(myCustomFont);

        // mEmail.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", LoginActivity.this));
        // mForgotPassword.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", LoginActivity.this));

        auth = FirebaseAuth.getInstance();

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mProgresWheel.spin();
                String email = mEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    //  mProgresWheel.stopSpinning();
                    Toast.makeText(LoginActivity.this, "Please enter your email address", Toast.LENGTH_SHORT).show();
                } else {

                    Log.d(TAG, email + mPassword.getText().toString());
                    dialog.dismiss();
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // mProgresWheel.stopSpinning();
                                Toast.makeText(LoginActivity.this, "We have sent you an Password Reset email", Toast.LENGTH_SHORT).show();
                            } else {
                                // mProgresWheel.stopSpinning();
                                Toast.makeText(LoginActivity.this, "Please check your email", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onClick(View view) {

        if(Helper.hasActiveInternetConnection(LoginActivity.this)){
            switch (view.getId()){
                case R.id.forgot_password:
                    forgotPassword();
                    break;

                case R.id.sign_up_instead:
                    signUp();
                    break;

 /*               case R.id.apply:
                    applyAsHandyman();
                    break;*/

                case R.id.login:
                    if (AppUtils.isConnected(getApplicationContext())) {
                        login();
                    } else {
                        Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        icon_manager = new Icon_Manager();

        ((TextView)findViewById(R.id.textViewIcon)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        ((TextView)findViewById(R.id.textViewPassword)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        sign_up_instead = (Button) findViewById(R.id.sign_up_instead);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        sign_up_instead.setTypeface(myCustomFont);

        mLoginActivity = (Button) findViewById(R.id.login);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLoginActivity.setTypeface(myCustomFont1);

        title = (TextView) findViewById(R.id.title);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        title.setTypeface(myCustomFont5);

        mEmail = (EditText) findViewById(R.id.email);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mEmail.setTypeface(myCustomFont2);

        learn_more = (TextView) findViewById(R.id.learn_more);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book Oblique.otf");
        learn_more.setTypeface(myCustomFont8);

        mPassword = (EditText) findViewById(R.id.password);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mPassword.setTypeface(myCustomFont3);

        mForgotPassword = (TextView) findViewById(R.id.forgot_password);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mForgotPassword.setTypeface(myCustomFont4);

        subtitle = (TextView) findViewById(R.id.subtitle);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        subtitle.setTypeface(myCustomFont6);

   /*     apply = (Button) findViewById(R.id.apply);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        apply.setTypeface(myCustomFont7);*/

        auth = FirebaseAuth.getInstance();

        SharedPreferences sp1=this.getSharedPreferences("Login", 0);

        String email = sp1.getString("email", "");

        mEmail.setText(email);


        mForgotPassword.setOnClickListener(this);
        sign_up_instead.setOnClickListener(this);
//        apply.setOnClickListener(this);
        mLoginActivity.setOnClickListener(this);

        learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, LearnMoreActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}





