package com.example.toolbar.tcp;

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

import com.example.toolbar.R;

public class TcpConnectActivity extends AppCompatActivity {

    private TcpServerThread mServerThread;
    private TcpClientThread mClientThread;
    private EditText mEditText;
    private Button mClientSendBtn;
    private Button mServerSendBtn;
    private Button mOpenBtn;
    private Button mCloseBtn;
    private TextView mServerTextView;
    private TextView mClientTextView;
    private String address = "192.168.10.12";
//    private String address = "10.13.5.131";
//    private String address = "192.168.75.83";
    private int port = 12345;
    private boolean isOpen = false;

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

        mClientSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpen){
                    mClientThread = new TcpClientThread(true, false, address, port, mEditText.getText().toString(), mHandler);
                    mClientThread.start();
                }
            }
        });
        mServerSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String mClientcontent= (String) msg.obj;
                    mClientTextView.setText(mClientcontent);
                    break;
                case 1:
                    String mServerContent= (String) msg.obj;
                    mServerTextView.setText(mServerContent);
                    break;

            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
//        mServerThread = new TcpServerThread(false);
//        mServerThread.start();
//        if(mServerThread == null){
//            mServerThread = new TcpServerThread(false, mEditText.getText().toString(), mHandler);
//            mServerThread.start();
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOpen = false;
        if(mServerThread != null){
            mServerThread.interrupt();
        }
    }

}