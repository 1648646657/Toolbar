package com.lzx.test.netty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzx.test.R;
import com.lzx.test.databinding.ActivityTcpServiceBinding;
import com.lzx.test.netty.server.NettyTcpServerImpl;
import com.lzx.test.netty.server.TcpServerCallback;
import com.lzx.test.netty.server.TcpServerConfiguration;
import com.lzx.test.netty.server.TcpServerManage;
import com.lzx.test.service.FrontDeskService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class TcpServerActivity extends AppCompatActivity implements TcpServerCallback {

    private static final String TAG = "TcpServerActivity";
    private NettyTcpServerImpl mTcpServer;
    private ActivityTcpServiceBinding dataBinding;
    public static final int port = 6666;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_tcp_service);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"后台弹窗权限未授权",Toast.LENGTH_SHORT).show();
            // 请求后台弹窗权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW},
                    12345);
        }
        FrontDeskService.start(this);


        TcpServerManage.instance().init(new TcpServerConfiguration(6666));
    }

    public void openClick(View view) {

        TcpServerManage.instance().open();
        TcpServerManage.instance().sendMessageToAll("123456");

//        if(mTcpServer != null){
//            Log.d("lzx1", "openClick: mTcpService != null");
//        }else {
//            Log.d("lzx1", "openClick: mTcpService = null");
//        }
//        if(mTcpServer != null && mTcpServer.isOpen()){
//            Log.d("lzx1", "已打开");
//            Toast.makeText(TcpServerActivity.this, "已打开", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        try{
//            mTcpServer = null;
//            mTcpServer = new NettyTcpServerImpl(new TcpServerConfiguration(port));
//            mTcpServer.setCallback(this);
//            final ChannelFuture future = mTcpServer.open();
//            if(future == null){
//                Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            future.addListener(new ChannelFutureListener() {
//                @Override
//                public void operationComplete(ChannelFuture future) throws Exception {
//                    if (!future.isSuccess()) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(TcpServerActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        Log.d("lzx1", "打开失败: ");
//                    } else {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(TcpServerActivity.this, "打开成功", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                        Log.d("lzx1", "打开成功: ");
//                    }
//                }
//            });
//        }catch (Exception e){
//            if(mTcpServer != null){
//                mTcpServer.close();
//            }
//        }
    }

    public void closeClick(View view){
        TcpServerManage.instance().close();
        Log.d(TAG, "TCP服务端已关闭！");
//        if(mTcpServer != null){
//            mTcpServer.close();
//            mTcpServer = null;
////            dataBinding.btnOpen.setEnabled(true);
//            Log.d("lzx1", "关闭成功: ");
//            Toast.makeText(TcpServerActivity.this, "关闭成功", Toast.LENGTH_SHORT).show();
//        }else {
//            Log.d("lzx1", "已关闭: ");
//            Toast.makeText(TcpServerActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTcpServer != null){
            mTcpServer.close();
            mTcpServer = null;
        }
        FrontDeskService.stop(this);
    }

    @Override
    public void onChannelActive(@Nullable Channel channel) {
        Log.d("lzx1", "onChannelActive: ");
    }

    @Override
    public void onChannelRead(@Nullable Channel channel, @NonNull Object message) {
        Log.d("lzx1", "onChannelRead: message = "+message);
        dataBinding.editText.setText(message.toString());
        if(message.toString().equals("1")){
            Log.d("lzx", "onChannelRead: intent");
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.ubains.wisdomscreen.external","com.ubains.wisdomscreen.launcher.ui.activities.TestActivity"));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }else if(message.toString().equals("2")){
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI"));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }else if(message.toString().equals("3")){
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.tencent.wemeet.rooms","com.tencent.wemeet.rooms.MainActivity"));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }else if(message.toString().equals("4")){
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.ubains.multiplewhiteboard","com.ubains.multiplewhiteboard.ui.RouterActivity"));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }else if(message.toString().equals("0")){
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.lzx.test","com.lzx.test.netty.TcpServerActivity"));
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(launchIntent);
        }
    }

    @Override
    public void onChannelInactive(@Nullable Channel channel) {
        Log.d("lzx1", "onChannelInactive: ");
//        dataBinding.btnOpen.setEnabled(true);
    }

    @Override
    public void onError(@Nullable Channel channel, @NonNull Throwable throwable) {
//        dataBinding.btnOpen.setEnabled(true);
        Log.d("lzx1", "onError: ");
    }

}
