package com.lzx.test.netty.client;

import io.netty.channel.nio.NioEventLoopGroup;

public class ConnectClient {
    private TcpClient tcpClient;
    private NioEventLoopGroup nioEventLoopGroup;

    public TcpClient getTcpClient() {
        return tcpClient;
    }

    public void setTcpClient(TcpClient tcpClient) {
        this.tcpClient = tcpClient;
    }

    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    public void setNioEventLoopGroup(NioEventLoopGroup nioEventLoopGroup) {
        this.nioEventLoopGroup = nioEventLoopGroup;
    }
}
