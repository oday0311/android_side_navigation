package com.devspark.sidenavigation.sample;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;

import java.io.File;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: apple
 * Date: 13-6-15
 * Time: 下午11:21
 * To change this template use File | Settings | File Templates.
 */
public class Utils  {
    private Utils(){

    }




    public static File appExternalDirPath(){
        File result = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            result = Environment.getExternalStorageDirectory();
        }
//        Log.d("CuzyAdSDK","appExternalDirPath:" + result.getAbsolutePath());
        return result;
    }
    public static String uniqueIdentifier(Context context){
//        return "android";


        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            return deviceUuid.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
