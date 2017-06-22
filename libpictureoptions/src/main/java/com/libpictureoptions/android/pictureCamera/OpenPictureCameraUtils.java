package com.libpictureoptions.android.pictureCamera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.libpictureoptions.android.common.utils.LogUtils;

import java.io.File;


/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 13:57
 * 创建人：王亮（Loren wang）
 * 功能作用：开启拍摄图片框架
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class OpenPictureCameraUtils {
    private static final String TAG = "OpenPictureCameraUtils";
    /**
     * 开启系统相机
     * @param activity
     * @param savePath 拍照后的保存地址
     * @param requestCode 拍照的请求码
     */
    public static void openSystemCamera(Activity activity,String savePath,int requestCode){
        //检测权限，相机权限以及写存储卡权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            LogUtils.logE(TAG,"未获得相机权限或存储卡权限");
            return;
        }

        File imagePathFile = new File(savePath);
        if(activity == null || imagePathFile == null || imagePathFile.isDirectory()){
            return;
        }
        if(!imagePathFile.getParentFile().exists()){
            imagePathFile.getParentFile().mkdirs();
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //7.0及以上
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            Uri uri = FileProvider.getUriForFile(activity, "com.libpictureoptions.android",new File(savePath));
            if(uri == null){
                return;
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }else {
            Uri imageUri = Uri.fromFile(imagePathFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        }
        activity.startActivityForResult(intent, requestCode);
    }


    private static Context context;
    private static CameraTakePictureConfig config;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void open(Activity ctx, int requestCode, CameraTakePictureConfig pictureSelectConfig){
        if(ctx == null){
            return;
        }else {
            context = ctx;
        }
        if(pictureSelectConfig == null){
            config = new CameraTakePictureConfig.Bulid().build(context);
        }else {
            config = pictureSelectConfig;
        }
        Intent intent = new Intent(context, PictureCameraActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivityForResult(intent,requestCode);
    }

    public static CameraTakePictureConfig getConfig() {
        if(config == null){
            config = new CameraTakePictureConfig.Bulid().build(context);
        }
        return config;
    }

}
