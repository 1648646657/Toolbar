package com.lzx.test.calendar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;
import com.lxj.xpopup.enums.PopupPosition;
import com.lzx.test.R;


public class CalendarActivity extends AppCompatActivity implements CalendarPopupView.OnDateSelectedListener{

    private Button calendarBtn;
    private CalendarPopupView calendarPopupView;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarBtn = findViewById(R.id.cancel_btn);
        text = findViewById(R.id.text);
        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarPopupView(v);
            }
        });
    }

    public void showCalendarPopupView(View view){
        calendarPopupView = (CalendarPopupView) new XPopup.Builder(this)
                .atView(view)
                .hasStatusBar(false)
                .hasNavigationBar(false)
                .isDestroyOnDismiss(true)
                .dismissOnTouchOutside(true)
                .hasShadowBg(false)
                .hasStatusBarShadow(false)
                .popupAnimation(PopupAnimation.ScaleAlphaFromCenter)
                .asCustom(new CalendarPopupView(this));
        calendarPopupView.setOnDateSelectedListener(this);
        calendarPopupView.show();
    }

    @Override
    public void OnDateSelected(String date) {
        text.setText(date);
    }
}