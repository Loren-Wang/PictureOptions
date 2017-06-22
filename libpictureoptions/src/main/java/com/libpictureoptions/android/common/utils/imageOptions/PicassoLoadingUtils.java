package com.libpictureoptions.android.common.utils.imageOptions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.AppCommon;
import com.libpictureoptions.android.common.utils.CheckUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

/**
 * Created by wangliang on 0027/2016/10/27.
 */
public class PicassoLoadingUtils {

    private static Context context;

    private static PicassoLoadingUtils picassoLoadingUtils;
    private final String loadingCachePath = AppCommon.PROJECT_FILE_DIR_IMAGE;
    private Map<String,Integer> mapHeight = new HashMap<>();

    public static PicassoLoadingUtils getInstance(Context ctx){
        if(ctx != null){
            context = ctx;
        }

        if(picassoLoadingUtils == null){
            picassoLoadingUtils = new PicassoLoadingUtils();
        }

        return picassoLoadingUtils;
    }

    private PicassoLoadingUtils(){
//        handlerThread.start();
//        handler = new Handler(handlerThread.getLooper());


        File file = new File(AppCommon.PROJECT_FILE_DIR_IMAGE);
        if (!file.exists()) {
            file.mkdirs();
        }

        long maxSize = Runtime.getRuntime().maxMemory() / 8;//设置图片缓存大小为运行时缓存的八分之一
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(new Cache(file, maxSize))
                .build();

        Picasso picasso = new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(client))//注意此处替换为 OkHttp3Downloader
                .build();

