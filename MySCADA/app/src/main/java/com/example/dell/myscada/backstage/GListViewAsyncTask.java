package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.widget.ListView;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.ui.adapter.EventListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2017/5/18.
 */

public class GListViewAsyncTask extends AsyncTask<Integer, Integer, List<String>> {
    private ListView listView;
    private EventListAdapter eventAdapter;
    private DBDefine dbDefine;
    private DBFactory dbFactory;
    private String str;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public GListViewAsyncTask(ListView listView, EventListAdapter eventAdapter, String str){
        super();
        this.listView = listView;
        this.eventAdapter = eventAdapter;
        this.str = str;
        initDataBase();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected List<String> doInBackground(Integer... params){
        try {
            dbFactory.selectEvent(str);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dbFactory.getList();
    }

    @Override
    protected void onPostExecute(List<String> result){
        ArrayList<String> arrayList = new ArrayList<>(result);
        eventAdapter.setPopList(arrayList);
        listView.setAdapter(eventAdapter);
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }
}
