package com.lzx.test.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekView;

/**
 * @author: lzx
 * @date: 20223.5.9
 * @description:
 */
public class CentralControlWeekView extends WeekView {

    public CentralControlWeekView(Context context) {
        super(context);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, boolean hasScheme) {
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, boolean hasScheme, boolean isSelected) {
    }
}
