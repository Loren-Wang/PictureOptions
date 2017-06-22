package com.libpictureoptions.android.pictureSelect;

import android.content.Context;
import android.view.View;

import com.libpictureoptions.android.common.AppCommon;
import com.libpictureoptions.android.pictureSelect.dto.StorePictureItemDto;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.PictureAppSetNowDirectoryCallback;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.PictureSelectDataListChangeCallback;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.PictureSelectViewOnClickListener;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

public class PictureSelectConfig implements Serializable{

    private Context context;
    private View pictureSelectActionBarView;//图片选择界面顶部操作栏，一旦设定则原本点击方法会被覆盖
    private View pictureSelectBottomOptionsView;//图片选择界面顶部操作栏，一旦设定则原本点击方法会被覆盖
    private View pictureSelectPreviewActionBarView;//图片选择界面预览界面顶部操作栏，一旦设定则原本点击方法会被覆盖
    private View pictureSelectPreviewBottomOptionsView;//图片选择界面预览界面底部操作栏，一旦设定则原本点击方法会被覆盖
    private Integer pictureSelectItemStateIconIdSelectedY;//图片选择界面item的选中状态之已选图片资源id
    private Integer pictureSelectItemStateIconIdSelectedN;//图片选择界面item的选中状态之未选图片资源id
    private int pictureSelectListColumnNum = 3;//图片选择列表的列数，默认为3
    private int pictureSelectListMaxSelectedNum = 9;//图片选择中最大的选择数量，默认为9
    private boolean whetherNeedCamera = true;//是否需要拍照
    private PictureSelectViewOnClickListener sendPictureOnClickListener;//发送按钮的点击相应事件
    private PictureSelectViewOnClickListener changeNowDirectoryOnClickListener;//切换当前正在显示的文件夹的点击相应事件
    private PictureSelectViewOnClickListener goToPreviewOnClickListener;//去预览的点击相应事件
    private PictureSelectViewOnClickListener previewBackOnClickListener;//预览后退按钮点击事件
    private PictureSelectViewOnClickListener selectNowPreviewPictureOnClickListener;//当前正在预览的图片选中与未选中的点击事件
    private PictureAppSetNowDirectoryCallback pictureAppSetNowDirectoryCallback;//设置当前正在显示的文件夹，该方法由调用该框架的一方获取，而实现是在框架内部
    private PictureSelectDataListChangeCallback pictureSelectDataListChangeCallback;//图片选择框架数据改变时的回调
    private String cameraPhotographSaveDirectory = AppCommon.PROJECT_FILE_DIR_IMAGE;//拍照之后的图片存储文件夹，默认是PictureSelects/images/

    public PictureSelectConfig(Bulid bulid,Context context) {
        this.context = context;
        this.pictureSelectActionBarView = bulid.pictureSelectActionBarView;
        this.pictureSelectBottomOptionsView = bulid.pictureSelectBottomOptionsView;
        this.pictureSelectItemStateIconIdSelectedY = bulid.pictureSelectItemStateIconIdSelectedY;
        this.pictureSelectItemStateIconIdSelectedN = bulid.pictureSelectItemStateIconIdSelectedN;
        this.pictureSelectListColumnNum = bulid.pictureSelectListColumnNum;
        this.whetherNeedCamera = bulid.whetherNeedCamera;
        this.sendPictureOnClickListener = bulid.sendPictureOnClickListener;
        this.changeNowDirectoryOnClickListener = bulid.changeNowDirectoryOnClickListener;
        this.goToPreviewOnClickListener = bulid.goToPreviewOnClickListener;
        this.previewBackOnClickListener = bulid.previewBackOnClickListener;
        this.selectNowPreviewPictureOnClickListener = bulid.selectNowPreviewPictureOnClickListener;
        this.pictureSelectListMaxSelectedNum = bulid.pictureSelectListMaxSelectedNum;
        if(this.pictureSelectDataListChangeCallback == null){
            this.pictureSelectDataListChangeCallback = new PictureSelectDataListChangeCallback() {
                @Override
                public void initialDataAll(Map<String, List<StorePictureItemDto>> allMapList) {

                }

                @Override
                public void initialDataAllDirectoryPath(List<String> allDirectoryList) {

                }

                @Override
                public void nowDirectoryChangeList(String nowDirectory, List<StorePictureItemDto> nowDirectoryPicturesList) {

                }

                @Override
                public void selectedPicturesChangeList(List<StorePictureItemDto> nowDirectoryPicturesList) {

                }
            };
        }else {
            this.pictureSelectDataListChangeCallback = bulid.pictureSelectDataListChangeCallback;
        }
        this.cameraPhotographSaveDirectory = bulid.cameraPhotographSaveDirectory;
    }

