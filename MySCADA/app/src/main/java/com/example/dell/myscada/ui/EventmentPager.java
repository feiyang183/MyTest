package com.example.dell.myscada.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.dell.myscada.R;
import com.example.dell.myscada.backstage.GListViewAsyncTask;
import com.example.dell.myscada.model.FragmentInfo;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.adapter.EventListAdapter;


/**
 * Created by yuankaifeng on 11/29/16.
 */

public class EventmentPager extends Fragment {
    private View  rootView;
    private TextView tv;
    private FragmentInfo baseInfo;
    private TableLayout tableLayout;
    private mEnergy energy;
    private ListView eventList;
    private EventListAdapter adapter;
    private GListViewAsyncTask gListViewAsyncTask;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runnable);
        if (gListViewAsyncTask != null && gListViewAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            gListViewAsyncTask.cancel(true);
        super.onDestroy();
    }

    public View initView(){
        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.eventment_pager,null);
//            tableLayout = (TableLayout) rootView.findViewById(R.id.table4);
//            tv.setText(this.getClass().getSimpleName()+"----"+baseInfo.getTitle());
            eventList = (ListView) rootView.findViewById(R.id.eventlistView);
            adapter = new EventListAdapter(getActivity());
            if (isNetworkConnected(getContext())){
                initData(eventList, adapter, energy.getId());
                handler.postDelayed(runnable, 1000 * 5);
            }
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public void initData(ListView listView, EventListAdapter eventAdapter, String str){
        gListViewAsyncTask = new GListViewAsyncTask(listView, eventAdapter, str);
        gListViewAsyncTask.execute();
    }

    public EventmentPager setBaseInfo(FragmentInfo baseInfo, mEnergy energy){
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

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 5);
        }

        void update(){
            if(isNetworkConnected(getContext())){
                initData(eventList, adapter, energy.getId());
            }
        }
    };
}
