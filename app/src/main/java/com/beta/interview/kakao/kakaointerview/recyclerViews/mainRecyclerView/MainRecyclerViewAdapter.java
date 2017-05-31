package com.beta.interview.kakao.kakaointerview.recyclerViews.mainRecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beta.interview.kakao.kakaointerview.R;
import com.beta.interview.kakao.kakaointerview.activitys.ImageDetailActivity;
import com.beta.interview.kakao.kakaointerview.common.KakaoInterviewApplication;
import com.beta.interview.kakao.kakaointerview.viewPagers.mainViewPager.MainFragmentPagerAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;
    private Activity activity;
    private ArrayList<MainValueObject> mainList;
    private MainFragmentPagerAdapter pagerAdapter;

    public MainRecyclerViewAdapter(Activity activity, FragmentManager fragmentManager){
        //변수 초기화
        this.activity = activity;
        mainList = new ArrayList<>();
        pagerAdapter =  new MainFragmentPagerAdapter(fragmentManager);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        //Header와 Item를 구분하여 view 설정
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_items, parent, false);
            return new MainViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_header, parent, false);
            return new MainHeaderViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //position의 type 반환
        if (position == 0){
            return TYPE_HEADER;
        }else{
            return TYPE_ITEM;
        }
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView webImage;
        TextView webImageName;
        CardView cardView;

        private MainViewHolder(View itemRootView) {
            super(itemRootView);
            webImage = (ImageView) itemRootView.findViewById(R.id.webImage);
            webImageName = (TextView)itemRootView.findViewById(R.id.webImageName);
            cardView = (CardView)itemRootView.findViewById(R.id.cardView);
        }
    }

    private class MainHeaderViewHolder extends RecyclerView.ViewHolder {
        ViewPager pager;
        TabLayout tabLayout;

        private MainHeaderViewHolder(View itemView) {
            super(itemView);
            pager = (ViewPager) itemView.findViewById(R.id.pager);
            tabLayout = (TabLayout) itemView.findViewById(R.id.tabDots);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MainViewHolder) {
            final MainValueObject valueObject = mainList.get(position-1);

            ((MainViewHolder) holder).webImageName.setText(valueObject.getMainImageName());

            Glide.with(activity)
                    .load(valueObject.getMainImage())
                    .animate(android.R.anim.slide_in_left)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.error)
                    .into(((MainViewHolder) holder).webImage);

            ((MainViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(KakaoInterviewApplication.getInstance(),ImageDetailActivity.class);
                    intent.putExtra("url",valueObject.getMainUrl());
                    intent.putExtra("title",valueObject.getMainImageName());
                    KakaoInterviewApplication.getInstance().startActivity(intent);
                }
            });
        } else if (holder instanceof MainHeaderViewHolder) {
            ((MainHeaderViewHolder) holder).pager.setAdapter(pagerAdapter);
            ((MainHeaderViewHolder) holder).tabLayout.setupWithViewPager(((MainHeaderViewHolder) holder).pager, true);
        }
    }

    @Override
    public int getItemCount() {
        return mainList.size()+1; //전체 item의 갯수 반환
    }

    public void addMember(MainValueObject mainValueObject){
        mainList.add(mainValueObject); //아이템 추가
    }

    public void addHeader(String valueObject){
        pagerAdapter.add(valueObject); //헤더 아이템 추가
    }

    public void updateImage(){
        notifyDataSetChanged(); //데이터 변경 사실 알림
    }

}
