package com.libpictureoptions.android.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.libpictureoptions.android.R;


/**
 * 该类功能：dialog的弹出样式与操作
 * 创建者：wangliang_dev
 * 创建时间：2015-12-14
 */
public class DialogUtils {
    private static final String TAG = DialogUtils.class.getName();
    private static Toast mToast=null;

    /**
     * 显示 长提示
     *
     * @param context
     * @param text
     */
    public static void showToastLong(Context context, String text) {
        cancelToast();
        try {
            if (context != null && ParamsAndJudgeUtils.getInstance().isNotBackground(context)) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                // ToastUtils.showMsg(context, text, Toast.LENGTH_LONG);
            }
        }catch (Exception e){
            LogUtils.logE(e);
        }
    }

    /**
     * 显示 长提示
     *
     * @param context
     * @param resId
     */
    public static void showToastLong(Context context, int resId) {
        cancelToast();
        try {
            if (context != null && ParamsAndJudgeUtils.getInstance().isNotBackground(context)) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        }catch (Exception e){
            LogUtils.logE(e);
        }
    }
    /**
     * 显示 短提示
     *
     * @param context
     * @param text
     */
    public static void showToastShort(Context context, String text) {
        cancelToast();
        try {
            if (context != null && ParamsAndJudgeUtils.getInstance().isNotBackground(context)) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        }catch (Exception e){
            LogUtils.logE(e);
        }

    }

    /**
     * 显示 短提示
     *
     * @param context
     * @param text
     */
    public static Toast showToastShortAndReturnToast(Context context, String text) {
        cancelToast();
        try {
            if (context != null && ParamsAndJudgeUtils.getInstance().isNotBackground(context)) {
                mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
            }
        }catch (Exception e){
            LogUtils.logE(e);
        }
        return mToast;
    }

    /**
     * 显示 短提示
     *
     * @param context
     * @param resId
     */
    public static void showToastShort(Context context, int resId) {
        cancelToast();
        try {
            if (context != null && ParamsAndJudgeUtils.getInstance().isNotBackground(context)) {
                mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                // ToastUtils.showMsg(context, resId, Toast.LENGTH_SHORT);
            }
        }catch (Exception e){
            LogUtils.logE(e);
        }
    }

    /**
     * 取消提示
     */
    public static void cancelToast(){
        if(mToast != null){
            mToast.cancel();
        }
    }



    /**
     * 显示下拉弹出气泡
     *
     * @param context
     * @param parent
     *            父View
     * @param child
     *            弹出气泡的内容
     * @param width
     *            气泡宽度
     * @param height
     *            气泡高度
     * @param xoff
     *            相对于父View的X偏移
     * @param yoff
     *            相对于父View的Y偏移
     * @param listener
     *            弹出层消失的监听
     */
    public static PopupWindow showPopupDropDown(Context context, View parent,
                                         View child, int width, int height, int xoff, int yoff,
                                         final PopupWindow.OnDismissListener listener) {
        PopupWindow popMain = null;
        try {
            if (parent == null) {
                return null;
            }
            child.measure(0,0);
            if(width == 0){
                width = child.getMeasuredWidth();
            }
            if(height == 0){
                height = child.getMeasuredHeight();
            }

            // 弹出层显示的内容
            popMain = new PopupWindow(child, width, height,true);
            popMain.setBackgroundDrawable(new BitmapDrawable());
            popMain.setOutsideTouchable(true);
            popMain.setClippingEnabled(true);
            popMain.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (listener != null) {
                        listener.onDismiss();
                    }
                }
            });
            popMain.showAsDropDown(parent, xoff, yoff);
        } catch (Exception e) {
            LogUtils.logE(TAG, e);
        }
        return popMain;
    }

    public static Dialog showDialogView(Context context,View view){
        Dialog dialog = new Dialog(context, R.style.dialog_be_full_off_window);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setWindowAnimations(R.style.dialog_anim_picture_select_preview);
        return dialog;
    }



}
