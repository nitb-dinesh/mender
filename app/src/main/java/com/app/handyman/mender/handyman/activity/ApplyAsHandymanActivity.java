package com.app.handyman.mender.handyman.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.app.handyman.mender.R;

public class ApplyAsHandymanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_dispute);


        WebView browser = (WebView) findViewById(R.id.webview);
        browser.loadUrl("https://form.jotform.com/80701163581148");


    }
}
