package com.example.toolbar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class DragAndDropActivity extends AppCompatActivity {


    private ImageView image;
    private LinearLayout.LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_and_drop);

        image=(ImageView) findViewById(R.id.image);


        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item=new ClipData.Item("lzx");
                String[] mimeTypes={ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData=new ClipData("lzx",mimeTypes,item);
                View.DragShadowBuilder myShadow=new View.DragShadowBuilder(image);
                v.startDrag(dragData,myShadow,null,0);
//                image.setVisibility(View.INVISIBLE);
                return true;
            }
        });
        image.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d("lzx", "拖拽开始");
                        layoutParams = (LinearLayout.LayoutParams) v.getLayoutParams();
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        int x = (int)event.getX();
                        int y = (int)event.getY();
                        Log.d("lzx", "拖拽View,在监听View中的位置:"+x+","+y);
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d("lzx", "释放");
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d("lzx", "拖拽结束");
                        x = (int)event.getX();
                        y = (int)event.getY();
                        layoutParams.leftMargin=x;
                        layoutParams.topMargin=y;
                        v.setLayoutParams(layoutParams);
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("lzx", "进入监听的View");
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("lzx", "退出监听的View");
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

//        image.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(image);
//
//                    image.startDrag(data, shadowBuilder, image, 0);
//                    image.setVisibility(View.VISIBLE);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

    }
}