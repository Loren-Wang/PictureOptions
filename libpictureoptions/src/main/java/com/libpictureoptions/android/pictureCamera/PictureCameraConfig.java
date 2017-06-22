package com.libpictureoptions.android.pictureCamera;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.libpictureoptions.android.common.utils.SystemInfoUtils;
import com.libpictureoptions.android.pictureCamera.interface_and_abstract.CameraPictureConfirmPreviewViewOnClickListener;
import com.libpictureoptions.android.pictureCamera.interface_and_abstract.CameraPictureViewOnClickListener;

import java.io.Serializable;

/**
 * Created by wangliang on 0016/2017/3/16.
 * 创建时间： 0016/2017/3/16 17:56
 * 创建人：王亮（Loren wang）
 * 功能作用：单独的配置文件
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class PictureCameraConfig implements Serializable{

    private Context context;
    private int takePictureAfterCropLeftPercentForBitmapWidth = 0;//拍照之后生成的图片所有裁剪的图片的位置x坐标
    private int takePictureAfterCropTopPercentForBitmapHeight = 0;//拍照之后生成的图片所有裁剪的图片的位置y坐标
    private Integer takePictureAfterCropRightPercentForBitmapWidth;//拍照之后生成的图片所有裁剪的图片宽度
    private Integer takePictureAfterCropBottomPercentForBitmapHeight;//拍照之后生成的图片所有裁剪的图片高度
    private View cameraOptionsView;//拍照界面视图
    private View previewView;//预览界面视图
    private CameraPictureViewOnClickListener takePictureClickListener;//拍照点击事件回调
    private CameraPictureViewOnClickListener openLightClickListener;//开启闪光灯点击事件回调
    private CameraPictureViewOnClickListener cancelPreviewClickListener;//取消预览点击事件回调
    private CameraPictureConfirmPreviewViewOnClickListener confirmPreviewClickListener;//确定预览点击事件回调
    private ImageView imgPreview;//拍照之后的生成的图片的预览

    public PictureCameraConfig(Bulid bulid, Context context) {
        this.context = context;
        this.takePictureAfterCropLeftPercentForBitmapWidth = bulid.takePictureAfterCropLeftPercentForBitmapWidth;
        this.takePictureAfterCropTopPercentForBitmapHeight = bulid.takePictureAfterCropTopPercentForBitmapHeight;
        this.takePictureAfterCropRightPercentForBitmapWidth = bulid.takePictureAfterCropRightPercentForBitmapWidth == null ? SystemInfoUtils.getInstance(context).getWindowWidth() : bulid.takePictureAfterCropRightPercentForBitmapWidth;
        this.takePictureAfterCropBottomPercentForBitmapHeight = bulid.takePictureAfterCropBottomPercentForBitmapHeight == null ? SystemInfoUtils.getInstance(context).getWindowHeight() : bulid.takePictureAfterCropBottomPercentForBitmapHeight;
        this.cameraOptionsView = bulid.cameraOptionsView;
        this.takePictureClickListener = bulid.takePictureClickListener;
        this.openLightClickListener = bulid.openLightClickListener;
        this.cancelPreviewClickListener = bulid.cancelPreviewClickListener;
        this.confirmPreviewClickListener = bulid.confirmPreviewClickListener;
        this.imgPreview = bulid.imgPreview;
    }

    public static class Bulid{
        private int takePictureAfterCropLeftPercentForBitmapWidth = 0;//拍照之后生成的图片所有裁剪的图片的位置x坐标
        private int takePictureAfterCropTopPercentForBitmapHeight = 0;//拍照之后生成的图片所有裁剪的图片的位置y坐标
        private Integer takePictureAfterCropRightPercentForBitmapWidth;//拍照之后生成的图片所有裁剪的图片宽度
        private Integer takePictureAfterCropBottomPercentForBitmapHeight;//拍照之后生成的图片所有裁剪的图片高度
        private View cameraOptionsView;//拍照界面视图
        private View previewView;//预览界面视图
        private CameraPictureViewOnClickListener takePictureClickListener;//拍照点击事件回调
        private CameraPictureViewOnClickListener openLightClickListener;//开启闪光灯点击事件回调
        private CameraPictureViewOnClickListener cancelPreviewClickListener;//取消预览点击事件回调
        private CameraPictureConfirmPreviewViewOnClickListener confirmPreviewClickListener;//确定预览点击事件回调
        private ImageView imgPreview;//拍照之后的生成的图片的预览

        public Bulid setTakePictureAfterCrop(int takePictureAfterCropLeftPercentForBitmapWidth, int takePictureAfterCropTopPercentForBitmapHeight,
                                             int takePictureAfterCropRightPercentForBitmapWidth, int takePictureAfterCropBottomPercentForBitmapHeight){
            this.takePictureAfterCropLeftPercentForBitmapWidth = takePictureAfterCropLeftPercentForBitmapWidth;
            this.takePictureAfterCropTopPercentForBitmapHeight = takePictureAfterCropTopPercentForBitmapHeight;
            this.takePictureAfterCropRightPercentForBitmapWidth = takePictureAfterCropRightPercentForBitmapWidth;
            this.takePictureAfterCropBottomPercentForBitmapHeight = takePictureAfterCropBottomPercentForBitmapHeight;
            return this;
        }

        public Bulid setCameraOptionsView(View cameraOptionsView) {
            this.cameraOptionsView = cameraOptionsView;
            return this;
        }

        public Bulid setPreviewView(View previewView) {
            this.previewView = previewView;
            return this;
        }

        public PictureCameraConfig build(Context context){
            return new PictureCameraConfig(this,context);
        }

        public Bulid setTakePictureClickListener(CameraPictureViewOnClickListener takePictureClickListener) {
            this.takePictureClickListener = takePictureClickListener;
            return this;
        }

        public Bulid setOpenLightClickListener(CameraPictureViewOnClickListener openLightClickListener) {
            this.openLightClickListener = openLightClickListener;
            return this;
        }

        public Bulid setCancelPreviewClickListener(CameraPictureViewOnClickListener cancelPreviewClickListener) {
            this.cancelPreviewClickListener = cancelPreviewClickListener;
            return this;
        }

        public Bulid setConfirmPreviewClickListener(CameraPictureConfirmPreviewViewOnClickListener confirmPreviewClickListener) {
            this.confirmPreviewClickListener = confirmPreviewClickListener;
            return this;
        }

        public Bulid setImgPreview(ImageView imgPreview) {
            this.imgPreview = imgPreview;
            return this;
        }
    }

    public int getTakePictureAfterCropLocationX() {
        return takePictureAfterCropLeftPercentForBitmapWidth;
    }

    public int getTakePictureAfterCropLocationY() {
        return takePictureAfterCropTopPercentForBitmapHeight;
    }

    public int getTakePictureAfterCropWidth() {
        return takePictureAfterCropRightPercentForBitmapWidth;
    }

    public int getTakePictureAfterCropHeight() {
        return takePictureAfterCropBottomPercentForBitmapHeight;
    }

    public View getCameraOptionsView() {
        return cameraOptionsView;
    }

    public View getPreviewView() {
        return previewView;
    }

    public CameraPictureViewOnClickListener getTakePictureClickListener() {
        return takePictureClickListener;
    }

    public CameraPictureViewOnClickListener getOpenLightClickListener() {
        return openLightClickListener;
    }

    public CameraPictureViewOnClickListener getCancelPreviewClickListener() {
        return cancelPreviewClickListener;
    }

    public CameraPictureConfirmPreviewViewOnClickListener getConfirmPreviewClickListener() {
        return confirmPreviewClickListener;
    }

    public ImageView getImgPreview() {
        return imgPreview;
    }
}
