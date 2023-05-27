package com.lzx.test.netty.client;

public class MsgItem {
    String date;
    String type;
    String message;
    int color;

    public MsgItem() {}

    public MsgItem(String date, String type, String message, int color) {
        this.date = date;
        this.type = type;
        this.message = message;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
