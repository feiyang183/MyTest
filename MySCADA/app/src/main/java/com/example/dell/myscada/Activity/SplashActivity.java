package com.example.dell.myscada.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.myscada.R;
import com.example.dell.myscada.bean.UpdataInfo;
import com.example.dell.myscada.ui.customview.CustomProgressDialog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dell on 2017/3/10.
 */
public class SplashActivity extends Activity{

    private static final int sleepTime = 2000;

    public final static int UPDATA_CLIENT = 0;
    public final static int GET_UNDATAINFO_ERROR = 1;
    public final static int DOWN_ERROR = 2;

    private final static String nameSpace = "http://tempuri.org/";
    private final static String methodName = "getVer";
    private final static String endPoint = "http://zhucegengxin.imwork.net:8010/";
    private final static String soapAction = "http://tempuri.org/getVer";

//    private CustomProgressDialog progressDialog = null;

    private final String TAG = this.getClass().getName();

    private UpdataInfo info;
    private int localVersion;

    @Override
    protected void onCreate(Bundle arg0){
        final View view = View.inflate(this, R.layout.activity_splash,null);
        super.onCreate(arg0);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(view);

        if (Build.VERSION.SDK_INT >= 23){
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            for (String str : permissions){
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED){
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }

    }

//    @Override
//    protected void onDestroy(){
//        progressDialog.dismiss();
//        super.onDestroy();
//    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();

        new Thread(CheckVersionTask).start();
    }

    private void LoginMain(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }


    private int getVersionCode() throws Exception{
        PackageManager packageManager = getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        return packageInfo.versionCode;
    }

    public static UpdataInfo getUpdataInfo(String fanID){
        SoapObject rpc = new SoapObject(nameSpace, methodName);
        rpc.addProperty("sFanID", fanID);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.bodyOut = rpc;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transport = new HttpTransportSE(endPoint);
        UpdataInfo updataInfo = new UpdataInfo();
        SoapObject resultso = null;
        try{
            transport.call(soapAction, envelope);

            if (envelope.getResponse() != null){
                resultso = (SoapObject) envelope.bodyIn;
                SoapObject soapObject = (SoapObject) resultso.getProperty("getVerResult");
                Log.e("getUpdataInfo", soapObject.toString());
                updataInfo.setVersionCode(Integer.valueOf(soapObject.getPropertyAsString("WindInsideVer")));
                updataInfo.setVersionName(soapObject.getPropertyAsString("WindVer"));
                updataInfo.setDescription(soapObject.getPropertyAsString("WindDescribe"));
                updataInfo.setUrl(soapObject.getPropertyAsString("WindAddr"));
            }
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof java.net.SocketTimeoutException){
//                Toast.makeText(getApplicationContext(), "连接服务器超时,请检查网络", Toast.LENGTH_LONG).show();
                updataInfo.setMsg("连接服务器超时,请检查网咯");
            }else if (e instanceof java.net.UnknownHostException){
//                Toast.makeText(getApplicationContext(), "未知服务器，请检查配置", Toast.LENGTH_LONG).show();
                updataInfo.setMsg("未知服务器，请检查配置");
            }
        }
        return updataInfo;
    }

    public static File getFileFrmServer(String path, CustomProgressDialog pd) throws Exception{
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            Log.e("conn", conn.getContentType().toString());
            pd.setMax(conn.getContentLength());
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();

            File file = new File(Environment.getExternalStorageDirectory(), "updata.apk");
            if (file.exists()){
                file.delete();
                Log.e("getFileFromServer", "文件已存在，执行删除");
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);


            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1){
                fos.write(buffer, 0, len);
                total += len;
                Log.e("getFileFromServer", "下载量:" +  (total * 100 / length) + "%");
//                pd.setProgress((total * 100 / length));
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        }else {
            return null;
        }
    }

    private Runnable CheckVersionTask = new Runnable() {
        @Override
        public void run() {
            try{
                long start = System.currentTimeMillis();
                long costTime = System.currentTimeMillis() - start;
                localVersion = getVersionCode();
                info = getUpdataInfo("牛排山");
//                String path = info.getUrl();
//                URL url = new URL(path);
                if (localVersion < info.getVersionCode()){
                    Log.i(TAG, "版本号不同，提示用户升级 系统版本：" + localVersion + "" + "服务器版本：" + info.getVersionCode());
                    Message msg = new Message();
                    msg.what = UPDATA_CLIENT;
                    handler.sendMessage(msg);
                }else {
                    if (sleepTime - costTime > 0){
                        try{
                            Thread.sleep(sleepTime - costTime);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    Log.i(TAG, "无需升级");
                    LoginMain();
                }

            }catch (Exception e){
                Message msg = new Message();
                msg.what = GET_UNDATAINFO_ERROR;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    };

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case UPDATA_CLIENT:
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_LONG).show();
                    LoginMain();
                    break;
                case DOWN_ERROR:
                    Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_LONG).show();
                    LoginMain();
                    break;
            }
        }
    };

    protected void showUpdataDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.dialog_updata_info, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_dialog_title);
        TextView tv_message = (TextView) view.findViewById(R.id.tv_dialog_message);
        tv_title.setText("版本更新");
        tv_message.setText(info.getDescription());
        builder.setView(view);

//        builder.setTitle("版本升级");
//        builder.setMessage(info.getVersionName() + "\n"+ info.getDescription());
        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LoginMain();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "下载apk,更新");
                downLoadApk();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void downLoadApk(){

//        final ProgressDialog pd;
//        pd = new ProgressDialog(this);
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setTitle("正在下载更新");
//        pd.setMessage("正在下载更新");
//        pd.show();
//        Log.e("DownloadAPK", pd.toString());

        final CustomProgressDialog progressDialog;
        progressDialog = new CustomProgressDialog(this);
        progressDialog.setMessage("正在下载更新");
        progressDialog.show();

        new Thread(){
            @Override
            public void run(){
                try{
                    Log.e(TAG, "DOWNLOADURL = " + info.getUrl());
                    File file = getFileFrmServer(info.getUrl(), progressDialog);
                    Log.e(TAG, "getFileFromServer" + file.toString());
                    sleep(3000);
                    installApk(file);
                    progressDialog.dismiss();
//                    pd.dismiss();
                }catch (Exception e){
                    Message msg = new Message();
                    msg.what = DOWN_ERROR;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                }
            }
        }.start();
    }

    protected void installApk(File file){
        Intent intent = new Intent();
        Log.e(TAG, file.toString());
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}

