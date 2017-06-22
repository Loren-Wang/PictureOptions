package com.libpictureoptions.android.pictureCrop;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.libpictureoptions.android.R;
import com.libpictureoptions.android.common.BaseActivity;
import com.libpictureoptions.android.common.utils.FileIOUtils;
import com.libpictureoptions.android.common.utils.imageOptions.PicassoLoadingUtils;
import com.libpictureoptions.android.pictureCrop.view.HollowView;
import com.libpictureoptions.android.pictureCrop.view.TouchImageView;

public class PictureCropActivity extends BaseActivity {

    private TouchImageView imgCropBase;
    private HollowView hollowView;
    private Button btnCrop;
    private PictureCropConfig pictureCropConfig;

    @Override
    public void initChildView() {
        addChildView(R.layout.activitys_crop);
        imgCropBase = (TouchImageView) findViewById(R.id.imgCropBase);
        hollowView = (HollowView) findViewById(R.id.hollowView);
        btnCrop = (Button) findViewById(R.id.btnCrop);
        imgCropBase.setMaxZoom(25);

        hollowView.setHollowWidthHeight(500,500);

        hollowView.setTouchImageView(imgCropBase);

    }

    @Override
    public void initChildListener() {
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pictureCropConfig != null && pictureCropConfig.getSaveCropPicturePath() != null
                        && !"".equals(pictureCropConfig.getSaveCropPicturePath())){
                    Bitmap bitmap = hollowView.cropImageToBitmap();
                    if(bitmap != null) {
                        boolean saveBitmapState = FileIOUtils.getInstance(getContext()).saveBitmap(bitmap, pictureCropConfig.getSaveCropPicturePath());
                        if(saveBitmapState){
                            setResult(RESULT_OK);
                        }else {
                            setResult(RESULT_CANCELED);
                        }
                    }else {
                        setResult(RESULT_CANCELED);
                    }
                }else {
                    setResult(RESULT_CANCELED);
                }
                onBackPressed();
                finish();
            }
        });
    }

    @Override
    public void setClickEnabledStates(boolean states) {

    }

    @Override
    public void initChildData(Bundle savedInstanceState) {
        pictureCropConfig = OpenPictureCropUtils.getConfig();
        if(pictureCropConfig != null){
            //开始进行裁剪图片的类型的判断
            Integer cropPictureType = pictureCropConfig.getCropPictureType();
            String picturePath = pictureCropConfig.getPicturePath();
            if(cropPictureType != null && picturePath != null){
                switch (cropPictureType){
                    case PictureCropConfig.Bulid.CROP_PICTURE_TYPE_LOCAL:
                        break;
                    case PictureCropConfig.Bulid.CROP_PICTURE_TYPE_NET:
                        PicassoLoadingUtils.getInstance(getContext()).loadingNet(picturePath
                                ,null,null,imgCropBase,0,0,false);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
        finish();
    }
}
