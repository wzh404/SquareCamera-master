package com.desmond.demo.base.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.desmond.demo.R;

/**
 * Created by wangzunhui on 2016/6/13.
 */
public class WebViewActivity extends AppCompatActivity {
    private ProgressWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String url = bundle.getString("url");
        String title = bundle.getString("title");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setToolbarOnBackPressed(this);

        webView = (ProgressWebView) findViewById(R.id.baseweb_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsOperation(), "client");
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class JsOperation {
        @JavascriptInterface
        public void pageClick(String paramString)
        {
            String[] paras = paramString.split("\\|");
            for (String p : paras){
                Log.e("webview", p);
            }
        }
    }

    public void setToolbarOnBackPressed(final AppCompatActivity activity){
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }
}
