package com.lzx.test.CustomView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.lzx.test.R;

public class TaiJiView extends View {

    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private int useWidth;
    private int leftWidth;
    private int leftcolor;
    private int rightcolor;
    public ObjectAnimator objectAnimator;
    private int animaltime;

    public TaiJiView(Context context) {
        super(context);
        initCustomAttrs(context,null);
    }

    public TaiJiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCustomAttrs(context,attrs);
    }

    public TaiJiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCustomAttrs(context,attrs);
    }

    /**
     获取自定义属性
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        //获取自定义属性。
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TaiJiView);
        //获取太极颜色
        leftcolor = ta.getColor(R.styleable.TaiJiView_leftcolor, Color.BLACK);
        rightcolor=ta.getColor(R.styleable.TaiJiView_rightcolor, Color.WHITE);
        animaltime=ta.getInt(R.styleable.TaiJiView_animaltime,1000);
        //回收
        ta.recycle();
        initPaint();

    }
    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();        //创建画笔对象
        mPaint.setColor(Color.BLACK);    //设置画笔颜色
        mPaint.setStyle(Paint.Style.FILL); //设置画笔模式为填充
        mPaint.setStrokeWidth(10f);     //设置画笔宽度为10px
        mPaint.setAntiAlias(true);     //设置抗锯齿
        mPaint.setAlpha(255);        //设置画笔透明度
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        mWidth = w;
//        mHeight = h;
//        useWidth=mWidth;
//        if (mWidth>mHeight){
//            useWidth=mHeight;
//        }
//    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        useWidth = mWidth;
        leftWidth = (mWidth-useWidth)/2;
        if (mWidth>mHeight){
            useWidth=mHeight;
            leftWidth = (mWidth-useWidth)/2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(leftcolor);
        canvas.drawArc(new RectF(leftWidth, 0, useWidth+leftWidth, useWidth), 270, -180, true, mPaint);

        mPaint.setColor(rightcolor);
        canvas.drawArc(new RectF(leftWidth, 0, useWidth+leftWidth, useWidth), 270, 180, true, mPaint);

        mPaint.setColor(leftcolor);
        canvas.drawArc(new RectF(useWidth / 4+leftWidth, 0, useWidth / 2 +useWidth / 4+leftWidth, useWidth / 2),
                270, 360, true, mPaint);

        mPaint.setColor(rightcolor);
        canvas.drawArc(new RectF(useWidth / 4+leftWidth, useWidth / 2, useWidth / 2 + useWidth / 4+leftWidth,useWidth),
                270, 360, true, mPaint);
        mPaint.setColor(leftcolor);
        canvas.drawCircle(useWidth/ 2+leftWidth, useWidth * 3 / 4, useWidth/16, mPaint);

        mPaint.setColor(rightcolor);
        canvas.drawCircle(useWidth / 2+leftWidth, useWidth / 4, useWidth/16, mPaint);
    }

    public void createAnimation() {
        if (objectAnimator == null) {
            objectAnimator = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
            objectAnimator.setDuration(animaltime);//设置动画时间
            objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
            objectAnimator.start();//动画开始
        } else {
            objectAnimator.resume();//动画继续开始
        }
    }

    public void stopAnimation(){
        if (objectAnimator!=null){
            objectAnimator.pause();//动画暂停
        }
    }

    public  void cleanAnimation(){
        if (objectAnimator!=null){
            objectAnimator.end(); //结束动画
        }
    }

}
