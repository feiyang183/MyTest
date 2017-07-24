package com.example.dell.myscada.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.dell.myscada.Base.BaseActivity;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.FirstPager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Dell on 2017/5/3.
 */

public class WindActivity extends BaseActivity {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
    private Date curDate = new Date(System.currentTimeMillis());
    private String str = formatter.format(curDate);

    private mEnergy energy = new mEnergy();
    private FirstPager firstPager;


    public static void actionStart(Context context, mEnergy energy){
        Intent intent = new Intent(context, WindActivity.class);
        intent.putExtra("energy_id", energy.getId());
        intent.putExtra("energy_speed", String.format("%.2f", energy.getSpeed()));
        intent.putExtra("energy_temp", String.format("%.2f", energy.getTemperature()));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind);



        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21){
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = new View(window.getContext());
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(getResources().getColor(R.color.blue));
            decorViewGroup.addView(statusBarView);
        }else if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }

        Log.e("tag","WindActivity");
        energy.setId(getIntent().getStringExtra("energy_id"));
        energy.setSpeed(getIntent().getStringExtra("energy_speed"));
        energy.setTemperature(getIntent().getStringExtra("energy_temp"));
        energy.setDate(str);
        firstPager = (FirstPager) getSupportFragmentManager()
                .findFragmentById(R.id.wind_content_fragment);
        firstPager.refresh(energy);
    }

    private static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    public mEnergy getEnergy(){
        return energy;
    }

    @Override
    public void onBackPressed() {
        if (!getSupportFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
            energy = null;
        }
    }
}
