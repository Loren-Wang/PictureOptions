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

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.BaseActivity;
import com.libpictureoptions.android.common.utils.LogUtils;
import com.libpictureoptions.android.common.utils.imageOptions.ImageUtils;
import com.libpictureoptions.android.pictureCamera.utils.CameraOptionsUtils;


public class PictureCameraActivity extends BaseActivity {

    private SurfaceView mpreview;
    private Button btnOpenLite;//开启闪光灯
    private Button btnTakePicture;//拍照
    private Button btnPreviewCancel;//取消预览
    private Button btnPreviewConfirm;//确认预览
    private ImageView imgPreview;//预览界面图片预览
    private RelativeLayout relCameraOptions;//拍照时的操作界面
    private RelativeLayout relPreviewOptions;//预览父界面

    private boolean whetherFlashlightOpen = false;//当前是否开启了闪光灯
    private CameraTakePictureConfig config;//配置文件
    private Bitmap nowTakePictureAfterBitmap;

    @Override
    public void initChildView() {
        addChildView(R.layout.activity_picture_camera);

        mpreview = (SurfaceView) this.findViewById(R.id.surfaceView);
        btnOpenLite = (Button) this.findViewById(R.id.btnOpenLite);
        btnTakePicture = (Button) this.findViewById(R.id.btnTakePicture);//拍照
        btnPreviewCancel = (Button) this.findViewById(R.id.btnPreviewCancel);//取消预览
        btnPreviewConfirm = (Button) this.findViewById(R.id.btnPreviewConfirm);//确认预览
        imgPreview = (ImageView) this.findViewById(R.id.imgPreview);//预览界面
        relCameraOptions = (RelativeLayout) this.findViewById(R.id.relCameraOptions);//拍照时的操作界面
        relPreviewOptions = (RelativeLayout) this.findViewById(R.id.relPreviewOptions);//预览父界面




    }

    @Override
    public void initChildListener() {
        btnOpenLite.setOnClickListener(openLightClickListener);
        btnTakePicture.setOnClickListener(takePictureClickListener);
        btnPreviewCancel.setOnClickListener(cancelPreviewClickListener);
        btnPreviewConfirm.setOnClickListener(confirmPreviewClickListener);

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
            //判断是否有预览界面视图
            if(config.getPreviewView() != null){
                relPreviewOptions.removeAllViews();
                relPreviewOptions.addView(config.getPreviewView());
            }
            //拍照按钮点击事件
            if(config.getTakePictureClickListener() != null){
                config.getTakePictureClickListener().setOnClickListener(takePictureClickListener);
            }
            //开启闪光灯点击事件
            if(config.getOpenLightClickListener() != null){
                config.getOpenLightClickListener().setOnClickListener(openLightClickListener);
            }
            //确认预览图片
            if(config.getConfirmPreviewClickListener() != null){
                config.getConfirmPreviewClickListener().setOnClickListener(confirmPreviewClickListener);
            }
            //取消预览图片
            if(config.getCancelPreviewClickListener() != null){
                config.getCancelPreviewClickListener().setOnClickListener(cancelPreviewClickListener);
            }
        }
        //开启摄像
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

    @Override
    public void perissionRequestSuccessCallback(String[] perissions) {
        super.perissionRequestSuccessCallback(perissions);
        if (perissions[0].equals(Manifest.permission.CAMERA)) {
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
            imgPreview.setImageBitmap(bitmap);
            setClickEnabledStates(true);
            relPreviewOptions.setVisibility(View.VISIBLE);
        }
    };
    //开启闪关灯点击事件
    private View.OnClickListener openLightClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            perissionRequest(new String[]{Manifest.permission.CAMERA});
        }
    };
    //拍照点击事件
    private View.OnClickListener takePictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setClickEnabledStates(false);
            CameraOptionsUtils.getInstance(getContext(),mpreview).takePicture(null,null,null,jpegPictureDataCallback);
            imgPreview.setImageResource(R.mipmap.ic_launcher);
            relasePreview();//释放所有的预览资源
        }
    };
    //取消预览点击事件
    private View.OnClickListener cancelPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CameraOptionsUtils.getInstance(getContext(),mpreview).resetCamera();
            relPreviewOptions.setVisibility(View.GONE);
            relasePreview();//释放所有的预览资源
        }
    };
    //确认预览点击事件
    private View.OnClickListener confirmPreviewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //回传位图
            if(config != null && nowTakePictureAfterBitmap != null){
                config.getConfirmPreviewClickListener().onClick(Bitmap.createBitmap(nowTakePictureAfterBitmap));//使用create用来防止在释放之后对于回传的产生影响
            }
            relasePreview();//释放所有的预览资源
            onBackPressed();
        }
    };

    /**
     * 释放预览视图所有的相关
     */
    private void relasePreview(){
        ImageUtils.getInstance(getContext()).releaseImageViewResouce(imgPreview);//释放控件图片资源
        ImageUtils.getInstance(getContext()).releaseBitmap(nowTakePictureAfterBitmap);//释放暂存图片资源
    }


}
