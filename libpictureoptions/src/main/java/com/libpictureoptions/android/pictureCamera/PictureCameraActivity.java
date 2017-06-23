package com.libpictureoptions.android.pictureCamera;

import android.Manifest;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.BaseActivity;
import com.libpictureoptions.android.common.utils.LogUtils;
import com.libpictureoptions.android.common.utils.imageOptions.ImageUtils;
import com.libpictureoptions.android.pictureCamera.utils.CameraOptionsUtils;
import com.libpictureoptions.android.pictureCamera.utils.CameraVideoUtils;
import com.libpictureoptions.android.pictureCamera.view.WxTakePictureOrVideoView;

import java.io.File;


public class PictureCameraActivity extends BaseActivity {

    private SurfaceView mpreview;
    private RelativeLayout relCameraOptions;//相机操作界面
    private Button btnOpenLite;//开启闪光灯
    private Button btnTakePicture;//拍照
    private Button btnVideo;//录像
    private RelativeLayout relTakePicturePreviewOptions;//拍照预览父界面
    private ImageView imgTakePicturePreview;//拍照预览界面图片预览
    private Button btnTakePicturePreviewCancel;//取消拍照预览
    private Button btnTakePicturePreviewConfirm;//确认拍照预览
    private RelativeLayout relVideoPreviewOptions;//摄像预览父界面
    private VideoView videoView;//视频预览控件
    private Button btnVideoPreviewCancel;//取消预览视频
    private Button btnVideoPreviewConfirm;//确认预览视频

    private boolean whetherFlashlightOpen = false;//当前是否开启了闪光灯
    private PictureCameraConfig config;//配置文件
    private Bitmap nowTakePictureAfterBitmap;//拍照之后产生的图片位图
    

    @Override
    public void initChildView() {
        addChildView(R.layout.activity_picture_camera);

        mpreview = (SurfaceView) findViewById(R.id.surfaceView);
        relCameraOptions = (RelativeLayout) findViewById(R.id.relCameraOptions);//相机操作界面
        btnOpenLite = (Button) findViewById(R.id.btnOpenLite);//开启闪光灯
        btnTakePicture = (Button) findViewById(R.id.btnTakePicture);//拍照
        btnVideo = (Button) findViewById(R.id.btnVideo);//录像
        relTakePicturePreviewOptions = (RelativeLayout) findViewById(R.id.relTakePicturePreviewOptions);//拍照预览父界面
        imgTakePicturePreview = (ImageView) findViewById(R.id.imgTakePicturePreview);//拍照预览界面图片预览
        btnTakePicturePreviewCancel = (Button) findViewById(R.id.btnTakePicturePreviewCancel);//取消拍照预览
        btnTakePicturePreviewConfirm = (Button) findViewById(R.id.btnTakePicturePreviewConfirm);//确认拍照预览
        relVideoPreviewOptions = (RelativeLayout) findViewById(R.id.relVideoPreviewOptions);//摄像预览父界面
        videoView = (VideoView) findViewById(R.id.videoView);//视频预览控件
        btnVideoPreviewCancel = (Button) findViewById(R.id.btnVideoPreviewCancel);//取消预览视频
        btnVideoPreviewConfirm = (Button) findViewById(R.id.btnVideoPreviewConfirm);//确认预览视频




    }

