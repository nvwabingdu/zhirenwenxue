package com.example.zrwenxue.moudel.main.home;

import android.graphics.drawable.Drawable;

/**
 * Created by Android Studio.
 * User: 86182
 * Date: 2022-01-15
 * Time: 17:36
 */
class HomeBean {
    String content;
    Drawable img;

    public HomeBean(String content, Drawable img) {
        this.content = content;
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
