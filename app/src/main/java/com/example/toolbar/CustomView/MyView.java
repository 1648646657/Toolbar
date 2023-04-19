package com.example.toolbar.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.toolbar.R;

import kotlin.text.UStringsKt;

@SuppressLint("AppCompatCustomView")
public class MyView extends TextView {

    private int textColor;
    private float textSize;
    private String text;

    private Paint mPaint;
    private Rect mBoud;

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    public MyView(Context context) {
        this(context,null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
//        LayoutInflater.from(context).inflate(R.layout.child, this);
//
//        textView1=(TextView) findViewById(R.id.textView1);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        LayoutInflater.from(context).inflate(R.layout.child, this);

//        textView1=(TextView) findViewById(R.id.textView1);

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs,R.styleable.MyView,0,0);
        try{
            textColor=typedArray.getColor(R.styleable.MyView_textColor,0);
            textSize=typedArray.getDimensionPixelSize(R.styleable.MyView_textSize,0);
            text=typedArray.getString(R.styleable.MyView_text);
        }finally {
            typedArray.recycle();
        }
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mBoud=new Rect();
        mPaint.getTextBounds(text,0,text.length(),mBoud);
    }

    public void setTextView1(String text1){
        textView1.setText(text1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=mesureWidth(widthMeasureSpec);
        int height=mesureHeight(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    private int mesureWidth(int widthMeasureSpec){
        int mode=MeasureSpec.getMode(widthMeasureSpec);
        int size=MeasureSpec.getSize(widthMeasureSpec);
        int width=0;
        if(mode==MeasureSpec.EXACTLY){
            //math_parent
            width=size;
        }else if(mode==MeasureSpec.AT_MOST){
            //wrap_parent
            width=getPaddingLeft()+mBoud.width()+getPaddingRight();
        }
        return width;
    }
    private int mesureHeight(int heightMeasureSpec){
        int mode=MeasureSpec.getMode(heightMeasureSpec);
        int size=MeasureSpec.getSize(heightMeasureSpec);
        int height=0;
        if(mode==MeasureSpec.EXACTLY){
            //math_parent
            height=size;
        }else if(mode==MeasureSpec.AT_MOST){
            //wrap_parent
            height=getPaddingTop()+mBoud.height()+getPaddingBottom();
        }
        return height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetricsInt fontMetrics=mPaint.getFontMetricsInt();
        int baseline=(getMeasuredHeight()/2)-((fontMetrics.bottom+fontMetrics.top)/2);
        canvas.drawText(text,getWidth()/2-mBoud.width()/2,baseline,mPaint);
    }
}
