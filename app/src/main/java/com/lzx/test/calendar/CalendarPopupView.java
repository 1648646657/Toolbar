package com.lzx.test.calendar;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.lxj.xpopup.core.AttachPopupView;
import com.lzx.test.R;

/**
 * @author: lzx
 * @date: 2023.5.9
 * @description:
 */
public class CalendarPopupView extends AttachPopupView {

    private CalendarView mCalendarView;
    private TextView mDataText;

    private OnDateSelectedListener onDateSelectedListener;

    public CalendarPopupView(@NonNull Context context) {
        super(context);
    }

    public CalendarPopupView(@NonNull Context context, CalendarView mCalendarView, TextView mDataText) {
        super(context);
        this.mCalendarView = mCalendarView;
        this.mDataText = mDataText;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public interface OnDateSelectedListener {
        void OnDateSelected(String date);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.canlendar_popup_view;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mCalendarView = findViewById(R.id.calendar_view);
        mCalendarView.setMonthView(CentralControlMonthView.class);
        mDataText = findViewById(R.id.data_text);
        mDataText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mCalendarView.showYearSelectLayout(2025);
            }
        });
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {
            }
            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mDataText.setText(calendar.getYear() + "年" + calendar.getMonth() + "月");
                Log.d("lzx1", "onCalendarSelect: "+calendar.getYear() + "年" + calendar.getMonth() + "月"+ calendar.getDay() + "日");
            }
        });
        //下个月
//        mCalendarView.scrollToNext();
        //上个月
//        mCalendarView.scrollToPre();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        if(mCalendarView != null){
            Calendar calendar = mCalendarView.getSelectedCalendar();
            onDateSelectedListener.OnDateSelected(calendar.getYear() + "年" + calendar.getMonth() + "月"+ calendar.getDay() + "日");
        }
    }
}
