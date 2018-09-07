package com.rovin.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URI;
import java.net.URISyntaxException;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        WebView webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();

        String url = intent.getStringExtra("content");

        try {
            URI uri = new URI(url);
            if(uri.getHost().endsWith("techcrunch.com") || uri.getHost().endsWith("bitmidi.com")) {
                webView.getSettings().setJavaScriptEnabled(false);
            } else {
                webView.getSettings().setJavaScriptEnabled(true);
            }


            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.setVerticalScrollBarEnabled(true);
            webView.setWebViewClient(new WebViewClient());

            //webView.loadData(intent.getStringExtra("content"),"text/http","UTF-8");

            Log.i("webView", "Url address: " + url);

            webView.loadUrl(url);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
