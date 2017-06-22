package com.libpictureoptions.android.pictureCrop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;


/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 13:57
 * 创建人：王亮（Loren wang）
 * 功能作用：开启图片裁切框架
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class OpenPictureCropUtils {
    private static Context context;
    private static PictureCropConfig config;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void open(Activity ctx, int requestCode, PictureCropConfig pictureSelectConfig){
        if(ctx == null){
            return;
        }else {
            context = ctx;
        }
        if(pictureSelectConfig == null){
            config = new PictureCropConfig.Bulid().bulid(context);
        }else {
            config = pictureSelectConfig;
        }
        Intent intent = new Intent(context,  PictureCropActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivityForResult(intent,requestCode);
    }

    public static PictureCropConfig getConfig() {
        if(config == null){
            config = new PictureCropConfig.Bulid().bulid(context);
        }
        return config;
    }
}
