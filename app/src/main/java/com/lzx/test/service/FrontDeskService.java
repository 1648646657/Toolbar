package com.lzx.test.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.lzx.test.R;
import com.lzx.test.netty.TcpServerActivity;

/**
 * @author: lzx
 * @date: 2023.6
 * @description: 前台服务
 */
public class FrontDeskService extends Service {
    private static final String TAG = "FrontDeskService1";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    public static void start(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, FrontDeskService.class));
        } else {
            context.startService(new Intent(context, FrontDeskService.class));
        }
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, FrontDeskService.class));
    }

    /**
     * 创建通知通道
     * @param channelId
     * @param channelName
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName){
        NotificationChannel chan = new NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager service = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        service.createNotificationChannel(chan);
        return channelId;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        //前台通知
        Intent notificationIntent = new Intent(this, TcpServerActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "onStartCommand: VERSION >= O");
            String channelId = createNotificationChannel("FrontDeskService1", "FrontDeskService1");
            notification = new Notification.Builder(this, channelId)
                    .setContentTitle("IDEATOP")
                    .setContentText("正在运行中")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .build();
        }else {
            Log.d(TAG, "onStartCommand: VERSION < O");
            notification = new Notification.Builder(this)
                    .setContentTitle("IDEATOP")
                    .setContentText("正在运行中")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentIntent(pendingIntent)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(pendingIntent)
                    .build();
        }
        startForeground(1, notification);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d(TAG, "onDestroy: VERSION >= N");
            stopForeground(Service.STOP_FOREGROUND_REMOVE);
        }else {
            Log.d(TAG, "onDestroy: VERSION < N");
            stopForeground(true);
        }
        Log.d(TAG, "onDestroy: ");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return null;
    }

}
