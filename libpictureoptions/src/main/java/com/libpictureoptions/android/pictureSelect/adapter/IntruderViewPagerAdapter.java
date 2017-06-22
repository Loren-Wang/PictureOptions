package com.libpictureoptions.android.pictureSelect.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.utils.imageOptions.PicassoLoadingUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wangliang on 0016/2017/3/16.
 * 创建时间： 0016/2017/3/16 11:49
 * 创建人：王亮（Loren wang）
 * 功能作用：
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class IntruderViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mDrawableResIdList;

    public IntruderViewPagerAdapter(Context context, List<String> resIdList) {
        super();
        mContext = context;
        mDrawableResIdList = resIdList;
    }



    @Override
    public int getCount() {
        if (mDrawableResIdList != null) {
            return mDrawableResIdList.size();
        }
        return 0;
    }

    @Override
    public int getItemPosition(Object object) {
        if (object != null && mDrawableResIdList != null) {
            Integer resId = (Integer)((ImageView)object).getTag();
            if (resId != null) {
                for (int i = 0; i < mDrawableResIdList.size(); i++) {
                    if (resId.equals(mDrawableResIdList.get(i))) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    @Override
    public Object instantiateItem(View container, int position) {
        if (mDrawableResIdList != null && position < mDrawableResIdList.size()) {
            String path = mDrawableResIdList.get(position);
            if (path != null) {
                ImageView itemView = new ImageView(mContext);
                itemView.setImageResource(R.drawable.icon_camera);
                PicassoLoadingUtils.getInstance(mContext).loadingLocal(
                        path,itemView,0,0,false,path);
                Picasso.with(mContext).resumeTag(path);
                //此处假设所有的照片都不同，用resId唯一标识一个itemView；也可用其它Object来标识，只要保证唯一即可
                itemView.setTag(path);

                ((ViewPager) container).addView(itemView);
                return itemView;
            }
        }
        return null;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        //注意：此处position是ViewPager中所有要显示的页面的position，与Adapter mDrawableResIdList并不是一一对应的。
        //因为mDrawableResIdList有可能被修改删除某一个item，在调用notifyDataSetChanged()的时候，ViewPager中的页面
        //数量并没有改变，只有当ViewPager遍历完自己所有的页面，并将不存在的页面删除后，二者才能对应起来
        if (object != null) {
            ViewGroup viewPager = ((ViewGroup) container);
            int count = viewPager.getChildCount();
            for (int i = 0; i < count; i++) {
                View childView = viewPager.getChildAt(i);
                if (childView == object) {
                    Picasso.with(mContext).pauseTag(childView.getTag());
                    viewPager.removeView(childView);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public void startUpdate(View container) {
    }
    @Override
    public void finishUpdate(View container) {
    }

}
