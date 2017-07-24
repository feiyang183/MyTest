package com.example.dell.myscada.Service;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

import java.util.HashMap;

/**
 * Created by 25468 on 2017/7/12.
 */

public class UpdataManager {
    private static final int DOWNLOAD = 1;
    private static final int DOWNLOAD_FINISH = 2;
    HashMap<String, String> mHashMap;
    private String mSavePath;
    private int progress;
    private boolean cancelUpdata = false;
    private Context mContext;
    private ProgressBar mProgress;
    private Dialog mDownloadDialog;


}
