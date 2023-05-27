package com.lzx.test.camera;

import android.content.Context;
//import android.graphics.Camera;

import android.graphics.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG="CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;


    public CameraPreview(Context context) {
        super(context);
        mHolder=getHolder();
        mHolder.addCallback(this);
    }
    private static Camera getCameraInstance(){
        Camera c=null;
        try{
//            c=Camera.open();
        }catch (Exception e){
            Log.d(TAG, "camera is not  ");
        }
        return c;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