    @Override
    public void initChildListener() {
        btnOpenLite.setOnClickListener(openLightClickListener);//开启闪光灯
        btnTakePicture.setOnClickListener(takePictureClickListener);//拍照
        btnVideo.setOnClickListener(videoClickListener);//录像
        btnTakePicturePreviewCancel.setOnClickListener(cancelTakePicturePreviewClickListener);//取消拍照预览
        btnTakePicturePreviewConfirm.setOnClickListener(confirmTakePicturePreviewClickListener);//确认拍照预览
        btnVideoPreviewCancel.setOnClickListener(cancelVideoPreviewClickListener);//取消预览视频
        btnVideoPreviewConfirm.setOnClickListener(confirmVideoPreviewClickListener);//确认预览视频

        /**
         * 手动对焦
         */
        mpreview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        CameraOptionsUtils.touchFocus();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void setClickEnabledStates(boolean states) {

    }

    @Override
    public void initChildData(Bundle savedInstanceState) {
        config = OpenPictureCameraUtils.getConfig();
        if(config != null){
            //判断是否有拍照时的操作视图
            if(config.getCameraOptionsView() != null){
                relCameraOptions.removeAllViews();
                relCameraOptions.addView(config.getCameraOptionsView());
            }
            //判断是否有拍照预览时的上层操作视图
            if(config.getTakePicturePreviewView() != null){
                relTakePicturePreviewOptions.removeView(btnTakePicturePreviewCancel);
                relTakePicturePreviewOptions.removeView(btnTakePicturePreviewConfirm);
                relTakePicturePreviewOptions.addView(config.getTakePicturePreviewView());
            }
            //判断是否替换拍照预览时图片显示控件
            if(config.getImgPreview() != null){
                relTakePicturePreviewOptions.removeView(imgTakePicturePreview);
                relTakePicturePreviewOptions.addView(config.getImgPreview(),0);
            }
            //判断是否替换录像是录像预览的上层操作视图
            if(config.getVideoPreviewView() != null){
                relVideoPreviewOptions.removeView(btnVideoPreviewCancel);
                relVideoPreviewOptions.removeView(btnVideoPreviewConfirm);
                relVideoPreviewOptions.addView(config.getVideoPreviewView());
            }
            
            //拍照按钮点击事件
            if(config.getTakePictureClickListener() != null){
                config.getTakePictureClickListener().setOnClickListener(takePictureClickListener);
            }
            //开启闪光灯点击事件
            if(config.getOpenLightClickListener() != null){
                config.getOpenLightClickListener().setOnClickListener(openLightClickListener);
            }
            //录像按钮点击事件
            if(config.getVideoClickListener() != null){
                config.getVideoClickListener().setOnClickListener(videoClickListener);
            }
            //确认拍照预览图片
            if(config.getConfirmTakePicturePreviewClickListener() != null){
                config.getConfirmTakePicturePreviewClickListener().setOnClickListener(confirmTakePicturePreviewClickListener);
            }
            //取消拍照预览图片
            if(config.getCancelTakePicturePreviewClickListener() != null){
                config.getCancelTakePicturePreviewClickListener().setOnClickListener(cancelTakePicturePreviewClickListener);
            }
            //确认录像预览图片
            if(config.getConfirmVideoPreviewClickListener() != null){
                config.getConfirmVideoPreviewClickListener().setOnClickListener(confirmVideoPreviewClickListener);
            }
            //取消录像预览图片
            if(config.getCancelVideoPreviewClickListener() != null){
                config.getCancelVideoPreviewClickListener().setOnClickListener(cancelVideoPreviewClickListener);
            }
        }
        ((WxTakePictureOrVideoView)findViewById(R.id.wxView)).setSavePictureOrVideoPath(config.getPictureOrVideoSavePath());
        ((WxTakePictureOrVideoView)findViewById(R.id.wxView)).setTakePictureCallbackAndSufaceView(
                mpreview,
                null,null,null,jpegPictureDataCallback);

        //获取相机权限并开启摄像头
        perissionRequest(new String[]{Manifest.permission.CAMERA});
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mpreview != null){//界面重新获得焦点的时候需要需要重置相机，否则会导致相机卡住
            CameraOptionsUtils.getInstance(getContext(),mpreview).resetCamera();
        }
    }

    @Override
    public void perissionRequestSuccessCallback(String[] perissions) {
        super.perissionRequestSuccessCallback(perissions);
        if (perissions[0].equals(Manifest.permission.CAMERA)) {
            //开启摄像头
            CameraOptionsUtils.getInstance(getContext(),mpreview).openCamera();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    while (true){
                        try {
                            CameraOptionsUtils.touchFocus();
                            Thread.sleep(5000);//5秒对焦一次，除了自动对焦之外
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },10000);
        }
    }