    public static class Bulid{
        private View pictureSelectActionBarView;//图片选择界面顶部操作栏，一旦设定则原本点击方法会被覆盖
        private View pictureSelectBottomOptionsView;//图片选择界面顶部操作栏，一旦设定则原本点击方法会被覆盖
        private View pictureSelectPreviewActionBarView;//图片选择界面预览界面顶部操作栏，一旦设定则原本点击方法会被覆盖
        private View pictureSelectPreviewBottomOptionsView;//图片选择界面预览界面底部操作栏，一旦设定则原本点击方法会被覆盖
        private Integer pictureSelectItemStateIconIdSelectedY;//图片选择界面item的选中状态之已选图片资源id
        private Integer pictureSelectItemStateIconIdSelectedN;//图片选择界面item的选中状态之未选图片资源id
        private int pictureSelectListColumnNum = 3;//图片选择列表的列数，默认为3
        private int pictureSelectListMaxSelectedNum = 9;//图片选择中最大的选择数量，默认为9
        private boolean whetherNeedCamera = true;//是否需要拍照
        private PictureSelectViewOnClickListener sendPictureOnClickListener;//发送按钮的点击相应事件
        private PictureSelectViewOnClickListener changeNowDirectoryOnClickListener;//切换当前正在显示的文件夹的点击相应事件
        private PictureSelectViewOnClickListener goToPreviewOnClickListener;//去预览的点击相应事件
        private PictureSelectViewOnClickListener previewBackOnClickListener;//预览后退按钮点击事件
        private PictureSelectViewOnClickListener selectNowPreviewPictureOnClickListener;//当前正在预览的图片选中与未选中的点击事件
        private PictureSelectDataListChangeCallback pictureSelectDataListChangeCallback;//图片选择框架数据改变时的回调
        private String cameraPhotographSaveDirectory = AppCommon.PROJECT_FILE_DIR_IMAGE;//拍照之后的图片存储文件夹，默认是PictureSelects/images/


        public Bulid setPictureSelectActionBarView(View pictureSelectActionBarView) {
            this.pictureSelectActionBarView = pictureSelectActionBarView;
            return this;
        }

        public Bulid setPictureSelectBottomOptionsView(View pictureSelectBottomOptionsView) {
            this.pictureSelectBottomOptionsView = pictureSelectBottomOptionsView;
            return this;
        }

        public Bulid setPictureSelectPreviewActionBarView(View pictureSelectPreviewActionBarView) {
            this.pictureSelectPreviewActionBarView = pictureSelectPreviewActionBarView;
            return this;
        }

        public Bulid setPictureSelectPreviewBottomOptionsView(View pictureSelectPreviewBottomOptionsView) {
            this.pictureSelectPreviewBottomOptionsView = pictureSelectPreviewBottomOptionsView;
            return this;
        }

        public Bulid setPictureSelectItemStateIconIdSelectedY(Integer pictureSelectItemStateIconIdSelectedY) {
            this.pictureSelectItemStateIconIdSelectedY = pictureSelectItemStateIconIdSelectedY;
            return this;
        }

        public Bulid setPictureSelectItemStateIconIdSelectedN(Integer pictureSelectItemStateIconIdSelectedN) {
            this.pictureSelectItemStateIconIdSelectedN = pictureSelectItemStateIconIdSelectedN;
            return this;
        }

        public Bulid setPictureSelectListColumnNum(int pictureSelectListColumnNum) {
            this.pictureSelectListColumnNum = pictureSelectListColumnNum;
            return this;
        }

        public Bulid setPictureSelectListMaxSelectedNum(int pictureSelectListMaxSelectedNum) {
            this.pictureSelectListMaxSelectedNum = pictureSelectListMaxSelectedNum;
            return this;
        }

        public Bulid setWhetherNeedCamera(boolean whetherNeedCamera) {
            this.whetherNeedCamera = whetherNeedCamera;
            return this;
        }


        public Bulid setSendPictureOnClickListener(PictureSelectViewOnClickListener sendPictureOnClickListener) {
            this.sendPictureOnClickListener = sendPictureOnClickListener;
            return this;
        }

