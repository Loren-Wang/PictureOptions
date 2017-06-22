package com.libpictureoptions.android.pictureSelect.interface_and_abstract;


import com.libpictureoptions.android.pictureSelect.dto.StorePictureItemDto;

import java.util.List;
import java.util.Map;

/**
 * Created by wangliang on 0020/2017/3/20.
 * 创建时间： 0020/2017/3/20 17:45
 * 创建人：王亮（Loren wang）
 * 功能作用：图片选择框架数据改变回调
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public interface PictureSelectDataListChangeCallback {
    //所有的数据集合
    void initialDataAll(Map<String, List<StorePictureItemDto>> allMapList);
    //所有的文件夹名称
    void initialDataAllDirectoryPath(List<String> allDirectoryList);
    //当前正在显示的文件夹
    void nowDirectoryChangeList(String nowDirectory, List<StorePictureItemDto> nowDirectoryPicturesList);
    //选中列表发生改变
    void selectedPicturesChangeList(List<StorePictureItemDto> nowDirectoryPicturesList);
}
