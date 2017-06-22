package com.libpictureoptions.android.common.utils.imageOptions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by lenovo on 2016/7/11.
 */
public class ImageUtils {
    private static ImageUtils imageUtils;
    private static Context context;
    private final  String TAG = getClass().getName();
    public static ImageUtils getInstance(Context ctx){
        if(ctx != null) {
            context = ctx;
            if (imageUtils == null) {
                return new ImageUtils();
            }
        }
        return imageUtils;
    }


    /**
     * 读取照片exif信息中的旋转角度
     * @param path 照片路径
     * @return角度
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转指定图片一定的角度
     * @param img
     * @param degree
     * @return
     */
    public Bitmap toturnPicture(Bitmap img,int degree){
        System.out.print("tag" + degree);
        if(degree != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree); /*翻转90度*/
            int width = img.getWidth();
            int height = img.getHeight();
            img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        }
        return img;
    }

    /**
     * 释放图片资源的方法
     * @param imageView
     */
    public void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
                bitmap=null;
            }
        }
        imageView.setImageBitmap(null);
    }

    /**
     * 释放bitmap
     * @param bitmap
     */
    public void releaseBitmap(Bitmap bitmap){
        if(bitmap != null){
            if(!bitmap.isRecycled()){
                bitmap.recycle();
            }
            bitmap = null;
        }
    }

    /**
     * 裁剪
     *
     * @param bitmap 原图
     * @return 裁剪后的图像
     */
    public Bitmap cropBitmap(Bitmap bitmap,int leftPercentForBitmapWidth,int topPercentForBitmapHeight
            ,int rightPercentForBitmapWidth,int bottomPercentForBitmapHeight) {
        //先进行约束
        if(leftPercentForBitmapWidth < 0){
            leftPercentForBitmapWidth = 0;
        }
        if(topPercentForBitmapHeight < 0){
            topPercentForBitmapHeight = 0;
        }
        if(rightPercentForBitmapWidth < 0){
            rightPercentForBitmapWidth = 0;
        }
        if(bottomPercentForBitmapHeight < 0){
            bottomPercentForBitmapHeight = 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap,(int)(width * leftPercentForBitmapWidth / 100.0)
                ,(int)(height * topPercentForBitmapHeight / 100.0)
                ,(int)(width * (100 - leftPercentForBitmapWidth - rightPercentForBitmapWidth) / 100.0)
                ,(int)(height * (100 - topPercentForBitmapHeight - bottomPercentForBitmapHeight) / 100.0), null, false);
    }


}
