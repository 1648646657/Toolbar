package com.lzx.test.tcp;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerThread extends Thread{

    private boolean isSend;
    private boolean isOpen;
    private String msg;
    private Handler mHandler;
    private ServerSocket serverSocket;

    public TcpServerThread(boolean isSend, boolean isOpen, String msg, Handler mHandler){
        this.isSend = isSend;
        this.isOpen = isOpen;
        this.msg = msg;
        this.mHandler = mHandler;
    }

    public void stoped(){
        isOpen = false;
        try{
            serverSocket.close();
        }catch (Exception e){
            //do nothing
        }
    }

    @Override
    public void run() {
        super.run();
        InputStreamReader reader = null;
        BufferedReader bufReader = null;
        OutputStream os = null;
        InputStream is = null;
        try{
            serverSocket = new ServerSocket(12345);
//            Socket socket = serverSocket.accept();
            if (isSend){
//                ServerSocket serverSocket = new ServerSocket(12345);
                Socket socket = serverSocket.accept();
                ////发送数据
                os = socket.getOutputStream();
                os.write(("我是服务端，我给客户端发送的数据是：" + msg).getBytes());
                os.flush();
                socket.shutdownOutput();
            }
            while (isOpen) {
//                ServerSocket serverSocket = new ServerSocket(12345);
                Socket socket = serverSocket.accept();
//                //发送数据
//                os = socket.getOutputStream();
//                os.write(("我是服务端，我给客户端发送的数据是：" + msg).getBytes());
//                os.flush();
//                socket.shutdownOutput();

//                //服务端接收数据
                is = socket.getInputStream();
                reader = new InputStreamReader(is);
                bufReader = new BufferedReader(reader);
                String s = null;
                StringBuffer sb = new StringBuffer();
                while ((s = bufReader.readLine()) != null) {
                    sb.append(s);
                }
                sendMsg(1, sb.toString());
                socket.shutdownInput();
                Log.d("lzx", "TcpServerThread.run: ");

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {// 关闭IO资源
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufReader != null) {
                try {
                    bufReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    

    private void sendMsg(int what, Object object){
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mHandler.sendMessage(msg);
    }

}
