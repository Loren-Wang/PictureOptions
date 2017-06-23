package com.libpictureoptions.android.pictureCamera.interface_and_abstract;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by wangliang on 0023/2017/6/23.
 * 创建时间： 0023/2017/6/23 11:31
 * 创建人：王亮（Loren wang）
 * 功能作用：当前视频录制状态回调
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class VideoTranscribeStatueCallBack {
    private Integer maxTime;
    private HandlerThread handlerThread = new HandlerThread(getClass().getName());
    private Handler handler;

    public VideoTranscribeStatueCallBack(Integer maxTime) {
        this.maxTime = maxTime;
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }
    public void startVideoTranscribeAnd(){
        start();
        if(maxTime != null && maxTime != 0) {
            Runnable runnable = new Runnable() {
                int recordedTime = 0;
                double progress;
                @Override
                public void run() {
                    progress = recordedTime * 1.0 / maxTime;
                    if(progress <= 1) {
                        onProgress(recordedTime * 1.0 / maxTime);
                        recordedTime += 1000;
                        handler.postDelayed(this,1000);
                    }
                }
            };
            handler.post(runnable);
        }
    }
    public void stopVideoTranscribeAnd(){
        stop();
        //退出looper
        if(android.os.Build.VERSION.SDK.compareTo("18") < 0) {
            handlerThread.quit();
        }else {
            handlerThread.quitSafely();
        }
    }

    public abstract void start();//开始录制
    public abstract void stop();//停止录制
    public abstract void onProgress(Double progress);//录制进度
}
