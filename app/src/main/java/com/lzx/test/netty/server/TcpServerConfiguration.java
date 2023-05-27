package com.lzx.test.netty.server;

/**
 * @author: lzx
 * @date: 2023.5.11
 * @description:
 */
public class TcpServerConfiguration {

    public int port;
    public int maxFrameLength;

    public TcpServerConfiguration(int port) {
        this.port = port;
    }
}
