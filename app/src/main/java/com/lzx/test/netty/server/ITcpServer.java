package com.lzx.test.netty.server;

import androidx.annotation.Nullable;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public interface ITcpServer {

    /**
     * 判断是否已已开启服务端
     * @return
     */
    boolean isOpen();
    /**
     * 开启服务端
     * @return
     */
    @Nullable
    ChannelFuture open();
    /**
     * 关闭服务端
     */
    void close();
    /**
     * 向某个客户端发送消息
     * @param channel
     * @param message
     */
    void sendMessage(Channel channel, Object message);
    /**
     * 向所有客户端发送消息
     * @param message
     */
    void sendMessageToAll(Object message);
    /**
     * 设置回调
     * @param callback
     */
    void setCallback(TcpServerCallback callback);
}
