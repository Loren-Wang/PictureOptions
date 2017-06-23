package com.libpictureoptions.android.pictureCamera.utils;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.MediaController;
import android.widget.VideoView;

import com.libpictureoptions.android.common.utils.LogUtils;
import com.libpictureoptions.android.pictureCamera.interface_and_abstract.VideoTranscribeStatueCallBack;

import java.io.File;
import java.io.IOException;

/**
 * Created by wangliang on 0021/2017/6/21.
 * 创建时间： 0021/2017/6/21 18:04
 * 创建人：王亮（Loren wang）
 * 功能作用：视频录制单例
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class CameraVideoUtils {
    private static String TAG = "CameraVideoUtils";
    private static MediaController mediaController;//播放录像使用的
    private static boolean whetherStartVideoPlay = false;//是否已经开始播放录像
    private static MediaRecorder mRecorder;//录像使用的
    private static boolean whetherStartVideoTranscribe = false;//是否已经开始录像
    private static VideoTranscribeStatueCallBack videoTranscribeStatueCallBack;//录像状态回调，每次回调（停止方法）完成之后需要清除掉回调对象

    /**
     * 获取是否已经开始录像
     * @return
     */
    public static boolean getWhetherStartVideoTranscribe() {
        return whetherStartVideoTranscribe;
    }
    public static boolean getWhetherStartVideoPlay() {
        return whetherStartVideoPlay;
    }

    /**
     * 开启视频录制
     * @param savePath
     * @param maxDuration 最大录制时间
     */
    public synchronized static void startVideoTranscribe(String savePath
            ,Integer maxDuration,VideoTranscribeStatueCallBack callBack){
        if(savePath != null && !"".equals(savePath) && !whetherStartVideoTranscribe){
            File file = new File(savePath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            Camera camera = CameraOptionsUtils.getCamera();
            SurfaceHolder mSurfaceHolder = CameraOptionsUtils.getSurfaceHolder();
            if (camera != null && mSurfaceHolder != null) {
                videoTranscribeStatueCallBack = callBack;
                mRecorder = new MediaRecorder();
                camera.unlock();
                mRecorder.setCamera(camera);
                mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
                mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//输出编码格式
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);//输入源
                mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
                if(maxDuration != null && maxDuration != 0) {
                    mRecorder.setMaxDuration(maxDuration);
                }
                mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
                    @Override
                    public void onInfo(MediaRecorder mr, int what, int extra) {
                        //录制完成则停止录制
                        if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                            closeVideoTranscribe();
                        }
                    }
                });
                mRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
                mRecorder.setOutputFile(savePath);//输出目标文件
                //setOutputFormat、setAudioEncoder、setVideoEncoder 与 setProfile 不能同时使用，否则会报错
//                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
//                mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
//                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//输出格式
                //以下两个参数还是不要设置了，录像和拍照的界面设置全屏，否则会出现空白部分
//                mRecorder.setVideoSize(1920,1080);//设置最佳分辨率
//                mRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);//帧频率
                Log.d(TAG, "bf mRecorder.prepare()" );
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d(TAG, "af mRecorder.prepare()" );
                    Log.d(TAG, "bf mRecorder.start()" );
                    mRecorder.start();   // Recording is now started
                    Log.d(TAG, "af mRecorder.start()" );
                    whetherStartVideoTranscribe = true;
                    //回调信息
                    if(videoTranscribeStatueCallBack != null){
                        videoTranscribeStatueCallBack.startVideoTranscribeAnd();
                    }
                }catch (Exception e) {
                    LogUtils.logD(TAG,"");
                }
            }
        }
    }

    /**
     * 关闭视频录制
     */
    public synchronized static  void closeVideoTranscribe(){
        // stop
        if (whetherStartVideoTranscribe && mRecorder != null) {
            try {
                mRecorder.stop();
                mRecorder.reset();
                mRecorder = null;
                whetherStartVideoTranscribe = false;
                if(videoTranscribeStatueCallBack != null){
                    videoTranscribeStatueCallBack.stopVideoTranscribeAnd();
                    videoTranscribeStatueCallBack = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 开始播放视频
     * @param context
     * @param playPath
     * @param videoView
     * @param onCompletionListener
     */
    public static synchronized void playVideoPlay(Context context, String playPath, VideoView videoView
            , final MediaPlayer.OnCompletionListener onCompletionListener){
        if(playPath != null
                && videoView != null
                && !"".equals(playPath)
                && !whetherStartVideoPlay){
            File file = new File(playPath);
            if(file.isFile() && file.exists()){
                mediaController = new MediaController(context);
                videoView.setVideoPath(playPath);
                videoView.setMediaController(mediaController);
                videoView.requestFocus();
                try {
                    videoView.start();      // 播放视频
                    whetherStartVideoPlay = true;
                }catch(Exception e) {
                    e.printStackTrace();
                }
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        LogUtils.logD(TAG,"播放完成");
                        mediaController = null;
                        whetherStartVideoPlay = false;
                        if(onCompletionListener != null) {
                            onCompletionListener.onCompletion(mp);
                        }
                    }
                });
            }
        }
    }

    /**
     * 停止播放视频
     * @param videoView
     */
    public static synchronized void closeVideoPlay(VideoView videoView){
        if(videoView != null && whetherStartVideoPlay){
            //停止播放
            videoView.stopPlayback();
            //清除上一次播放地址
            videoView.setVideoPath(null);
            mediaController = null;
            whetherStartVideoPlay = false;
        }
    }


}
