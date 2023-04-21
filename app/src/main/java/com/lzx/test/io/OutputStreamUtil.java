package com.lzx.toolbar.io;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamUtil {

    public static String FileSaveToInside(Context context, String fileName, Bitmap bitmap){
        FileOutputStream fos = null;
        String path = null;
        try{
            File folder = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            if(folder.exists() || folder.mkdir()){
                File file = new File(folder, fileName);
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                path = file.getAbsolutePath();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try{
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return path;
    }

    public static String FileSaveToPublic(Context context, String fileName, Bitmap bitmap){
        String path = null;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            FileOutputStream fos = null;
            try{
                File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if(folder.exists() || folder.mkdir()){
                    File file = new File(folder, fileName);
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    path = file.getAbsolutePath();
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(fos != null){
                    try{
                        fos.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }

        }else {
            String folder = Environment.DIRECTORY_PICTURES;
            ContentValues contentValues = new ContentValues();
            //设置路径
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            //设置格式
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            //设置路径
            contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, folder);
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            OutputStream os = null;
            try{
                if(uri != null){
                    os = context.getContentResolver().openOutputStream(uri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    path = uri.getPath();
                }

            }catch (IOException e){
                e.printStackTrace();
            }finally{
                if(os != null){
                    try{
                        os.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        return path;
    }

    public static Uri FileGetFromPublic(Context context, String filePath, String fileName){
        String queryPath;
        if(filePath.endsWith("/")){
            filePath = filePath+File.separator;
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            filePath = filePath+fileName;
            queryPath = MediaStore.Images.Media.DATA;
        }else {
            //Android10及以上
            //使用RELATIVE_PATH字段做查询
            queryPath = MediaStore.Images.Media.RELATIVE_PATH;
        }

        String selection = queryPath +"=? and "+MediaStore.Images.Media.DISPLAY_NAME+"=?";
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Files.FileColumns._ID}, selection, new String[]{filePath, fileName}, null);
        if (cursor != null && cursor.moveToFirst()) {
            //查出id
            @SuppressLint("Range")
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            //根据id查询URI
            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
            return uri;
        }
        //关闭查询
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }



}
