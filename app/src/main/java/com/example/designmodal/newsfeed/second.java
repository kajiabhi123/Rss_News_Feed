package com.example.designmodal.newsfeed;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            String Url = (String) bd.get("link");
            WebView w1 = (WebView) findViewById(R.id.webView);
            w1.loadUrl(Url);
        }


//        WebView w1 = (WebView) findViewById(R.id.webView);
//        w1.loadUrl("https://yubapost.com/");
    }
}