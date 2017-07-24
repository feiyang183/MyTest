package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Dell on 2017/5/25.
 */

public class GLineChartAsyncTask extends AsyncTask<Integer, Integer, float[]> {

    private DBDefine dbDefine;
    private DBFactory dbFactory;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public GLineChartAsyncTask(){

    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected float[] doInBackground(Integer... params){

        try {
            dbFactory.selectRuntime();

        }catch (SQLException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        List<String> result = dbFactory.getList();
        float[] Runtime = new float[12] ;
        for (int i = 0; i < result.size(); i ++){
            Runtime[i] = Float.valueOf(result.get(i));
            Log.e("Runtime",i + " = " +result.get(i));
        }
        return Runtime;
    }

    @Override
    protected void onPostExecute(float[] result){
        setLineDatas(result);
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }

    private void setLineDatas(float[] result){

    }
}
