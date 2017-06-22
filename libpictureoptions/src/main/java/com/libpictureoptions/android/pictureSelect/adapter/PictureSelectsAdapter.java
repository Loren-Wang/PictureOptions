package com.libpictureoptions.android.pictureSelect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.utils.SystemInfoUtils;
import com.libpictureoptions.android.common.utils.imageOptions.PicassoLoadingUtils;
import com.libpictureoptions.android.pictureSelect.PictureSelectConfig;
import com.libpictureoptions.android.pictureSelect.dto.StorePictureItemDto;
import com.libpictureoptions.android.pictureSelect.interface_and_abstract.RecycleViewAdapterCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 16:02
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class PictureSelectsAdapter extends RecyclerView.Adapter {
    private Context context;
    private PictureSelectConfig config;
    private List<StorePictureItemDto> list;
    private String listViewImageLoadingTag;
    private LayoutInflater inflater;
    private int windowWidth;
    private RecycleViewAdapterCallback pictureSelectAdapterCallback;

    public PictureSelectsAdapter(Context context,
                                 PictureSelectConfig config,
                                 List<StorePictureItemDto> list,
                                 String listViewImageLoadingTag,
                                 RecycleViewAdapterCallback pictureSelectAdapterCallback) {
        this.context = context;
        this.config = config;
        this.list = new ArrayList<>();
        if(list != null) {
            this.list.addAll(list);
        }
        this.listViewImageLoadingTag = listViewImageLoadingTag;
        this.pictureSelectAdapterCallback = pictureSelectAdapterCallback;
        inflater = LayoutInflater.from(context);
        windowWidth = SystemInfoUtils.getInstance(context).getWindowWidth();
    }

    public PictureSelectsAdapter setList(List<StorePictureItemDto> list) {
        if(list != null) {
            this.list.removeAll(this.list);
            this.list = new ArrayList<>();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
        return this;
    }

    public void addItemDto(StorePictureItemDto itemDto,int position){
        if(itemDto != null){
            if(this.list == null){
                this.list = new ArrayList<>();
            }
            this.list.add(position,itemDto);
            notifyDataSetChanged();
        }
    }

    /**
     * 修改莫一项的状态
     * @param storePictureItemDto
     * @param position
     */
    public void modifySelectState(StorePictureItemDto storePictureItemDto, int position){
        if(this.list != null){
            if(config != null && !config.isWhetherNeedCamera()){
                this.list.set(position,storePictureItemDto);
            }else if(position != 0){//由于有拍照的功能，所以新增的数据的位置要比进行改变的位置靠前一位
                this.list.set(position - 1, storePictureItemDto);
            }
            notifyItemChanged(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_list_picture_selects,null));
    }

    private int columnNum;
    private int pictureWidthHeight;
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        columnNum = 3;//默认值是3
        if(config != null && config.getPictureSelectListColumnNum() != null){
            columnNum = config.getPictureSelectListColumnNum();
        }

        pictureWidthHeight = (int) ((1.0 / columnNum + 0.02) * windowWidth);

        if(config != null && !config.isWhetherNeedCamera()){//不需要拍照
            StorePictureItemDto itemDto = list.get(position);
            if (itemDto != null && itemDto.absolutePath != null) {
                PicassoLoadingUtils.getInstance(context).loadingLocalCenterCrop(
                        itemDto.absolutePath,
                        ((Holder) holder).imageView,
                        pictureWidthHeight,
                        pictureWidthHeight,
                        listViewImageLoadingTag);
            }
        }else {//需要拍照
            if(position == 0){
                PicassoLoadingUtils.getInstance(context).loadingRes(R.drawable.icon_camera, ((Holder) holder).imageView, pictureWidthHeight, pictureWidthHeight);
            }else {
                StorePictureItemDto itemDto = list.get(position - 1);
                if (itemDto != null && itemDto.absolutePath != null) {
                    PicassoLoadingUtils.getInstance(context).loadingLocalCenterCrop
                            (itemDto.absolutePath, ((Holder) holder).imageView, pictureWidthHeight, pictureWidthHeight, listViewImageLoadingTag);
                }
            }
        }

        if(pictureSelectAdapterCallback != null){
            pictureSelectAdapterCallback.setDataAndView(holder,position);
        }

    }

    @Override
    public int getItemCount() {
        if(config != null && !config.isWhetherNeedCamera()){//不需要拍照
            return list != null ? list.size() : 0;
        }else {//默认是需要拍照的
            return list != null ? list.size() + 1 : 1;
        }
    }

    private class Holder extends RecyclerView.ViewHolder {
        ImageView imageView;//图片展示控件
        ImageView imageViewSelectState;//图片选中状态
        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageViewSelectState = (ImageView) itemView.findViewById(R.id.imageViewSelectState);
        }
    }


}
