package com.libpictureoptions.android.pictureSelect.interface_and_abstract;

import android.view.View;

import com.libpictureoptions.android.pictureSelect.dto.StorePictureItemDto;

import java.util.List;

/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 15:42
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择中控件的点击事件
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class PictureSelectViewOnClickListener {
    private View view;

    public PictureSelectViewOnClickListener(View view) {
        this.view = view;
    }

    public abstract void onClick(List<StorePictureItemDto> selectedItemList);

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
