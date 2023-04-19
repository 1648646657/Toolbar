package com.example.toolbar.CustomView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.media.Ringtone;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.toolbar.R;

public class SuperCircleView extends View {


    private String mExampleString;
    private int mExampleColor = Color.RED;
    private float mExampleDimension = 0;
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private ValueAnimator valueAnimator;
    private int mViewCenterX;
    private int mViewCenterY; //中心点坐标

    private int mMinRadio; //最里面白色圆的半径
    private float mRingWidth; //圆环的宽度
    private int mMinCircleColor;    //最里面圆的颜色
    private int mRingNormalColor;    //默认圆环的颜色
    private Paint mPaint;
    private int color[] = new int[3];   //渐变颜色

    private RectF mRectF; //圆环的矩形区域
    private int mSelectRing = 0; //要显示的彩色区域(岁数值变化)
    float ringRadius;
    private int mMaxValue;


    public SuperCircleView(Context context) {
        super(context);
        init( null, 0 );
    }

    public SuperCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);
    }

    public SuperCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs,defStyleAttr);
    }


    public void init(AttributeSet attrs, int defStyleAttr){
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SuperCircleView);
        mMinRadio = array.getInteger(R.styleable.SuperCircleView_min_circle_radio,120);
        mRingWidth = array.getFloat(R.styleable.SuperCircleView_ring_width, 40);
        mMinCircleColor = array.getColor(R.styleable.SuperCircleView_circle_color,getContext().getResources().getColor(R.color.white));
        mRingNormalColor = array.getColor(R.styleable.SuperCircleView_ring_normal_color,getContext().getResources().getColor(R.color.blue));
        mSelectRing = array.getInt(R.styleable.SuperCircleView_ring_color_select,0);
        mMaxValue = array.getInt(R.styleable.SuperCircleView_maxValue,100);
        ringRadius = array.getDimension(R.styleable.SuperCircleView_ring_radius,dp2px(100));
        array.recycle();

        //抗锯齿画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //防止边缘锯齿
        mPaint.setAntiAlias(true);
        //需要重写onDraw就得调用此
        this.setWillNotDraw(false);

        //圆环渐变的颜色
        color[0] = Color.parseColor("#FFD300");
        color[1] = Color.parseColor("#FF0084");
        color[2] = Color.parseColor("#16FF00");

    }

    public int dp2px(float dpValue){
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        Log.d("lzx", "dp2px: density = "+scale +" :: dpValue*scale+0.5f = "+dpValue*scale+0.5f );
        return (int)(dpValue*scale+0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();
        mViewCenterX = viewWidth/2;
        mViewCenterY = viewHeight/2;
        mRectF = new RectF(
                mViewCenterX - ringRadius,
                mViewCenterY - ringRadius,
                mViewCenterX + ringRadius,
                mViewCenterY + ringRadius
        );
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize( mExampleDimension );
        mTextPaint.setColor( mExampleColor );
        mTextWidth = mTextPaint.measureText( mExampleString );

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mMinCircleColor);
        canvas.drawCircle(mViewCenterX,mViewCenterY,mMinRadio,mPaint);

        drawNormalRing(canvas);
        drawColorRing(canvas);
    }

    private void drawNormalRing(Canvas canvas){
        Log.d("lzx", "drawNormalRing: ");
        Paint ringNormalPaint = new Paint(mPaint);
        ringNormalPaint.setStyle(Paint.Style.STROKE);
        ringNormalPaint.setStrokeWidth(mRingWidth);
        ringNormalPaint.setColor(mRingNormalColor);
        canvas.drawArc(mRectF,360,360,false,ringNormalPaint);
    }

    private void drawColorRing(Canvas canvas){
        Log.d("lzx", "drawColorRing: ");
        Paint ringColorPaint = new Paint(mPaint);
        ringColorPaint.setStyle(Paint.Style.STROKE);
        ringColorPaint.setStrokeWidth(mRingWidth);
        ringColorPaint.setShader(new SweepGradient(mViewCenterX,mViewCenterY,color,null));
        canvas.rotate(-90,mViewCenterX,mViewCenterY);
        canvas.drawArc(mRectF,360,mSelectRing,false,ringColorPaint);
        ringColorPaint.setShader(null);
    }

    public void setValue(int value, TextView textView){
        if(value > mMaxValue){
            value = mMaxValue;
        }
        int start = 0;
        int end = value;
        startAnimator(start,end,1000,textView);
    }

    private void startAnimator(int start, int end, long animTime, final TextView textView){
        Log.d("lzx", "startAnimator: ");
        valueAnimator = ValueAnimator.ofInt(start,end);
        valueAnimator.setDuration(animTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int i = Integer.valueOf(String.valueOf(valueAnimator.getAnimatedValue()));
                textView.setText(i+"");
                mSelectRing = (int)(360*(i/100f));
                invalidate();
            }
        });
        valueAnimator.start();
    }


    public String getmExampleString() {
        return mExampleString;
    }

    public void setmExampleString(String mExampleString) {
        this.mExampleString = mExampleString;
        invalidateTextPaintAndMeasurements();
    }

    public int getmExampleColor() {
        return mExampleColor;
    }

    public void setmExampleColor(int mExampleColor) {
        this.mExampleColor = mExampleColor;
        invalidateTextPaintAndMeasurements();
    }

    public float getmExampleDimension() {
        return mExampleDimension;
    }

    public void setmExampleDimension(float mExampleDimension) {
        this.mExampleDimension = mExampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    public Drawable getmExampleDrawable() {
        return mExampleDrawable;
    }

    public void setmExampleDrawable(Drawable mExampleDrawable) {
        this.mExampleDrawable = mExampleDrawable;
    }
}
