package com.lzx.test.netty;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzx.test.R;
import com.lzx.test.databinding.ActivityTcpServiceBinding;
import com.lzx.test.netty.server.NettyTcpServerImpl;
import com.lzx.test.netty.server.TcpServerCallback;
import com.lzx.test.netty.server.TcpServerConfiguration;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class TcpServerActivity extends AppCompatActivity implements TcpServerCallback {

    private NettyTcpServerImpl mTcpServer;
    private ActivityTcpServiceBinding dataBinding;

    public static final int port = 6666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_tcp_service);
    }

    public void openClick(View view) {
        if(mTcpServer != null){
            Log.d("lzx1", "openClick: mTcpService != null");
        }else {
            Log.d("lzx1", "openClick: mTcpService = null");
        }
        if(mTcpServer != null && mTcpServer.isOpen()){
            Log.d("lzx1", "已打开");
            Toast.makeText(TcpServerActivity.this, "已打开", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            mTcpServer = null;
            mTcpServer = new NettyTcpServerImpl(new TcpServerConfiguration(port));
            mTcpServer.setCallback(this);
            final ChannelFuture future = mTcpServer.open();
            if(future == null){
                Toast.makeText(this, "打开失败", Toast.LENGTH_SHORT).show();
                return;
            }
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TcpServerActivity.this, "打开失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("lzx1", "打开失败: ");
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TcpServerActivity.this, "打开成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d("lzx1", "打开成功: ");
                    }
                }
            });
        }catch (Exception e){
            if(mTcpServer != null){
                mTcpServer.close();
            }
        }
    }

    public void closeClick(View view){
        if(mTcpServer != null){
            mTcpServer.close();
            mTcpServer = null;
//            dataBinding.btnOpen.setEnabled(true);
            Log.d("lzx1", "关闭成功: ");
            Toast.makeText(TcpServerActivity.this, "关闭成功", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("lzx1", "已关闭: ");
            Toast.makeText(TcpServerActivity.this, "已关闭", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if(mTcpServer != null){
            mTcpServer.close();
            mTcpServer = null;
//            dataBinding.btnOpen.setEnabled(true);
        }
        super.onDestroy();
    }


    @Override
    public void onChannelActive(@Nullable Channel channel) {
        Log.d("lzx1", "onChannelActive: ");
    }

    @Override
    public void onChannelReadComplete(@Nullable Channel channel) {
        Log.d("lzx1", "onChannelReadComplete: ");
    }

    @Override
    public void onChannelRead(@Nullable Channel channel, @NonNull Object message) {
        Log.d("lzx1", "onChannelRead: message = "+message);
        dataBinding.editText.setText(message.toString());
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
