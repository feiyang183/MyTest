package com.example.dell.myscada.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.backstage.ListViewAsyncTask;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.adapter.EnergyAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuankaifeng on 11/29/16.
 */

public class VideoPager extends Fragment {
    private View view;
    private RecyclerView recyclerView;
//    private EnergyAdapter arrayAdapter;
    private LinearLayoutManager layoutManager;
    private DBDefine dbDefine;
    private DBFactory dbFactory;
    private EnergyAdapter energyAdapter;
    private List<mEnergy> energyList;
    private ListViewAsyncTask listViewAsyncTask;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().build());

        if(view == null){
            view = inflater.inflate(R.layout.fragment_list_pager, container, false);
//            initEnergy();

            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
//            arrayAdapter = new EnergyAdapter(getActivity());
            if(isNetworkConnected(getContext())){
                initEnergy();
                handler.postDelayed(runnable, 1000*5);
            }
        }else{
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){//防止已经被添加过父view了，但是一般不会的
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runnable);
        if (listViewAsyncTask != null && listViewAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            listViewAsyncTask.cancel(true);
        super.onDestroy();
    }

    public void initEnergy(){
//        ListViewAsyncTask asyncTask = new ListViewAsyncTask(recyclerView);
//        asyncTask.execute(1000);
        initDataBase();
        try {
            dbFactory.selectFGenerator();
        }catch (SQLException e){
            e.printStackTrace();
        }
        List<String> result = dbFactory.getList();
        energyList = new ArrayList<mEnergy>();
        for(String tmp:result){
            String[] list1 = String.valueOf(tmp).split("@_@@");
            mEnergy energy = new mEnergy();
            energy.setId(list1[0]);
            energy.setState(list1[1]);
            energy.setSpeed(list1[2]);
            energy.setCapacity(list1[3]);
            energy.setTemperature(list1[4]);
            energyList.add(energy);
        }
        energyAdapter = new EnergyAdapter(energyList);
        recyclerView.setAdapter(energyAdapter);
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
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
            handler.postDelayed(this, 1000*5);
        }

        void update(){
            if(isNetworkConnected(getContext())){
//                initEnergy(recyclerView);
                listViewAsyncTask = new ListViewAsyncTask(recyclerView,energyList);
                listViewAsyncTask.execute();
            }
        }
    };
}