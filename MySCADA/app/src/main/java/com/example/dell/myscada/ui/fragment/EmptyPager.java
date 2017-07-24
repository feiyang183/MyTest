package com.example.dell.myscada.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dell.myscada.R;
import com.example.dell.myscada.backstage.ColumnAsyncTask;
import com.example.dell.myscada.backstage.SpinnerAsyncTask;
import com.example.dell.myscada.ui.UIInterface;
import com.example.dell.myscada.ui.Util;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class EmptyPager extends Fragment implements View.OnClickListener,UIInterface {

    public void onClick(View v){
        switch (v.getId()){
            default:
                processClick(v);
                break;
        }
    }

    protected void baseToast(String msg){
        Toast.makeText(rootView.getContext(),msg,Toast.LENGTH_SHORT).show();
    }

    protected void baseToast(int msgId){
        Toast.makeText(rootView.getContext(),msgId,Toast.LENGTH_SHORT).show();
    }

//    private DBDefine dbDefine;
//    private DBFactory dbFactory;
//
//    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
//    private final static String LOGIN_NAME = "sa";
//    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    private LineChartView mLineView;
    private ColumnChartView mColumnView;
    private Spinner spinner;

    private SpinnerAsyncTask spinnerAsyncTask;
    private ColumnAsyncTask columnAsyncTask;

    private LineChartData mLineData;
//    private ColumnChartData mColumnData;
    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private String selectsp = "运行时间";

//    public final static String[] monthStrs = new String[]{"W01","W02","W03","W04","W05","W06","W07","W08","W09","W10","W11","W12","W13","W14","W15","W16","W17","W18","W19","W20","W21","W22","W23","W24"};
    public final static String[] dayStrs = new String[]{"1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"};

    @Override
    public int getLayoutId(){
        return R.layout.fragment_empty_pager;
    }

    @Override
    public void initView(){
        mLineView = (LineChartView) rootView.findViewById(R.id.lcv_depend_main);
        mColumnView = (ColumnChartView) rootView.findViewById(R.id.ccv_depend_main);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
    }

    @Override
    public void initData(){
        setInitalLineDatas();
        setSpinner();
//        initDataBase();
//        try {
//            dbFactory.selectRuntime();
//        }catch (SQLException e){
//            e.printStackTrace();
//        }
//        List<String> result = dbFactory.getList();
//        float[] Runtime = new float[24];
//        for (int i = 0; i < 24; i ++){
//            Runtime[i] = Float.valueOf(result.get(i));
//        }
//        setColumnDatas(Runtime);
    }

    @Override
    public void initListener(){
        mLineView.setOnValueTouchListener(new LineValueTouchListener());
        mColumnView.setOnValueTouchListener(new ColumnValueTouchListener());
        spinner.setOnItemSelectedListener(new OnItemSelectedListener());
    }

    @Override
    public void processClick(View v){

    }

    private void setSpinner(){
        dataList = new ArrayList<String>();
        dataList.add("运行时间");
//        dataList.add("转速");
        dataList.add("发电量");
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, dataList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener());
    }

    private void setInitalLineDatas(){
//        int numValues = 7;
        int numValues = dayStrs.length;

        List<AxisValue> axisValues = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();
        for (int i = 0; i < numValues; ++i){
            values.add(new PointValue(i,0));
            axisValues.add(new AxisValue(i).setLabel(dayStrs[i]));
        }

        Line line = new Line(values);
        line.setColor(ChartUtils.COLOR_GREEN).setCubic(false);

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        mLineData = new LineChartData(lines);
        mLineData.setAxisXBottom(new Axis(axisValues).setHasLines(true).setName("全年" + selectsp));
        mLineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        mLineView.setLineChartData(mLineData);
        mLineView.setViewportCalculationEnabled(false);

        Viewport v = new Viewport(0,900,11,0);
        mLineView.setMaximumViewport(v);
        mLineView.setCurrentViewport(v);
        mLineView.setZoomEnabled(false);
    }

