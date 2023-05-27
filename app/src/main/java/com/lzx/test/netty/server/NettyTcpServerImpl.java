package com.lzx.test.netty.server;

import android.util.Log;

import androidx.annotation.NonNull;

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

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public class NettyTcpServerImpl implements ITcpServer {

    private static final int DEFAULT_MAX_FRAME_LENGTH = 10 * 1024; //消息帧大小 1M

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
//                        ch.pipeline().addLast(new LineBasedFrameDecoder(configuration.maxFrameLength > 0 ?
//                                configuration.maxFrameLength : DEFAULT_MAX_FRAME_LENGTH));
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
                if (future.isSuccess()) {
                    mChannel = future.channel();
                } else {
                    mChannel.close().sync();
                }
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
    public ChannelFuture sendMessage(Object message) {
        if (mChannel != null) {
            return mChannel.writeAndFlush(message);
        }
        return null;
    }

    @Override
    public void setCallback(TcpServerCallback callback) {
        this.mCallback = callback;
    }

    private class ServiceHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            Log.d("lzx1", "channelActive: 远端地址[" + ctx.channel().remoteAddress() + "] 本地地址+[" + ctx.channel().localAddress() + "]");
            ctx.channel().writeAndFlush("connect success!!!");
            if (mCallback != null) {
                mCallback.onChannelActive(ctx.channel());
            }
        }

        @Override
        public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
            super.channelRead(ctx, msg);
            Log.d("lzx1", "channelRead: ");
            if (mCallback != null) {
                mCallback.onChannelRead(ctx.channel(),msg);
            }
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            super.channelReadComplete(ctx);
            Log.d("lzx1", "channelReadComplete: ");
            if (mCallback != null) {
                mCallback.onChannelReadComplete(ctx.channel());
            }
        }

        @Override
        public void channelInactive(@NonNull ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            Log.d("lzx1", "channelInactive: 远端地址[" + ctx.channel().remoteAddress() + "] 本地地址+[" + ctx.channel().localAddress() + "]");
            if (mCallback != null) {
                mCallback.onChannelInactive(ctx.channel());
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
            Log.d("lzx1", "exceptionCaught: ");
            if (mCallback != null) {
                mCallback.onError(ctx.channel(), cause == null ? new RuntimeException("exceptionCaught cause is null") : cause);
            }
        }

    }

}
