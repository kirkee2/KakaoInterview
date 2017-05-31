package com.beta.interview.kakao.kakaointerview.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.beta.interview.kakao.kakaointerview.recyclerViews.mainRecyclerView.MainRecyclerViewAdapter;
import com.beta.interview.kakao.kakaointerview.recyclerViews.mainRecyclerView.MainValueObject;
import com.beta.interview.kakao.kakaointerview.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private  RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private MainRecyclerViewAdapter mainRecyclerViewAdapter;
    private ProgressBar progressBar;

    private ArrayList<String> mainImageList; // 메인의 이미지 리스트
    private ArrayList<String> mainImageNameList; //메인의 이미지 이름 리스트
    private ArrayList<String> mainUrlList; //메인이 이미지 url 리스트 -> webView로 출력하기 위해 사용
    private ArrayList<String> headerImageList; //헤더의 이미지 리스트

    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //변수 초기화
        headerImageList = new ArrayList<>();
        mainImageList = new ArrayList<>();
        mainImageNameList = new ArrayList<>();
        mainUrlList = new ArrayList<>();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refreshLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Toolbar

        setSupportActionBar(toolbar); //Toolbar를 현재 Activity의 Actionbar로 설정

        //LayoutManager

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2); //RecyclerView에 설정 할 LayoutManager 초기화

        //Header(0번 째)는 span을 2를 할당하고 나머지는 span을 1을 할당.
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(mainRecyclerViewAdapter.getItemViewType(position) == MainRecyclerViewAdapter.TYPE_HEADER){
                    return 2;
                }else {
                    return 1;
                }
            }
        });

        //RecyclerView에 LayoutManager 설정 및 adapter 설정
        recyclerView.setLayoutManager(gridLayoutManager);
        mainRecyclerViewAdapter = new MainRecyclerViewAdapter(this,this.getSupportFragmentManager());
        recyclerView.setAdapter(mainRecyclerViewAdapter);

        //ActionBarDrawerToggle

        //ActionBarDrawerToggle 초기화 및 싱크 설정
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView

        //NavigationView의 아이템 선택 시 이벤트 설정
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Uri uri;

                if (id == R.id.university) {
                    Toast.makeText(getApplicationContext(),"숭실대학교 컴퓨터학부",Toast.LENGTH_LONG).show();
                } else if (id == R.id.ENOW) {
                    uri = Uri.parse("https://github.com/ENOW-IJI");
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                } else if (id == R.id.Tacademy) {
                    uri = Uri.parse("https://tacademy.sktechx.com/frontMain.action");
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                } else if (id == R.id.androidClass) {
                    uri = Uri.parse("https://github.com/kirkee2/Android-bicycle");
                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //SwipeRefreshLayout

        refreshLayout.setColorSchemeResources(R.color.progress);  //SwipeRefreshLayout 색상 설정

        refreshLayout.setEnabled(false); //초기에는 SwipeRefreshLayout 비활성화

        //SwipeRefreshLayout refresh 시 이벤트 설정.
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainRecyclerViewAdapter.updateImage();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        new ParseHTMLAsyncTask().execute(); //HTML 파싱 수행
    }

    //back 버튼 클릭 시 이벤트 설정.
    @Override
    public void onBackPressed() {
        final long FINSH_INTERVAL_TIME = 2000;
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
                super.onBackPressed();
            } else {
                backPressedTime = currentTime;
                Toast.makeText(getApplicationContext(), "'뒤로' 버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //http 통신으로 HTML을 받아와 원하는 정보만을 파싱하여 저장해두는 AsyncTask
    private class ParseHTMLAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //시작 전에 ProgressBar를 보여주어 사용자와 interact
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //HTML 입력
                Document doc = Jsoup.connect(getString(R.string.url)).get();

                //HTML 파싱
                Elements headerImages = doc.select("div.ex-col-wrapperleft div ul li img");
                Elements mainImages = doc.select("div.gallery-item-group a img");
                Elements mainUrlTitles = doc.select("div.gallery-item-caption p a");

                for (Element headerImage : headerImages) {
                    headerImageList.add(getString(R.string.imageUrl) + headerImage.attr("src"));
                }

                for(Element mainImage : mainImages){
                    mainImageList.add(getString(R.string.imageUrl) + mainImage.attr("src"));
                }

                for(Element mainUrlTitle : mainUrlTitles){
                    mainUrlList.add(getString(R.string.imageUrl) + mainUrlTitle.attr("href"));

                    if(mainUrlTitle.text().length() == 0){
                        mainImageNameList.add("No Title");
                    }else{
                        mainImageNameList.add(mainUrlTitle.text());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //RecyclerView에 해더 및 아이템 추가
            addHeaders();
            addItems();

            //마무리 된 이후에 ProgressBar 제거하고 SwipeRefreshLayout을 사용할 수 있게 설정
            progressBar.setVisibility(View.GONE);
            refreshLayout.setEnabled(true);
        }
    }

    //RecyclerView에 해더 추가
    public void addHeaders(){
        for(String tmp : headerImageList){
            mainRecyclerViewAdapter.addHeader(tmp);
        }
    }

    //RecyclerView에 아이템 추가
    public void addItems(){
        for(int i = 0 ; i<mainUrlList.size() ; i++) {
                mainRecyclerViewAdapter.addMember(new MainValueObject(mainImageList.get(i), mainImageNameList.get(i), mainUrlList.get(i)));
        }
    }
}
