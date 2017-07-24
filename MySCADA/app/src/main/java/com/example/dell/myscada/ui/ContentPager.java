package com.example.dell.myscada.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.dell.myscada.R;
import com.example.dell.myscada.backstage.GTableAsyncTask;
import com.example.dell.myscada.model.FragmentInfo;
import com.example.dell.myscada.model.mEnergy;

/**
 * Created by yuankaifeng on 11/29/16.
 */

public class ContentPager extends Fragment {
    private View  rootView;
    private TextView tv;
    private FragmentInfo baseInfo;
    private mEnergy energy;
    private TableLayout tableLayout;
    private GTableAsyncTask gTableAsyncTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onDestroy(){
        if (gTableAsyncTask != null && gTableAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            gTableAsyncTask.cancel(true);
        super.onDestroy();
    }

    public View initView(){
        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.content_pager,null);
            tableLayout = (TableLayout) rootView.findViewById(R.id.table1);
            if(isNetworkConnected(getContext())){
                initData(rootView, energy.getId());
            }

//            tv.setText(this.getClass().getSimpleName()+"----"+baseInfo.getTitle());
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public void initData(View view, String str){
        gTableAsyncTask = new GTableAsyncTask(view, str);
        gTableAsyncTask.execute();
    }


    public ContentPager setBaseInfo(FragmentInfo baseInfo, mEnergy energy){
        this.baseInfo = baseInfo;
        this.energy = energy;
        return this;
    }

    public FragmentInfo getBaseInfo() {
        return baseInfo;
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
}
