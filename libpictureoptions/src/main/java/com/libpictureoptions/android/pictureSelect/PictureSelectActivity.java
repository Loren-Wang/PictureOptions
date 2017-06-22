package com.libpictureoptions.android.pictureSelect;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.AppCommon;
import com.libpictureoptions.android.common.BaseActivity;
import com.libpictureoptions.android.common.utils.DialogUtils;
import com.libpictureoptions.android.common.utils.ParamsAndJudgeUtils;
import com.libpictureoptions.android.common.utils.SystemInfoUtils;
import com.libpictureoptions.android.pictureSelect.adapter.IntruderViewPagerAdapter;
import com.libpictureoptions.android.pictureSelect.adapter.PictureSelectsAdapter;
import com.libpictureoptions.android.pictureSelect.adapter.PictureSelectsChangeDirctoryAdapter;
import com.libpictureoptions.android.pictureSelect.database.DbPhonePicturesList;
import com.libpictureoptions.android.pictureSelect.dto.StorePictureItemDto;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.PictureAppSetNowDirectoryCallback;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.RecycleViewAdapterCallback;
import com.libpictureoptions.android.pictureSelect.view.DividerGridItemDecoration;
import com.libpictureoptions.android.pictureSelect.view.DividerLineraItemDecoration;
import com.squareup.picasso.Picasso;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 14:14
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择功能
 * 思路：1、创建一个基础的图片选择功能，然后将标题栏以及底部操作栏还有item栏暴露出去进行操作
 *      2、逻辑：①本页面是activity
 *              ②预览页是dialog
 *              ③首先进行布局的配置，图片选择页面中顶部以及底部的布局使用的是鸿阳大神的百分比相对布局，中间的列表显示使用的是recycleview列表进行显示
 *                                ，预览界面也是这样的布局，不同的是预览界面中间的列表显示使用的是viewpager显示
 *              ④然后是获取图片列表，获取图片列表使用的是contentProvide获取系统数据库，具体的获取方法以及内容查看DbPhonePicturesList.java文件
 *              ⑤首页对于初始状态的数据配置：图片为所有的图片；最大的图片数量默认是9张
 *              ⑥在更改选中图片的时候将选中图片存到选中集合中同时修改适配器相应位置的数据，在切换文件夹的时候更新整个数据；
 *                在预览的时候分如果是点击预览按钮是显示已选中的，如果是图片列表点击的话是显示当前点击图片开始的viewpager图片预览，
 *                原图按钮是在最后返回的时候进行操作，而预览按钮的选择操作会同步到recycleview所在的适配器中，两者会互相同步
 *              ⑦重点是预览图片与图片列表的同步问题
 * 修改人：
 * 修改时间：
 * 备注：1、对于点击事件来说，如果是视图背覆盖的点击事件来说，那么如果被覆盖则调用几口回调将相应的列表值传回，否则不传回
 */
public class PictureSelectActivity extends BaseActivity {

    private RecyclerView recySelects;
    private PercentRelativeLayout relSelectActionBar;//顶部操作栏
    private PercentRelativeLayout relBottomOptions;//底部操作框布局
    private ImageView imgSelectBack;//选择图片也后退
    private Button btnSelectSend;//选择图片后退
    private Button btnNowDirectory;//当前正在选择的文件夹
    private Button btnPreview;//预览按钮
    //弹出文件夹选择相关
    private RecyclerView recyChangeDirectory;//切换当前正在显示的文件夹的文件夹显示列表
    private PopupWindow popupWindowShowChangeDirectory;
    private PictureSelectsAdapter pictureSelectsAdapter;//图片选择适配器

    //预览相关视图
    private View dialogPreviewView;//预览弹出框视图
    private ViewPager vpgShow;
    private ImageView imgPreviewBack;//预览后退
    private Button btnPreviewSend;//发送
    private CheckBox cbOriginalPicture;//选中原图
    private CheckBox cbPreviewSelect;//选中当前图片
    private Dialog dialogPreview;//预览弹出框

