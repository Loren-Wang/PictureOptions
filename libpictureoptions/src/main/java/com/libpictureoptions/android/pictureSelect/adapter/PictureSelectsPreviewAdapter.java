package com.libpictureoptions.android.pictureSelect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.utils.SystemInfoUtils;
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

public class PictureSelectsPreviewAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<StorePictureItemDto> list;
    private String listViewImageLoadingTag;
    private LayoutInflater inflater;
    private int windowWidth;
    private RecycleViewAdapterCallback pictureSelectAdapterCallback;

    public PictureSelectsPreviewAdapter(Context context, List<StorePictureItemDto> list, String listViewImageLoadingTag, RecycleViewAdapterCallback pictureSelectAdapterCallback) {
        this.context = context;
        this.list = new ArrayList<>();
        if(list != null) {
            this.list.addAll(list);
        }
        this.listViewImageLoadingTag = listViewImageLoadingTag;
        this.pictureSelectAdapterCallback = pictureSelectAdapterCallback;
        inflater = LayoutInflater.from(context);
        windowWidth = SystemInfoUtils.getInstance(context).getWindowWidth();
    }

    public PictureSelectsPreviewAdapter setList(List<StorePictureItemDto> list) {
        if(list != null) {
            this.list.removeAll(this.list);
            this.list = new ArrayList<>();
            this.list.addAll(list);
            notifyDataSetChanged();
        }
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_list_picture_selects,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Holder) holder).imageViewSelectState.setVisibility(View.GONE);
        ((Holder) holder).imageView.setImageResource(R.mipmap.ic_launcher);
//        if(position == 0){
//            PicassoLoadingUtils.getInstance(context).loadingRes(R.drawable.icon_camera, ((Holder) holder).imageView, (int) (windowWidth * 0.34), (int) (windowWidth * 0.34));
//        }else {
//            StorePictureItemDto itemDto = list.get(position - 1);
//            if (itemDto != null && itemDto.absolutePath != null) {
//                PicassoLoadingUtils.getInstance(context).loadingLocalCenterCrop
//                        (itemDto.absolutePath, ((Holder) holder).imageView, (int) (windowWidth * 0.34), (int) (windowWidth * 0.34), listViewImageLoadingTag);
//            }
//        }


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
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
