package com.lzx.test.netty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.lzx.test.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NettyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String oneToOne = "SET IN1 VIDEO OUT1\r\n";
    private static final String twoToOne = "SET IN2 VIDEO OUT1\r\n";
    private static final String host = "192.168.1.138";
    private static final int port = 8080;
    private static final String clientId = "1";
    private Button oneToOneBtn;
    private Button twoToOneBtn;
    private Button connectBtn;
    private Button disConnectBtn;
    private Button cleanBtn;
    private Button sendBtn;
    private Spinner spinner;
    private ArrayAdapter<String> arrayAdapter;
    private RecyclerView recyclerView;
    private MessageListAdapter mMessageListAdapter;
    ArrayList<MsgItem> mMsgList = new ArrayList<>();

    private Handler mHandler;

    private ConnectClient connectClient;
    private TcpClient mTcpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_netty);

        init();
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        String receiveData= (String) msg.obj;
                        addMsgList("RECEIVE : ", receiveData, Color.RED);
                        break;
                }
            }
        };

    }

    public void init() {
        oneToOneBtn = findViewById(R.id.bt_one_to_one);
        twoToOneBtn = findViewById(R.id.bt_two_to_one);
        connectBtn = findViewById(R.id.bt_connect);
        disConnectBtn = findViewById(R.id.bt_disconnect);
        cleanBtn = findViewById(R.id.bt_clean);
        sendBtn = findViewById(R.id.bt_send);
        sendBtn.setOnClickListener(this);
        cleanBtn.setOnClickListener(this);
        disConnectBtn.setOnClickListener(this);
        oneToOneBtn.setOnClickListener(this);
        twoToOneBtn.setOnClickListener(this);
        connectBtn.setOnClickListener(this);
        spinner = findViewById(R.id.spinner);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item,getResources().getStringArray(R.array.messages));
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0,true);
        recyclerView = findViewById(R.id.rv_message_log);
        mMessageListAdapter = new MessageListAdapter(this,mMsgList);
        recyclerView.setAdapter(mMessageListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_connect:
                Toast.makeText(this, "正在连接...", Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                new TcpClient().clientConnect(clientId, host, port, mHandler);
                            } catch (Exception e) {
                                Log.d("lzx", "连接失败 ");
                                e.printStackTrace();
                            }
                        }
                    }).start();
                break;
            case R.id.bt_disconnect:
                connectClient = TcpClient.concurrentHashMap.get(clientId);
                if(connectClient != null){
                    mTcpClient = connectClient.getTcpClient();
                    mTcpClient.channel.close();
                    Toast.makeText(this, "连接已关闭", Toast.LENGTH_SHORT).show();
                    Log.d("lzx", "连接已关闭");
                }
                break;
            case R.id.bt_one_to_one:
                sendMessage(oneToOne);
                addMsgList("SEND : ", oneToOne, Color.GREEN);
                break;
            case R.id.bt_two_to_one:
                sendMessage(twoToOne);
                addMsgList("SEND : ", twoToOne, Color.GREEN);
                break;
            case R.id.bt_clean:
                mMsgList.clear();
                mMessageListAdapter.updateData(mMsgList);
            case R.id.bt_send:
                sendMessage(spinner.getSelectedItem().toString()+"\r\n");
                addMsgList("SEND : ", spinner.getSelectedItem().toString()+"\r\n", Color.GREEN);
                break;
        }
    }

    private void sendMessage(String message){
        connectClient = TcpClient.concurrentHashMap.get(clientId);
        if(connectClient == null){
            Toast.makeText(this, "发送失败1", Toast.LENGTH_SHORT).show();
            Log.d("lzx", "服务端未连接");
            return;
        }
        try{
            mTcpClient = connectClient.getTcpClient();
            if(mTcpClient == null){
                Toast.makeText(this, "发送失败2", Toast.LENGTH_SHORT).show();
                return;
            }
//            mTcpClient.tcpClientHandler.channelHandlerContext.writeAndFlush(message);
            mTcpClient.channel.writeAndFlush(message);
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "发送失败3", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
//        finally {
//            connectClient.getNioEventLoopGroup().shutdownGracefully();
//        }
    }

    public void addMsgList(String type, String message, int color){
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        MsgItem msgItem = new MsgItem();
        msgItem.setDate(" [ "+formatter.format(date)+" ]#");
        msgItem.setType(type);
        msgItem.setMessage(message);
        msgItem.setColor(color);
        mMsgList.add(msgItem);
        mMessageListAdapter.updateData(mMsgList);
        recyclerView.scrollToPosition(mMessageListAdapter.getItemCount()-1);
    }


}