package com.example.dell.myscada.model;

/**
 * Created by yuankaifeng on 11/30/16.
 *  用来封装创建fragment的一些信息
 **/
public class FragmentInfo {
    private String title;
    private int type;

    public FragmentInfo(int type, String title) {
        this.type = type;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