    //jpeg图像数据
    private Camera.PictureCallback jpegPictureDataCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.logI(TAG,"get jpegPictureData");
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            //如果是竖屏
            if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                bitmap = ImageUtils.getInstance(getContext()).toturnPicture(bitmap,90);
            }
            //如果有裁剪的话那么先进行裁剪
            if(config != null) {
                bitmap = ImageUtils.getInstance(getContext()).cropBitmap(bitmap,
                        config.getTakePictureAfterCropLocationX(),
                        config.getTakePictureAfterCropLocationY(),
                        config.getTakePictureAfterCropWidth(),
                        config.getTakePictureAfterCropHeight());
            }
            nowTakePictureAfterBitmap = Bitmap.createBitmap(bitmap);
            imgTakePicturePreview.setImageBitmap(bitmap);
            setClickEnabledStates(true);
            relTakePicturePreviewOptions.setVisibility(View.VISIBLE);
        }
    };
    //开启闪关灯点击事件
    private View.OnClickListener openLightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config != null && config.getOpenLightClickListener() != null){
                config.getOpenLightClickListener().onClickListener(v);
            }
            
            if(CameraOptionsUtils.getCamera() == null){
                return;
            }
            if (!whetherFlashlightOpen) {
                whetherFlashlightOpen = true;

                try{
                    Camera.Parameters mParameters;
                    mParameters = CameraOptionsUtils.getCamera().getParameters();
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    CameraOptionsUtils.getCamera().setParameters(mParameters);
                } catch(Exception ex){
                }

            } else {
                whetherFlashlightOpen = false;
                try{
                    Camera.Parameters mParameters;
                    mParameters = CameraOptionsUtils.getCamera().getParameters();
                    mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    CameraOptionsUtils.getCamera().setParameters(mParameters);
                } catch(Exception ex){
                }
            }
        }
    };
    //拍照点击事件
    private View.OnClickListener takePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //回传位图
            if(config != null && config.getTakePictureClickListener() != null){
                config.getTakePictureClickListener().onClickListener(v);
            }
            CameraOptionsUtils.getInstance(getContext(),mpreview).takePicture(null,null,null,jpegPictureDataCallback);
            showTakePicturePreview();
        }
    };
    //取消拍照预览点击事件
    private View.OnClickListener cancelTakePicturePreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //回传位图
            if(config != null && config.getCancelTakePicturePreviewClickListener() != null){
                config.getCancelTakePicturePreviewClickListener().onClickListener(v);
            }
            hindTakePicturePreview();//释放所有的预览资源
        }
    };
    //确认拍照预览点击事件
    private View.OnClickListener confirmTakePicturePreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //回传位图
            if(config != null && config.getConfirmTakePicturePreviewClickListener() != null && nowTakePictureAfterBitmap != null){
                config.getConfirmTakePicturePreviewClickListener().onClickListener(v);
            }
            hindTakePicturePreview();//释放所有的预览资源
            setResult(RESULT_OK);
            onBackPressed();
            finish();
        }
    };

    //录像点击事件
    private View.OnClickListener videoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config == null || config.getPictureOrVideoSavePath() == null
                    || "".equals(config.getPictureOrVideoSavePath())){
                LogUtils.logD(TAG,"未接受到配置文件或者所要存储的文件地址为空");
                return;
            }
            if(config.getVideoClickListener() != null){
                config.getVideoClickListener().onClickListener(v);
            }
            if(CameraVideoUtils.getWhetherStartVideoTranscribe()){
                CameraVideoUtils.closeVideoTranscribe();
                showVideoPreview();
            }else {
                CameraVideoUtils.startVideoTranscribe(config.getPictureOrVideoSavePath(), null,null);
            }
        }
    };
    //取消录像预览点击事件
    private View.OnClickListener cancelVideoPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config != null && config.getCancelVideoPreviewClickListener() != null){
                config.getCancelVideoPreviewClickListener().onClickListener(v);
            }
            if(config != null && config.getPictureOrVideoSavePath() != null){
                File file = new File(config.getPictureOrVideoSavePath());
                if(file.exists()){
                    file.delete();
                }
            }
            hindVideoPreview();
        }
    };
    //确认录像预览点击事件
    private View.OnClickListener confirmVideoPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config != null && config.getConfirmVideoPreviewClickListener() != null){
                config.getConfirmVideoPreviewClickListener().onClickListener(v);
            }
            hindVideoPreview();
            setResult(RESULT_OK);
            onBackPressed();
            finish();
        }
    };


    /**
     * 显示拍照预览界面
     */
    private void showTakePicturePreview(){
        relCameraOptions.setVisibility(View.GONE);
        relVideoPreviewOptions.setVisibility(View.GONE);
        relTakePicturePreviewOptions.setVisibility(View.VISIBLE);
        ImageUtils.getInstance(getContext()).releaseImageViewResouce(imgTakePicturePreview);//释放控件图片资源
        ImageUtils.getInstance(getContext()).releaseBitmap(nowTakePictureAfterBitmap);//释放暂存图片资源
    }

    /**
     * 隐藏拍照预览界面
     */
    private void hindTakePicturePreview(){
        relCameraOptions.setVisibility(View.VISIBLE);
        relVideoPreviewOptions.setVisibility(View.GONE);
        relTakePicturePreviewOptions.setVisibility(View.GONE);
        ImageUtils.getInstance(getContext()).releaseImageViewResouce(imgTakePicturePreview);//释放控件图片资源
        ImageUtils.getInstance(getContext()).releaseBitmap(nowTakePictureAfterBitmap);//释放暂存图片资源
        //只能在隐藏的时候重置，如果在显示的时候重置容易导致部分机型无法拍照，或者可以再拍照之后在重置
        CameraOptionsUtils.getInstance(getContext(),mpreview).resetCamera();
    }

    /**
     * 显示视频拍摄预览界面
     */
    private void showVideoPreview(){
        if(config != null && config.getPictureOrVideoSavePath() != null && !"".equals(config.getPictureOrVideoSavePath())) {
            CameraVideoUtils.playVideoPlay(getContext(), config.getPictureOrVideoSavePath(), videoView, null);
            mpreview.setVisibility(View.GONE);
            relCameraOptions.setVisibility(View.GONE);
            relTakePicturePreviewOptions.setVisibility(View.GONE);
            relVideoPreviewOptions.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏视频拍摄界面
     */
    private void hindVideoPreview(){
        mpreview.setVisibility(View.VISIBLE);
        relCameraOptions.setVisibility(View.VISIBLE);
        relTakePicturePreviewOptions.setVisibility(View.GONE);
        relVideoPreviewOptions.setVisibility(View.GONE);
        CameraVideoUtils.closeVideoPlay(videoView);
    }

}
