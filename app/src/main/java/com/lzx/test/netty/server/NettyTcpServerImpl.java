package com.lzx.test.netty.server;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public class NettyTcpServerImpl implements ITcpServer {

    private static final String TAG = "NettyTcpServerImpl";

    //自身通道
    private Channel mChannel;

    private final EventLoopGroup bossGroup;

    private final EventLoopGroup workerGroup;

    private final ServerBootstrap mServerBootstrap;

    private final int port;

    private TcpServerCallback mCallback;


    public NettyTcpServerImpl(TcpServerConfiguration configuration) {

        this.port = configuration.port;
        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup();
        mServerBootstrap = new ServerBootstrap();
        mServerBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(@NonNull SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast(new ServiceHandler());
                    }
                });

    }

    @Override
    public boolean isOpen() {
        return mChannel != null && mChannel.isActive();
    }

    @Override
    public ChannelFuture open() {
        if (this.mChannel == null) {
            return mServerBootstrap.bind(port).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    Log.e(TAG, "TCP打开失败!");
                    return;
                }
                mChannel = future.channel();
                Log.d(TAG, "TCP打开成功!");
            });
        }
        return null;
    }

    @Override
    public void close() {
        if (mChannel != null) {
            mChannel.close().syncUninterruptibly();
            mChannel = null;
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    @Override
    public void sendMessage(Channel channel, Object message) {
        TcpServerChannelGroup.sendMessage(channel, message);
    }

    @Override
    public void sendMessageToAll(Object message) {
        TcpServerChannelGroup.sendMessageToAll(message);
    }

    private void disconnect(Channel channel){
        boolean isSucceed = TcpServerChannelGroup.disconnect(channel);
        if(isSucceed){
            Log.d(TAG, "关闭连接成功：["+channel.remoteAddress()+"]");
        }
    }

    @Override
    public void setCallback(TcpServerCallback callback) {
        this.mCallback = callback;
    }

    private class ServiceHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            Log.d(TAG, "channelActive: 远端地址[" + ctx.channel().remoteAddress() + "] 本地地址+[" + ctx.channel().localAddress() + "]");
            ctx.channel().writeAndFlush("connect success!!!");
            if (mCallback != null) {
                mCallback.onChannelActive(ctx.channel());
            }
            //添加管道到ChanelGroup
            TcpServerChannelGroup.add(ctx.channel());
            Log.d(TAG, "已连接客户端数量: "+TcpServerChannelGroup.size());
            Iterator<Channel> channelGroup = TcpServerChannelGroup.getChannelGroup();
            while (channelGroup.hasNext()) {
                Channel channel = channelGroup.next();
                Log.d(TAG, "已连接客户端: " + channel.remoteAddress().toString());
            }

        }

        @Override
        public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
            super.channelRead(ctx, msg);
            Log.d(TAG, "channelRead: ");
            if (mCallback != null) {
                mCallback.onChannelRead(ctx.channel(),msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);
            Log.d(TAG, "channelReadComplete: ");
        }

        @Override
        public void channelInactive(@NonNull ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            Log.d(TAG, "channelInactive: 远端地址[" + ctx.channel().remoteAddress() + "] 本地地址+[" + ctx.channel().localAddress() + "]");
            if (mCallback != null) {
                mCallback.onChannelInactive(ctx.channel());
            }
            disconnect(ctx.channel());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            Log.d(TAG, "exceptionCaught: ");
            if (mCallback != null) {
                mCallback.onError(ctx.channel(), cause == null ? new RuntimeException("exceptionCaught cause is null") : cause);
            }
            disconnect(ctx.channel());
        }
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            super.userEventTriggered(ctx, evt);
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.ALL_IDLE) {
                    disconnect(ctx.channel());
                }
            }
        }

    }

}
