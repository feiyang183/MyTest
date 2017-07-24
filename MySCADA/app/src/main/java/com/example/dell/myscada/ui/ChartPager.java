package com.example.dell.myscada.ui;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CompoundButtonCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.FragmentInfo;
import com.example.dell.myscada.model.mEnergy;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * Created by yuankaifeng on 11/29/16.
 */

public class ChartPager extends Fragment implements View.OnClickListener,UIInterface {

    private DBDefine dbDefine;
    private DBFactory dbFactory;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    private LineChartView mLineChartView;
    private CheckBox Cbgeneralcap;
    private CheckBox Cbgeneraltemp;
    private CheckBox Cbgeneralspeed;

    private LineChartData mLineData;
    private int numberOfLines = 3;
    private int maxNumberOfLines = 4;
    private int numberOfPoints = 12;

    private boolean isHasAxes = true;
    private boolean isHasAxesNames = true;
    private boolean isHasLines = true;
    private boolean isHasPoints = true;
    private boolean isFilled = false;
    private boolean isHasPointsLabels = false;
    private boolean isCubic = false;
    private boolean isPointsHasSelected = false;
    private boolean isPointsHaveDifferentColor;

    private ValueShape pointsShape = ValueShape.CIRCLE;
    float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

    @Override
    public int getLayoutId(){
        return R.layout.chart_pager;
    }

    @Override
    public void initView(){
        mLineChartView = (LineChartView) rootView.findViewById(R.id.lvc_main);
        mLineChartView.setViewportCalculationEnabled(false);

        Cbgeneralcap = (CheckBox) rootView.findViewById(R.id.generalcap);
        Cbgeneralcap.setTextColor(Color.RED);
        Cbgeneraltemp = (CheckBox) rootView.findViewById(R.id.generaltep);
        Cbgeneraltemp.setTextColor(Color.BLUE);
        Cbgeneralspeed = (CheckBox) rootView.findViewById(R.id.generalspeed);
        Cbgeneralspeed.setTextColor(Color.GREEN);
    }

    @Override
    public void initData(){
        setPointsValues();
        setLinesDatas();
        resetViewport();
    }

    @Override
    public void initListener(){
        mLineChartView.setOnValueTouchListener(new ValueTouchListener());
        Cbgeneralcap.setOnCheckedChangeListener(new CbgeneralcapListener());
        Cbgeneraltemp.setOnCheckedChangeListener(new CbgeneraltempListener());
        Cbgeneralspeed.setOnCheckedChangeListener(new CbgeneralspeedListener());
    }

    @Override
    public void processClick(View v){

    }

    private void setPointsValues(){
        for(int i = 0; i < maxNumberOfLines; ++i){
            for(int j = 0; j < numberOfPoints; ++j){
                randomNumbersTab[i][j] = (float) Math.random() * 100f;
            }
        }
    }

    private void setLinesDatas(){
        List<Line> lines = new ArrayList<>();
//        List<AxisValue> axisValues = new ArrayList<>();

        for(int i = 0; i < numberOfLines; ++i){
            List<PointValue> values = new ArrayList<>();

            for(int j = 0; j < numberOfPoints; ++j){
                values.add(new PointValue(j,randomNumbersTab[i][j]));
//                axisValues.add(new AxisValue(i).setLabel(j+""));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(pointsShape);
            line.setHasLines(isHasLines);
            line.setHasPoints(isHasPoints);
            line.setCubic(isCubic);
            line.setFilled(isFilled);
            line.setHasLabels(isHasPointsLabels);
            line.setHasLabelsOnlyForSelected(isPointsHasSelected);
            if(isPointsHaveDifferentColor){
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }

        mLineData = new LineChartData(lines);
        mLineData.setBaseValue(Float.NEGATIVE_INFINITY);

        if(isHasAxes){
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            axisX.setTextColor(Color.GRAY);
            axisY.setTextColor(Color.GRAY);

            if(isHasAxesNames){
                axisX.setName("时间");
                axisY.setName("Axis Y");
            }
            mLineData.setAxisXBottom(axisX);
            mLineData.setAxisYLeft(axisY);
        }else {
            mLineData.setAxisXBottom(null);
            mLineData.setAxisYLeft(null);
        }

        mLineChartView.setLineChartData(mLineData);
    }

    private void resetViewport(){
        final Viewport v = new Viewport(mLineChartView.getMaximumViewport());
        v.left = 0;
        v.bottom = 0;
        v.top = 100;
        v.right = numberOfPoints - 1;
        mLineChartView.setMaximumViewport(v);
        mLineChartView.setCurrentViewport(v);
    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {
        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value){
            Toast.makeText(rootView.getContext(),"选中第" + ((int) value.getX() + 1) + " 个节点",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected(){

        }
    }

    private class ChangeLinesAnimListener implements ChartAnimationListener{
        private Viewport v;
        public ChangeLinesAnimListener(Viewport v){
            this.v = v;
        }
        @Override
        public void onAnimationStarted(){

        }
        @Override
        public void onAnimationFinished(){
            mLineChartView.setMaximumViewport(v);
            mLineChartView.setViewportAnimationListener(null);
        }
    }

    private class CbgeneralcapListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if (isChecked){
                isHasCap = true;
            }else {
                isHasCap = false;
            }
        }
    }

    private class CbgeneraltempListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if (isChecked){
                isHasTemp = true;
            }else {
                isHasTemp = false;
            }
        }
    }

