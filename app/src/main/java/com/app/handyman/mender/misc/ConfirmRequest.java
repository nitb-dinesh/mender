package com.app.handyman.mender.misc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.app.handyman.mender.R;

/**
 * The request user has created will be shown and confirmed to the user here.
 */

public class ConfirmRequest extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);



    }
}
