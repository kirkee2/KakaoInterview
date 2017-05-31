package com.beta.interview.kakao.kakaointerview.module;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

public class KakaoInterviewGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);

        //default cache size와 bitmapPool size를 호출
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        //현재 Glide가 관리하는 cache size와 bitmapPool size를 10%를 증가
        int customMemoryCacheSize = (int) (1.1 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.1 * defaultBitmapPoolSize);

        //cache와 bitmapPool의 LRU알고리즘 할당
        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
    }
    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
