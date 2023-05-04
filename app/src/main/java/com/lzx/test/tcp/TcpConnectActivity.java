package com.lzx.test.tcp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzx.test.R;

public class TcpConnectActivity extends AppCompatActivity {

    private TcpServerThread mServerThread;
    private TcpClientThread mClientThread;
    private Handler mHandler;
    private EditText mEditText;
    private Button mClientSendBtn;
    private Button mServerSendBtn;
    private Button mOpenBtn;
    private Button mCloseBtn;
    private TextView mServerTextView;
    private TextView mClientTextView;
    private static final String address = "192.168.1.221";
//    private String address = "10.13.5.131";
//    private String address = "192.168.75.83";
    private int port = 23;
    private boolean isOpen = false;

    private static final String oneToOne = "SET IN1 VIDEO OUT1\r\n";
    private static final String twoToOne = "SET IN2 VIDEO OUT1\r\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_connect);
        mEditText = findViewById(R.id.edit_text);
        mClientSendBtn = findViewById(R.id.client_send_btn);
        mServerSendBtn = findViewById(R.id.server_send_btn);
        mOpenBtn = findViewById(R.id.open_btn);
        mCloseBtn = findViewById(R.id.close_btn);
        mServerTextView = findViewById(R.id.server_receive_text);
        mClientTextView = findViewById(R.id.client_receive_text);
//        mServerSendBtn.setVisibility(View.GONE);
        mEditText.setVisibility(View.GONE);
        mOpenBtn.setVisibility(View.GONE);
        mCloseBtn.setVisibility(View.GONE);
        mServerTextView.setVisibility(View.GONE);
        mClientTextView.setVisibility(View.GONE);
        Log.d("lzx", "onCreate: "+mServerTextView.toString());

        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        String mClientContent= (String) msg.obj;
                        mClientTextView.setText(mClientContent);
                        break;
                    case 1:
                        String mServerContent= (String) msg.obj;
                        Log.d("lzx", "onCreate: 1 "+mServerTextView.toString());
                        mServerTextView.setText(mServerContent);
                        break;

                }
            }
        };

        mClientSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true) {
//                    if(mClientThread != null){
                    if (false) {
                        mClientThread.stoped();
                        //客户端断开后服务端需重新连接
                        if (mServerThread != null) {
                            mServerThread.stoped();
                            mServerThread = new TcpServerThread(false, true, mEditText.getText().toString(), mHandler);
                            mServerThread.start();
                        }
                        mClientThread = new TcpClientThread(true, false, address, port, mEditText.getText().toString(), mHandler);
                        mClientThread.start();
                        Log.d("lzx", "onClick: if");
                    } else {
                        if (mClientThread != null) {
                            mClientThread.stoped();
                            mClientThread = new TcpClientThread(true, false,
                                    address, port, oneToOne, mHandler);
                            mClientThread.start();
                        } else {
                            mClientThread = new TcpClientThread(true, false,
                                    address, port, oneToOne, mHandler);
                            mClientThread.start();
                        }
                        Toast.makeText(TcpConnectActivity.this, "发送1切1", Toast.LENGTH_SHORT).show();
                    }
                    if (isOpen) {
                        mClientThread = new TcpClientThread(true, false, address, port, mEditText.getText().toString(), mHandler);
                        mClientThread.start();
                    }
                }
            }
        });
        mServerSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mClientThread != null){
                    mClientThread.stoped();
                    mClientThread = new TcpClientThread(true, false,
                            address, port, twoToOne, mHandler);
                    mClientThread.start();
                    Log.d("lzx", "onClick: mClientThread != null");
                }else {
                    mClientThread = new TcpClientThread(true, false,
                            address, port, twoToOne, mHandler);
                    mClientThread.start();
                }
                Toast.makeText(TcpConnectActivity.this,"发送2切1",Toast.LENGTH_SHORT).show();

//                if(mServerThread == null){
//                    mServerThread = new TcpServerThread(true, true, mEditText.getText().toString(), mHandler);
//                    mServerThread.start();
//                    Log.d("lzx", "onClick: mServerThread == null");
//                }else {
//                    mServerThread.stoped();
//                    mServerThread = new TcpServerThread(true, true, mEditText.getText().toString(), mHandler);
//                    mServerThread.start();
//                    Log.d("lzx", "onClick: else");
//                }
//                if(mClientThread == null){
//                    mClientThread = new TcpClientThread(false, true, address, port, mEditText.getText().toString(), mHandler);
//                    mClientThread.start();
//                }else {
//                    mClientThread.stoped();
//                    mClientThread = new TcpClientThread(false, true, address, port, mEditText.getText().toString(), mHandler);
//                    mClientThread.start();
//                }
//                if(mServerThread != null){
////                    mServerThread.setOpen(false);
//                    mServerThread.setSend(true);
//                }
//                    mClientThread = new TcpClientThread(false, true, address, port, mEditText.getText().toString(), mHandler);
//                    mClientThread.start();
////                    Log.d("lzx", "mServerSendBtn.onClick: mClientThread == null");
//                    mServerThread = new TcpServerThread(true, false, mEditText.getText().toString(), mHandler);
//                    mServerThread.start();
                if(isOpen){
//                    if(mClientThread == null){
//                        mClientThread = new TcpClientThread(false, true, address, port, mEditText.getText().toString(), mHandler);
//                        mClientThread.start();
//                        Log.d("lzx", "mServerSendBtn.onClick: mClientThread == null");
//                    }
//                    mServerThread = new TcpServerThread(true, true, mEditText.getText().toString(), mHandler);
//                    mServerThread.start();
                }
            }
        });
        mOpenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = true;
                try {
                    if(mServerThread == null){
                        mServerThread = new TcpServerThread(false, true, mEditText.getText().toString(), mHandler);
                        mServerThread.start();
                    }
//                    mClientThread = new TcpClientThread(false, true, address, port, mEditText.getText().toString(), mHandler);
//                    mClientThread.start();
                    mServerThread = new TcpServerThread(false, true, mEditText.getText().toString(), mHandler);
                    mServerThread.start();
                    Toast.makeText(TcpConnectActivity.this,"已开启服务",Toast.LENGTH_SHORT).show();
//                    TcpClientThread mClientThread = new TcpClientThread(false, address, port, mEditText.getText().toString(), mHandler);
//                    mClientThread.start();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpen = false;
                try {
                    if(mServerThread != null){
                        mServerThread.stoped();
                    }
                    if(mClientThread != null){
                        mClientThread.stoped();
                    }
                    Toast.makeText(TcpConnectActivity.this,"已关闭服务",Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

//    private Handler mHandler = new Handler(Looper.myLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0:
//                    String mClientcontent= (String) msg.obj;
//                    mClientTextView.setText(mClientcontent);
//                    break;
//                case 1:
//                    String mServerContent= (String) msg.obj;
//                    mServerTextView.setText(mServerContent);
//                    break;
//
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
        if(mServerThread != null){
//            mServerThread.interrupt();
            mServerThread.stoped();
            Log.d("lzx1", "onDestroy: mServerThread != null");
        }
        if(mClientThread != null){
//            mClientThread.interrupt();
            mClientThread.stoped();

        }
    }

}