package com.libpictureoptions.android.pictureCrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wangliang on 0019/2017/6/19.
 * 创建时间： 0019/2017/6/19 15:02
 * 创建人：王亮（Loren wang）
 * 功能作用：中心透明的view视图
 * 思路： 1、首先必备的条件是要知道空心部分的宽高以及视图的宽高还有空心的边缘线等参数；
 *       2、需要初始化空心的坐标，即左上右下的坐标，在根据这个坐标去画阴影部分以及空心边缘线部分
 *       3、空心部分的初始坐标为视图中心，事件只有在空心部分才会被拦截处理，其他部分的点击触摸事件依旧会向下分发
 *       4、由于该控件初始目的是为了截图做准备，那么就需要一个可以放大缩小的图片控件来配合，所以新增了一个方法来传入图片控件
 *       5、图片控件使用的是自定义控件，图片控件需要实现在放大缩小的过程中的监听并实时回调
 *       6、接下来该控件需要在传入图片控件后开启图片控件的放大缩小监听，在放大缩小的过程中实时获取图片控件的最大图片
 *         边界，然后判定当前选取的空心部分的边界在不在范围内，不在的话需要将空心部分移动到图片最大范围内
 *       7、在每次设置空心部分宽高的时候需要重置视图空心部分为初始位置
 * 修改人：
 * 修改时间：
 * 备注：
 */

public class HollowView extends View {
    private int hollowWidth;//空心宽度
    private int hollowHeight;//空心高度
    private int unHollowColor;//非空心部分的颜色
    private int viewWidth;//视图宽度
    private int viewHeight;//视图高度
    private Paint unHollowPaint;//非空心部分画笔
    private Paint hollowMarginLinePaint;//空心边缘线画笔
    private int hollowMarginLineWidth;//空心边缘线的宽度
    private int hollowMarginLineColor;//空心边缘线的颜色

    //空心部分边距，即位置
    private int hollowLeft;//空心部分左边距
    private int hollowTop;//空心部分上边距
    private int hollowRight;//空心部分右边距
    private int hollowBottom;//空心部分下边距

    //设置空心所能移动的最大范围
    private Rect hollowMoveMaxRect;
    private TouchImageView touchImageView;//和触摸放大view想配合使用可以设置空心所能移动的最大范围

    public HollowView(Context context) {
        super(context);
        init(context);
    }

    public HollowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HollowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        hollowWidth = 0;
        hollowHeight = 0;
        viewWidth = 0;
        viewHeight = 0;
        //初始化非空心部分
        unHollowColor = Color.parseColor("#66000000");
        unHollowPaint = new Paint();
        unHollowPaint.setColor(unHollowColor);
        //初始化空心边缘线
        hollowMarginLineWidth = 5;
        hollowMarginLinePaint = new Paint();
        hollowMarginLineColor = Color.RED;
        hollowMarginLinePaint.setColor(hollowMarginLineColor);
        hollowMarginLinePaint.setStrokeWidth(hollowMarginLineWidth);
        hollowMarginLinePaint.setAntiAlias(true);

