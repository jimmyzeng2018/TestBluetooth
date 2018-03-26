package com.jimmy.bluetoothExtend;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/3/26.
 */

public class BluetoothUtil {

    public static void showToast(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
        ZjmUtil.Log(context,msg);
    }

    public void test(){

    }

}
