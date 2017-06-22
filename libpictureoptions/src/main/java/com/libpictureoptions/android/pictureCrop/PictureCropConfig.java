package com.libpictureoptions.android.pictureCrop;

import android.content.Context;

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

public class PictureCropConfig implements Serializable{

    private Context context;
    private Integer cropPictureType;//所要截图的图片来源
    private String picturePath;//所要截图的图片地址
    private String saveCropPicturePath;//完成截图后图片的存储地址，不传的话代表不进行存储


    public PictureCropConfig(Bulid bulid, Context context) {
        this.context = context;
        this.cropPictureType = bulid.cropPictureType;
        this.picturePath = bulid.picturePath;
    }

    public static class Bulid{
        public static final int CROP_PICTURE_TYPE_LOCAL = 0;
        public static final int CROP_PICTURE_TYPE_NET = 1;
        private int cropPictureType;
        private String picturePath;
        private String saveCropPicturePath;//完成截图后图片的存储地址，不传的话代表不进行存储

        public Bulid setCropPictureType(int cropPictureType) {
            this.cropPictureType = cropPictureType;
            return this;
        }

        public Bulid setPicturePath(String picturePath) {
            this.picturePath = picturePath;
            return this;
        }

        public Bulid setSaveCropPicturePath(String saveCropPicturePath) {
            this.saveCropPicturePath = saveCropPicturePath;
            return this;
        }

        public PictureCropConfig bulid(Context context){
            return new PictureCropConfig(this,context);
        }
    }

    public Context getContext() {
        return context;
    }

    public Integer getCropPictureType() {
        return cropPictureType;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public String getSaveCropPicturePath() {
        return saveCropPicturePath;
    }
}
