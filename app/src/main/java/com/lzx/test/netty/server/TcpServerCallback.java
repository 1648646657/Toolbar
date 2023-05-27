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

    void onChannelActive(@Nullable Channel channel);

    void onChannelReadComplete(@Nullable Channel channel);

    void onChannelRead(@Nullable Channel channel,@NonNull Object message);
    void onChannelInactive(@Nullable Channel channel);

    void onError(@Nullable Channel channel,@NonNull Throwable throwable);

}
