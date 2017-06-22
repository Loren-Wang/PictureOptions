package com.libpictureoptions.android.common.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;
import java.util.UUID;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * 应用工具类
 * 
 * @author yynie
 * @since 2013-09-23
 */
public final class AppUtils {

	private static final String TAG = "AppUtils";


	/*uuid产生器*/
	public static String generateUuid(){
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		return uuid ;
	}

	/**
	 * 获取当前应用程序的包名
	 * @param context 上下文对象
	 * @return 返回包名
	 */
	public static String getAppProcessName(Context context) {
		//当前应用pid
		int pid = android.os.Process.myPid();
		//任务管理类
		ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
		//遍历所有应用
		List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo info : infos) {
			if (info.pid == pid)//得到当前应用
				return info.processName;//返回包名
		}
		return "";
	}

}
