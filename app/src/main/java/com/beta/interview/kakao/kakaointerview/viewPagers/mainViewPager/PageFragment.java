package com.beta.interview.kakao.kakaointerview.viewPagers.mainViewPager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.beta.interview.kakao.kakaointerview.R;
import com.bumptech.glide.Glide;

public class PageFragment extends Fragment {

    ImageView imageView;
    String image;

    public PageFragment() {
    }

    public PageFragment(String image) {
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        imageView = (ImageView) view.findViewById(R.id.imageView);

        Glide.with(this)
                .load(image)
                .animate(android.R.anim.slide_in_left)
                .override(150,100)
                .placeholder(R.drawable.loading)
                .error(R.drawable.error)
                .into(imageView);

        return view;
    }
}