    private class CbgeneralspeedListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
            if (isChecked){
                isHasSpeed = true;
            }else {
                isHasSpeed = false;
            }
        }
    }

    private Timer timer = new Timer();
    private boolean isFinish = false;
    private int position = 0;
    private List<PointValue> pointValueList = new ArrayList<>();
    private List<PointValue> CapValueList = new ArrayList<>();
    private List<PointValue> TempValueList = new ArrayList<>();
    private List<PointValue> SpeedValueList = new ArrayList<>();
    private List<AxisValue> axisValues = new ArrayList<>();
    private boolean isHasCap = true;
    private boolean isHasTemp = false;
    private boolean isHasSpeed = false;
    private Random random = new Random();
    private List<Line> linesList = new ArrayList<>();
    private LineChartData lineChartData;
    private static float MaxY = 0;
    private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");


//    public final static String[] timeStrs = new String[]{}
    private static int abs = 0;

    private void dynamicDataDisplay(){
        mLineChartView.setInteractive(false);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!isFinish && isNetworkConnected(getContext())){

                    initDataBase();
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    try {
                        dbFactory.selectFGenerator(energy.getId());
                    }catch (SQLException e){
                        e.printStackTrace();
                    }catch (RuntimeException e){
                        e.printStackTrace();
                    }
                    String result = dbFactory.getString();
                    String[] list = String.valueOf(result).split("@_@@");
//                    float time = Float.valueOf(list[0]);
                    float genercap = Float.valueOf(list[1]);
                    float genertep = Float.valueOf(list[2]);
                    float generspeed = Float.valueOf(list[3]);

                    PointValue pointValue1 = new PointValue(position*5, genercap).setLabel("GeneralCap");
                    PointValue pointValue2 = new PointValue(position*5, genertep).setLabel("GeneralTemp");
                    PointValue pointValue3 = new PointValue(position*5, generspeed).setLabel("GeneralSpeed");
                    CapValueList.add(pointValue1);
                    TempValueList.add(pointValue2);
                    SpeedValueList.add(pointValue3);
                    float x = pointValue1.getX();

                    Line capline = new Line(CapValueList).setColor(Color.RED).setShape(ValueShape.CIRCLE).setCubic(false).setHasLines(isHasCap).setHasPoints(isHasCap);
                    Line templine = new Line(TempValueList).setColor(Color.BLUE).setShape(ValueShape.CIRCLE).setCubic(false).setHasLines(isHasTemp).setHasPoints(isHasTemp);
                    Line speedline = new Line(SpeedValueList).setColor(Color.GREEN).setShape(ValueShape.CIRCLE).setCubic(false).setHasLines(isHasSpeed).setHasPoints(isHasSpeed);

                    axisValues.add(new AxisValue(position*5).setLabel(str));
                    linesList.clear();
                    linesList.add(capline);
                    linesList.add(templine);
                    linesList.add(speedline);
                    lineChartData = initDatas(linesList);
                    lineChartData.setAxisXBottom(new Axis(axisValues));
                    mLineChartView.setLineChartData(lineChartData);
//                    PointValue value1 = new PointValue(position * 5,40 + random.nextInt(20));
//                    value1.setLabel("00:00");
//                    pointValueList.add(value1);
//                    float x = value1.getX();
//                    Line line = new Line(pointValueList);
//                    line.setColor(Color.GREEN);
//                    line.setShape(ValueShape.CIRCLE);
//                    line.setCubic(true);

//                    axisValues.add(new AxisValue(position * 5).setLabel(abs+"s"));
//
//                    linesList.clear();
//                    linesList.add(line);
//                    lineChartData = initDatas(linesList);
//                    lineChartData.setAxisXBottom(new Axis(axisValues).setName("时间轴"));
//                    mLineChartView.setLineChartData(lineChartData);
                    float y = maxY(genercap, genertep, generspeed);
                    MaxY = MaxY > y ? MaxY:y;
                    Viewport port;
                    if(x > 25){
                        port = initViewPort(x - 25, x, MaxY);
                    }else{
                        port = initViewPort(0, 25, MaxY);
                    }
                    mLineChartView.setCurrentViewport(port);

                    Viewport maPort = initMaxViewPort(x, MaxY);
                    mLineChartView.setMaximumViewport(maPort);

                    position++;
                    if(position > 100 -1){
                        isFinish = true;
                        mLineChartView.setInteractive(true);
                    }
                    abs +=3;
                }
            }
        },0,5000);
    }

    private LineChartData initDatas(List<Line> lines){
        LineChartData data = new LineChartData(lines);
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisX.setTextColor(Color.GRAY);
        axisY.setTextColor(Color.GRAY);
        axisX.setName("时间轴(S)");
//        axisY.setName("风速");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        return data;
    }

    private Viewport initViewPort(float left, float right, float top){
        Viewport port = new Viewport();
        port.top = top + 50;
        port.bottom = 0;
        port.left = left;
        port.right = right;
        return port;
    }

    private Viewport initMaxViewPort(float right, float top){
        Viewport port = new Viewport();
        port.top = 50 + top;
        port.bottom = 0;
        port.left = 0;
        port.right = right + 50;
        return port;
    }

    private float maxY(float a, float b, float c){
        if(a >= b & a >= c){
            return a;
        }else if (b >= a & b >= c){
            return b;
        }else {
            return c;
        }
    }


    @Override
    public void onClick(View v){
        switch (v.getId()){
            default:
                processClick(v);
                break;
        }
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
    }

    private View  rootView;
    private TextView tv;
    private FragmentInfo baseInfo;
    private TableLayout tableLayout;
    private mEnergy energy;
    private boolean isHasNet = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
//            isHasNet = true;
//        }

        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.chart_pager,null);

            initView();

//            initData();
            dynamicDataDisplay();

            initListener();
//            tv.setText(this.getClass().getSimpleName()+"----"+baseInfo.getTitle());
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public ChartPager setBaseInfo(FragmentInfo baseInfo, mEnergy energy){
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
}
