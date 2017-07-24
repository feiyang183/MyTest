package com.example.dell.myscada.backstage;

import android.os.AsyncTask;
import android.util.Log;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.ui.fragment.EmptyPager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Dell on 2017/5/25.
 */

public class SpinnerAsyncTask extends AsyncTask<Integer, Integer, float[]> {
    public final static String[] monthStrs = new String[]{"W01","W02","W03","W04","W05","W06","W07","W08","W09","W10","W11","W12","W13","W14","W15","W16","W17","W18","W19","W20","W21","W22","W23","W24"};
    private ColumnChartView mColumnView;
    private String selectsp;
    private String str;
    private DBDefine dbDefine;
    private DBFactory dbFactory;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    public SpinnerAsyncTask(ColumnChartView mColumnView, String selectsp){
        this.mColumnView = mColumnView;
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
                    dbFactory.selectRuntime();
                    break;
                case "转速":
                    dbFactory.selectGeneratingCap();
                    break;
                case "发电量":
                    dbFactory.selectGeneratingCap();
                    break;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        List<String> result = dbFactory.getList();
        float[] Runtime = new float[24];
        for (int i = 0; i < 24; i ++){
            Runtime[i] = Float.valueOf(result.get(i));
        }
        return Runtime;
    }

    @Override
    protected void onPostExecute(float[] result){
        setColumnDatas(result);
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }

    private void setColumnDatas(float[] Runtime){
        int numSubcolumns = 1;
        int numColumns = monthStrs.length;
        float maxY = 0;

        List<AxisValue> axisValuesX = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for(int i = 0; i < numColumns; ++i){
            values = new ArrayList<>();
            maxY = maxY > Runtime[i] ? maxY:Runtime[i];
            for(int j = 0; j < numSubcolumns; ++j){
                values.add(new SubcolumnValue(Runtime[i], ChartUtils.pickColor()).setLabel(monthStrs[i]));
            }
            axisValuesX.add(new AxisValue(i).setValue(i).setLabel(monthStrs[i]));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        ColumnChartData mColumnData = new ColumnChartData(columns);
//        mColumnData = new ColumnChartData(columns);
        mColumnData.setAxisXBottom(new Axis(axisValuesX).setHasLines(true).setName("全场" + selectsp));
        mColumnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5));
        mColumnView.setColumnChartData(mColumnData);
//        mColumnView.setOnValueTouchListener(new EmptyPager.ColumnValueTouchListener());
        mColumnView.setValueSelectionEnabled(true);
        mColumnView.setZoomEnabled(false);

        Viewport v = new Viewport(-1,maxY+1000,24,0);
        mColumnView.setMaximumViewport(v);
        mColumnView.setCurrentViewport(v);
    }
}
