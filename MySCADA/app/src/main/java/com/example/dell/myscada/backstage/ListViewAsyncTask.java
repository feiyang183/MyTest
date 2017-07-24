package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.adapter.EnergyAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/16.
 */

public class ListViewAsyncTask extends AsyncTask<Integer, Integer, List<mEnergy>> {
    private RecyclerView recyclerView;
    private List<mEnergy> mEnergyList;
    private DBDefine dbDefine;
    private DBFactory dbFactory;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public ListViewAsyncTask(RecyclerView recyclerView, List<mEnergy> mEnergyList){
        super();
        this.recyclerView = recyclerView;
        this.mEnergyList = mEnergyList;
        initDataBase();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected List<mEnergy> doInBackground(Integer... params){
        try {
            dbFactory.selectFGenerator();
        }catch (SQLException e){
            e.printStackTrace();
        }
        List<String> result = dbFactory.getList();
        List<mEnergy> energyList = new ArrayList<mEnergy>();
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
        return energyList;
    }

    @Override
    protected void onPostExecute(List<mEnergy> result){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int firstposition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            int lastposition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
            if(RecyclerView.NO_POSITION != firstposition){
                for(int i = firstposition; i <= lastposition; i ++){
                    mEnergy data = result.get(i);
                    mEnergy energy = mEnergyList.get(i);
                    energy.setId(data.getId());
                    energy.setState(data.getState());
                    energy.setSpeed(data.getSpeed());
                    energy.setCapacity(data.getCapacity());
                    energy.setTemperature(data.getTemperature());
                }
                recyclerView.getAdapter().notifyItemRangeChanged(firstposition, lastposition - firstposition + 1);
            }
        }
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }
}
