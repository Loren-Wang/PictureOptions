package com.libpictureoptions.android.pictureCamera.interface_and_abstract;

import android.view.View;

/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 15:42
 * 创建人：王亮（Loren wang）
 * 功能作用：拍照控件点击事件
 * 思路： 1、在创建该回调的时候需要将需要替换的控件传入
 *       2、在替换控件传入之后需要给替换控件设置点击事件，但是点击事件的添加是要在最终界面初始化的时候有最终界面添加
 *       3、最终界面（例如：pictureCameraActivity）在初始化的时候根据需要调取相应的本类的setOnClickListener方法，传入真正的相应事件
 *       4、真正的相应事件传入回调中之后开始添加替换控件的点击事件，在替换控件的点击事件中药执行真正的点击相响应事件以及替换控件的点击回调
 *       5、在被替换控件的点击事件中需要调用回调事件中的onClickListener(View view)方法回调给被替换控件的点击响应事件
 * 修改人：
 * 修改时间：
 * 备注：
 */

public abstract class CameraPictureViewOnClickListener{
    private View view;

    public CameraPictureViewOnClickListener(View view) {
        this.view = view;
    }

    public abstract void onClickListener(View view);

    /**
     * 设置点击事件
     * @param onClickListener
     */
    public void setOnClickListener(final View.OnClickListener onClickListener){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(view);
                onClickListener(view);
            }
        });
    }
}
