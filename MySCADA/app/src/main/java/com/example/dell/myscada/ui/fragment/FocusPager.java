package com.example.dell.myscada.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.myscada.R;
import com.example.dell.myscada.backstage.TableAsyncTask;

public class FocusPager extends Fragment {
    private View  rootView;
    private TableAsyncTask tableAsyncTask;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.fragment_compare_pager,null);
            if (isNetworkConnected(getContext())){
                initData(rootView);
                handler.postDelayed(runnable, 1000 * 5);
            }
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){//防止已经被添加过父view了，但是一般不会的
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runnable);
        if (tableAsyncTask != null && tableAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            tableAsyncTask.cancel(true);
        super.onDestroy();
    }

    public void initData(View view){
        tableAsyncTask = new TableAsyncTask(view);
        tableAsyncTask.execute();

    }

    public boolean isNetworkConnected(Context context){
        if (context != null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(mNetworkInfo != null){
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 5);
        }

        void update(){
            if(isNetworkConnected(getContext())){
                initData(rootView);
            }
        }
    };
}
