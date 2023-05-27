package com.lzx.test.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.MonthView;
import com.lzx.test.R;

/**
 * @author:
 * @date:
 * @description:
 */
public class UbCalendarView extends CalendarView {

    private static int mCalendarSquareWidth;//日期方格宽度
    private int mWeekBackground;//星期栏背景色
    private int mWeekTextColor;//星期栏文本颜色
    private int mSelectedBackground;//选中的背景主题颜色
    private int mSelectedTextColor;//选中的字体颜色
    private int mDayTextColor;//月份日期字体颜色
    private int mCurDayTextColor;//当前月份日期字体颜色
    private int mOtherDayTextColor;//其他月份日期字体颜色

    public UbCalendarView(@NonNull Context context) {
        super(context);
    }

    public UbCalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs,0);
        initView(context);
    }

    public void initView(Context context){
        //设置颜色
        setTextColor(mCurDayTextColor,mDayTextColor,mOtherDayTextColor,mDayTextColor,mOtherDayTextColor);
        setWeeColor(mWeekBackground,mWeekTextColor);
        setBackgroundColor(mWeekBackground);
        setSelectedColor(mSelectedBackground,mSelectedTextColor,mSelectedTextColor);
        setThemeColor(mSelectedBackground,mSelectedBackground);

        setMonthView(CalendarMonthView1.class);
        setOnCalendarSelectListener(new OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
            }
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                Log.d("lzx", "onCalendarSelect: "+calendar.getYear() + "年" + calendar.getMonth() + "月"+ calendar.getDay() + "日");
            }
        });

    }

    public void initAttrs(AttributeSet attrs, int defStyleAttr){
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);
        mCalendarSquareWidth = array.getDimensionPixelSize(R.styleable.CalendarView_calendar_square_width,57);
        mWeekBackground = array.getColor(R.styleable.CalendarView_week_background,0xff333333);
        mWeekTextColor = array.getColor(R.styleable.CalendarView_week_text_color,0xffffffff);
        mSelectedBackground = array.getColor(R.styleable.CalendarView_selected_background_color,0xff0099ff);
        mSelectedTextColor = array.getColor(R.styleable.CalendarView_selected_text_color,0xffffffff);
        mDayTextColor = array.getColor(R.styleable.CalendarView_day_text_color,0xffffffff);
        mCurDayTextColor = array.getColor(R.styleable.CalendarView_cur_day_text_color,0xffff0000);
        mOtherDayTextColor = array.getColor(R.styleable.CalendarView_other_day_text_color,0xff7a7a7a);
        array.recycle();
    }

    public static class CalendarMonthView1 extends MonthView {
        private static int mRadius;

        public CalendarMonthView1(Context context) {
            super(context);
        }

        @Override
        protected void onPreviewHook() {
            //设置日期方块大小
            //mItemHeight = Math.min(mItemWidth, mItemHeight);
            mItemHeight = mCalendarSquareWidth;
            mItemWidth = mItemHeight;
            Log.d("lzx", "onPreviewHook: mItemWidth = "+mItemWidth);
            mRadius = mItemWidth / 2;
        }
        /**
         * 如果需要点击Scheme没有效果，则return true
         *
         * @param canvas    canvas
         * @param calendar  日历calendar
         *                  (x,y)=(0,0)(左上角)
         * @param x         日历Card x起点坐标 往右递增
         * @param y         日历Card y起点坐标 往下递增
         * @param hasScheme 是否是标记的日期
         * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
         */
        @Override
        protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
            Log.d("lzx", "onDrawSelected: x = "+x+" y = "+y);
//            mSelectedPaint.setColor(0xff0099ff);
            int cx = x + mItemWidth / 2;
            int cy = y + mItemHeight / 2;
            canvas.drawCircle(cx, cy, mRadius, mSelectedPaint);
            return true;
        }

        @Override
        protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        }

        @Override
        protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
            initPaints();
            int cx =  x + mItemWidth / 2 ;
            int top = y - mItemHeight / 2 ;
            mTextBaseLine = mItemWidth*8/7;
            if (isSelected) {
                Log.d("lzx", "onDrawSelected: x = "+x+" y = "+y);
                Log.d("lzx", "onDrawText: cx = "+cx+"  mTextBaseLine = "+mTextBaseLine);
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
//            mCurDayTextPaint.setColor(0xffff0000);
            mCurDayTextPaint.setFakeBoldText(false);
//            mCurDayTextPaint.setTextSize(30);

//            mCurMonthTextPaint.setColor(0xffffffff);
            mCurMonthTextPaint.setFakeBoldText(false);
//            mCurMonthTextPaint.setTextSize(30);

//            mSelectTextPaint.setColor(0xffffffff);
            mSelectTextPaint.setFakeBoldText(false);
//            mSelectTextPaint.setTextSize(30);

//            mOtherMonthTextPaint.setColor(0xff7a7a7a);
            mOtherMonthTextPaint.setFakeBoldText(false);
//            mOtherMonthTextPaint.setTextSize(30);
        }
    }

}
