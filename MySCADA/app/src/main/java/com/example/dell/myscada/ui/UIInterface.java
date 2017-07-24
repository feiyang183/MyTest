package com.example.dell.myscada.ui;

import android.view.View;

/**
 * Created by Dell on 2017/4/14.
 */

public interface UIInterface {

    int getLayoutId();

    void initView();

    void initData();

    void initListener();

    void processClick(View v);

}
