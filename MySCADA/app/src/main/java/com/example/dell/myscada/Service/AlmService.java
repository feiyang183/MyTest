package com.example.dell.myscada.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.example.dell.myscada.Data.DBDefine;
import com.example.dell.myscada.Data.DBFactory;
import com.example.dell.myscada.R;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Dell on 2017/5/31.
 */

public class AlmService extends Service {
    private DBDefine dbDefine;
    private DBFactory dbFactory;

    private final static String SERVER_NAME = "jdbc:jtds:sqlserver://222.243.104.7:5555/LIZP_DB";
    private final static String LOGIN_NAME = "sa";
    private final static String LOGIN_PASSWORD = "apptech@0520*%";

    private static final int NOTIFY_ID = 0;
    private boolean cancelled = false;
    private Context mContext = this;
    private NotificationManager notificationManager;
    private Notification notification;

    public AlmService(){
        initDataBase();
    }

    private AlarmBinder mBinder = new AlarmBinder();

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            this.update();
            handler.postDelayed(this, 1000 * 60);
        }

        void update(){
            int i = 0;
            Log.e("update()", "刷新" + ++i);
            if(isNetworkConnected(getApplicationContext())){
                monitorAlarm();
            }
        }
    };

    public class AlarmBinder extends Binder{
        public AlmService getService(){
            return AlmService.this;
        }
    }



    private ArrayList<String> oldlist = new ArrayList<>();

    public void monitorAlarm(){

        Log.e("AlmService.monitorAlarm", "--->启动告警");
        try {
            if (dbFactory.selectAlarm()){
                ArrayList<String> list = new ArrayList<>();
                String result = "";

                list = dbFactory.getList();
                result = updateAlarm(list);

                Log.e("updateAlarm", "result --->" + result);
                if (cancelled){
                    Log.e("dbFactory", "查询数据库成功--->" + result);
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notification = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle("告警")
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(result))
                            .setWhen(System.currentTimeMillis())
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                            .setAutoCancel(true)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVibrate(new long[]{0, 2000, 300, 2000, 200, 2000})
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .build();
                    notificationManager.notify(NOTIFY_ID, notification);
                    cancelled = false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent){
        Log.e("AlmService", "--->服务绑定");
        throw new UnsupportedOperationException("Not yet implemented");
//        return mBinder;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.e("AlmService", "--->创建服务");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e("AlmService", "--->服务运行");
        if (isNetworkConnected(mContext)){
            handler.postDelayed(runnable, 1000 * 6);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        handler.removeCallbacks(runnable);
        Log.e("AlmService", "--->销毁服务");
    }

    private void initDataBase(){
        dbDefine = new DBDefine();
        dbDefine.setServer(SERVER_NAME);
        dbDefine.setUsername(LOGIN_NAME);
        dbDefine.setPassword(LOGIN_PASSWORD);
        dbFactory = new DBFactory(dbDefine);
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

    private String updateAlarm(ArrayList<String> list){
        String result = "";
        Log.e("updateAlarm","list.size()--->" + list.size() +" oldlist.size()--->" + oldlist.size());
        if (oldlist.size() == 0){
            for (int i = 0; i < list.size(); i ++){
                String[] list2 = String.valueOf(list.get(i)).split("@_@@");
                oldlist.add(list.get(i));
                result += list2[0] + " " + list2[3] + "\n";
                cancelled = true;
            }
        }else {
            for (int i = 0; i < list.size(); i ++){
                Log.e("For(list)","list.get(" + i + ")--->" + list.get(i));
                for (int a = 0; a < oldlist.size(); a ++ ){
                    Log.e("For(oldlist)", "oldlist.get(" + a + ")--->" + oldlist.get(a));
                    Log.e("是否一样", "--->" + list.get(i).equals(oldlist.get(a)));
                    if (list.get(i).equals(oldlist.get(a))){
                        cancelled = false;
                        break;
                    }else {
                        cancelled = true;
                    }
                }
                if (cancelled){
                    String[] list2 = String.valueOf(list.get(i)).split("@_@@");
                    oldlist.add(list.get(i));
                    result += list2[0] + list2[3] + "\n";
                }
            }
        }
        Log.e("输出结果为：", "result --->" + result + " cancelled ---> " + cancelled );
        return result;
    }
}
