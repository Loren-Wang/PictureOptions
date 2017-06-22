package com.libpictureoptions.android.pictureCompress;

import android.content.Context;
import android.graphics.Bitmap;

import net.bither.util.NativeUtil;


/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 13:57
 * 创建人：王亮（Loren wang）
 * 功能作用：开启图片压缩框架(只是起到一个间接跳转的作用)
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class OpenPictureCompressUtils {
    /**
     * @param image    bitmap对象
     * @param filePath 要保存的指定目录
     * @param maxSize // 最大图片大小 1000KB
     * @Description: 通过JNI图片压缩把Bitmap保存到指定目录
     */
    public static synchronized void compressBitmap(Bitmap image, String filePath, int maxSize) {
        NativeUtil.compressBitmap(image,filePath,maxSize);
    }

    /**
     * 压缩图片
     * @param context
     * @param compressFilePath
     * @param savePath
     * @param maxSize
     * @param isDegree 是否进行旋转
     * @return
     */
    public synchronized static boolean compressFile(Context context, String compressFilePath, String savePath, int maxSize,boolean isDegree){
        return NativeUtil.compressFile(context,compressFilePath,savePath,maxSize,isDegree);
    }

    /**
     * 压缩图片
     * @param context
     * @param compressFilePath
     * @param savePath
     * @param maxSize
     * @param degree 旋转角度
     * @return
     */
    public synchronized static boolean compressFile(Context context, String compressFilePath,  String savePath, int maxSize,int degree){
        return NativeUtil.compressFile(context,compressFilePath,savePath,maxSize,degree);
    }

}
