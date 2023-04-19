package com.example.toolbar.http;

import com.example.toolbar.http.object.User;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttp {

    //get
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client= new OkHttpClient();
        Request request=new Request.Builder()
                .url(address)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    //post
    public static void postOkHttpRequest(String address, User user, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("name", user.getName())
                .add("age",String.valueOf(user.getAge()))
                .build();

        Request request = new Request.Builder()
                .url(address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    //put
    public static void putOkHttpRequest(MediaType mediaType, String address, String localPath,
                                        okhttp3.Callback callback) throws IOException {
        File file = new File(localPath);
        if(file == null){
            return;
        }
        RequestBody requestBody = RequestBody.create(file,mediaType);
        Request request = new Request.Builder()
                .url(address)
                .put(requestBody)
                .build();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();

        client.newCall(request).enqueue(callback);

    }
    //上传图片
    public void putImg(String address, String localPath, okhttp3.Callback callback) throws IOException {
        MediaType imageType = MediaType.parse("image/jpeg; charset=utf-8");
        putOkHttpRequest(imageType, address, localPath, callback);
    }

}
