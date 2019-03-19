package com.example.sichunluo.fac1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int TAKE_PICTURE = 100;
    public static final int SELECT_PICTURE = 101;
    private Uri outputFileUri ;

    public static final String APP_ID = "14321335";
    public static final String API_KEY = "XTfGD9FleBDA1CbZB1jDG7Oa";
    public static final String SECRET_KEY = "xxw7y77MRREp9dVNDhmZkfVMMNXVjqPZ";

    private TextView mTextMessage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_main);

        final SQLiteDatabase mSQLiteDatabase = this.openOrCreateDatabase("myuser.db", MODE_PRIVATE, null);
        String sql = "create table if not exists " + "User" + " (username text primary key, password text)";
        mSQLiteDatabase.execSQL(sql);



        initPermission();

        PackageManager pm = getPackageManager();
        boolean permission_readStorage = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", "packageName"));


        final EditText ed1 = findViewById(R.id.et_userName);
        final EditText ed2 = findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("hereis","111");
                Cursor cur = mSQLiteDatabase.rawQuery("SELECT * FROM User", null);
                if ("admin".equals(ed1.getText().toString()) && "123456".equals(ed2.getText().toString())) {
                    Intent intent1 = new Intent(MainActivity.this, textureActivity.class);
                    startActivity(intent1);
                }else {


                    boolean alert = false;

                    if (cur != null) {
                        Log.d("hereis", "222");
                        if (cur.moveToFirst()) {
                            do {
                                int numColumn = cur.getColumnIndex("username");
                                String num = cur.getString(numColumn);
                                int numColumn2 = cur.getColumnIndex("password");
                                String num2 = cur.getString(numColumn2);
                                if (num.equals(ed1.getText().toString()) && num2.equals(ed2.getText().toString())) {
                                    Intent intent1 = new Intent(MainActivity.this, textureActivity.class);
                                    startActivity(intent1);
                                }

                            } while (cur.moveToNext());
                        }

                        alert = true;
                    }


                    Log.d("hereis", "333");

                    if (alert) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("用户名密码错误");
                        builder.setPositiveButton("OK", null);
                        builder.show();

                        ed1.setText("");
                        ed2.setText("");
                    }

                }

            }
        });


        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, register.class);
                startActivity(intent1);

            }
        });




    }

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
            ,Manifest.permission.CAMERA,Manifest.permission.INTERNET
    };
    //2、创建一个mPermissionList，逐个判断哪些权限未授予，未授予的权限存储到mPerrrmissionList中
    List<String> mPermissionList = new ArrayList<>();

    private final int mRequestCode = 100;//权限请求码


    //权限判断和申请
    private void initPermission() {

        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        }else{
            //说明权限都已经通过，可以做你想做的事情去
//            Thread myThread=new Thread(){//创建子线程
//                @Override
//                public void run() {
//                    try{
//                        sleep(2000);//使程序休眠3秒
//                        Intent it=new Intent(MainActivity.this,textureActivity.class);//启动MainActivity
//                        startActivity(it);
//                        finish();//关闭当前活动
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            };
//            myThread.start();//启动线程
        }
    }




}
