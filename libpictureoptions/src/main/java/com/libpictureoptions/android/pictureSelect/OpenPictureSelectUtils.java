package com.libpictureoptions.android.pictureSelect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;


/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 13:57
 * 创建人：王亮（Loren wang）
 * 功能作用：开启图片选择框架
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class OpenPictureSelectUtils {
    private static Context context;
    private static PictureSelectConfig config;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void open(Activity ctx, int requestCode, PictureSelectConfig pictureSelectConfig){
        if(ctx == null){
            return;
        }else {
            context = ctx;
        }
        if(pictureSelectConfig == null){
            config = new PictureSelectConfig.Bulid().bulid(context);
        }else {
            config = pictureSelectConfig;
        }
        Intent intent = new Intent(context,  PictureSelectActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivityForResult(intent,requestCode);
    }

    public static PictureSelectConfig getConfig() {
        if(config == null){
            config = new PictureSelectConfig.Bulid().bulid(context);
        }
        return config;
    }
}
