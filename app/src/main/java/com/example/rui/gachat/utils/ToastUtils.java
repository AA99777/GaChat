package com.example.rui.gachat.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Rui on 2017/12/8.
 */

public class ToastUtils {
    public static void toast(Context context ,String s){
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }
}
