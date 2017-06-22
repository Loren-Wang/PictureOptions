package com.libpictureoptions.android.pictureSelect.dto;

/**
 * Created by wangliang on 0013/2017/3/13.
 * 创建时间： 0013/2017/3/13 16:28
 * 创建人：王亮（Loren wang）
 * 功能作用：解析系统是图片数据库的实体类
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：之所以不使用get、set方法是因为感觉方法数过多，少的实体类还好，多了就完了，多了很容易触发64k的，
 *      所以直接把内容暴露出来，虽然安全性有所降低吧，但是在使用的时候判一下null就能避免一些了
 */
public class StorePictureItemDto {
    public Integer _id;//主键id
    public String directoryPath;//文件所在的文件夹
    public String absolutePath;//绝对路径
    public Long size;//文件大小
    public String fileName;//文件名称
    public String mimeType;//文件类型（image/png or image/jpeg or ...）
    public Long timeAdd;//添加进入系统数据库的时间（毫秒值）
    public Long timeModify;//图片最后的修改时间
    public Double lat;//拍摄的照片的纬度坐标
    public Double lng;//拍摄的照片的经度坐标
    public Integer orientation;//旋转角度
    public Integer width;//图片高度
    public Integer height;//图片宽度

    @Override
    public String toString() {
        return "StorePictureItemDto{" +
                "_id='" + _id + '\'' +
                ", directoryPath='" + directoryPath + '\'' +
                ", absolutePath='" + absolutePath + '\'' +
                ", size=" + size +
                ", fileName='" + fileName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", timeAdd=" + timeAdd +
                ", timeModify=" + timeModify +
                ", lat=" + lat +
                ", lng=" + lng +
                ", orientation=" + orientation +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
