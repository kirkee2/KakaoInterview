package com.beta.interview.kakao.kakaointerview.common;

import android.app.Application;
import android.content.res.Configuration;

public class KakaoInterviewApplication extends Application {
    //컴포넌트들이 공유 할 수 있는 어플리케이션 객체.
    private static KakaoInterviewApplication kakaoInterviewApplication;

    public static KakaoInterviewApplication getInstance(){
        return kakaoInterviewApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        kakaoInterviewApplication = this;
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
        kakaoInterviewApplication = null;
    }
}
