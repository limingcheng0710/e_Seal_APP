package com.nerosong.sittingmonitor;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

public class ImageUtils {
    private static String TAG = "Test";
    public static File tempFile;

    public static Uri getImageUri(Context content) {
        File file = setTempFile( content );
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= 24){
            //将File对象转换成封装过的Uri对象，这个Uri对象标志着照片的真实路径
            Uri imageUri = FileProvider.getUriForFile( content, "com.nerosong.sittingmonitor.fileprovider", file );
            return imageUri;
        }else{
            //将File对象转换成Uri对象，这个Uri对象标志着照片的真实路径
            Uri imageUri = Uri.fromFile( file );
            return imageUri;
        }
    }

    public static File setTempFile(Context content) {
        //自定义图片名称
        String name = DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance( Locale.CHINA)) + ".png";
        Log.i( TAG, " name : "+name );
        //定义图片存放的位置
        tempFile = new File(content.getExternalCacheDir(),name);
        Log.i( TAG, " tempFile : "+tempFile );
        return tempFile;
    }

    public static File getTempFile() {
        return tempFile;
    }
}

