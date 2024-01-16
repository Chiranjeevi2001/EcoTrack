package com.example.ecotrack_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class NewsFullActivity extends AppCompatActivity {

    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_full);

        String url = getIntent().getStringExtra("url");
        webview = findViewById(R.id.web_view);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(webview.canGoBack())
            webview.goBack();
        else
            super.onBackPressed();
    }
}