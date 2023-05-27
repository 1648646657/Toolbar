package com.lzx.test.calendar;

import android.content.Context;
import android.graphics.Canvas;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * @author: lzx
 * @date: 20223.5.9
 * @description:
 */
public class CentralControlMonthView extends MonthView {
    private int mRadius;
    public CentralControlMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mItemHeight = Math.min(mItemWidth, mItemHeight);
        mItemWidth = mItemHeight;
        mRadius = mItemWidth / 5 * 2;
    }

    /**
     * 如果需要点击Scheme没有效果，则return true
     *
     * @param canvas    canvas
     * @param calendar  日历calendar
     * @param x         日历Card x起点坐标
     * @param y         日历Card y起点坐标
     * @param hasScheme 是否是标记的日期
     * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
     */
    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        mSelectedPaint.setColor(0xff0099ff);
        int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight * 4 / 7;
        canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        initPaints();
        int cx = x + mItemWidth / 2;
        int top = y - mItemHeight / 2;
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint : mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }

    private void initPaints(){
        mCurDayTextPaint.setColor(0xffff0000);
        mCurDayTextPaint.setFakeBoldText(false);
        mCurDayTextPaint.setTextSize(30);

        mCurMonthTextPaint.setColor(0xffffffff);
        mCurMonthTextPaint.setFakeBoldText(false);
        mCurMonthTextPaint.setTextSize(30);

        mSelectTextPaint.setColor(0xffffffff);
        mSelectTextPaint.setFakeBoldText(false);
        mSelectTextPaint.setTextSize(30);

        mOtherMonthTextPaint.setColor(0xff7a7a7a);
        mOtherMonthTextPaint.setFakeBoldText(false);
        mOtherMonthTextPaint.setTextSize(30);

    }

}
