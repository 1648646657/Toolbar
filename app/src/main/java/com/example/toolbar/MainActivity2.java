package com.example.toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.toolbar.CustomView.MyView;
import com.example.toolbar.CustomView.SuperCircleView;
import com.example.toolbar.CustomView.TaiJiView;

import java.util.Random;

public class MainActivity2 extends AppCompatActivity {

    private MyView myView;
    private SuperCircleView mSuperCircleView;
    private TextView mTextView;
    private TaiJiView taiJiView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

//        myView=(MyView) findViewById(R.id.myview2);
//        myView.setTextView1("lzx");
        initSuperCircleView();
        initTaiJiView();
    }

    private void initSuperCircleView() {
        mTextView = (TextView) findViewById( R.id.tv);
        mSuperCircleView = findViewById(R.id.superview);
//        mSuperCircleView.setValue(70, mTextView);
        mSuperCircleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //随机设定圆环大小
                int i = new Random().nextInt(100) + 1;
                Log.i("lzx", "onClick: i::" + i);
//                int i =100;
                mSuperCircleView.setValue(i, mTextView);
            }
        });
    }
    private void initTaiJiView(){
        taiJiView = findViewById(R.id.taiji_view);
        taiJiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taiJiView.objectAnimator == null){
                    Log.d("lzx1", "onClick: isNull");
                    taiJiView.createAnimation();
                }else if(taiJiView.objectAnimator.isRunning() && !taiJiView.objectAnimator.isPaused()){
                    Log.d("lzx1", "onClick: isRunning");
                    taiJiView.objectAnimator.pause();
                }else if(taiJiView.objectAnimator.isPaused()){
                    Log.d("lzx1", "onClick: isPaused");
                    taiJiView.objectAnimator.resume();
                }
            }
        });
    }

}