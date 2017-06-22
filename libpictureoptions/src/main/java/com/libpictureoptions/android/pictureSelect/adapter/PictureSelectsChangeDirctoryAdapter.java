package com.libpictureoptions.android.pictureSelect.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.utils.SystemInfoUtils;
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

public class PictureSelectsChangeDirctoryAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<String> list;
    private LayoutInflater inflater;
    private int windowWidth;
    private RecycleViewAdapterCallback pictureSelectAdapterCallback;

    public PictureSelectsChangeDirctoryAdapter(Context context, List<String> list, RecycleViewAdapterCallback pictureSelectAdapterCallback) {
        this.context = context;
        this.list = new ArrayList<>();
        if(list != null) {
            this.list.addAll(list);
        }
        this.pictureSelectAdapterCallback = pictureSelectAdapterCallback;
        inflater = LayoutInflater.from(context);
        windowWidth = SystemInfoUtils.getInstance(context).getWindowWidth();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(inflater.inflate(R.layout.item_list_picture_selects_change_dicetory,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(list != null && list.get(position) != null) {
            ((Holder) holder).tvDirectory.setText(list.get(position));
        }
        if(pictureSelectAdapterCallback != null){
            pictureSelectAdapterCallback.setDataAndView(holder,position);
        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView tvDirectory;
        public Holder(View itemView) {
            super(itemView);
            tvDirectory = (TextView) itemView.findViewById(R.id.tvDirectory);
        }
    }
}
