package com.libpictureoptions.android.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.libpictureoptions.android.common.utils.CrashHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *创建时间：2016-7-11
 * 创建人：王亮
 * 功能作用：全局操作
 */
public class MyApplication extends Application {

    private static List<Map<String,Activity>> activityContextMapList = new ArrayList<>();
    private static Context context;

    public static Context getContext() {
        if(context == null){
            new MyApplication();
        }
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
        CrashHandler catchHandler = CrashHandler.getInstance();
        catchHandler.init(getApplicationContext());
//        //初始化文件存储路径
//        AppCommon.PROJECT_FILE_DIR = Environment.getExternalStorageDirectory().getPath()+ "/file/PictureSelects/";

    }

}