        //初始化空心位置
        hollowLeft = 0;
        hollowTop = 0;
        hollowRight = 0;
        hollowBottom = 0;

    }

    /**
     * 设置空心的宽高
     * @param width
     * @param height
     */
    public void setHollowWidthHeight(int width,int height){
        this.hollowWidth = width;
        this.hollowHeight = height;
        resetView();
    }

    /**
     * 设置非空心部分的颜色
     * @param unHollowColor
     */
    public void setUnHollowColor(int unHollowColor) {
        this.unHollowColor = unHollowColor;
        unHollowPaint.setColor(unHollowColor);
    }

    public void setHollowMarginLineColor(int hollowMarginLineColor) {
        this.hollowMarginLineColor = hollowMarginLineColor;
        hollowMarginLinePaint.setColor(hollowMarginLineColor);
    }

    public void setHollowMarginLineWidth(int hollowMarginLineWidth) {
        this.hollowMarginLineWidth = hollowMarginLineWidth;
        hollowMarginLinePaint.setStrokeWidth(hollowMarginLineWidth);
    }

    /**
     * //和触摸放大view想配合使用可以设置空心所能移动的最大范围
     * @param touchImageView
     * @return
     */
    public HollowView setTouchImageView(final TouchImageView touchImageView) {
        this.touchImageView = touchImageView;
        if(touchImageView != null){
            setHollowMoveMaxRect(touchImageView.getImageMaxRect());
            //监听触摸视图的改变并相应改变选取框的最大范围
            touchImageView.addOnTouchImageViewListener(new TouchImageView.OnTouchImageViewListener() {
                @Override
                public void onMove() {

                }

                @Override
                public void imageChangedBorder() {
                    setHollowMoveMaxRect(touchImageView.getImageMaxRect());
                }
            });
        }
        return this;
    }

    /**
     * 重置显示view
     */
    public void resetView(){
        if(viewWidth != 0 && viewHeight != 0) {
            hollowLeft = (int) ((viewWidth - hollowWidth) / 2.0);
            hollowTop = (int) ((viewHeight - hollowHeight) / 2.0);
            hollowRight = (int) ((viewWidth - hollowWidth) / 2.0 + hollowWidth);
            hollowBottom = (int) ((viewHeight - hollowHeight) / 2.0 + hollowHeight);
            postInvalidate();
        }
    }

    /**
     * 设置图片的最大显示范围
     * @param maxRect
     */
    public void setHollowMoveMaxRect(Rect maxRect){
        if(maxRect != null && maxRect.width() != 0 && maxRect.height() != 0){
            hollowMoveMaxRect = maxRect;
            if(hollowTop < hollowMoveMaxRect.top){
                hollowTop = hollowMoveMaxRect.top;
                hollowBottom = hollowTop + hollowHeight;
            }
            if(hollowBottom > hollowMoveMaxRect.bottom){
                hollowBottom = hollowMoveMaxRect.bottom;
                hollowTop = hollowBottom - hollowHeight;
            }
            postInvalidate();
        }
    }

    /**
     * 获取宽高并初始化空心视图位置
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getMeasuredWidth() != 0){
            viewWidth = getMeasuredWidth();
        }
        if(getWidth() != 0){
            viewWidth = getWidth();
        }
        if(getMeasuredHeight() != 0){
            viewHeight = getMeasuredHeight();
        }
        if(getHeight() != 0){
            viewHeight = getHeight();
        }
        //在获取到视图的宽度且同时空心位置为确定的情况下初始空心位置
        if(viewWidth != 0 && viewHeight != 0
                && hollowLeft == 0 && hollowTop == 0
                && hollowRight == 0 && hollowBottom == 0){
            hollowLeft = (int) ((viewWidth - hollowWidth) / 2.0);
            hollowTop = (int) ((viewHeight - hollowHeight) / 2.0);
            hollowRight = (int) ((viewWidth - hollowWidth) / 2.0 + hollowWidth);
            hollowBottom = (int) ((viewHeight - hollowHeight) / 2.0 + hollowHeight);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //视图的宽高不为空
        if(viewWidth != 0 && viewHeight != 0){
            //空心中只要有一个为0那就绘制全部，否则就分四个部分绘制
            if(hollowWidth != 0 && hollowHeight != 0){
                //防止超出视图
                if(hollowWidth > viewWidth){
                    hollowWidth = viewWidth;
                }
                if(hollowHeight > viewHeight){
                    hollowHeight = viewHeight;
                }
                //绘制顶部视图
                canvas.drawRect(new RectF(0,0,viewWidth, hollowTop),unHollowPaint);
                //绘制左侧视图
                canvas.drawRect(new RectF(0,hollowTop,hollowLeft,hollowBottom),unHollowPaint);
                //绘制右侧视图
                canvas.drawRect(new RectF(hollowRight,hollowTop,viewWidth,hollowBottom),unHollowPaint);
                //绘制底部视图
                canvas.drawRect(new RectF(0,hollowBottom,viewWidth,viewHeight),unHollowPaint);

                //绘制线（此时的左右线的高度需要需要减掉双倍的线条宽度）
                //绘制顶部线
                canvas.drawLine((float) (hollowLeft - hollowMarginLineWidth / 2.0)
                        ,hollowTop,(float) (hollowRight + hollowMarginLineWidth / 2.0)
                        ,hollowTop,hollowMarginLinePaint);
                //绘制左侧线
                canvas.drawLine(hollowLeft,(float) (hollowTop + hollowMarginLineWidth / 2.0)
                        ,hollowLeft,(float) (hollowBottom - hollowMarginLineWidth / 2.0),hollowMarginLinePaint);
                //绘制右侧线
                canvas.drawLine(hollowRight
                        ,(float) (hollowTop + hollowMarginLineWidth / 2.0),hollowRight
                        ,(float) (hollowBottom - hollowMarginLineWidth / 2.0),hollowMarginLinePaint);
                //绘制底部线
                canvas.drawLine((float) (hollowLeft - hollowMarginLineWidth / 2.0)
                        ,hollowBottom,(float) (hollowRight + hollowMarginLineWidth / 2.0)
                        ,hollowBottom,hollowMarginLinePaint);
            }else {
                canvas.drawColor(unHollowColor);
            }
        }
    }

    int lastX,lastY;
    int downX,downY;

    /**
     * 视图的移动监听，保证移动在允许范围内
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX=(int)event.getRawX();//获取触摸事件触摸位置的原始X坐标
                lastY=(int)event.getRawY();
                downX=(int)event.getRawX();//获取触摸事件触摸位置的原始X坐标
                downY=(int)event.getRawY();
                //不在空心范围也要向下分发
                if(downX < hollowLeft || downX > hollowRight || downY < hollowTop || downY > hollowBottom){
                    return super.onTouchEvent(event);
                } else {
                    if(touchImageView != null){
                        setHollowMoveMaxRect(touchImageView.getImageMaxRect());
                    }
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                int dx = (int)event.getRawX() - lastX;
                int dy = (int)event.getRawY() - lastY;

                if(Math.abs(event.getRawX() - downX) < 10 && Math.abs(event.getRawY() - downY) < 10){
                    setEnabled(true);
                    return super.onTouchEvent(event);
                }

                setEnabled(false);

                int l = hollowLeft + dx;
                int b = hollowBottom + dy;
                int r = hollowRight + dx;
                int t = hollowTop + dy;


                //下面判断移动是否超出屏幕
                if( l < 0){
                    l = 0;
                    r = l + hollowWidth;
                }

                if(t < 0){
                    t = 0;
                    b = t + hollowHeight;
                }

                if(r > viewWidth){
                    r = viewWidth;
                    l = r - hollowWidth;
                }

                if(b > viewHeight){
                    b = viewHeight;
                    t = b - hollowHeight;
                }

                if(hollowMoveMaxRect != null){
                    if(t < hollowMoveMaxRect.top){
                        t = hollowMoveMaxRect.top;
                        b = t + hollowHeight;
                    }
                    if(b > hollowMoveMaxRect.bottom){
                        b = hollowMoveMaxRect.bottom;
                        t = b - hollowHeight;
                    }
                }


                hollowLeft = l;
                hollowTop = t;
                hollowRight = r;
                hollowBottom = b;
                postInvalidate();

                lastX=(int)event.getRawX();
                lastY=(int)event.getRawY();
                return true;
            case MotionEvent.ACTION_UP:
                if(Math.abs(event.getRawX() - downX) < 10 && Math.abs(event.getRawY() - downY) < 10){
                    setEnabled(true);
                    return super.onTouchEvent(event);
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }

    }


    public Bitmap cropImageToBitmap(){
        if(touchImageView != null){
            setHollowMoveMaxRect(touchImageView.getImageMaxRect());

            //获取边界在图片位置百分比
            RectF zoomedRectF = touchImageView.getZoomedRectF();
            Bitmap source = ((BitmapDrawable) touchImageView.getDrawable()).getBitmap();
            if(zoomedRectF != null && source != null) {
                int width = source.getWidth();
                int height = source.getHeight();

                int left = 0;
                int top = 0;
                if(zoomedRectF.left == 0){
                    left = (int) (hollowLeft * 1.0 / viewWidth * width);
                }else {
                    left = (int) (width * zoomedRectF.left + hollowLeft * 1.0 / viewWidth * zoomedRectF.width());
                }

                if(zoomedRectF.top == 0){
                    if(hollowMoveMaxRect == null){
                        top = (int) ((height - hollowHeight) / 2.0);
                    }else {//先将屏幕相对位置转化为图片相对屏幕位置在进行减运算
                        top = (int) ((hollowTop * 1.0 / viewHeight) * height - hollowMoveMaxRect.top);
                    }
                }else {
                    top = (int) (height * zoomedRectF.top + hollowTop * 1.0 / viewHeight * zoomedRectF.height());
                }

                return Bitmap.createBitmap(source,left,top,hollowWidth,hollowHeight,null,false);
            }

        }
        return null;
    }


}
