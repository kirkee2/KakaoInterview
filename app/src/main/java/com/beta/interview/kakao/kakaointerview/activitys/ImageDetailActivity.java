package com.beta.interview.kakao.kakaointerview.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.beta.interview.kakao.kakaointerview.R;

public class ImageDetailActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);

        webView = (WebView)findViewById(R.id.webView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        //Toolbar

        setSupportActionBar(toolbar); //Toolbar를 현재 Activity의 Actionbar로 설정.

        //Toolbar 설정
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent(); //호출 activity의 intent객체를 받아옴

        String url = intent.getStringExtra("url"); //호출 activity에서 url을 받아옴
        String title = intent.getStringExtra("title"); //호출 activity에서 title을 받아옴

        toolbar.setTitle(title);
        toolbar.setTitleTextColor(Color.WHITE);


        webView.loadUrl(url); //webView에 url설정
        webView.getSettings().setJavaScriptEnabled(true); //webView에 javaScirpt 활성화
        webView.setVerticalScrollBarEnabled(true); //webView에 수직 스크롤바 활성화
        webView.setHorizontalScrollBarEnabled(true); //webView에 수평 스크롤바 활성화
        webView.getSettings().setBuiltInZoomControls(true); //webView에 줌 기능 활성화
        webView.getSettings().setSupportZoom(true); //webView에 줌 기능 활성화
        webView.getSettings().setLoadWithOverviewMode(true); //웹뷰가 html 컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정
        webView.getSettings().setUseWideViewPort(true); //webView가 html의 viewport 메타 태그를 지원(스크린 크기에 맞게 조정)

        //시작 전에 ProgressBar를 보여주어 사용자와 interact
        webView.setWebViewClient(new WebViewClient() {
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
