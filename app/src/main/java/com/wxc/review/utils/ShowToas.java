package com.wxc.review.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wxc on 2017/5/5.
 */

public class ShowToas {
    private static Toast instance;
    private ShowToas(){}
    public static void showToast(Context context, String message) {
        if (instance==null) {
            instance = Toast.makeText(context, message, Toast.LENGTH_SHORT);

        }else{
            instance.setText(message);
        }
        instance.show();
    }
}
