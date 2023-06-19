package com.lzx.test.netty.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.netty.channel.Channel;

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public interface TcpServerCallback {
    /**
     * 客户端连接成功
     * @param channel
     */
    void onChannelActive(@Nullable Channel channel);

    /**
     * 获取消息
     * @param channel
     * @param message
     */
    void onChannelRead(@Nullable Channel channel,@NonNull Object message);

    /**
     * 客户端断开连接
     * @param channel
     */
    void onChannelInactive(@Nullable Channel channel);

    /**
     * 客户端连接出错
     * @param channel
     * @param throwable
     */
    void onError(@Nullable Channel channel,@NonNull Throwable throwable);

}
