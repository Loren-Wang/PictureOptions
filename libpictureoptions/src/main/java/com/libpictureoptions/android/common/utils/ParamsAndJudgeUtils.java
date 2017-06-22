package com.libpictureoptions.android.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.libpictureoptions.android.common.MyApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wangliang on 0024/2017/2/24.
 * 创建人：王亮
 * 创建时间：2017.2.24
 * 功能作用：整个app中所有的和格式化以及判定是否符合莫伊条件的工具类都在这里
 */

public class ParamsAndJudgeUtils {
    private static ParamsAndJudgeUtils paramsAndJudgeUtils;

    private HandlerThread handlerThread;
    public Handler handler;

    public ParamsAndJudgeUtils() {
        handlerThread = new HandlerThread(getClass().getName());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public static ParamsAndJudgeUtils getInstance(){
        if(paramsAndJudgeUtils==null) {
            paramsAndJudgeUtils = new ParamsAndJudgeUtils();
        }
        return paramsAndJudgeUtils;
    }

    /**
     * 检查传入参数
     * @param objects
     * @return
     */
    private boolean check(Object... objects){
        if(objects == null || paramsAndJudgeUtils == null){
            return false;
        }
        //判定其中是否有空值
        for (Object object : objects){
            if(object == null){
                return false;
            }
        }
        //如果执行到了这一步那么就代表着所传递进来的参数中没有为空的参数
        return true;
    }


    /**
     *   判断程序是否在后台运行
     * @param context
     * @return 在后台返回false，前台返回true
     */
    public boolean isNotBackground(Context context) {
        try {
            if(!check(context)){
                context = MyApplication.getContext();
            }
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                    Log.i(context.getPackageName(), "此appimportace ="
                            + appProcess.importance
                            + ",context.getClass().getName()="
                            + context.getClass().getName());

                    if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        Log.i(context.getPackageName(), "处于后台"
                                + appProcess.processName);
                        return false;
                    } else {
                        Log.i(context.getPackageName(), "处于前台"
                                + appProcess.processName);
                        return true;
                    }
                }
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 将map的所有key值转成集合
     * @param map
     * @param <T>
     * @return
     */
    public<K,T> List<K> paramsHashMapKeyToArrayList(Map<K,List<T>> map ){
        List<K> list = new ArrayList<>();
        if(map == null){
            return list;
        }
        Iterator<Map.Entry<K, List<T>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            list.add(iterator.next().getKey());
        }
        return list;
    }

    /**
     * 获取绝对路径下最后一个文件夹名称
     * @param absolutePath
     * @return
     */
    public String getLastDirctoryName(String absolutePath){
        if(absolutePath == null){
            return "";
        }
        //创建新的，防止由于使用同一个对象对于调用该方法的值产生影响
        String path = new String(absolutePath);
        //判断是不是文件，是文件的话获取父文件夹路径
        File file = new File(path);
        if(file.isFile()){
            path = file.getParentFile().getAbsolutePath();
        }

        if(path.contains("/")) {
            //循环移除末尾的“/”，防止一个路径下有多个“/”
            while (path.substring(path.lastIndexOf("/")).intern().equals("/")) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.substring(path.lastIndexOf("/") + 1);
        }
        return path;
    }



}
