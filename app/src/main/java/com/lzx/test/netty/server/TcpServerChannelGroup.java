package com.lzx.test.netty.server;

import android.util.Log;

import java.util.Iterator;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author: lzx
 * @date: 2023.6
 * @description: tcp已连接客户端通道组
 */
public class TcpServerChannelGroup {

    private static final String TAG = "TcpServerChannelGroup";
    private static final ChannelGroup mChannelGroup= new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void add(Channel channel) {
        mChannelGroup.add(channel);
    }
    /**
     * 向所有通道发送消息
     * @param message
     * @return
     */
    public static ChannelGroupFuture sendMessageToAll(Object message) {
        return mChannelGroup.writeAndFlush(message, new ChannelMatchers());
    }
    /**
     * 向某个客户端通道发送消息
     */
    public static ChannelFuture sendMessage(Channel channel, Object object) {
        for (Channel channel0 : mChannelGroup) {
            if (channel0.equals(channel)) {
                Log.d(TAG, "已向客户端["+channel.remoteAddress()+"]发送消息："+object.toString());
                return channel.writeAndFlush(object);
            }
        }
        Log.e(TAG, "不存在该客户端，发送消息失败！");
        return null;
    }
    /**
     * 获取所有通道遍历
     * @return
     */
    public static Iterator<Channel> getChannelGroup() {
        return mChannelGroup.iterator();
    }
    /**
     * 移除某个通道
     * @param channel
     * @return
     */
    public static boolean disconnect(Channel channel) {
        return mChannelGroup.remove(channel);
    }

    /**
     * 断开所有通道
     * @return
     */
    public static ChannelGroupFuture disconnectAll() {
        return mChannelGroup.disconnect();
    }
    public static ChannelGroupFuture disconnect(ChannelMatcher matcher) {
        return mChannelGroup.disconnect(matcher);
    }

    /**
     * 获取连接数量
     * @return
     */
    public static int size() {
        return mChannelGroup.size();
    }
    private static class ChannelMatchers implements ChannelMatcher {
        @Override
        public boolean matches(Channel channel) {
            return true;
        }
    }

}