    //数据相关
    private Map<String, List<StorePictureItemDto>> allMapList;//所有图片的键值对集合
    private List<StorePictureItemDto> allList = new ArrayList<>();//所有的图片集合
    private List<String> allKeyDirectoryList = new ArrayList<>();//所有的文件夹名称集合
    private List<StorePictureItemDto> nowDirectoryPicturesList = new ArrayList<>();//当前正在显示的文件夹内的图片集合
    private List<StorePictureItemDto> selectedPicturesList = new ArrayList<>();//已经选中的图片列表,使用哈希表存储已选中的数据由于key的唯一
                                                                                   // 可以使数据保证唯一性，在添加或者移除上亦可以直接根据key值进行操作,
                                                                                   // value代表着新增进入选中列表的时间
    private int windowWidth;
    private int windowHeight;
    private String nowDirectory;
    private int maxSelectedPictureNum = 9;//最大选择的图片数量，初始默认值为9
    private PictureSelectConfig config;//配置文件内容
    private int pictureSelectStateY = R.drawable.icon_picture_select_y;//图片被选中时显示的状态图标
    private int pictureSelectStateN = R.drawable.icon_picture_select_n;//图片没有被选中时显示的状态图标
    private String allPictureTag = "ALL";//显示所有图片的文件夹标记
    private final int forCameraRequestCode = 1;//去拍照的请求码
    private String cameraPhotographSaveDirectory = AppCommon.PROJECT_FILE_DIR_IMAGE;//拍照之后的图片存储文件夹，默认是PictureSelects/images/


