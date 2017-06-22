package com.libpictureoptions.android.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.utils.LogUtils;
import com.libpictureoptions.android.common.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 14:14
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */
public abstract class BaseActivity extends AppCompatActivity {
    private LinearLayout lnChildLayout;

    private Context context;
    //标题栏下空间使用参数及关联控件名称
    protected String TAG;
    //列表图片加载的tag标签，用来只有在列表停止的时候才开始加载picasso图片的标记
    protected String listViewImageLoadingTag;
    private HandlerThread handlerThread;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

//        context = this;
//        if(!context.getClass().getName().equals(WelcomeActivity.class.getName())) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                setTranslucentStatus(true);
//                SystemBarTintManager tintManager = new SystemBarTintManager(this);
//                tintManager.setStatusBarTintEnabled(true);
//                tintManager.setStatusBarTintResource(R.color.notify_bg);//通知栏所需颜色
//            }
//        }

        TAG = getLocalClassName();//初始化tag标记
        listViewImageLoadingTag = getLocalClassName();//初始化tag标记
        handlerThread = new HandlerThread(getLocalClassName());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        setContentView(R.layout.activity_base);
        initView();//初始化基类控件界面
        initListener();//初始化基类事件监听
        initNetRequestCallBack();//初始化网络请求回调
        initChildView();//初始化子类控件界面
        initChildData(savedInstanceState);//初始化子类数据
        initChildListener();//初始化子类的点击事件

    }

    /**
     * 初始化基类控件界面
     */
    private void initView(){
        lnChildLayout = (LinearLayout) findViewById(R.id.lnChildLayout);
    }

    private void initListener(){
    }

    /**
     *  添加子部局
     * @param layoutId  子部局id
     * @return
     */
    public View addChildView(int layoutId) {
        View childView = LayoutInflater.from(context).inflate(layoutId, null);
        lnChildLayout.addView(childView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        return childView;
    }

    /**
     * 初始化子控件布局
     */
    public abstract void initChildView();

    /**
     * 初始化子控件的点击事件
     */
    public abstract void initChildListener();

    /**
     * 添加需要子类必须重写的函数，该函数方法负责禁用或者启用布局中所有控件的是否可操作的功能
     * @param states 是否可以进行操作
     */
    public abstract void setClickEnabledStates(boolean states);

    /**
     * 初始化子类数据+
     */
    public abstract void initChildData(Bundle savedInstanceState);

    /**
     * 初始化网络请求回调
     */
    public void initNetRequestCallBack(){

    }

    private final int permissionsRequestCode = 0;
    public void perissionRequest(String permissions[]){
        if(SystemInfoUtils.getInstance(context).getSdkVersion() < 23){//版本判断，小于23的不执行权限请求
            perissionRequestSuccessCallback(permissions);
            perissionRequestFailCallback(new String[]{});
            return;
        }
        List<String> noPermissions = new ArrayList<>();
        List<String> havePermissions = new ArrayList<>();
        for(int i = 0 ; i < permissions.length ; i++){
            if (ContextCompat.checkSelfPermission(context,
                    permissions[i])
                    != PackageManager.PERMISSION_GRANTED) {
                noPermissions.add(permissions[i]);
            }else {
                havePermissions.add(permissions[i]);
            }
        }

        if(noPermissions.size() != 0){
            String[] strings = new String[noPermissions.size()];
            for(int i = 0 ; i < noPermissions.size() ; i++){
                strings[i] = noPermissions.get(i);
            }
            ActivityCompat.requestPermissions(this, strings,
                    permissionsRequestCode);
        }else {
            String[] strings = new String[havePermissions.size()];
            for(int i = 0 ; i < havePermissions.size() ; i++){
                strings[i] = havePermissions.get(i);
            }
            perissionRequestSuccessCallback(strings);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case permissionsRequestCode: {
                // If request is cancelled, the result arrays are empty.

                List<String> successPermissionList = new ArrayList<>();
                List<String> failPermissionList = new ArrayList<>();

                if(grantResults.length > 0 && grantResults.length == permissions.length) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            successPermissionList.add(permissions[i]);
                            LogUtils.logI("用户同意权限", "user granted the permission!" + permissions[i]);
                        } else {
                            LogUtils.logI("用户不同意权限", "user denied the permission!" + permissions[i]);
                            failPermissionList.add(permissions[i]);
                        }
                    }
                }else {
                    for(int i = 0 ; i < permissions.length ; i++){
                        failPermissionList.add(permissions[i]);
                    }
                }

                try {
                    String[] successPermissions = new String[successPermissionList.size()];
                    for(int i = 0 ; i < successPermissionList.size() ; i++){
                        successPermissions[i] = successPermissionList.get(i);
                    }
                    String[] failPermissions = new String[failPermissionList.size()];
                    for(int i = 0 ; i < failPermissionList.size() ; i++){
                        failPermissions[i] = failPermissionList.get(i);
                    }
                    perissionRequestSuccessCallback(successPermissions);
                    perissionRequestFailCallback(failPermissions);

                }catch (Exception e){
                    LogUtils.logE(TAG,e.getMessage());
                }

                return;
            }
        }
    }

    /**
     * 权限请求成功回调
     * @param perissions 请求成功的权限集合
     */
    public void  perissionRequestSuccessCallback(String perissions[]){

    }
    /**
     * 权限请求失败回调
     * @param perissions 请求失败的权限集合
     */
    public void  perissionRequestFailCallback(String perissions[]){

    }

    public Context getContext() {
        return context;
    }
}
