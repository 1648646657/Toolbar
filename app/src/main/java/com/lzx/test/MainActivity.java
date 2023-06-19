package com.lzx.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzx.test.CustomView.MyConstraintLayout;
import com.lzx.test.CustomView.MyView;
import com.lzx.test.calendar.CalendarActivity;
import com.lzx.test.http.OkHttp;
import com.google.android.material.navigation.NavigationView;
import com.lzx.test.netty.TcpServerActivity;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout mDrawerLayout;
    private TextView responseText;
    private String responseData;

    private TextView textView2;

    private MyView myView;
    private MyConstraintLayout myLayout;

    private Intent listenIntent = null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 全屏显示，隐藏状态栏和导航栏，拉出状态栏和导航栏显示一会儿后消失。
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                //| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN//隐藏状态栏
                                //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION//隐藏导航栏
                                //| View.SYSTEM_UI_FLAG_FULLSCREEN
                                //| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY//滑动时显示
                );
                Log.d("lzx", "onCreate: >=Build.VERSION_CODES.LOLLIPOP");
            } else {
                // 全屏显示，隐藏状态栏
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
                Log.d("lzx", "onCreate: <Build.VERSION_CODES.LOLLIPOP");
            }
        }

////        lzx
//        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE //稳定布局
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_IMMERSIVE
//                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
//        getWindow().getDecorView().setSystemUiVisibility(flags);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout =(DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);
        }

//        myView= (MyView) findViewById(R.id.myview);
//        myView.setTextView1("lizhongxi");
        myLayout=(MyConstraintLayout) findViewById(R.id.myLayout);
        myLayout.setTextView("lizhongxi");

        textView2=(TextView) findViewById(R.id.textView2);

//网络请求
        responseText=(TextView) findViewById(R.id.text_view);
        //sendRequestWithOkHttp();

//NavigationView滑动菜单
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

//        Intent intent =new Intent();
//        intent.setAction("hongshitx.intent.action.SHOW_NAV_BAR");
//        intent.putExtra("SHOW_NAV_BAR", 0);
//        sendBroadcast(intent);

        listenIntent = new Intent(this, ActivityListenSevice.class);
//        startService(listenIntent);

        toARGB();
    }

    public void toARGB() {
        System.out.println("透明度 | 十六进制");
        System.out.println("---- | ----");
        for (double i = 1; i >= 0; i -= 0.01) {
            i = Math.round(i * 100) / 100.0d;
            int alpha = (int) Math.round(i * 255);
            String hex = Integer.toHexString(alpha).toUpperCase();
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            int percent = (int) (i * 100);
            Log.d("TAG", "toARGB: "+percent+"---"+hex+"---"+alpha+"---"+i);
        }
    }

    public static String rgbAlphaToHex(int red, int green, int blue, String alpha){
        int alphaInt = Integer.parseInt(alpha);
        String hex = Integer.toHexString(alphaInt).toUpperCase();
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        String hr = Integer.toHexString(red);
        String hg = Integer.toHexString(green);
        String hb = Integer.toHexString(blue);
        Log.d(TAG, "rgbAlphaToHex: #"+hex+hr+hg+hb);
        return "#"+hex+hr+hg+hb;
    }
    public boolean isHuawei() {
        if (Build.BRAND == null) {
            return false;
        } else {
            return Build.BRAND.toLowerCase().equals("huawei") || Build.BRAND.toLowerCase().equals("honor");
        }
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private void showActivity(@NonNull String packageName, @NonNull String activityDir) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(packageName, activityDir));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 跳转到指定应用的首页
     */
    private void showActivity(@NonNull String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        startActivity(intent);
    }

    private void goHuaweiSetting() {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity");
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lzx", "onResume: 手机厂商: "+Build.BRAND);
        Log.d("lzx", "onResume: SDK版本: "+Build.VERSION.SDK_INT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
//                Intent intent=new Intent(this,TweenActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.scale,0);
//                Toast.makeText(this,"clicked Backup",Toast.LENGTH_SHORT).show();imageSaver
//                Intent intent=new Intent(this, CameraActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.scale,0);
                Intent intent=new Intent(this, AppTest.class);
                startActivity(intent);

//                if(isHuawei()){
//                    goHuaweiSetting();
//                }

                Toast.makeText(this,"Clicked Backup",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Intent intent1=new Intent(this, TcpServerActivity.class);
                startActivity(intent1);
                Toast.makeText(this,"Clicked Delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"Clicked Settings",Toast.LENGTH_SHORT).show();
//                sendRequestWithOkHttp();
                OkHttp.sendOkHttpRequest("https://www.baidu.com",new okhttp3.Callback(){

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        if(response.body() != null){
                            responseData= response.body().string();
                            showResponse(responseData);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("lzx", "onFailure: ");
                                Toast.makeText(MainActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                break;
            case R.id.provider:
//                Uri uri=Uri.parse("content://com.example.mallproject.provider/User");
//                Cursor cursor=getContentResolver().query(uri, new String[]{"account"},"account=?",
//                        new String[]{"lzx"},null);
//                if(cursor!=null){
//                    while (cursor.moveToNext()){
//                        @SuppressLint("Range") String account=cursor.getString(cursor.getColumnIndex("account"));
//                        Log.d("lzx","account:"+account);
//                        textView2.setText(account);
//                    }
//                    cursor.close();
//                }else {
//                    Intent intent0=new Intent(this, DragAndDropActivity.class);
//                    startActivity(intent0);
//                    Log.d("lzx", "onOptionsItemSelected: cursor == null");
//                }
                finish();
                break;
            default:
                break;
        }
        return  true;
    }
//
//    //发送网络请求
//    private void sendRequestWithOkHttp(){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    OkHttpClient client=new OkHttpClient();
//                    Request request=new Request.Builder()
//                            .url("https://www.baidu.com")
//                            .build();
//                    Response response=client.newCall(request).execute();
//                    String responseData=response.body().string();
//                    showResponse(responseData);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
    //展示网络数据
    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(listenIntent != null){
            stopService(listenIntent);
        }
    }
}