package com.lzx.test.netty;

import android.os.Message;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ConcurrentHashMap;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class TcpClient {

    private String mClientId="";
    public static ConcurrentHashMap<String, ConnectClient> concurrentHashMap = new ConcurrentHashMap();
    public TcpClientHandler tcpClientHandler;
    public Channel channel;
    public void clientConnect(String clientId, String host, int port, Handler handler) throws Exception {
        this.mClientId = clientId;
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(@NonNull Channel ch) throws Exception {
                            tcpClientHandler = new TcpClientHandler(handler);
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(tcpClientHandler);
                        }
                    });
            ChannelFuture connect = bootstrap.connect(host, port);
            channel = connect.channel();

            ChannelFuture channelFuture = connect.sync();

            //保存该客户端
            ConnectClient connectClient = new ConnectClient();
            connectClient.setTcpClient(this);
            connectClient.setNioEventLoopGroup(eventLoopGroup);
            concurrentHashMap.put(clientId, connectClient);
            Log.i("lzx", "Connect succeed");

            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            concurrentHashMap.remove(mClientId);
            eventLoopGroup.shutdownGracefully();
        }
    }

    public class TcpClientHandler extends ChannelInboundHandlerAdapter {

        private Handler handler;
//        public ChannelHandlerContext channelHandlerContext;

        public TcpClientHandler() {}

        public TcpClientHandler(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void channelActive(@NonNull ChannelHandlerContext ctx) throws Exception {
            Log.d("lzx", "客户端连接成功 :address"+ctx.channel().remoteAddress());
            ctx.channel().writeAndFlush("connect succeed");
//            channelHandlerContext = ctx;
        }

        @Override
        public void channelRead(@NonNull ChannelHandlerContext ctx, @NonNull Object msg) throws Exception {
            Log.d("lzx", "channelRead: msg = "+msg);
//            ByteBuf buf = (ByteBuf) msg;
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.readBytes(bytes);
//            String message = new String(bytes, "UTF-8");
//            Log.d("lzx", "message: 客户端接收 :"+message);
//            sendMsg(0,msg);

        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            Log.d("lzx", "channelReadComplete: ");
//            ctx.channel().writeAndFlush("数据读取完成");
        }


        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            concurrentHashMap.remove(mClientId);
            Log.d("lzx", "exceptionCaught: mClientId = "+mClientId);
            ctx.close();
        }

        private void sendMsg(int what, Object object){
            Message msg = new Message();
            msg.what = what;
            msg.obj = object;
            handler.sendMessage(msg);
        }

    }

}
