package com.lzx.test.CustomView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;


import androidx.annotation.Nullable;

import com.lzx.toolbar.Do.Point;
import com.lzx.toolbar.Evaluator.PointEvaluator;
import com.lzx.toolbar.Interpolator.DecelerateAccelerateInterpolator;


@SuppressLint("AppCompatCustomView")
public class AnimationView extends TextView {

    //当前点的坐标
    private Point currentPoint;
    private Paint mPaint;

    public AnimationView(Context context) {
        this(context,null);
    }

    public AnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(5);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(currentPoint==null){
            currentPoint=new Point(100,100);
            float x=currentPoint.getX();
            float y=currentPoint.getY();
            canvas.drawCircle(x,y,100,mPaint);

            //添加属性动画
            Point starPoint=new Point(100,100);
            Point endPoint=new Point(600,1000);
            Point endPoint1=new Point(100,100);
            ValueAnimator valueAnimator=ValueAnimator.ofObject(new PointEvaluator(),starPoint,endPoint,endPoint1);
            //设置插值器
            valueAnimator.setInterpolator(new DecelerateAccelerateInterpolator());
            valueAnimator.setDuration(5000);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator.setRepeatCount(10);
            valueAnimator.setStartDelay(500);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    currentPoint=(Point) valueAnimator.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        }else{
            float x=currentPoint.getX();
            float y=currentPoint.getY();
            canvas.drawCircle(x,y,100,mPaint);
        }
    }

}
