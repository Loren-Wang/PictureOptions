package com.libpictureoptions.android.pictureCamera.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.libpictureoptions.android.pictureCamera.interface_and_abstract.VideoTranscribeStatueCallBack;

/**
 * Created by wangliang on 0023/2017/6/23.
 * 创建时间： 0023/2017/6/23 10:13
 * 创建人：王亮（Loren wang）
 * 功能作用：仿微信的点击拍照长按录制小视频的按钮ui控件，
 * 思路：
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class WxTakePictureOrVideoUIView extends WxTakePictureOrVideoView{
    private Context context;
    private Paint paintDown;//底层圆形画笔
    private Paint paintUp;//顶层圆形画笔
    private Paint paintProgress;//进度画笔
    private float downRadius;// 底层圆形半径
    private float upRadius;//顶层圆形半径
    private int viewHeight;//视图高度，和宽度相同
    private int viewWidth;//视图宽度，和宽度相同
    private int progressWidth;//进度条宽度
    private int progressColor;//进度条颜色
    private int downColor;//底部圆环颜色
    private int upColor;//顶层圆形颜色
    private TimeControlChartAnimation enlargementAnim;//放大的计时动画
    private TimeControlChartAnimation shrinkAnim;//收缩的计时动画
    private int enlargementTime;//放大所要用到的时间
    private int shrinkTime;//缩小所要用到的时间
    private Float nowProgress;//当前录制进度

    private static final int UP_DATA_UI_START_ENLARGEMENT_ANIM = 0;
    private static final int UP_DATA_UI_START_SHRINK_ANIM = 1;
    private static final int UP_DATA_UI_PROGRESS = 2;

    private Handler handlerUpDataUi = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg != null){
                switch (msg.what){
                    case UP_DATA_UI_START_ENLARGEMENT_ANIM://开始放大动画
                        if(enlargementAnim != null) {
                            nowProgress = 0f;
                            startAnimation(enlargementAnim);
                        }
                        break;
                    case UP_DATA_UI_START_SHRINK_ANIM://开始缩小动画
                        if(enlargementAnim != null) {
                            nowProgress = 0f;
                            startAnimation(shrinkAnim);
                        }
                        break;
                    case UP_DATA_UI_PROGRESS://更新进度
                        postInvalidate();
                        break;
                }
            }
        }
    };

    public WxTakePictureOrVideoUIView(Context context) {
        super(context);
        init(context);
    }

    public WxTakePictureOrVideoUIView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WxTakePictureOrVideoUIView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(final Context context){
        this.context = context;

        downRadius = 0;// 底层圆形半径
        upRadius = 0;//顶层圆形半径
        viewHeight = 0;//视图高度，和宽度相同
        viewWidth = 0;//视图宽度，和宽度相同
        progressWidth = 0;//进度条宽度
        progressColor = Color.RED;//进度条颜色
        downColor = Color.parseColor("#88ffffff");//底部圆环颜色
        upColor = Color.WHITE;//顶层圆形颜色
        enlargementTime = 500;//放大所要用到的时间
        shrinkTime = 500;//缩小所要用到的时间
        nowProgress = 0f;

        paintDown = new Paint();
        paintDown.setAntiAlias(true);
        paintDown.setColor(downColor);

        paintUp = new Paint(paintDown);
        paintUp.setColor(upColor);

        paintProgress = new Paint();
        paintProgress.setAntiAlias(true);
        paintProgress.setStrokeWidth(progressWidth);
        paintProgress.setColor(progressColor);
        paintProgress.setStyle(Paint.Style.STROKE);


        addVideoTranscribeStatueCallBack(new VideoTranscribeStatueCallBack(videoMaxTime) {
            @Override
            public void start() {
                if(viewWidth != 0) {
                    Message message = Message.obtain();
                    message.what = UP_DATA_UI_START_ENLARGEMENT_ANIM;
                    handlerUpDataUi.sendMessage(message);
                }
            }

            @Override
            public void stop() {
                if(viewWidth != 0){
                    Message message = Message.obtain();
                    message.what = UP_DATA_UI_START_SHRINK_ANIM;
                    handlerUpDataUi.sendMessage(message);
                }
            }

            @Override
            public void onProgress(Double progress) {
                if(viewWidth != 0 && progress != null){
                    nowProgress = progress.floatValue();
                    Message message = Message.obtain();
                    message.what = UP_DATA_UI_PROGRESS;
                    handlerUpDataUi.sendMessage(message);
                }
            }
        });

        enlargementAnim = new TimeControlChartAnimation(this, new TimeControlChartAnimation.SetPercentCallbackListener() {
            @Override
            public void percent(float interpolatedTime) {
                Log.e(TAG, "enlargement:::" + String.valueOf(Double.valueOf(interpolatedTime)));
                if(interpolatedTime <= 1 && interpolatedTime >= 0){//interpolatedTime：根据动画的显示时间返回0-1之间的值，未显示为0，显示完全为1
                    downRadius = (float) (viewWidth / 3.0 + interpolatedTime * viewWidth / 6.0);
                    upRadius = (float) (viewWidth / 6.0 - interpolatedTime * viewWidth / 24.0);
                    postInvalidate();
                }
            }
        });
        enlargementAnim.setDuration(enlargementTime);
        shrinkAnim = new TimeControlChartAnimation(this, new TimeControlChartAnimation.SetPercentCallbackListener() {
            @Override
            public void percent(float interpolatedTime) {
                Log.e(TAG, "enlargement:::" + String.valueOf(Double.valueOf(interpolatedTime)));
                if(interpolatedTime <= 1 && interpolatedTime >= 0){//interpolatedTime：根据动画的显示时间返回0-1之间的值，未显示为0，显示完全为1
                    downRadius = (float) (viewWidth / 2.0 - interpolatedTime * viewWidth / 6.0);
                    upRadius = (float) (viewWidth / 8.0 + interpolatedTime * viewWidth / 24.0);
                    postInvalidate();
                }
            }
        });
        shrinkAnim.setDuration(shrinkTime);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(viewHeight == 0 || viewWidth == 0){
            if(getWidth() != 0){
                viewWidth = getWidth();
            }
            if(getMeasuredWidth() != 0){
                viewWidth = getMeasuredWidth();
            }
            if(getHeight() != 0){
                viewHeight = getHeight();
            }
            if(getMeasuredHeight() != 0){
                viewHeight = getMeasuredHeight();
            }
            viewWidth = viewHeight = Math.min(viewWidth, viewHeight);

            if(viewWidth != 0){
                downRadius = (float) (viewWidth / 3.0);
                upRadius = (float) (viewWidth / 6.0);
                postInvalidate();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(viewWidth != 0 && viewHeight != 0) {
            canvas.drawCircle((float) (viewWidth / 2.0), (float) (viewHeight / 2.0), downRadius, paintDown);
            canvas.drawCircle((float) (viewWidth / 2.0), (float) (viewHeight / 2.0), upRadius, paintUp);
            canvas.drawArc(0,0,viewWidth,viewHeight,-90,nowProgress * 360,false,paintProgress);
        }
    }

    /**
     * 设置进度条宽度
     * @param progressWidth
     * @return
     */
    public WxTakePictureOrVideoUIView setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
        if(paintProgress != null){
            paintProgress.setStrokeWidth(progressWidth);
        }
        return this;
    }

    /**
     * 设置进度条颜色
     * @param progressColor
     * @return
     */
    public WxTakePictureOrVideoUIView setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        if(paintProgress != null){
            paintProgress.setColor(progressColor);
        }
        return this;
    }

    /**
     * 设置底部圆形颜色
     * @param downColor
     * @return
     */
    public WxTakePictureOrVideoUIView setDownColor(int downColor) {
        this.downColor = downColor;
        if(paintDown != null){
            paintDown.setColor(downColor);
        }
        return this;
    }

    /**
     * 设置顶层圆形颜色
     * @param upColor
     * @return
     */
    public WxTakePictureOrVideoUIView setUpColor(int upColor) {
        this.upColor = upColor;
        if(paintUp != null){
            paintUp.setColor(upColor);
        }
        return this;
    }

    /**
     * 设置放大动画
     * @param enlargementTime
     * @return
     */
    public WxTakePictureOrVideoUIView setEnlargementTime(int enlargementTime) {
        this.enlargementTime = enlargementTime;
        if(enlargementAnim != null){
            enlargementAnim.setDuration(enlargementTime);
        }
        return this;
    }

    /**
     * 设置收缩动画时间
     * @param shrinkTime
     * @return
     */
    public WxTakePictureOrVideoUIView setShrinkTime(int shrinkTime) {
        this.shrinkTime = shrinkTime;
        if(shrinkAnim != null){
            shrinkAnim.setDuration(shrinkTime);
        }
        return this;
    }
}
