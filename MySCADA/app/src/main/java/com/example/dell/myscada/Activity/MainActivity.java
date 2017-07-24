package com.example.dell.myscada.Activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myscada.Base.BaseActivity;
import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.TabItemViewHolder;
import com.example.dell.myscada.ui.Util;
import com.example.dell.myscada.ui.adapter.TabHostAdapter;
import com.example.dell.myscada.ui.fragment.EmptyPager;
import com.example.dell.myscada.ui.fragment.FocusPager;
import com.example.dell.myscada.ui.fragment.VideoPager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dell on 2017/3/10.
 */
public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    private String TAG = "断点";
    private View view;
    private static int iback = 0;
    private static Boolean isExit = false;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    private TextView timeText;
    private TextView textView;

    class itm{
        int a,b,c,d;
        public itm(){
            this.a = 0;
            this.b = 0;
            this.c = 0;
            this.d = 0;
        }
    }
    private itm aa = new itm();

//    private AlmService almService;
//    private boolean mBound = false;
//
//    private ServiceConnection connection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            AlmService.AlarmBinder alarmBinder = (AlmService.AlarmBinder) service;
//            almService = alarmBinder.getService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBound = false;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().build());
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION );
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            获取样式中的属性值

            ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
            View statusBarView = new View(window.getContext());
            int statusBarHeight = getStatusBarHeight(window.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
            params.gravity = Gravity.TOP;
            statusBarView.setLayoutParams(params);
            statusBarView.setBackgroundColor(getResources().getColor(R.color.blue));
            decorViewGroup.addView(statusBarView);
        }else if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.blue));
        }

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initFragments();

        if(isNetworkConnected(getApplicationContext())){
            initData();
            handler.postDelayed(runnable, 1000 * 5);
//            if(mBound){
//                almService.monitorAlarm();
//            }
        }
    }

    private static int getStatusBarHeight(Context context){
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0){
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private String getVersionName() throws Exception{
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionName;
    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus){
//        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus && Build.VERSION.SDK_INT >= 19){
//            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//            | View.SYSTEM_UI_FLAG_FULLSCREEN
//            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//        }
//    }

    @Override
    protected  void onStart(){
        super.onStart();
//        Intent bindIntent = new Intent(getApplicationContext(), AlmService.class);
//        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onStop(){
        super.onStop();
//        if (mBound){
//            unbindService(connection);
//            mBound = false;
//        }
    }
    @Override
    protected void onDestroy(){
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_setting:
                Log.d(TAG,"--->设置按钮");
//                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
//                startActivity(intent);
                Util.showToast(this, "设置");
                break;
            case R.id.actin_about:
                try {
                    Util.showToast(this, "软件版本：" + getVersionName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d(TAG,"--->关于按钮");
                break;
            default:
                break;
        }
        return true;
    }

    public List<TabItemViewHolder> initTabItems(){
        List<TabItemViewHolder> tabItems = new ArrayList<>();
        tabItems.add(new TabItemViewHolder(MainActivity.this,"风车概况",R.drawable.tab_icon_first, FocusPager.class));
        tabItems.add(new TabItemViewHolder(MainActivity.this,"实时监测",R.drawable.tab_icon_video, VideoPager.class));
        tabItems.add(new TabItemViewHolder(MainActivity.this,"参数对比",R.drawable.tab_icon_focus, EmptyPager.class));
        return tabItems;
    }

    public void initFragments(){
        List<TabItemViewHolder> tabItems = initTabItems();
        FragmentTabHost mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        timeText = (TextView) findViewById(R.id.textTime);
        textView = (TextView) findViewById(R.id.textView);
        mTabHost.setup(MainActivity.this,getSupportFragmentManager(),android.R.id.tabcontent);
        mTabHost.getTabWidget().setBackgroundColor(Color.WHITE);//设置背景颜色
        mTabHost.getTabWidget().setDividerDrawable(null);//设置分割线
        //设置自定义的adapter
        final TabHostAdapter tabHostAdapter = new TabHostAdapter(MainActivity.this,mTabHost,tabItems);
        tabHostAdapter.notifyTabItemChange();
        tabHostAdapter.setSelectedTabByPosition(0);
//        设置选择监听器
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                tabHostAdapter.setSelectedTabByTag(s);
            }
        });
    }

    @Override
    public void onBackPressed(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (keyCode == KeyEvent.KEYCODE_BACK){
            exitByDoubleClick();
        }
        return false;
    }

    private void exitByDoubleClick(){
        Timer tExit = null;
        if (!isExit){
            isExit = true;
            Toast.makeText(this,"再按一次返回键退出SCADA",Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        }else {
            finish();
            System.exit(0);
        }
    }


    public void initData(){
        DBDefine dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        DBFactory dbFactory = new DBFactory(dbDefine);
        try{
            dbFactory.selectState();
        }catch (SQLException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        List<String> result = dbFactory.getList();
        for(String tmp:result){
            String[] list = String.valueOf(tmp).split("@_@@");
            isState(list[0]);
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        timeText.setText(str);
        aa = new itm();
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                String ret = Database.search();
//                Message msg = new Message();
//                msg.what = 1001;
//                Bundle data = new Bundle();
//                data.putString("result", ret);
//                msg.setData(data);
//                mHandler.sendMessage(msg);
//            }
//        };
//        new Thread(run).start();
    }

    public void isState(String tmp){
        String resultstr;
        switch (tmp){
            case "0.00":
                aa.d += 1;
                break;
            case "2.00":
            case "3.00":
                aa.b += 1;
                break;
            case "4.00":
            case "5.00":
            case "6.00":
                aa.a += 1;
                break;
            default:
                aa.c += 1;
                break;
        }
        resultstr = "发电： " + aa.a + "   待机： " + aa.b + "   停机： " + aa.c + "   未知:  " + aa.d;
        textView.setText(resultstr);
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
            int i = 0;
            Log.e("update()", "刷新" + ++i);
            if(isNetworkConnected(getApplicationContext())){
                initData();
            }
        }
    };

}
