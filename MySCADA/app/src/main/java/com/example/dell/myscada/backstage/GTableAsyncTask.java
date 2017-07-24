package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.mEnergy;

import java.sql.SQLException;

/**
 * Created by Dell on 2017/5/18.
 */

public class GTableAsyncTask extends AsyncTask<Integer, Integer, mEnergy> {
    private DBDefine dbDefine;
    private DBFactory dbFactory;
    private String str;

    private TextView Lruntime;
    private TextView Lstoptime;
    private TextView Lusef;
    private TextView Mruntime;
    private TextView Mstoptime;
    private TextView Musef;
    private TextView Truntime;
    private TextView Tstoptime;
    private TextView Tusef;
    private TextView Energy_allcap;
    private TextView Energy_yearcap;
    private TextView Energy_monthcap;
    private TextView Energy_monthcapl;
    private TextView Energy_daycap;
    private TextView Energy_hourcap;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public GTableAsyncTask(View view, String str){
        this.str = str;
        Lruntime = (TextView) view.findViewById(R.id.lruntime);
        Lstoptime = (TextView) view.findViewById(R.id.lstoptime);
        Lusef = (TextView) view.findViewById(R.id.lusef);
        Mruntime = (TextView) view.findViewById(R.id.mruntime);
        Mstoptime = (TextView) view.findViewById(R.id.mstoptime);
        Musef = (TextView) view.findViewById(R.id.musef);
        Truntime = (TextView) view.findViewById(R.id.truntime);
        Tstoptime = (TextView) view.findViewById(R.id.tstoptime);
        Tusef = (TextView) view.findViewById(R.id.tusef);
        Energy_allcap = (TextView) view.findViewById(R.id.energy_allcap);
        Energy_yearcap = (TextView) view.findViewById(R.id.energy_yearcap);
        Energy_monthcap = (TextView) view.findViewById(R.id.energy_monthcap);
        Energy_monthcapl = (TextView) view.findViewById(R.id.energy_monthcapl);
        Energy_daycap = (TextView) view.findViewById(R.id.energy_daycap);
        Energy_hourcap = (TextView) view.findViewById(R.id.energy_hourcap);
        initDataBase();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected mEnergy doInBackground(Integer... params){
        try {
            dbFactory.selectGenerator(str);
        }catch (SQLException e){
            e.printStackTrace();
        }
        String result = dbFactory.getString();
        mEnergy Energy = new mEnergy();
        String[] list1 = String.valueOf(result).split("@_@@");
        Energy.setLruntime(list1[0]);
        Energy.setLstoptime(list1[1]);
        Energy.setLusef(list1[2]);
        Energy.setMruntime(list1[3]);
        Energy.setMstoptime(list1[4]);
        Energy.setMusef(list1[5]);
        Energy.setTruntime(list1[6]);
        Energy.setTstoptime(list1[7]);
        Energy.setTusef(list1[8]);
        Energy.setAllCap(list1[9]);
        Energy.setYearCap(list1[10]);
        Energy.setMonthCap(list1[11]);
        Energy.setMonthCapL(list1[12]);
        Energy.setDayCap(list1[13]);
        Energy.setHourCap(list1[14]);
        return Energy;
    }

    @Override
    protected void onPostExecute(mEnergy Energy){
        Lruntime.setText(Energy.getLruntime() + " h");
        Lstoptime.setText(Energy.getLstoptime() + " h");
        Lusef.setText(Energy.getLusef() + " %");
        Mruntime.setText(Energy.getMruntime() + " h");
        Mstoptime.setText(Energy.getMstoptime() + " h");
        Musef.setText(Energy.getMusef() + " %");
        Truntime.setText(Energy.getTruntime() + " h");
        Tstoptime.setText(Energy.getTstoptime() + " h");
        Tusef.setText(Energy.getTusef() + " %");
        Energy_allcap.setText(Energy.getAllCap() + " MWh");
        Energy_yearcap.setText(Energy.getYearCap() + " MWh");
        Energy_monthcap.setText(Energy.getMonthCap() + " MWh");
        Energy_monthcapl.setText(Energy.getMonthCapL() + " MWh");
        Energy_daycap.setText(Energy.getDayCap() + " MWh");
        Energy_hourcap.setText(Energy.getHourCap() + " MWh");
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }
}
