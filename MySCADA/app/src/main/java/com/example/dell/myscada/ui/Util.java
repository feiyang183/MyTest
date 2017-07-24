package com.example.dell.myscada.ui;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by 25468 on 2017/7/21.
 */

public class Util {
    private static Toast toast;

    public static void showToast(Context context, String content){
        if (toast == null){
            toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        }else {
            toast.setText(content);
        }
        toast.show();
    }
}
