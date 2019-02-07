package com.example.fujimiya.tumbuhkembanganak;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class GrafikBerat extends AppCompatActivity {
    WebView wevView;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_berat);
        SharedPreferences sp=getSharedPreferences("balita", Context.MODE_PRIVATE);
        value = sp.getString("id_balita", "");
        wevView = findViewById(R.id.wevView);
        wevView.setWebViewClient(new MyBrowser());
        wevView.getSettings().setJavaScriptEnabled(true);
        wevView.getSettings().setDomStorageEnabled(true);
        wevView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        wevView.getSettings().setUseWideViewPort(true);
        wevView.setInitialScale(140);
        wevView.getSettings().setBuiltInZoomControls(true);
        wevView.getSettings().setSupportZoom(true);
        wevView.loadUrl("http://balita.alfalaqproject.xyz/Controller_Grafik/berat/"+value);
        wevView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Toast.makeText(GrafikBerat.this,"",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void print(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://balita.alfalaqproject.xyz/Controller_Grafik/berat/"+value));
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