        public Bulid setChangeNowDirectoryOnClickListener(PictureSelectViewOnClickListener changeNowDirectoryOnClickListener) {
            this.changeNowDirectoryOnClickListener = changeNowDirectoryOnClickListener;
            return this;
        }

        public Bulid setGoToPreviewOnClickListener(PictureSelectViewOnClickListener goToPreviewOnClickListener) {
            this.goToPreviewOnClickListener = goToPreviewOnClickListener;
            return this;
        }

        public Bulid setPreviewBackOnClickListener(PictureSelectViewOnClickListener previewBackOnClickListener) {
            this.previewBackOnClickListener = previewBackOnClickListener;
            return this;
        }

        public Bulid setSelectNowPreviewPictureOnClickListener(PictureSelectViewOnClickListener selectNowPreviewPictureOnClickListener) {
            this.selectNowPreviewPictureOnClickListener = selectNowPreviewPictureOnClickListener;
            return this;
        }

        public Bulid setPictureSelectDataListChangeCallback(PictureSelectDataListChangeCallback pictureSelectDataListChangeCallback) {
            this.pictureSelectDataListChangeCallback = pictureSelectDataListChangeCallback;
            return this;
        }

        public Bulid setCameraPhotographSaveDirectory(String cameraPhotographSaveDirectory) {
            if(cameraPhotographSaveDirectory != null) {
                if(cameraPhotographSaveDirectory.lastIndexOf("/") == cameraPhotographSaveDirectory.length()) {
                    this.cameraPhotographSaveDirectory = cameraPhotographSaveDirectory;
                }else {
                    this.cameraPhotographSaveDirectory = cameraPhotographSaveDirectory + "/";
                }
            }
            return this;
        }

        public PictureSelectConfig bulid(Context context){
            return new PictureSelectConfig(this,context);
        }
    }

    public View getPictureSelectActionBarView() {
        return pictureSelectActionBarView;
    }

    public View getPictureSelectBottomOptionsView() {
        return pictureSelectBottomOptionsView;
    }

    public View getPictureSelectPreviewActionBarView() {
        return pictureSelectPreviewActionBarView;
    }

    public View getPictureSelectPreviewBottomOptionsView() {
        return pictureSelectPreviewBottomOptionsView;
    }

    public Integer getPictureSelectItemStateIconIdSelectedY() {
        return pictureSelectItemStateIconIdSelectedY;
    }

    public Integer getPictureSelectItemStateIconIdSelectedN() {
        return pictureSelectItemStateIconIdSelectedN;
    }

    public Integer getPictureSelectListColumnNum() {
        return pictureSelectListColumnNum;
    }

    public Integer getPictureSelectListMaxSelectedNum() {
        return pictureSelectListMaxSelectedNum;
    }

    public boolean isWhetherNeedCamera() {
        return whetherNeedCamera;
    }

    public PictureSelectViewOnClickListener getSendPictureOnClickListener() {
        return sendPictureOnClickListener;
    }

    public PictureSelectViewOnClickListener getChangeNowDirectoryOnClickListener() {
        return changeNowDirectoryOnClickListener;
    }

    public PictureSelectViewOnClickListener getGoToPreviewOnClickListener() {
        return goToPreviewOnClickListener;
    }

    public PictureSelectViewOnClickListener getPreviewBackOnClickListener() {
        return previewBackOnClickListener;
    }

    public PictureSelectViewOnClickListener getSelectNowPreviewPictureOnClickListener() {
        return selectNowPreviewPictureOnClickListener;
    }

    public PictureAppSetNowDirectoryCallback getPictureAppSetNowDirectoryCallback() {
        return pictureAppSetNowDirectoryCallback;
    }

    //该方法由框架调用
    public PictureSelectConfig setChangeNowDirectoryOnClickListener(PictureAppSetNowDirectoryCallback pictureAppSetNowDirectoryCallback) {
        if(context.getClass().getName().equals(PictureSelectActivity.class.getName())) {
            this.pictureAppSetNowDirectoryCallback = pictureAppSetNowDirectoryCallback;
        }
        return this;
    }

    public String getCameraPhotographSaveDirectory() {
        return cameraPhotographSaveDirectory;
    }

    public PictureSelectDataListChangeCallback getPictureSelectDataListChangeCallback() {
        return pictureSelectDataListChangeCallback;
    }
}
