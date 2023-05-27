package com.lzx.test.netty.server;

import androidx.annotation.Nullable;

import io.netty.channel.ChannelFuture;

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public interface ITcpServer {

    boolean isOpen();
    @Nullable
    ChannelFuture open();

    void close();
    /**
     * 发送消息
     */
    @Nullable
    ChannelFuture sendMessage(Object message);
    /**
     * 设置回调
     */
    void setCallback(TcpServerCallback callback);
}
