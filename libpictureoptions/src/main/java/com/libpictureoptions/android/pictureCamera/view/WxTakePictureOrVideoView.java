package com.libpictureoptions.android.pictureCamera.view;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.libpictureoptions.android.pictureCamera.interface_and_abstract.VideoTranscribeStatueCallBack;
import com.libpictureoptions.android.pictureCamera.utils.CameraOptionsUtils;
import com.libpictureoptions.android.pictureCamera.utils.CameraVideoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangliang on 0023/2017/6/23.
 * 创建时间： 0023/2017/6/23 10:13
 * 创建人：王亮（Loren wang）
 * 功能作用：仿微信的点击拍照长按录制小视频的按钮控件，如果要使用这种控件的话需要继承该控件，
 * 该控件内的方法所有方法都不要动，尤其是触摸的事件分发，否则不一定会有什么反应，在调用方法上依旧使用config调用，其他方式调用容易出错
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class WxTakePictureOrVideoView extends View{
    protected static final String TAG = "WxTakePictureOrVideoView";
    private Context context;
    protected Integer videoMaxTime = 60000;//录制视屏的最大时间，单位毫秒，默认值60000毫秒，即60秒
    private Integer longPressMaxTimeGoToVideo = 500;//长按最大的时间算是去录制算是长按录制，默认是2000毫秒的长按就去录制视屏
    private String savePictureOrVideoPath;//拍照或者录像之后所保存的地址
    private boolean whetherVideotaping = false;//是否正在录制视频
    private List<VideoTranscribeStatueCallBack> videoTranscribeStatueCallBackList = new ArrayList<>();//传入的视频录制回调
    private SurfaceView surfaceView;//预览窗口
    private Camera.ShutterCallback shutterCallback;//拍照的四个回调之一
    private Camera.PictureCallback originalPictureDataCallback;//拍照的四个回调之一
    private Camera.PictureCallback zoomPictureDataCallback;//拍照的四个回调之一
    private Camera.PictureCallback jpegPictureDataCallback;//拍照的四个回调之一

    private HandlerThread handlerThread = new HandlerThread(getClass().getName());
    private Handler handler;
    private Runnable gotoVideoTimekeepingRunnable;//判断是否要去录像的计时runnable


    public WxTakePictureOrVideoView(Context context) {
        super(context);
        init(context);
    }

    public WxTakePictureOrVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WxTakePictureOrVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        this.context = context;
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        gotoVideoTimekeepingRunnable = new Runnable() {
            @Override
            public void run() {
                if(!whetherVideotaping){
                    startVideo();
                }
            }
        };
    }

    public WxTakePictureOrVideoView setLongPressMaxTimeGoToVideo(Integer longPressMaxTimeGoToVideo) {
        this.longPressMaxTimeGoToVideo = longPressMaxTimeGoToVideo;
        return this;
    }

    public WxTakePictureOrVideoView setVideoMaxTime(Integer videoMaxTime) {
        this.videoMaxTime = videoMaxTime;
        return this;
    }

    public WxTakePictureOrVideoView setSavePictureOrVideoPath(String savePictureOrVideoPath) {
        this.savePictureOrVideoPath = savePictureOrVideoPath;
        return this;
    }

    public WxTakePictureOrVideoView addVideoTranscribeStatueCallBack(VideoTranscribeStatueCallBack videoTranscribeStatueCallBack) {
        this.videoTranscribeStatueCallBackList.add(videoTranscribeStatueCallBack);
        return this;
    }

    public void setTakePictureCallbackAndSufaceView(SurfaceView surfaceView,
                                                    Camera.ShutterCallback shutterCallback,
                                                    Camera.PictureCallback originalPictureDataCallback,
                                                    Camera.PictureCallback zoomPictureDataCallback,
                                                    Camera.PictureCallback jpegPictureDataCallback){
        this.surfaceView = surfaceView;
        this.shutterCallback = shutterCallback;
        this.originalPictureDataCallback = originalPictureDataCallback;
        this.zoomPictureDataCallback = zoomPictureDataCallback;
        this.jpegPictureDataCallback = jpegPictureDataCallback;
    }

    private float downX;
    private float downY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getRawX();
                downY = event.getRawY();
                //开启计时器
                handler.postDelayed(gotoVideoTimekeepingRunnable,longPressMaxTimeGoToVideo);
                break;
            case MotionEvent.ACTION_UP:
                //先移除计时器
                handler.removeCallbacks(gotoVideoTimekeepingRunnable);
                if(whetherVideotaping) {
                    //停止录制视频
                    stopVideo();
                }else {
                    //判断是否是要去拍照
                    if (Math.abs(downX - event.getRawX()) < 20
                            && Math.abs(downY - event.getRawY()) < 20
                            && !whetherVideotaping) {
                        takePicture();
                    }
                }
                downX = 0f;
                downY = 0f;
                break;
            default:
                return super.dispatchTouchEvent(event);
        }
        return true;
    }

    /**
     * 拍照
     */
    private void takePicture(){
        if(surfaceView != null && !whetherVideotaping) {
            CameraOptionsUtils.getInstance(context,surfaceView).takePicture(shutterCallback,originalPictureDataCallback,zoomPictureDataCallback,jpegPictureDataCallback);
        }
    }

    private void startVideo() {
        if(!whetherVideotaping
                && !CameraVideoUtils.getWhetherStartVideoTranscribe()
                && savePictureOrVideoPath != null){
            CameraVideoUtils.startVideoTranscribe(savePictureOrVideoPath, videoMaxTime, new VideoTranscribeStatueCallBack(videoMaxTime) {
                @Override
                public void start() {
                    whetherVideotaping = true;
                    if(videoTranscribeStatueCallBackList != null){
                        for (VideoTranscribeStatueCallBack videoTranscribeStatueCallBack : videoTranscribeStatueCallBackList) {
                            if(videoTranscribeStatueCallBack != null) {
                                videoTranscribeStatueCallBack.start();
                            }
                        }
                    }
                }

                @Override
                public void stop() {
                    stopVideo();
                    whetherVideotaping = false;
                    if(videoTranscribeStatueCallBackList != null){
                        for (VideoTranscribeStatueCallBack videoTranscribeStatueCallBack : videoTranscribeStatueCallBackList) {
                            if(videoTranscribeStatueCallBack != null) {
                                videoTranscribeStatueCallBack.stop();
                            }
                        }
                    }
                }

                @Override
                public void onProgress(Double progress) {
                    if(videoTranscribeStatueCallBackList != null){
                        for (VideoTranscribeStatueCallBack videoTranscribeStatueCallBack : videoTranscribeStatueCallBackList) {
                            if(videoTranscribeStatueCallBack != null) {
                                videoTranscribeStatueCallBack.onProgress(progress);
                            }
                        }
                    }
                }
            });
        }
    }

    private void stopVideo(){
        if(whetherVideotaping
                && CameraVideoUtils.getWhetherStartVideoTranscribe()){
            CameraVideoUtils.closeVideoTranscribe();
            whetherVideotaping = false;
            if(videoTranscribeStatueCallBackList != null){
                for (VideoTranscribeStatueCallBack videoTranscribeStatueCallBack : videoTranscribeStatueCallBackList) {
                    if(videoTranscribeStatueCallBack != null) {
                        videoTranscribeStatueCallBack.stop();
                    }
                }
            }
        }
    }


}
