package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.ui.customview.FlyView;

import java.sql.SQLException;

/**
 * Created by Dell on 2017/5/17.
 */

public class TableAsyncTask extends AsyncTask<Integer, Integer, String[]> {
    private DBDefine dbDefine;
    private DBFactory dbFactory;
    private TextView Allcap;
    private TextView Yearcap;
    private TextView Monthcap;
    private TextView Daycap;
    private TextView Curpower;
    private TextView Equalhour;
    private TextView Speed;
    private TextView Windspeed;
    private TextView Vane;
    private TextView Temp;
    private FlyView myFlyView;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public TableAsyncTask(View view){
        super();
        myFlyView = (FlyView) view.findViewById(R.id.flyView);
        Allcap = (TextView) view.findViewById(R.id.all_cap);
        Yearcap = (TextView) view.findViewById(R.id.year_cap);
        Monthcap = (TextView) view.findViewById(R.id.month_cap);
        Daycap = (TextView) view.findViewById(R.id.day_cap);
        Curpower = (TextView) view.findViewById(R.id.curpower);
        Equalhour = (TextView) view.findViewById(R.id.equal_hour);
        Speed = (TextView) view.findViewById(R.id.speed);
        Windspeed = (TextView) view.findViewById(R.id.wind_speed);
        Vane = (TextView) view.findViewById(R.id.vane);
        Temp = (TextView) view.findViewById(R.id.temp);
        initDataBase();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected String[] doInBackground(Integer... params){
        try {
            dbFactory.selectSituation();
        }catch (SQLException e){
            e.printStackTrace();
        }
        String result = dbFactory.getString();
        String[] list1 = String.valueOf(result).split("@_@@");
        return list1;
    }

    @Override
    protected void onPostExecute(String[] result){

        Allcap.setText(result[0] + " MWh");
        Yearcap.setText(result[1] + " MWh");
        Monthcap.setText(result[2] + " MWh");
        Daycap.setText(result[3] + " MWh");
        Curpower.setText(result[4] + " kW");
        Equalhour.setText(result[5] + " h");
        Windspeed.setText(result[6] + " m/s");
        Temp.setText(result[7] + " ℃");
        Speed.setText(result[8] + " m/s");
        Vane.setText(result[9] + " 度");
        myFlyView.updateDirection(Float.valueOf(result[9]));
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }
}
