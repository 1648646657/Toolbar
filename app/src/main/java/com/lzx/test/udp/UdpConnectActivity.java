package com.lzx.test.udp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzx.test.R;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpConnectActivity extends AppCompatActivity {

    private final static String SEND_IP = "10.115.138.227";
    private final static int SEND_PORT = 8090;
    private final static int RECEIVE_PORT = 8090;

    private boolean listenStatus = true;
    private byte[] mReceiveBuf;
    private byte[] mSendBuf;
    private DatagramSocket mSendSocket;
    private DatagramSocket mReceiveSocket;
    private InetAddress addresses;

    private EditText mEditText;
    private Button mSendBtn;
    private Button mReceiveBtn;
    private TextView mSendText;
    private TextView mReceiveText;

    private UdpReceiveThread mUdpReceiveThread;
    private UdpHandler mUdpHandler = new UdpHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udp_connect);

        mEditText = findViewById(R.id.udp_edit_text);
        mSendBtn = findViewById(R.id.udp_send_btn);
        mReceiveBtn = findViewById(R.id.udp_receive_btn);
        mSendText = findViewById(R.id.udp_send_text);
        mReceiveText = findViewById(R.id.udp_receive_text);

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UdpSendThread().start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUdpReceiveThread = new UdpReceiveThread();
        mUdpReceiveThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listenStatus = false;
        if(mSendSocket != null){
            mSendSocket.close();
        }
        if(mReceiveSocket != null){
            Log.d("lzx", "onDestroy: mReceiveSocket.close()");
            mReceiveSocket.close();
        }
        if(mUdpReceiveThread != null){
            Log.d("lzx", "onDestroy: mUdpReceiveThread.interrupt()" + mUdpReceiveThread.isInterrupted());
            mUdpReceiveThread.interrupt();
            Log.d("lzx", "onDestroy: mUdpReceiveThread.interrupt()" + mUdpReceiveThread.isInterrupted());
        }
    }

    class UdpHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String content0 = (String)msg.obj;
                    mSendText.setText("0:"+content0);
                    break;
                case 1:
                    String content1 = (String)msg.obj;
                    mReceiveText.setText("1:"+content1);
                    break;
                case 2:
                    String content2 = (String)msg.obj;
                    mReceiveText.setText("2:"+content2);
                    break;
            }
        }
    }

    public class UdpSendThread extends Thread{
        @Override
        public void run() {
            super.run();
            try{
                mSendBuf = ("udp发送数据:"+mEditText.getText().toString()).getBytes();
                mSendSocket = new DatagramSocket();
                addresses = InetAddress.getByName(SEND_IP);
                DatagramPacket sendPacket = new DatagramPacket(mSendBuf, mSendBuf.length, addresses, SEND_PORT);
                mSendSocket.send(sendPacket);
                mSendSocket.close();
                sendMsg(0,"udp发送成功："+new String(mSendBuf));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class UdpReceiveThread extends Thread{
        @Override
        public void run() {
            super.run();
            try{
                mReceiveSocket = new DatagramSocket(RECEIVE_PORT);
                addresses = InetAddress.getByName(SEND_IP);

                while (listenStatus){
                    byte[] inbuf = new byte[1024];
                    DatagramPacket receivePacket = new DatagramPacket(inbuf, inbuf.length);
                    mReceiveSocket.receive(receivePacket);
                    if(!receivePacket.getAddress().equals(addresses)){
                        sendMsg(2,"udp收到未知报文");
                    }
                    mReceiveBuf = receivePacket.getData();
                    sendMsg(1,"udp接收成功："+new String(mReceiveBuf));
                }
            }catch (Exception e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }


    private void sendMsg(int what, Object object){
        Message msg = new Message();
        msg.what = what;
        msg.obj = object;
        mUdpHandler.sendMessage(msg);
    }

}