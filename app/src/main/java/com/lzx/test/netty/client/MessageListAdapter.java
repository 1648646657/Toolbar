package com.lzx.test.netty.client;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.lzx.test.R;

import java.util.ArrayList;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHoder>{

    Context mContext;
    ArrayList<MsgItem> mMsgList;
    MessageViewHoder mMessageViewHoder;

    public MessageListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public MessageListAdapter(Context mContext, ArrayList<MsgItem> mMsgList) {
        this.mContext = mContext;
        this.mMsgList = mMsgList;
    }

    @NonNull
    @Override
    public MessageViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_message,parent,false);
        mMessageViewHoder = new MessageViewHoder(view);
        return mMessageViewHoder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHoder holder, int position) {
        MsgItem msgItem = mMsgList.get(position);
        holder.mDataTv.setText(msgItem.date);
        holder.mTypeTv.setText(msgItem.type);
        holder.mMessageTv.setText(msgItem.message);
        holder.mMessageTv.setTextColor(msgItem.color);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
//        if(mMsgList == null){
//            return 0;
//        }
        return mMsgList.size();
    }

    class MessageViewHoder extends RecyclerView.ViewHolder {
        TextView mDataTv;
        TextView mTypeTv;
        TextView mMessageTv;

        public MessageViewHoder(@NonNull View itemView ) {
            super(itemView);
            mDataTv = itemView.findViewById(R.id.tv_date);
            mTypeTv = itemView.findViewById(R.id.tv_type);
            mMessageTv = itemView.findViewById(R.id.tv_message);
        }
    }

    public void updateData(ArrayList<MsgItem> msgList){
        this.mMsgList = msgList;
        notifyDataSetChanged();
    }

}