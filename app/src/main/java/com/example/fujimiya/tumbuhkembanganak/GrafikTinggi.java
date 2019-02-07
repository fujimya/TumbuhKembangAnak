package com.example.fujimiya.tumbuhkembanganak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GrafikTinggi extends AppCompatActivity {
    WebView wevView;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_tinggi);
        SharedPreferences sp=getSharedPreferences("balita", Context.MODE_PRIVATE);
        value = sp.getString("id_balita", "");
        wevView = findViewById(R.id.wevView);
        wevView.setWebViewClient(new GrafikTinggi.MyBrowser());
        wevView.getSettings().setJavaScriptEnabled(true);
        wevView.getSettings().setDomStorageEnabled(true);
        wevView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        wevView.getSettings().setUseWideViewPort(true);
        wevView.setInitialScale(140);
        wevView.getSettings().setBuiltInZoomControls(true);
        wevView.getSettings().setSupportZoom(true);
        wevView.loadUrl("http://balita.alfalaqproject.xyz/Controller_Grafik/tinggi/"+value);
    }

    public void print(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://balita.alfalaqproject.xyz/Controller_Grafik/tinggi/"+value));
        startActivity(browserIntent);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
