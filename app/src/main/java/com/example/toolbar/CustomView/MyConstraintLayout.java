package com.example.toolbar.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.toolbar.R;

public class MyConstraintLayout extends ConstraintLayout {

    private TextView textView1;

    public MyConstraintLayout(@NonNull Context context) {
        this(context,null);
    }

    public MyConstraintLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.child, this);
        textView1=(TextView) findViewById(R.id.textView1);
    }

    public void setTextView(String text1){
        textView1.setText(text1);
    }
}
