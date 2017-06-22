package com.libpictureoptions.android.pictureCamera.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.libpictureoptions.android.common.utils.LogUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by wangliang on 0022/2017/3/22.
 * 创建时间： 0022/2017/3/22 16:30
 * 创建人：王亮（Loren wang）
 * 功能作用：camera摄像头操作工具类
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class CameraOptionsUtils {
    private static Context context;
    private static CameraOptionsUtils cameraOptionsUtils;
    private static SurfaceView surfaceView;
    private final static String TAG = CameraOptionsUtils.class.getName();
    private static Camera camera;//单例摄像头
//    private static ArrayList<Camera.Size> allPreviewSize;


    /**
     * @param ctx
     * @param view
     * @return
     */
    public static CameraOptionsUtils getInstance(Context ctx, SurfaceView view){
        //先判定传入的参数是否为空在进行赋值操作
        if(ctx != null){
            context = ctx;
        }
        //上一个判定完成之后如果单例中的context还是为空的话那就要返回空
        if(context == null){
            LogUtils.logE(TAG,"context can not null");
            return null;
        }
        //如果传入的surfaceview为空的话那么也要返回空
        if(view == null){
            LogUtils.logE(TAG,"SurfaceView can not null");
            return null;
        }
        else if(surfaceView == null){
            surfaceView = view;
        }
        //传入的surfaceView与记录的不相同，那么则重新初始化
        else if(surfaceView != null && surfaceView.equals(view)){
            surfaceView = view;
            init();
        }
        if(cameraOptionsUtils == null){
            cameraOptionsUtils = new CameraOptionsUtils();
        }

        return cameraOptionsUtils;
    }

    private CameraOptionsUtils(){
        init();
//        allPreviewSize = new ArrayList<>();
//        Camera.Size size;
//        size = camera.new Size(1280,960);
//        allPreviewSize.add(size);
//        size = camera.new Size(960,1280);
//        allPreviewSize.add(size);
//        size = camera.new Size(1600,1200);
//        allPreviewSize.add(size);
//        size = camera.new Size(1200,1600);
//        allPreviewSize.add(size);
//        size = camera.new Size(2048,1536);
//        allPreviewSize.add(size);
//        size = camera.new Size(1536,2048);
//        allPreviewSize.add(size);
//        size = camera.new Size(2592,1456);
//        allPreviewSize.add(size);
//        size = camera.new Size(1456,2592);
//        allPreviewSize.add(size);
//        size = camera.new Size(2593,1936);
//        allPreviewSize.add(size);
//        size = camera.new Size(1936,2593);
//        allPreviewSize.add(size);
//        size = camera.new Size(176,144);
//        allPreviewSize.add(size);
//        size = camera.new Size(144,176);
//        allPreviewSize.add(size);
//        size = camera.new Size(320,240);
//        allPreviewSize.add(size);
//        size = camera.new Size(240,320);
//        allPreviewSize.add(size);
//        size = camera.new Size(352,288);
//        allPreviewSize.add(size);
//        size = camera.new Size(288,352);
//        allPreviewSize.add(size);
//        size = camera.new Size(640,480);
//        allPreviewSize.add(size);
//        size = camera.new Size(480,640);
//        allPreviewSize.add(size);
//        size = camera.new Size(720,480);
//        allPreviewSize.add(size);
//        size = camera.new Size(480,720);
//        allPreviewSize.add(size);
//        size = camera.new Size(720,576);
//        allPreviewSize.add(size);
//        size = camera.new Size(576,720);
//        allPreviewSize.add(size);
//        size = camera.new Size(848,480);
//        allPreviewSize.add(size);
//        size = camera.new Size(480,848);
//        allPreviewSize.add(size);
//        size = camera.new Size(1280,720);
//        allPreviewSize.add(size);
//        size = camera.new Size(720,1280);
//        allPreviewSize.add(size);
    }

    /**
     * 初始化配置信息
     */
    private static void init(){
        if(surfaceView != null) {
            SurfaceHolder holder = surfaceView.getHolder();
            holder.addCallback(surfaceViewCallback);//设置默认的surfaceview的回调
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            //设置视图在可见状态下是否保持唤醒
            holder.setKeepScreenOn(true);
        }
    }


    /**
     * 开启默认的摄像头
     * @return
     */
    public void openCamera() {
        openCamera(-1);
    }

    /**
     * 开启指定的cameraId的摄像头
     * @param cameraId
     * @return
     */
    public void openCamera(int cameraId){
        //如果之前的摄像头存在的话那么则先将上一个摄像头停止并释放才可以开启新的摄像头
        if(camera != null){
            closeCamera();
        }

        int numberOfCameras = Camera.getNumberOfCameras();
        if(numberOfCameras == 0){
            Log.w(TAG, "No cameras!");
            return ;
        }
        //可以打开camera的cameraid是大于0的，如果传入的值小于0那么就选用默认的camera
        if(cameraId < 0){
            // Select a camera if no explicit camera requested
            int index = 0;
            while (index < numberOfCameras) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(index, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    break;
                }
                index++;
            }
            cameraId = index;
        }
        if (cameraId < numberOfCameras) {
            Log.i(TAG, "Opening camera #" + cameraId);
            camera = Camera.open(cameraId);
        } else {
            if (cameraId >= 0) {
                Log.w(TAG, "Requested camera does not exist: " + cameraId);
                camera = null;
            } else {
                Log.i(TAG, "No camera facing back; returning camera #0");
                camera = Camera.open(0);
            }
        }

    }

    public static Camera getCamera() {
        return camera;
    }

    public void resetCamera(){
        initCamera();
    }

    /**
     * 关闭停止并释放摄像头
     */
    public void closeCamera(){
        if(camera != null){
            //停止预览
            camera.stopPreview();
            //释放camera
            camera.release();
            //camera之空
            camera = null;
        }
    }

    /**
     * 触摸对焦
     */
    public static void touchFocus(){
        if(camera != null){
            camera.autoFocus(autoFocusCallback);
        }
    }



    /**
     * 开启拍照回调，回调顺序 originalPictureDataCallback--》shutterCallback--》zoomPictureDataCallback--》jpegPictureDataCallback
     * @param shutterCallback    //快门按下回调，
     * @param originalPictureDataCallback 原始图像数据
     * @param zoomPictureDataCallback 缩放和压缩图像数据
     * @param jpegPictureDataCallback jpeg图像数据
     */
    public void takePicture(Camera.ShutterCallback shutterCallback,
                            Camera.PictureCallback originalPictureDataCallback,
                            Camera.PictureCallback zoomPictureDataCallback,
                            Camera.PictureCallback jpegPictureDataCallback){
        if(camera != null){
            //拍照，如果传空的则使用默认值
            camera.takePicture(shutterCallback == null ? this.shutterCallback : shutterCallback,
                    originalPictureDataCallback == null ? this.originalPictureDataCallback : originalPictureDataCallback,
                    zoomPictureDataCallback == null ? this.zoomPictureDataCallback : zoomPictureDataCallback,
                    jpegPictureDataCallback == null ? this.jpegPictureDataCallback : jpegPictureDataCallback);
        }

    }


    /**
     * 初始化相机参数
     */
    private static void initCamera(){
        if(camera == null){
            return;
        }
        try {
//            //不设置camera的params，默认的params参数就够
//            Camera.Parameters parameters = camera.getParameters();
//            camera.setParameters(parameters);

            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(1080,1080);
            camera.setParameters(parameters);
            camera.startPreview();

            //进行横竖屏判断然后对图像进行校正
            //如果是竖屏
            if(context.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                camera.setDisplayOrientation(90);
            }else {//如果是横屏
                camera.setDisplayOrientation(0);
            }
            //开启预览
            camera.startPreview();
            // 2如果要实现连续的自动对焦，这一句必须加
            camera.cancelAutoFocus();

        } catch (Exception e) {
            e.printStackTrace();
            camera.release();
            camera = null;
        }
    }

    /**
     * 设置surfaceview参数回调
     */
    private static SurfaceHolder.Callback surfaceViewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                //设置显示参数
                camera.setPreviewDisplay(holder);
                initCamera();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            //设置自动对焦
            //可以自动对焦
            if(camera.getParameters().getFocusMode().equals(Camera.Parameters.FOCUS_MODE_AUTO)){
                camera.autoFocus(autoFocusCallback);
            }

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    /**
     * 通过对比得到与宽高比最接近的尺寸（如果有相同尺寸，优先选择）
     *
     * @param surfaceWidth
     *            需要被进行对比的原宽
     * @param surfaceHeight
     *            需要被进行对比的原高
     * @param preSizeList
     *            需要对比的预览尺寸列表
     * @return 得到与原宽高比例最接近的尺寸
     */
    protected static Point findBestPreviewSizeValue(List<Camera.Size> sizeList,Point sceenResolution){
        int bestX = 0;
        int bestY = 0;
        int size = 0;
        for (Camera.Size nowSize : sizeList){
//            //如果有符合条件的直接返回
//            if(nowSize.width ==  )
            int newX = nowSize.width;
            int newY = nowSize.height;
            int newSize = Math.abs(newX * newX) + Math.abs(newY * newY);
            float ratio = (float) (newY * 1.0 / newX);
            if(newSize >= size && ratio != 0.75){//确保图片是16:9
                bestX  = newX;
                bestY = newY;
                size = newSize;
            }else if(newSize < size){
                continue;
            }
        }
        if(bestX > 0 && bestY > 0){
            return new Point(bestX,bestY);
        }
        return null;

    }


    //自动对焦回调
    private static Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        /**
         *
         * @param success success为true表示对焦成功，改变对焦状态图像
         * @param camera
         */
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if(success && camera != null){
                initCamera();
                camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
            }
        }
    };

    //快门按下回调，
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            LogUtils.logI(TAG,"Shutter Click");
        }
    };
    //原始图像数据
    private Camera.PictureCallback originalPictureDataCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.logI(TAG,"get originalPictureData");
        }
    };
    //缩放和压缩图像数据
    private Camera.PictureCallback zoomPictureDataCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.logI(TAG,"get zoomPictureData");
        }
    };
    //jpeg图像数据
    private Camera.PictureCallback jpegPictureDataCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LogUtils.logI(TAG,"get jpegPictureData");
        }
    };

}
