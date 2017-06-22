package com.wangliang_work163.www.pictureoptions;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.libpictureoptions.android.pictureCamera.OpenPictureCameraUtils;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        多选图片
//        PictureSelectConfig pictureSelectConfig = new PictureSelectConfig.Bulid()
//                .setPictureSelectListMaxSelectedNum(5)
//                .setPictureSelectItemStateIconIdSelectedY(R.mipmap.ic_launcher)
//                .bulid(this);
//        OpenPictureSelectUtils.open(this,0,pictureSelectConfig);

//        //裁剪图片
//        PictureCropConfig pictureCropConfig = new PictureCropConfig.Bulid()
//                .setCropPictureType(PictureCropConfig.Bulid.CROP_PICTURE_TYPE_NET)
//                .setPicturePath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1497864297259&di=2d72d09910a356e047d77e970c85c8a1&imgtype=0&src=http%3A%2F%2Ff.hiphotos.baidu.com%2Flvpics%2Fh%3D800%2Fsign%3Dbe260c4d7ff0f736c7fe41013a55b382%2F10dfa9ec8a1363277792f801968fa0ec08fac780.jpg")
//                .bulid(this);
//        OpenPictureCropUtils.open(this,0,pictureCropConfig);


//        //压缩图片
//        OpenPictureCompressUtils.compressFile();
//        OpenPictureCompressUtils.compressBitmap();
//        OpenPictureCompressUtils.compressFile();

//        //调用系统相机，需要先申请权限
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                if (grantResults.length > 0 && grantResults.length == permissions.length) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                        OpenPictureCameraUtils.openSystemCamera(this, Environment.getExternalStorageDirectory().getPath() + "/PictureSelects/1234.jpg",1);
                    }
                }
            }
        }
    }
}
