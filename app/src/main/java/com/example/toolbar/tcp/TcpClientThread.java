package com.example.toolbar.tcp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpClientThread extends Thread{

    private boolean isSend;
    private boolean isOpen;
    private String address;
    private int port;
    private String msg;
    private Handler mHandler;


    public TcpClientThread(boolean isSend, boolean isOpen, String address, int port, String msg, Handler handler){
        this.isSend = isSend;
        this.isOpen = isOpen;
        this.address = address;
        this.port = port;
        this.msg = msg;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        Log.d("lzx", "TcpClientThread.run: ");
        sendSocket();
    }

    public void stoped(){
        isOpen = false;
    }

    private void sendSocket(){
        InputStreamReader reader = null;
        BufferedReader bufReader = null;
//        Socket socket = null;

        try {
            if(isSend){
                Socket socket = new Socket(address,port);
                //客户端发送数据
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(("我是客户端，我给服务端发送的数据是："+msg).getBytes());
                outputStream.flush();
                socket.shutdownOutput();
            }
            while (isOpen){
                Socket socket = new Socket(address,port);
                //客户端接收数据
                InputStream inputStream = socket.getInputStream();
                reader = new InputStreamReader(inputStream);
                bufReader = new BufferedReader(reader);
                String s = null;
                StringBuffer sb = new StringBuffer();
                while ((s = bufReader.readLine()) != null){
                    sb.append(s);
                }
                sendMsg(0, sb.toString());
            }
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (bufReader != null)
                    bufReader.close();
            }catch (IOException ex) {
                ex.printStackTrace();
            }
//            try {
//                if (socket != null)
//                    socket.close();
//            }catch (IOException ex) {
//                ex.printStackTrace();
//            }
        }

    }

    private void sendMsg(int what, Object object){
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

}