        Picasso.setSingletonInstance(picasso);

    }

    /**
     * 优化不缓存策略
     * @param requestCreator
     * @return
     */
    private RequestCreator skipMemoryCache(RequestCreator requestCreator) {
        return requestCreator.memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_STORE, NetworkPolicy.NO_CACHE);
    }

    /**
     * 降低内存消耗
     * 设置RGB_565编码格式，降低内存消耗
     * @param requestCreator
     * @return
     */
    private RequestCreator cutDownMemory(RequestCreator requestCreator) {
        return requestCreator.config(Bitmap.Config.RGB_565);
    }


    /**
     * 加载图片
     * @param loaddingPath 加载路径，任意路径
     * @param localCachePath 本地缓存路径
     * @param imageView
     * @param transformation 加载之后特殊的显示效果，或者自己定义
     * @param width 图片宽度
     * @param height 图片高度
     * @param isCenterCrop 是否centerCrop
     * @param isCenterInside 是否centerInside
     * @param isCache 是否缓存
     * @param tag 加载的tag
     */
    private void loadingImage(Object loaddingPath, String localCachePath,
                              ImageView imageView, Transformation transformation,Drawable placeholder,Drawable error, Integer width, Integer height,
                              boolean isCenterCrop, boolean isCenterInside, boolean isCache,String tag){
        if(CheckUtils.isNotEmptyOrNull(loaddingPath) && imageView != null ){
            Picasso picasso = Picasso.with(context);
            if(!CheckUtils.isNotEmptyOrNull(picasso)){
                return;
            }
            RequestCreator requestCreator = null;
            //根据传入的不同类型进行转换
            if(loaddingPath instanceof String) {
                requestCreator = picasso.load((String) loaddingPath);
            }else if(loaddingPath instanceof File) {
                requestCreator = picasso.load((File) loaddingPath);
            }else if(loaddingPath instanceof Integer) {
                requestCreator = picasso.load((Integer) loaddingPath);
            }

            if(requestCreator != null) {
                //设置tag
                if(tag != null) {
                    requestCreator.tag(tag);
                }
                //设置transformation
                if (transformation != null) {
                    requestCreator.transform(transformation);
                }
                //设置加载中显示图片以及加载失败显示图片
                if(placeholder == null) {
                    requestCreator.placeholder(R.mipmap.ic_launcher);
                }else {
                    requestCreator.placeholder(placeholder);
                }
                if(error == null){
                    requestCreator.error(R.mipmap.ic_launcher);
                }else {
                    requestCreator.error(error);
                }

                //设置宽高
                if (width != null && height != null && width != 0 && height != 0) {
                    requestCreator.resize(width, height);
                }
                //设置是否centerCrop
                if (isCenterCrop) {
                    requestCreator.centerCrop();
                }
                //设置是否centerInside
                if (isCenterInside) {
                    requestCreator.centerInside();
                }
                //设置是否进行缓存
                if (!isCache) {
                    requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
                }

                if(isCache){
                    //如果是网络请求的话同时又允许使用缓存，则所有的图片都下载到本地做缓存
                    // ，路径使用默认的缓存路径，如果有特殊路径除外,如果不需要缓存的除外
                    if(CheckUtils.isNotEmptyOrNull(loaddingPath)
                            && loaddingPath instanceof String){
                        //先判断本地是否缓存过，有的话则直接取本地
                        String filePath = (AppCommon.PROJECT_FILE_DIR_IMAGE + ((String) loaddingPath).substring(((String) loaddingPath).lastIndexOf("/"))).intern();
                        filePath = filePath.replace(".jpg", "");
                        filePath = filePath.replace(".jpeg", "");
                        filePath = filePath.replace(".png", "");
                        File localFile = new File(filePath.toString());
                        if(CheckUtils.isNotEmptyOrNull(localFile)
                                && localFile.exists()){//本地存在
                            loadingImage(localFile,localCachePath,imageView,transformation,placeholder,error,width,height,isCenterCrop,isCenterInside,isCache,tag);
                        }else {//本地不存在，则下载并加载
                            downLoadImageToLocal((String) loaddingPath
                                    ,transformation
                                    ,requestCreator
                                    , localFile.getPath()
                                    , imageView);
                        }
                    }else {//如果是非网络请求的话
                        requestCreator.into(imageView);
                    }
                }else {//不使用缓存
                    requestCreator.into(imageView);
                }
            }
        }else {
            if(imageView != null){
                loadingImage(R.mipmap.ic_launcher,localCachePath,imageView,transformation,placeholder,error,width
                        ,height,isCenterCrop,isCenterInside,isCache,tag);
            }
        }
    }

    /**
     * 下载网络图片到本地
     * @param loaddingPath
     * @param requestCreator
     * @param localCachePath 本地存储路径
     * @param imageView 如果该参数不为空则代表着还需要加载图片显示
     */
    private void downLoadImageToLocal(final String loaddingPath
            , Transformation transformation
            , RequestCreator requestCreator
            , final String localCachePath
            , final ImageView imageView){

        if(CheckUtils.isNotEmptyOrNull(loaddingPath)
                && CheckUtils.isNotEmptyOrNull(requestCreator)) {
            if (imageView != null) {
                requestCreator.into(imageView);
            } else {
                requestCreator.into(new ImageView(context));
            }

            if(CheckUtils.isNotEmptyOrNull(localCachePath)) {
//                //下载照片
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        HttpOkHttpRequestUtils.getInstance(context).fileDownLoadRequest(
//                                loaddingPath, localCachePath, null);
//                    }
//                });
            }
        }
    }

    public void loadingNet(String urlPath,String localCachePath, ImageView imageView,boolean isCache){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,null,null,false,false,isCache,null);
    }
    public void loadingNet(String urlPath,String localCachePath, ImageView imageView,Integer width,Integer height){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,width,height,false,false,true,null);
    }
    public void loadingNet(String urlPath,String localCachePath,Transformation transformation, ImageView imageView,Integer width,Integer height,boolean isCache){
        loadingNet(urlPath,localCachePath,imageView,transformation,null,null,width,height,false,false,isCache,null);
    }
    public void loadingNet(String urlPath,String localCachePath, ImageView imageView,Integer width,Integer height,String loadongTag){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,width,height,false,false,true,loadongTag);
    }
    public void loadingNet(String urlPath,String localCachePath, ImageView imageView,int width,int height,boolean isCache){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,width,height,false,false,isCache,null);
    }
    public void loadingNet(String urlPath,String localCachePath, ImageView imageView,int width,int height,boolean isCache,String loadongTag){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,width,height,false,false,isCache,loadongTag);
    }
    public void loadingNetCenterCrop(String urlPath,String localCachePath, ImageView imageView){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,null,null,true,false,true,null);
    }
    public void loadingNetCenterCrop(String urlPath, String localCachePath, ImageView imageView, int width, int height,
                                     String listViewImageLoadingTag){
        loadingNet(urlPath,localCachePath,imageView,null,null,null,width,height,true,false,true,listViewImageLoadingTag);
    }

    /**
     * 发起请求前的判定
     */
    public void loadingNet(String loaddingPath, String localCachePath,
                           ImageView imageView, Transformation transformation,Drawable placeholder,Drawable error, Integer width, Integer height,
                           boolean isCenterCrop, boolean isCenterInside, boolean isCache,String tag){
        if(checkLoaddingUrlPath(loaddingPath)) {
            loadingImage(loaddingPath,localCachePath,imageView,transformation,placeholder,error,width
                    ,height,isCenterCrop,isCenterInside,isCache,tag);
        }else {
            loadingImage(R.mipmap.ic_launcher,localCachePath,imageView,transformation,placeholder,error,width
                    ,height,isCenterCrop,isCenterInside,isCache,tag);
        }
    }


    /**
     * 加载本地文件
     * @param localPath
     * @param imageView
     * @param width
     * @param height
     */
    public void loadingLocalCenterCrop(String localPath, ImageView imageView,Integer width,Integer height,String loadingTag){
        loadingLocal(localPath,imageView,width,height,true);
    }


    /**
     * 加载本地文件
     * @param localPath
     * @param imageView
     * @param width
     * @param height
     */
    public void loadingLocal(String localPath, ImageView imageView,Integer width,Integer height,boolean isCache,String loadingTag){
        loadingLocal(localPath,imageView,width,height,false);
    }

    /**
     * 加载资源文件
     * @param resid
     * @param imageView
     */
    public void loadingRes(int resid, ImageView imageView,int width,int height){
        loadingImage(resid,null,imageView,null,null,null,width,height,false,false,true,null);
    }

    /**
     * 加载本地文件
     * @param localPath
     * @param imageView
     * @param width
     * @param height
     */
    public void loadingLocal(String localPath, ImageView imageView,Integer width,Integer height,boolean isCenterCrop){
        if(localPath.contains(".jpg") || localPath.contains(".png")) {
            loadingImage(new File(localPath), null, imageView, null,null,null, width, height, isCenterCrop, false, true, null);
        }else {
            loadingImage(new File(localPath + ".jpg"), null, imageView, null,null,null, width, height, false, false, true, null);
        }
    }

    /**
     * 检查图片网址是否合法
     * @param loaddingPath
     * @return
     */
    private boolean checkLoaddingUrlPath(final String loaddingPath){
        //先进行是否是网址判断
        boolean loaddingPathIsUrl = loaddingPath != null
                && (loaddingPath.contains("http://") || loaddingPath.contains("https://"));
        return loaddingPathIsUrl;
    }


}
