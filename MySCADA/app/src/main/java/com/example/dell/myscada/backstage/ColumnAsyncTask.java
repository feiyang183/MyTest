package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Dell on 2017/5/19.
 */

public class ColumnAsyncTask extends AsyncTask<Integer, Integer, float[]> {
    private LineChartView mLineView;
    private String selectsp;
    private int color;
    private String str;
    private DBDefine dbDefine;
    private DBFactory dbFactory;

    public final static String[] dayStrs = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};
    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public ColumnAsyncTask(LineChartView mLineView, String str, int color, String selectsp){
        this.mLineView = mLineView;
        this.str = str;
        this.color = color;
        this.selectsp = selectsp;
        initDataBase();
    }

    @Override
    protected void onPreExecute(){

    }

    @Override
    protected float[] doInBackground(Integer... params){

        try {
            switch (selectsp){
                case "运行时间":
                    dbFactory.selectRuntime(str);
                    break;
                case "发电量":
                    dbFactory.selectGeneratingCap(str);
                    break;
                case "转速":
                    dbFactory.selectRuntime(str);
                    break;
            }

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
        setLineDatas(color, result);
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }

    private void setLineDatas(int color,float[] result){
        mLineView.cancelDataAnimation();

        List<AxisValue> axisValues = new ArrayList<>();
        Line line = mLineView.getLineChartData().getLines().get(0);
        line.setColor(color);
        for(int i = 0; i < 12; i ++){
            PointValue value = line.getValues().get(i);
            value.setTarget(value.getX(), result[i]);
            axisValues.add(new AxisValue(i).setLabel(dayStrs[i]));
        }
        mLineView.getLineChartData().setAxisXBottom(new Axis(axisValues).setHasLines(true).setName("全年" + selectsp));

        mLineView.startDataAnimation();
    }
}
