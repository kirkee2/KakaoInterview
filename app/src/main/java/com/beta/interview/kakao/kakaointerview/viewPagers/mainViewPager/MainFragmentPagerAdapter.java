package com.beta.interview.kakao.kakaointerview.viewPagers.mainViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<String> items;

    public void add(String item) {
        items.add(item); //아이템 추가
        notifyDataSetChanged(); //데이터 변경 사실 알림
    }

    public MainFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        items = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return new PageFragment(items.get(position)); //position번째 아이템을 반환
    }

    @Override
    public int getCount() {
        return items.size(); //전체 item의 갯수 반환
    }
}
