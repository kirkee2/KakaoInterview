package com.beta.interview.kakao.kakaointerview.recyclerViews.mainRecyclerView;

public class MainValueObject {
    private String mainImage; // 이미지 url
    private String mainImageName;//이미지 제목
    private String mainUrl; //이미지를 갖고 있는 url

    public MainValueObject(String mainImage,String mainImageName,String mainUrl) {
        this.mainImage = mainImage;
        this.mainImageName = mainImageName;
        this.mainUrl = mainUrl;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getMainImageName() {
        return mainImageName;
    }

    public void setMainImageName(String mainImageName) {
        this.mainImageName = mainImageName;
    }

    public String getMainUrl() {
        return mainUrl;
    }

    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }
}
