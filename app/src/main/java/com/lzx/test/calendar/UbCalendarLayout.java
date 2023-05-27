package com.lzx.test.calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.haibin.calendarview.MonthView;
import com.lzx.test.R;

/**
 * @author: lzx
 * @date: 20223.5.9
 * @description:
 */
public class UbCalendarLayout extends FrameLayout {

    private CalendarView mCalendarView;//日历
    private RelativeLayout mTitleRL;
    private TextView mTitleText;//标题日期显示

    private static int mCalendarSquareWidth;//日期方格宽度
    private int mTitleBackground;//标题背景色
    private int mTitleTextColor;//标题文本颜色
    private int mTitleTextSize;//标题字体大小

    private int mWeekBackground;//星期栏背景色
    private int mWeekTextColor;//星期栏文本颜色

    private int mSelectedBackground;//选中的背景主题颜色
    private int mSelectedTextColor;//选中的字体颜色

    private int mDayTextColor;//月份日期字体颜色

    private int mCurDayTextColor;//当前月份日期字体颜色

    private int mOtherDayTextColor;//其他月份日期字体颜色

    public UbCalendarLayout(@NonNull Context context) {
        super(context);
        initAttrs(null,0);
        initView(context);
    }
    public UbCalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs,0);
        initView(context);
    }
    public UbCalendarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs,defStyleAttr);
        initView(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int viewWidth = getMeasuredWidth();
        int viewHeight = getMeasuredHeight();

        Log.d("lzx", "onLayout: viewWidth = "+viewWidth+"---viewHeight = "+viewHeight);
//        mTitleRL.setLayoutParams(new ConstraintLayout.LayoutParams(mTitleLayoutWidth,mTitleLayoutHeight));
//        mCalendarView.setLayoutParams(new ConstraintLayout.LayoutParams(viewWidth-40,mWeekLayoutHeight));
    }

    public void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.calendar_view, this);

        mCalendarView = (CalendarView) findViewById(R.id.calendar_view);
        mTitleRL = (RelativeLayout) findViewById(R.id.text_rl);
        mTitleText = (TextView) findViewById(R.id.data_text);

        mTitleRL.setBackgroundColor(mTitleBackground);
        mTitleText.setTextColor(mTitleTextColor);
        mTitleText.setTextSize(mTitleTextSize);

        //设置颜色
        mCalendarView.setTextColor(mCurDayTextColor,mDayTextColor,mOtherDayTextColor,mDayTextColor,mOtherDayTextColor);
        mCalendarView.setWeeColor(mWeekBackground,mWeekTextColor);
        mCalendarView.setBackgroundColor(mWeekBackground);
        mCalendarView.setSelectedColor(mSelectedBackground,mSelectedTextColor,mSelectedTextColor);
        mCalendarView.setThemeColor(mSelectedBackground,mSelectedBackground);

        mCalendarView.setMonthView(CalendarMonthView1.class);//static生效
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
            }
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mTitleText.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
                Log.d("lzx", "onCalendarSelect: "+calendar.getYear() + "年" + calendar.getMonth() + "月"+ calendar.getDay() + "日");
            }
        });
    }

    public void initAttrs(AttributeSet attrs, int defStyleAttr){
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CalendarView);

        mCalendarSquareWidth = array.getDimensionPixelSize(R.styleable.CalendarView_calendar_square_width,57);

        mTitleBackground = array.getColor(R.styleable.CalendarView_title_background,0xff333333);
        mTitleTextColor = array.getColor(R.styleable.CalendarView_title_text_color,0xffffffff);
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.CalendarView_title_text_size,13);

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
//            mItemHeight = Math.min(mItemWidth, mItemHeight);
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
         * @param x         日历Card x起点坐标
         * @param y         日历Card y起点坐标
         * @param hasScheme 是否是标记的日期
         * @return 返回true 则绘制onDrawScheme，因为这里背景色不是是互斥的，所以返回true
         */
        @Override
        protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
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
            int cx = x + mItemWidth / 2;
            int top = y - mItemHeight / 2;
            mTextBaseLine = mItemWidth*8/7;
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
//            mCurDayTextPaint.setColor(0xffff0000);
            mCurDayTextPaint.setFakeBoldText(false);
//            mCurDayTextPaint.setTextSize(30);

//            mCurMonthTextPaint.setColor(0xffffffff);
            mCurMonthTextPaint.setFakeBoldText(false);
//            mCurMonthTextPaint.setTextSize(30);

            mSelectTextPaint.setColor(0xffffffff);
            mSelectTextPaint.setFakeBoldText(false);
//            mSelectTextPaint.setTextSize(30);

            mOtherMonthTextPaint.setColor(0xff7a7a7a);
            mOtherMonthTextPaint.setFakeBoldText(false);
//            mOtherMonthTextPaint.setTextSize(30);
        }

    }

}
