package com.libpictureoptions.android.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

/**
 * Created by wangliang on 0019/2016/10/19.
 * 
 * 方法类："获得窗口的宽度" +
 "获得窗口的高度" +
 "获得状态栏高度" +
 "获得屏幕密度" +
 "获得屏幕密度DPI" +
 "获得app版本名称" +
 "获得app版本号" +
 "获得签名" +
 "做签名匹配" +
 "获得设备分辨率，eg: dm.widthPixels dm.heightPixels dm.density dm.xdpi dm.ydpi" +
 "获得设备的DensityDpi" +
 "获得设备的型号" +
 "获得设备的基带信息" +
 "获取设备的产品信息" +
 "获得设备的SDK版本" +
 "获得设备的SDK版本" +
 "获得设备的系统版本" +
 "获得设备的IMEI" +
 "获得设备的IMSI" +
 "获得设备的Sim卡序列号" +
 "获取语言信息" +
 "获得设备IP" +
 "获得DNS信息" +
 "获得Wifi的MAC地址" +
 "获得AndroidId" +
 "判断是否模拟器" +
 "获取设备信息串" +
 "获得应用名称" +
 "获取Application中<meta-data>元素的数据" +
 "获取应用程序包名" +
 "获取Activity中<meta-data>元素的数据" +
 "获取Service中<meta-data>元素的数据" +
 "获取Receiver中<meta-data>元素的数据" +
 "获取网络类型" + 
 "获得应用是否在前台" +
 "返回软件是否已安装" +
 "返回软件的原始签名" +
 "返回软件的签名，经过Hash处理" +
 "返回Intent是否可用" +
 "检查网络是否可用" +
 "检查Sim卡状态" +
 "检查移动网络是否可用" +
 "检查WIFI网络是否可用" +
 "检查网络是否联通WCDMA" +
 "检查是否开通GPS定位或网络定位" +
 "检查指定的定位服务是否开通" +
 "根据指定的Provider获取位置" +
 "根据指定的Provider获取位置" +
 "检查设备存储卡是否装载" +
 "检查设备存储卡的读权限" +
 "检查设备存储卡的写权限" +
 "获得SD卡的可用大小(单位为MB)" +
 "获得SD卡的可用大小(单位为KB)" +
 "获得SD卡的总大小(单位为MB)" +
 "获得SD卡的总大小(单位为KB)" +
 "获得SD卡的可用大小(单位为Byte)" +
 "获得SD卡的总大小(单位为Byte)" +
 "检查相机是否能使用" +
 "使设备震动" +
 "使设备震动" +
 "使设备停止震动" +
 "点亮屏幕" +
 "返回屏幕是否点亮" +
 "禁用键盘锁" +
 "启用键盘锁" +
 "调出拨号程序" +
 "直接拨打电话" +
 "发送短信" +
 "从本地选取图片，应处理onActivityResult，示例： protected void onActivityResult(int" +
 "requestCode, int resultCode, Intent data) { //获得图片的真实地址 String path =" +
 "getPathByUri(context, data.getData()); }" +
 "调用拍照程序拍摄图片，返回图片对应的Uri，应处理onActivityResult" +
 "ContentResolver的insert方法会默认创建一张空图片，如取消了拍摄，应根据方法返回的Uri删除图片" +
 "调用地图程序" +
 "调用分享程序" +
 "调用发送电子邮件程序" +
 "调用网络搜索" +
 "在桌面创建快捷方式" +
 "删除本应用的桌面快捷方式" +
 "去系统定位设置界面" +
 "去系统无线设置界面" +
 "安装apk文件" +
 "卸载apk文件" +
 "清空缓存目录下的文件(不清除子文件夹内的文件)" +
 "根据Uri获取媒体文件的路径" +
 "复制文本信息到剪贴板" +
 "从剪贴板获取文本信息" +
 "从资源中获取View" +
 "从资源中获取View" +
 "从资源中获取View";
 */
@SuppressLint("NewApi")
public class SystemInfoUtils {
    private static Context context;
    private static SystemInfoUtils systemInfoUtils;
    private static final String TAG = SystemInfoUtils.class.getName();

    public static SystemInfoUtils getInstance(Context ctx) {
        if (systemInfoUtils == null) {
            systemInfoUtils = new SystemInfoUtils();
        }
        if(ctx != null) {
            context = ctx;
        }
        return systemInfoUtils;
    }


    /**
     * 获得窗口的宽度
     * @return
     */
    public Integer getWindowWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int windowWidth = dm != null ? dm.widthPixels : 0;
        return windowWidth; //窗口的宽度
    }

    /**
     * 获得窗口的高度
     * @return
     */
    public Integer getWindowHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int windowHeight = dm != null ? dm.heightPixels : 0;
        return windowHeight;  //窗口高度
    }

    /**
     * 获得设备的SDK版本
     */
    public int getSdkVersion() {
        return Integer.parseInt(Build.VERSION.SDK);
    }
}
