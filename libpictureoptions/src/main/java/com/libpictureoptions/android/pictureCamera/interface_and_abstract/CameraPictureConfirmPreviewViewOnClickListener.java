package com.libpictureoptions.android.pictureCamera.interface_and_abstract;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 15:42
 * 创建人：王亮（Loren wang）
 * 功能作用：拍照控件点击事件
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class CameraPictureConfirmPreviewViewOnClickListener {
    private View view;

    public CameraPictureConfirmPreviewViewOnClickListener(View view) {
        this.view = view;
    }

    public abstract void onClick(Bitmap bitmap);

    /**
     * 设置点击事件
     * @param onClickListener
     */
    public void setOnClickListener(final View.OnClickListener onClickListener){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(view);
            }
        });
    }

}