    @Override
    public void initChildView() {
        addChildView(R.layout.activitys_main);
        relSelectActionBar = (PercentRelativeLayout) findViewById(R.id.relSelectActionBar);
        relBottomOptions = (PercentRelativeLayout) findViewById(R.id.relBottomOptions);
        btnNowDirectory = (Button) findViewById(R.id.btnNowDirectory);
        btnPreview = (Button) findViewById(R.id.btnPreview);
        imgSelectBack = (ImageView) findViewById(R.id.imgSelectBack);
        btnSelectSend = (Button) findViewById(R.id.btnSelectSend);
        recySelects = (RecyclerView) findViewById(R.id.recySelects);
        recySelects.addItemDecoration(new DividerGridItemDecoration(getContext(), DividerLineraItemDecoration.VERTICAL_LIST,null));

        //初始化图片文件夹切换列表
        recyChangeDirectory = new RecyclerView(getContext());
        recyChangeDirectory.setLayoutManager(new LinearLayoutManager(getContext()));
        recyChangeDirectory.addItemDecoration(new DividerLineraItemDecoration(getContext(), DividerLineraItemDecoration.VERTICAL_LIST,null));
        recyChangeDirectory.setBackgroundColor(Color.WHITE);

        //初始化弹框显示的布局
        dialogPreviewView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_picture_select_preview,null);
        vpgShow = (ViewPager) dialogPreviewView.findViewById(R.id.vpgShow);
        imgPreviewBack = (ImageView) dialogPreviewView.findViewById(R.id.imgPreviewBack);//预览后退
        btnPreviewSend = (Button) dialogPreviewView.findViewById(R.id.btnPreviewSend);//发送
        cbOriginalPicture = (CheckBox) dialogPreviewView.findViewById(R.id.cbOriginalPicture);//选中原图
        cbPreviewSelect = (CheckBox) dialogPreviewView.findViewById(R.id.cbPreviewSelect);//选中当前图片
        dialogPreview = DialogUtils.showDialogView(getContext(), dialogPreviewView);

    }

    @Override
    public void initChildListener() {
        //切换当前正在显示的文件夹按钮，并弹出显示选择
        btnNowDirectory.setOnClickListener(changeNowDirectoryOnClickListener);
        //发送选择好的图片
        btnPreviewSend.setOnClickListener(sendSelectedListClick);
        btnSelectSend.setOnClickListener(sendSelectedListClick);
        //选中当前正在预览的图片
        cbPreviewSelect.setOnClickListener(selectNowPreviewPictureOnClick);
        //预览后退
        imgPreviewBack.setOnClickListener(previewBackOnClick);
        //预览已选择
        btnPreview.setOnClickListener(previewSelectedListOnClick);


        //图片选择列表滑动监听
        recySelects.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {//滑动停止的时候加载图片
                    Picasso.with(getContext()).resumeTag(listViewImageLoadingTag);
                } else {//滑动时候不加载图片
                    Picasso.with(getContext()).pauseTag(listViewImageLoadingTag);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        //vpg滑动监听，用来监听当前滑动到的是否是被选中的
        vpgShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(selectedPicturesList.toString().contains(nowDirectoryPicturesList.get(position).toString())){
                    cbPreviewSelect.setChecked(true);
                }else {
                    cbPreviewSelect.setChecked(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @Override
    public void setClickEnabledStates(boolean states) {

    }

    @Override
    public void initChildData(Bundle savedInstanceState) {
        windowWidth = SystemInfoUtils.getInstance(getContext()).getWindowWidth();
        windowHeight = SystemInfoUtils.getInstance(getContext()).getWindowHeight();
        allMapList = DbPhonePicturesList.getInstance(getContext()).getAllMapList();
        allList = DbPhonePicturesList.getInstance(getContext()).getAllList(allMapList);
        //初始化当前正在显示的文件夹的图片列表
        nowDirectoryPicturesList = new ArrayList<>();
        nowDirectoryPicturesList.addAll(allList);
        //初始化切换文件夹的文件夹列表数据
        allKeyDirectoryList = ParamsAndJudgeUtils.getInstance().paramsHashMapKeyToArrayList(allMapList);
        allKeyDirectoryList.add(0, allPictureTag);
        //初始化初始默认参数
        nowDirectory = allKeyDirectoryList.get(0);
        btnNowDirectory.setText(ParamsAndJudgeUtils.getInstance().getLastDirctoryName(nowDirectory));
        //初始化预览视图数据以及操作
        List<String> imageViews = new ArrayList<>();
        for (StorePictureItemDto itemDto : allList) {
            if (itemDto != null && itemDto.absolutePath != null) {
                imageViews.add(itemDto.absolutePath);
            }
        }


        if(OpenPictureSelectUtils.getConfig() != null) {
            config = OpenPictureSelectUtils.getConfig();
        }
        //初始化列表显示列数
        if(config != null) {
            recySelects.setLayoutManager(new StaggeredGridLayoutManager(config.getPictureSelectListColumnNum(), StaggeredGridLayoutManager.VERTICAL));
        }else {
            recySelects.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }



        //初始化图片列表数据
        pictureSelectsAdapter = new PictureSelectsAdapter(getContext(),config, allList, listViewImageLoadingTag, pictureSelectAdapterCallback);
        recySelects.setAdapter(pictureSelectsAdapter);
        recyChangeDirectory.setAdapter(new PictureSelectsChangeDirctoryAdapter(getContext(), allKeyDirectoryList, pictureSelectChangeDicetoryAdapterCallback));
        //初始化预览视图
        vpgShow.setAdapter(new IntruderViewPagerAdapter(getContext(), imageViews));

        if(config != null){
            //标题栏
            if(config.getPictureSelectActionBarView() != null){
                int length = relSelectActionBar.getChildCount();
                for(int i = 0 ; i < (length) ; i++){
                    relSelectActionBar.getChildAt(i).setVisibility(View.GONE);
                }
                relSelectActionBar.addView(config.getPictureSelectActionBarView());
            }
            //底部操作栏
            if(config.getPictureSelectBottomOptionsView() != null){
                int length = relBottomOptions.getChildCount();
                for(int i = 0 ; i < (length) ; i++){
                    relBottomOptions.getChildAt(i).setVisibility(View.GONE);
                }
                relBottomOptions.addView(config.getPictureSelectBottomOptionsView());
            }
            //预览顶部操作栏
            if(config.getPictureSelectPreviewActionBarView() != null){
                PercentRelativeLayout relPreviewActionBar = (PercentRelativeLayout) dialogPreviewView.findViewById(R.id.relPreviewActionBar);
                int length = relPreviewActionBar.getChildCount();
                for(int i = 0 ; i < (length) ; i++){
                    relPreviewActionBar.getChildAt(i).setVisibility(View.GONE);
                }
                relPreviewActionBar.addView(config.getPictureSelectPreviewActionBarView());
            }
            //预览底部操作栏
            if(config.getPictureSelectBottomOptionsView() != null){
                PercentRelativeLayout relPreviewBottomOptions = (PercentRelativeLayout) dialogPreviewView.findViewById(R.id.relPreviewBottomOptions);
                int length = relPreviewBottomOptions.getChildCount();
                for(int i = 0 ; i < (length) ; i++){
                    relPreviewBottomOptions.getChildAt(i).setVisibility(View.GONE);
                }
                relPreviewBottomOptions.addView(config.getPictureSelectBottomOptionsView());
            }
            //切换当前正在显示的文件夹监听
            if(config.getChangeNowDirectoryOnClickListener() != null){
                config.getChangeNowDirectoryOnClickListener().setOnClickListener(changeNowDirectoryOnClickListener);
            }
            //发送按钮点击事件
            if(config.getSendPictureOnClickListener() != null){
                config.getSendPictureOnClickListener().setOnClickListener(selectNowPreviewPictureOnClick);
            }
            //点击预览已经选择的
            if(config.getGoToPreviewOnClickListener() != null){
                config.getGoToPreviewOnClickListener().setOnClickListener(previewSelectedListOnClick);
            }
            //当前正在预览的图片选中与未选中点击事件
            if(config.getSelectNowPreviewPictureOnClickListener() != null){
                config.getSelectNowPreviewPictureOnClickListener().setOnClickListener(selectNowPreviewPictureOnClick);
            }
            //预览窗口后退点击事件
            if(config.getPreviewBackOnClickListener() != null){
                config.getPreviewBackOnClickListener().setOnClickListener(previewBackOnClick);
            }
            //设置图片未选中时的图标
            if(config.getPictureSelectItemStateIconIdSelectedN() != null){
                pictureSelectStateN = config.getPictureSelectItemStateIconIdSelectedN();
            }
            //设置图片已选中时的图标
            if(config.getPictureSelectItemStateIconIdSelectedY() != null){
                pictureSelectStateY = config.getPictureSelectItemStateIconIdSelectedY();
            }
            //设置最大选择数量
            if(config.getPictureSelectListMaxSelectedNum() != null){
                maxSelectedPictureNum = config.getPictureSelectListMaxSelectedNum();
            }
            //设置当前正在显示的文件夹，使用回调传回
           config.setChangeNowDirectoryOnClickListener(new PictureAppSetNowDirectoryCallback() {
               @Override
               public void changeToDirectory(String directory) {
                   setNowDirectoryItemList(allList,allMapList,directory);
               }
           });
            //设置拍照之后的图片存储文件夹
            cameraPhotographSaveDirectory = config.getCameraPhotographSaveDirectory();

            config.getPictureSelectDataListChangeCallback().initialDataAll(allMapList);
            config.getPictureSelectDataListChangeCallback().initialDataAllDirectoryPath(allKeyDirectoryList);
            config.getPictureSelectDataListChangeCallback().nowDirectoryChangeList(nowDirectory,nowDirectoryPicturesList);
            config.getPictureSelectDataListChangeCallback().selectedPicturesChangeList(selectedPicturesList);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setClickEnabledStates(true);
        if(data != null && resultCode == RESULT_OK){
            switch (requestCode){
                case forCameraRequestCode:
                    String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式

                    FileOutputStream b = null;
                    //???????????????????????????????为什么不能直接保存在系统相册位置呢？？？？？？？？？？？？
                    File file = new File(cameraPhotographSaveDirectory);
                    if(!file.exists()) {
                        file.mkdirs();// 创建文件夹
                    }
                    String fileName = cameraPhotographSaveDirectory + name;

                    try {
                        b = new FileOutputStream(fileName);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            b.flush();
                            b.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //更新
                    updateCameraPhotograph(fileName,cameraPhotographSaveDirectory);
                    break;
            }
        }
    }

    /**
     * 更新媒体库
     * @param filename filename是我们的文件全名，包括后缀哦
     * @param directoryPath 文件路径文件夹
     */
    private void updateCameraPhotograph(final String filename, final String directoryPath) {


        final Handler handlerUpDataUI = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //更新数据
                allMapList = DbPhonePicturesList.getInstance(getContext()).getAllMapList();
                allList = DbPhonePicturesList.getInstance(getContext()).getAllList(allMapList);
                if(!allKeyDirectoryList.toString().contains(directoryPath.toString())){
                    allKeyDirectoryList.add(directoryPath);
                    recyChangeDirectory.setAdapter(new PictureSelectsChangeDirctoryAdapter(getContext(), allKeyDirectoryList, pictureSelectChangeDicetoryAdapterCallback));
                }

                if(nowDirectory.equals(directoryPath) || nowDirectory.equals(allPictureTag)){
                    nowDirectoryPicturesList.clear();
                    nowDirectoryPicturesList = new ArrayList<StorePictureItemDto>();
                    nowDirectoryPicturesList.addAll(allMapList.get(directoryPath));
                }

                //回传刷新数据
                if(config.getPictureSelectDataListChangeCallback() != null){
                    config.getPictureSelectDataListChangeCallback().initialDataAll(allMapList);
                    config.getPictureSelectDataListChangeCallback().initialDataAllDirectoryPath(allKeyDirectoryList);
                    config.getPictureSelectDataListChangeCallback().nowDirectoryChangeList(nowDirectory,nowDirectoryPicturesList);
                    config.getPictureSelectDataListChangeCallback().selectedPicturesChangeList(selectedPicturesList);
                }

                StorePictureItemDto newDto = null;
                for(StorePictureItemDto itemDto : allMapList.get(directoryPath)){
                    if(itemDto.absolutePath.equals(filename)){
                        newDto = itemDto;
                        break;
                    }
                }
                //拍照之后的存储路径为当前正在显示的
                pictureSelectsAdapter.addItemDto(newDto,0);

                //更新选中列表
                if(nowDirectory.equals(directoryPath) || nowDirectory.equals(allPictureTag)){
                    setSelect(newDto,true,0);
                }else {
                    setSelect(newDto,true,-1);
                }
            }
        };

        MediaScannerConnection.scanFile(this,
                new String[] { filename }, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        handlerUpDataUI.sendMessage(Message.obtain());
                    }
                });
    }

    //图片选择适配器回调接口
    private RecycleViewAdapterCallback pictureSelectAdapterCallback = new RecycleViewAdapterCallback() {
        @Override
        public void setDataAndView(RecyclerView.ViewHolder holder, final int postion) {
            final View imageView = holder.itemView.findViewById(R.id.imageView);
            ImageView imageViewSelectState = (ImageView) holder.itemView.findViewById(R.id.imageViewSelectState);

            StorePictureItemDto itemDto = null;
            if(config == null || config.isWhetherNeedCamera()){
                if(postion == 0){
                    imageViewSelectState.setVisibility(View.GONE);
                }else {
                    imageViewSelectState.setVisibility(View.VISIBLE);
                    itemDto = nowDirectoryPicturesList.get(postion - 1);
                }
            }else {
                imageViewSelectState.setVisibility(View.VISIBLE);
                itemDto = nowDirectoryPicturesList.get(postion);
            }


            if(itemDto != null) {
                if (selectedPicturesList.toString().contains(itemDto.toString())) {
                    imageViewSelectState.setImageResource(pictureSelectStateY);
                } else {
                    imageViewSelectState.setImageResource(pictureSelectStateN);
                }
                final StorePictureItemDto finalItemDto = itemDto;
                imageViewSelectState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (finalItemDto != null) {
                            //如果已经有数据那就是要移除数据，否则就是要添加数据
                            setSelect(finalItemDto, !selectedPicturesList.toString().contains(finalItemDto.toString()), new Integer(postion));
                        }
                    }
                });
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(config != null && !config.isWhetherNeedCamera()){
                        showPreview(nowDirectoryPicturesList,postion);
                    }else {
                        if(postion == 0){
                            openCameraPhotograph.onClick(imageView);
                        }else {
                            showPreview(nowDirectoryPicturesList,postion);
                        }
                    }

                }
            });
        }
    };
    //图片文件夹选择适配器回调接口
    private RecycleViewAdapterCallback pictureSelectChangeDicetoryAdapterCallback = new RecycleViewAdapterCallback() {
        @Override
        public void setDataAndView(RecyclerView.ViewHolder holder, final int postion) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(allKeyDirectoryList != null && allKeyDirectoryList.get(postion) != null) {
                        setNowDirectoryItemList(allList,allMapList,allKeyDirectoryList.get(postion));
                    }

                    if(popupWindowShowChangeDirectory != null && popupWindowShowChangeDirectory.isShowing()){
                        popupWindowShowChangeDirectory.dismiss();
                    }
                }
            });
        }
    };

    /**
     * 设置当前正在显示的文件夹的数据
     * @param nowDirectory
     */
    private void setNowDirectoryItemList(List<StorePictureItemDto> allList,Map<String,List<StorePictureItemDto>> allMapList, String nowDirectory){
        if(allKeyDirectoryList.toString().contains(nowDirectory.toString())){
            nowDirectoryPicturesList =  new ArrayList<StorePictureItemDto>();
            if(!this.nowDirectory.equals(nowDirectory)){
                if(nowDirectory.equals(allPictureTag)){
                    nowDirectoryPicturesList.addAll(allList);
                }else {
                    nowDirectoryPicturesList.addAll(DbPhonePicturesList.getInstance(getContext()).getList(allMapList,nowDirectory));
                }

                this.nowDirectory = nowDirectory;
                pictureSelectsAdapter.setList(nowDirectoryPicturesList);
                btnNowDirectory.setText(ParamsAndJudgeUtils.getInstance().getLastDirctoryName(nowDirectory));
            }
        }

        if(config != null){
            config.getPictureSelectDataListChangeCallback().nowDirectoryChangeList(nowDirectory,nowDirectoryPicturesList);
        }
    }
    /**
     * 显示预览窗口
     * @param postion
     */
    private void showPreview(List<StorePictureItemDto> list, int postion){
        if(dialogPreview != null){
            if(dialogPreview.isShowing()){
                dialogPreview.dismiss();
            }else {
                int posi = 0;
                if(config != null && !config.isWhetherNeedCamera()){
                    posi = postion;
                }else {
                    if (list.toString().contains(nowDirectoryPicturesList.toString()) && postion > 0) {
                        posi = postion - 1;
                    } else {
                        posi = postion;
                    }
                }
                List<String> imageViews = new ArrayList<>();
                for (StorePictureItemDto itemDto : list) {
                    if (itemDto != null && itemDto.absolutePath != null) {
                        imageViews.add(itemDto.absolutePath);
                    }
                }
                vpgShow.setAdapter(new IntruderViewPagerAdapter(getContext(), imageViews));
                vpgShow.setCurrentItem(posi);
                //每次显示之前需要重新判定所要显示的图片是否被选中
                if (selectedPicturesList.toString().contains(list.get(posi).toString())) {
                    cbPreviewSelect.setChecked(true);
                } else {
                    cbPreviewSelect.setChecked(false);
                }
                dialogPreview.show();
            }
        }
    }

    /**
     * 设置选中的数据并更新要显示的数据
     * @param selectDto 当前被选中的图片信息
     * @param selectState 是否被选中
     * @param postion 更新位置，位置小于0的话那么就不更新适配器
     */
    private void setSelect(StorePictureItemDto selectDto, boolean selectState, int postion){
        //判定传入的数据是否为空
        if(selectDto == null ){
            return;
        }
        //先判定是否大于最大的选择图片的数量
        if(selectedPicturesList.size() >= maxSelectedPictureNum && selectState){
            DialogUtils.showToastShort(getContext(),R.string.toast_hint_exceed_max_selected_num);
            return;
        }
        if(selectState){
            if(!selectedPicturesList.toString().contains(selectDto.toString())){
                selectedPicturesList.add(selectDto);
                if(postion >= 0) {
                    pictureSelectsAdapter.modifySelectState(selectDto, postion);//之所以加1是由于选择界面有个拍照，而预览里是没有的
                }
            }
        }else {
            if(selectedPicturesList.toString().contains(selectDto.toString())){
                int length = selectedPicturesList.size();
                for(int i = 0 ; i < length ; i++){
                    if(selectedPicturesList.get(i).toString().equals(selectDto.toString())){
                        selectedPicturesList.remove(i);
                        break;
                    }
                }
                if(postion >= 0) {
                    pictureSelectsAdapter.modifySelectState(selectDto, postion);//之所以加1是由于选择界面有个拍照，而预览里是没有的
                }
            }
        }

        StringBuffer selectedNum = new StringBuffer("");
        if(selectedPicturesList.size() > 0) {
            selectedNum.append("(");
            selectedNum.append(selectedPicturesList.size());
            selectedNum.append("/");
            selectedNum.append(maxSelectedPictureNum);
            selectedNum.append(")");
        }

        btnPreview.setText(getString(R.string.preview) + selectedNum.toString());
        btnSelectSend.setText(getString(R.string.send) + selectedNum.toString());
        btnPreviewSend.setText(getString(R.string.send) + selectedNum.toString());

        if(config != null){
            config.getPictureSelectDataListChangeCallback().selectedPicturesChangeList(selectedPicturesList);
        }
    }

    //切换当前正在显示的文件夹按钮，并弹出显示选择
    private View.OnClickListener changeNowDirectoryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config != null && config.getChangeNowDirectoryOnClickListener() != null){
                config.getChangeNowDirectoryOnClickListener().onClick(nowDirectoryPicturesList);
            }else if(allMapList != null){
                popupWindowShowChangeDirectory = DialogUtils.showPopupDropDown(getContext(), relBottomOptions, recyChangeDirectory, windowWidth, (int) (windowWidth * 1.3), 0, 0, null);
            }
        }
    };
    //发送选择好的图片
    private View.OnClickListener sendSelectedListClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(config != null && config.getSendPictureOnClickListener() != null){
                config.getSendPictureOnClickListener().onClick(selectedPicturesList);
            }
            //后退消除
            onBackPressed();
        }
    };

    //选中当前预览的图片
    private View.OnClickListener selectNowPreviewPictureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //在cbPreviewSelect状态变更之后调用click方法
            StorePictureItemDto nowItemDto = nowDirectoryPicturesList.get(vpgShow.getCurrentItem());
            setSelect(nowItemDto,cbPreviewSelect.isChecked(),new Integer(vpgShow.getCurrentItem() + 1));
        }
    };

    //预览后退
    private View.OnClickListener previewBackOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(dialogPreview != null && dialogPreview.isShowing()){
                dialogPreview.dismiss();
            }
        }
    };

    //预览已经选择的
    private View.OnClickListener previewSelectedListOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selectedPicturesList.size() == 0){
                DialogUtils.showToastShort(getContext(),R.string.toast_hint_no_select_picture);
            }else {
                showPreview(selectedPicturesList, 0);
            }
        }
    };
    //开启拍照
    private View.OnClickListener openCameraPhotograph = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            perissionRequest(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    };

    @Override
    public void perissionRequestSuccessCallback(String[] perissions) {
        super.perissionRequestSuccessCallback(perissions);
        if(perissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) || perissions[1].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            setClickEnabledStates(false);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, forCameraRequestCode);
        }
    }

    @Override
    public void onBackPressed() {
        if(dialogPreview != null && dialogPreview.isShowing()){
            dialogPreview.dismiss();
        }
        super.onBackPressed();
    }
}
