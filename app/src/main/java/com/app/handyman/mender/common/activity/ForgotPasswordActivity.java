package com.app.handyman.mender.common.activity;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText mEmail;
    private Button mForgotPassword;
    ProgressDialog mProgress;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Icon_Manager icon_manager = new Icon_Manager();

        ((TextView)findViewById(R.id.textView1)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        mEmail = (EditText) findViewById(R.id.enter_email);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mEmail.setTypeface(myCustomFont1);

        mForgotPassword = (Button) findViewById(R.id.forgot_password);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mForgotPassword.setTypeface(myCustomFont3);

        mProgress = new ProgressDialog(ForgotPasswordActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);

        auth = FirebaseAuth.getInstance();

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                String email = mEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    mProgress.dismiss();
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                } else {

                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mProgress.dismiss();
                                Toast.makeText(ForgotPasswordActivity.this, "Password reset request sent! Please check your email.", Toast.LENGTH_SHORT).show();
                            } else {
                                mProgress.dismiss();
                                Toast.makeText(ForgotPasswordActivity.this, "Invalid Request", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }
        });

    }
}
