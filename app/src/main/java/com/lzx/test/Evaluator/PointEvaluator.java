package com.lzx.toolbar.Evaluator;

import android.animation.TypeEvaluator;

import com.lzx.toolbar.Do.Point;

public class PointEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object starValue, Object endValue) {
        Point starPoint=(Point) starValue;
        Point endPoint=(Point) endValue;

        float x=starPoint.getX()+fraction * (endPoint.getX()-starPoint.getX());
        float y=starPoint.getY()+fraction * (endPoint.getY()-starPoint.getY());

        Point point=new Point(x,y);

        return point;
    }
}