//    private void setColumnDatas(float[] Runtime){
//        int numSubcolumns = 1;
//        int numColumns = monthStrs.length;
//        float maxY = 0;
//
//        List<AxisValue> axisValuesX = new ArrayList<>();
//        List<Column> columns = new ArrayList<>();
//        List<SubcolumnValue> values;
//        for(int i = 0; i < numColumns; ++i){
//            values = new ArrayList<>();
//            maxY = maxY > Runtime[i] ? maxY:Runtime[i];
//            for(int j = 0; j < numSubcolumns; ++j){
//                values.add(new SubcolumnValue(Runtime[i], ChartUtils.pickColor()).setLabel(monthStrs[i]));
//            }
//            axisValuesX.add(new AxisValue(i).setValue(i).setLabel(monthStrs[i]));
//            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
//        }
//
//        mColumnData = new ColumnChartData(columns);
//        mColumnData.setAxisXBottom(new Axis(axisValuesX).setHasLines(true).setName("全场" + selectsp));
//        mColumnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5));
//        mColumnView.setColumnChartData(mColumnData);
//        mColumnView.setOnValueTouchListener(new ColumnValueTouchListener());
//        mColumnView.setValueSelectionEnabled(true);
//        mColumnView.setZoomEnabled(false);
//
//        Viewport v = new Viewport(-1,maxY+1000,24,0);
//        mColumnView.setMaximumViewport(v);
//        mColumnView.setCurrentViewport(v);
//    }

    private void setLineDatas(int color,float range){
        mLineView.cancelDataAnimation();

        Line line = mLineData.getLines().get(0);
        line.setColor(color);
        for(PointValue value : line.getValues()){
            value.setTarget(value.getX(), range);
        }
        mLineView.startDataAnimation();
    }


    private class LineValueTouchListener implements LineChartOnValueSelectListener{
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value){
            switch (selectsp){
                case "运行时间":
                    Util.showToast(rootView.getContext(), "第 " + ((int) value.getX()+ 1) + " 月，" + selectsp + " : " + value.getY() + " h");
                    break;
                case "发电量":
                    Util.showToast(rootView.getContext(), "第 " + ((int) value.getX()+ 1) + " 月，" + selectsp + " : " + value.getY() + " MWh");
                    break;
            }
        }

        @Override
        public void onValueDeselected(){

        }
    }

    private class ColumnValueTouchListener implements ColumnChartOnValueSelectListener{
        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value){
            if(value.getLabelAsChars() != null){
                String TB = String.valueOf(value.getLabelAsChars());
                switch (selectsp){
                    case "运行时间":
                        Util.showToast(rootView.getContext(), selectsp + " : " + value.getValue() + " h");
                        break;
                    case "发电量":
                        Util.showToast(rootView.getContext(), selectsp + " : " + value.getValue() + " MWh");
                        break;
                }
                if(isNetworkConnected(getContext())){
                    columnAsyncTask = new ColumnAsyncTask(mLineView, TB, value.getColor(), selectsp);
                    columnAsyncTask.execute();
                }
            }
        }

        @Override
        public void onValueDeselected(){
            setLineDatas(ChartUtils.COLOR_GREEN,0);
        }
    }

    private class OnItemSelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            selectsp = adapter.getItem(position);
            if(isNetworkConnected(getContext())){
                spinnerAsyncTask = new SpinnerAsyncTask(mColumnView, selectsp);
                spinnerAsyncTask.execute();
            }
        }

        @Deprecated
        public void onNothingSelected(AdapterView<?> parent){

        }
    }
//
//    private void initDataBase(){
//        dbDefine = new DBDefine();
//        dbDefine.setServer(SERVER_NAME);
//        dbDefine.setUsername(LOGIN_NAME);
//        dbDefine.setPassword(LOGIN_PASSWORD);
//        dbFactory = new DBFactory(dbDefine);
//    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.fragment_empty_pager,null);

            initView();

            initData();

            initListener();
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){//防止已经被添加过父view了，但是一般不会的
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    @Override
    public void onDestroy(){
        if (columnAsyncTask != null && columnAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            columnAsyncTask.cancel(true);
        if (spinnerAsyncTask != null && spinnerAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
            spinnerAsyncTask.cancel(true);
        super.onDestroy();
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
