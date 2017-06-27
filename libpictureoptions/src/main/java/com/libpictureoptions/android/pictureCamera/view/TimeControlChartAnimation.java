package com.libpictureoptions.android.pictureCamera.view;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by wangliang on 0026/2017/6/26.
 * 创建时间： 0026/2017/6/26 17:29
 * 创建人：王亮（Loren wang）
 * 功能作用：动画计时器
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class TimeControlChartAnimation extends Animation {
    View view;
    SetPercentCallbackListener setListener;

    public TimeControlChartAnimation(View view, SetPercentCallbackListener setListener) {
        this.view = view;
        this.setListener = setListener;
    }
    int i = 0;

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        view.postInvalidate();
        setListener.percent(interpolatedTime);
    }

    public interface SetPercentCallbackListener{
        void percent(float interpolatedTime);
    }
}

