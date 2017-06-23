package com.libpictureoptions.android.pictureCamera;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.libpictureoptions.android.common.utils.SystemInfoUtils;
import com.libpictureoptions.android.pictureCamera.interface_and_abstract.CameraPictureViewOnClickListener;
import com.libpictureoptions.android.pictureCamera.interface_and_abstract.VideoTranscribeStatueCallBack;
import com.libpictureoptions.android.pictureCamera.view.WxTakePictureOrVideoView;

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
    private boolean whetherPreview = true;// 是否预览，如果是拍摄的照片则预览照片，如果拍摄的是视频则预览视频，默认是预览
    private String pictureOrVideoSavePath;//拍照或者录像后的保存路径，如果不传递路径的话代表不保存

    //裁剪参数设置
    private int takePictureAfterCropLeftPercentForBitmapWidth = 0;//拍照之后生成的图片所有裁剪的图片的位置x坐标
    private int takePictureAfterCropTopPercentForBitmapHeight = 0;//拍照之后生成的图片所有裁剪的图片的位置y坐标
    private Integer takePictureAfterCropRightPercentForBitmapWidth;//拍照之后生成的图片所有裁剪的图片宽度
    private Integer takePictureAfterCropBottomPercentForBitmapHeight;//拍照之后生成的图片所有裁剪的图片高度

    //可替换视图设置
    private View cameraOptionsView;//拍照界面上层视图，如果有此时图的话该工具包中则不显示除view外的其他视图
    private View takePicturePreviewView;//拍照预览界面上层视图
    private ImageView imgPreview;//拍照之后的生成的图片的预览控件，作用于有特殊操作的时候使用,如果传递该控件则将该控件同时替换，否则不替换该控件
    private View videoPreviewView;//录像预览界面上层视图
    private WxTakePictureOrVideoView wxTakePictureOrVideoView;//仿微信的可替换视图

    //点击事件
    private CameraPictureViewOnClickListener takePictureClickListener;//拍照点击事件回调
    private CameraPictureViewOnClickListener videoClickListener;//录像点击事件回调
    private CameraPictureViewOnClickListener openLightClickListener;//开启闪光灯点击事件回调
    private CameraPictureViewOnClickListener cancelTakePicturePreviewClickListener;//取消拍照预览点击事件回调
    private CameraPictureViewOnClickListener confirmTakePicturePreviewClickListener;//确定拍照预览点击事件回调
    private CameraPictureViewOnClickListener cancelVideoPreviewClickListener;//取消录像点击预览
    private CameraPictureViewOnClickListener confirmVideoPreviewClickListener;//确定录像点击预览

    //仿微信录制小视频按钮设置
    private Integer videoMaxTime;//视频录制的最大时间
    private Integer longPressMaxTimeGoToVideo = 500;//长按最大的时间算是去录制算是长按录制，默认是2000毫秒的长按就去录制视屏
    private VideoTranscribeStatueCallBack videoTranscribeStatueCallBack;//传入的视频录制回调

    public PictureCameraConfig(Bulid bulid, Context context) {
        this.context = context;
        this.takePictureAfterCropLeftPercentForBitmapWidth = bulid.takePictureAfterCropLeftPercentForBitmapWidth;
        this.takePictureAfterCropTopPercentForBitmapHeight = bulid.takePictureAfterCropTopPercentForBitmapHeight;
        this.takePictureAfterCropRightPercentForBitmapWidth = bulid.takePictureAfterCropRightPercentForBitmapWidth == null ? SystemInfoUtils.getInstance(context).getWindowWidth() : bulid.takePictureAfterCropRightPercentForBitmapWidth;
        this.takePictureAfterCropBottomPercentForBitmapHeight = bulid.takePictureAfterCropBottomPercentForBitmapHeight == null ? SystemInfoUtils.getInstance(context).getWindowHeight() : bulid.takePictureAfterCropBottomPercentForBitmapHeight;

        this.whetherPreview = bulid.whetherPreview;
        this.pictureOrVideoSavePath = bulid.pictureOrVideoSavePath;

        this.cameraOptionsView = bulid.cameraOptionsView;
        this.takePicturePreviewView = bulid.takePicturePreviewView;
        this.imgPreview = bulid.imgPreview;
        this.videoPreviewView = bulid.videoPreviewView;
        this.wxTakePictureOrVideoView = bulid.wxTakePictureOrVideoView;

        this.takePictureClickListener = bulid.takePictureClickListener;
        this.videoClickListener = bulid.videoClickListener;
        this.openLightClickListener = bulid.openLightClickListener;
        this.cancelTakePicturePreviewClickListener = bulid.cancelTakePicturePreviewClickListener;
        this.confirmTakePicturePreviewClickListener = bulid.confirmTakePicturePreviewClickListener;
        this.cancelVideoPreviewClickListener = bulid.cancelVideoPreviewClickListener;
        this.confirmVideoPreviewClickListener = bulid.confirmVideoPreviewClickListener;

        this.videoMaxTime = bulid.videoMaxTime;
        this.longPressMaxTimeGoToVideo = bulid.longPressMaxTimeGoToVideo;
        this.videoTranscribeStatueCallBack = bulid.videoTranscribeStatueCallBack;
    }

    public static class Bulid{
        private boolean whetherPreview = true;// 是否预览，如果是拍摄的照片则预览照片，如果拍摄的是视频则预览视频，默认是预览
        private String pictureOrVideoSavePath;//拍照或者录像后的保存路径，如果不传递路径的话代表不保存

        //裁剪参数设置
        private int takePictureAfterCropLeftPercentForBitmapWidth = 0;//拍照之后生成的图片所有裁剪的图片的位置x坐标
        private int takePictureAfterCropTopPercentForBitmapHeight = 0;//拍照之后生成的图片所有裁剪的图片的位置y坐标
        private Integer takePictureAfterCropRightPercentForBitmapWidth;//拍照之后生成的图片所有裁剪的图片宽度
        private Integer takePictureAfterCropBottomPercentForBitmapHeight;//拍照之后生成的图片所有裁剪的图片高度

        //可替换视图设置
        private View cameraOptionsView;//拍照界面上层视图，如果有此时图的话该工具包中则不显示除view外的其他视图
        private View takePicturePreviewView;//拍照预览界面上层视图
        private ImageView imgPreview;//拍照之后的生成的图片的预览控件，作用于有特殊操作的时候使用,如果传递该控件则将该控件同时替换，否则不替换该控件
        private View videoPreviewView;//录像预览界面上层视图
        private WxTakePictureOrVideoView wxTakePictureOrVideoView;//仿微信的可替换视图

        //点击事件
        private CameraPictureViewOnClickListener takePictureClickListener;//拍照点击事件回调
        private CameraPictureViewOnClickListener videoClickListener;//录像点击事件回调
        private CameraPictureViewOnClickListener openLightClickListener;//开启闪光灯点击事件回调
        private CameraPictureViewOnClickListener cancelTakePicturePreviewClickListener;//取消拍照预览点击事件回调
        private CameraPictureViewOnClickListener confirmTakePicturePreviewClickListener;//确定拍照预览点击事件回调
        private CameraPictureViewOnClickListener cancelVideoPreviewClickListener;//取消录像点击预览
        private CameraPictureViewOnClickListener confirmVideoPreviewClickListener;//确定录像点击预览

        //仿微信录制小视频按钮设置
        private Integer videoMaxTime;//视频录制的最大时间
        private Integer longPressMaxTimeGoToVideo = 500;//长按最大的时间算是去录制算是长按录制，默认是2000毫秒的长按就去录制视屏
        private VideoTranscribeStatueCallBack videoTranscribeStatueCallBack;//传入的视频录制回调

        public Bulid setTakePictureAfterCrop(int takePictureAfterCropLeftPercentForBitmapWidth, int takePictureAfterCropTopPercentForBitmapHeight,
                                             int takePictureAfterCropRightPercentForBitmapWidth, int takePictureAfterCropBottomPercentForBitmapHeight){
            this.takePictureAfterCropLeftPercentForBitmapWidth = takePictureAfterCropLeftPercentForBitmapWidth;
            this.takePictureAfterCropTopPercentForBitmapHeight = takePictureAfterCropTopPercentForBitmapHeight;
            this.takePictureAfterCropRightPercentForBitmapWidth = takePictureAfterCropRightPercentForBitmapWidth;
            this.takePictureAfterCropBottomPercentForBitmapHeight = takePictureAfterCropBottomPercentForBitmapHeight;
            return this;
        }

        public Bulid setWhetherPreview(boolean whetherPreview) {
            this.whetherPreview = whetherPreview;
            return this;
        }

        public Bulid setPictureOrVideoSavePath(String pictureOrVideoSavePath) {
            this.pictureOrVideoSavePath = pictureOrVideoSavePath;
            return this;
        }

        public Bulid setCameraOptionsView(View cameraOptionsView) {
            this.cameraOptionsView = cameraOptionsView;
            return this;
        }

        public Bulid setTakePicturePreviewView(View takePicturePreviewView) {
            this.takePicturePreviewView = takePicturePreviewView;
            return this;
        }

        public Bulid setImgPreview(ImageView imgPreview) {
            this.imgPreview = imgPreview;
            return this;
        }

        public Bulid setVideoPreviewView(View videoPreviewView) {
            this.videoPreviewView = videoPreviewView;
            return this;
        }

        public Bulid setTakePictureClickListener(CameraPictureViewOnClickListener takePictureClickListener) {
            this.takePictureClickListener = takePictureClickListener;
            return this;
        }

        public Bulid setVideoClickListener(CameraPictureViewOnClickListener videoClickListener) {
            this.videoClickListener = videoClickListener;
            return this;
        }

        public Bulid setOpenLightClickListener(CameraPictureViewOnClickListener openLightClickListener) {
            this.openLightClickListener = openLightClickListener;
            return this;
        }

        public Bulid setCancelTakePicturePreviewClickListener(CameraPictureViewOnClickListener cancelTakePicturePreviewClickListener) {
            this.cancelTakePicturePreviewClickListener = cancelTakePicturePreviewClickListener;
            return this;
        }

        public Bulid setConfirmTakePicturePreviewClickListener(CameraPictureViewOnClickListener confirmTakePicturePreviewClickListener) {
            this.confirmTakePicturePreviewClickListener = confirmTakePicturePreviewClickListener;
            return this;
        }

        public Bulid setCancelVideoPreviewClickListener(CameraPictureViewOnClickListener cancelVideoPreviewClickListener) {
            this.cancelVideoPreviewClickListener = cancelVideoPreviewClickListener;
            return this;
        }

        public Bulid setConfirmVideoPreviewClickListener(CameraPictureViewOnClickListener confirmVideoPreviewClickListener) {
            this.confirmVideoPreviewClickListener = confirmVideoPreviewClickListener;
            return this;
        }

        public Bulid setWxTakePictureOrVideoView(WxTakePictureOrVideoView wxTakePictureOrVideoView) {
            this.wxTakePictureOrVideoView = wxTakePictureOrVideoView;
            return this;
        }

        public Bulid setVideoMaxTime(Integer videoMaxTime) {
            this.videoMaxTime = videoMaxTime;
            return this;
        }

        public Bulid setLongPressMaxTimeGoToVideo(Integer longPressMaxTimeGoToVideo) {
            this.longPressMaxTimeGoToVideo = longPressMaxTimeGoToVideo;
            return this;
        }

        public Bulid setVideoTranscribeStatueCallBack(VideoTranscribeStatueCallBack videoTranscribeStatueCallBack) {
            this.videoTranscribeStatueCallBack = videoTranscribeStatueCallBack;
            return this;
        }

        public PictureCameraConfig build(Context context){
            return new PictureCameraConfig(this,context);
        }

    }

    public boolean isWhetherPreview() {
        return whetherPreview;
    }

    public String getPictureOrVideoSavePath() {
        return pictureOrVideoSavePath;
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

    public View getTakePicturePreviewView() {
        return takePicturePreviewView;
    }

    public ImageView getImgPreview() {
        return imgPreview;
    }

    public View getVideoPreviewView() {
        return videoPreviewView;
    }

    public CameraPictureViewOnClickListener getTakePictureClickListener() {
        return takePictureClickListener;
    }

    public CameraPictureViewOnClickListener getVideoClickListener() {
        return videoClickListener;
    }

    public CameraPictureViewOnClickListener getOpenLightClickListener() {
        return openLightClickListener;
    }

    public CameraPictureViewOnClickListener getCancelTakePicturePreviewClickListener() {
        return cancelTakePicturePreviewClickListener;
    }

    public CameraPictureViewOnClickListener getConfirmTakePicturePreviewClickListener() {
        return confirmTakePicturePreviewClickListener;
    }

    public CameraPictureViewOnClickListener getCancelVideoPreviewClickListener() {
        return cancelVideoPreviewClickListener;
    }

    public CameraPictureViewOnClickListener getConfirmVideoPreviewClickListener() {
        return confirmVideoPreviewClickListener;
    }

    public WxTakePictureOrVideoView getWxTakePictureOrVideoView() {
        return wxTakePictureOrVideoView;
    }

    public Integer getVideoMaxTime() {
        return videoMaxTime;
    }

    public Integer getLongPressMaxTimeGoToVideo() {
        return longPressMaxTimeGoToVideo;
    }

    public VideoTranscribeStatueCallBack getVideoTranscribeStatueCallBack() {
        return videoTranscribeStatueCallBack;
    }
}
