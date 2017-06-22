package com.libpictureoptions.android.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by wangliang on 0017/2016/8/17.
 */
public class FileIOUtils {
    private static Context context;
    private static FileIOUtils fileIOUtils;
    private static final String TAG = "FileIOUtils";
    private LocationManager locationManager;
    private String provider;


    public FileIOUtils(Context context) {
        this.context = context.getApplicationContext();
    }

    public static FileIOUtils getInstance(Context ctx) {
        if (fileIOUtils == null) {
            fileIOUtils = new FileIOUtils(ctx);
        }
        return fileIOUtils;
    }


    /**
     * 保存图片
     * @param bitmap
     * @param localCachePath
     */
    public boolean saveBitmap(Bitmap bitmap, String localCachePath){
        String savePath = "";
        File file = new File(localCachePath);
        if(file.isDirectory()){
            file.mkdirs();
            savePath = localCachePath + "/" + AppUtils.generateUuid() + ".jpg";
        }else if(file.getAbsoluteFile().isDirectory() && !file.getAbsoluteFile().exists()){//file不是文件夹
            file.getAbsoluteFile().mkdirs();
            savePath = localCachePath;
        }else {
            savePath = localCachePath;
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(savePath));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


}
