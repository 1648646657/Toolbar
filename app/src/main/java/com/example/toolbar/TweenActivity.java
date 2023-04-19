package com.example.toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class TweenActivity extends AppCompatActivity {

    private Animation animation;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tween);
        imageView=(ImageView) findViewById(R.id.image);
    }

    public void OnAlpha(View view){
        animation= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.alpha);
        imageView.startAnimation(animation);
    }
    public void OnScale(View view){
        animation= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.scale);
        imageView.startAnimation(animation);
    }
    public void OnSet(View view){
        animation= AnimationUtils.loadAnimation(TweenActivity.this,R.anim.set);
        imageView.startAnimation(animation);
    }


}