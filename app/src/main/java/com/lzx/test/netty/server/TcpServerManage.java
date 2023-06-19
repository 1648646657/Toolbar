package com.lzx.test.netty.server;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * @author: lzx
 * @date: 2023.6
 * @description: tcp服务端管理
 */
public class TcpServerManage {

    private static final String TAG = "TcpServerManage";
    private NettyTcpServerImpl mNettyTcpServer;
    private TcpServerConfiguration mTcpServerConfiguration;
    private TcpServerCallback mTcpServerCallback;

    public TcpServerManage() {
    }
    private static final class Holder {
        private static final TcpServerManage INSTANCE = new TcpServerManage();
        private Holder() {
        }
    }
    public static TcpServerManage instance() {
        return Holder.INSTANCE;
    }

    @Subscribe(
            threadMode = ThreadMode.ASYNC
    )

    public void init(TcpServerConfiguration tcpServerConfiguration){
        this.mTcpServerConfiguration = tcpServerConfiguration;
        setTcpServerCallback();
        open();
        EventBus.getDefault().register(this);
    }

    public void open(){
        if(mNettyTcpServer != null && mNettyTcpServer.isOpen()){
            Log.d(TAG, "TCP服务端已打开");
            return;
        }
        try{
            mNettyTcpServer = null;
            if(mTcpServerConfiguration == null){
                mTcpServerConfiguration = new TcpServerConfiguration(6666);
            }
            if(mTcpServerCallback == null){
                setTcpServerCallback();
            }
            mNettyTcpServer = new NettyTcpServerImpl(mTcpServerConfiguration);
            mNettyTcpServer.setCallback(mTcpServerCallback);
            final ChannelFuture future = mNettyTcpServer.open();
            if(future == null){
                Log.e(TAG, "TCP打开失败！");
                return;
            }
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        Log.e(TAG, "TCP打开失败！");
                    } else {
                        Log.d(TAG, "TCP打开成功！");
                    }
                }
            });
        }catch (Exception e){
            if(mNettyTcpServer != null){
                mNettyTcpServer.close();
            }
        }
    }

    public void sendMessage(Channel channel, Object message) {
        if(mNettyTcpServer == null){
            Log.e(TAG, "TCP发送消息失败");
            return;
        }
        mNettyTcpServer.sendMessage(channel, message);
        Log.d(TAG, "TCP成功发送消息给["+channel.remoteAddress()+"]："+message);
    }

    public void sendMessageToAll(Object message) {
        if(mNettyTcpServer == null){
            Log.e(TAG, "TCP发送消息失败");
            return;
        }
        mNettyTcpServer.sendMessageToAll(message);
        Log.d(TAG, "TCP成功发送消息给所有客户端："+message);
    }

    public NettyTcpServerImpl getNettyTcpServer() {
        return mNettyTcpServer;
    }

    public void setNettyTcpServer(NettyTcpServerImpl mNettyTcpServer) {
        this.mNettyTcpServer = mNettyTcpServer;
    }

    public void setTcpServerCallback() {
        if(mTcpServerCallback != null){
            return;
        }
        this.mTcpServerCallback = new TcpServerCallback() {
            @Override
            public void onChannelActive(@Nullable Channel channel) {
                Log.d(TAG, "TCP连接成功！");
            }
            @Override
            public void onChannelRead(@Nullable Channel channel, @NonNull Object message) {
                Log.d(TAG, "服务端收到消息："+message);
            }
            @Override
            public void onChannelInactive(@Nullable Channel channel) {
                Log.e(TAG, "TCP连接断开！");
            }
            @Override
            public void onError(@Nullable Channel channel, @NonNull Throwable throwable) {
                Log.e(TAG, "TCP连接出错！");
            }
        };
    }
    public void close(){
        if(mNettyTcpServer == null){
            Log.e(TAG, "TCP服务端未连接！");
            return;
        }
        mNettyTcpServer.close();
        mNettyTcpServer = null;
        Log.d(TAG, "TCP服务端关闭成功！");
        EventBus.getDefault().unregister(this);
    }

}
