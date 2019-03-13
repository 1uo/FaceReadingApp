package com.example.sichunluo.fac1;


import android.content.Intent;
import android.graphics.Matrix;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.face.AipFace;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;

import static android.provider.UserDictionary.Words.APP_ID;

public class landmarkActivity extends AppCompatActivity {

    public static final String APP_ID = "14321335";
    public static final String API_KEY = "XTfGD9FleBDA1CbZB1jDG7Oa";
    public static final String SECRET_KEY = "xxw7y77MRREp9dVNDhmZkfVMMNXVjqPZ";
    public  double a1[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark);

        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        // setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


//        Button bt= findViewById(R.id.returnbutton);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent= new Intent(landmark.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy)
            ;}


        myview myView=new myview(this);
        setContentView(myView);


//        try {
//            detectface();
//        } catch (UnsupportedEncodingException e) {
//            Toast.makeText(this,"error 1",Toast.LENGTH_SHORT);
//            e.printStackTrace();
//            Log.d("Err:","err 2");
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Toast.makeText(this,"error 2",Toast.LENGTH_SHORT);
//            Log.d("Err:","err 3");
//        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void detectface() throws UnsupportedEncodingException, JSONException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();

//编码

        //System.out.println(encodedText);

        InputStream is = null;
        byte[] data = {1};
// 读取图片字节数组
        try {
            String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            // 图片文件路径
            String filePath = sdCardPath + File.separator + "test.png";
            File f = new File(filePath);
            Bitmap bmp;
            BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
            mBitmapOptions.inScaled = true;
            mBitmapOptions.inSampleSize = 4;
            bmp=BitmapFactory.decodeFile(filePath,mBitmapOptions);

            bmp=(getBitmap(bmp));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //这里100表示不压缩，把压缩后的数据存放到baos中
            bmp.compress(Bitmap.CompressFormat.PNG,30,baos);
            byte[] bytes = baos.toByteArray();

//            int bytes = bmp.getByteCount();
//
//            ByteBuffer buf = ByteBuffer.allocate(bytes);
//            bmp.copyPixelsToBuffer(buf);

            // data = buf.array();
            data=bytes;


        } catch (Exception e) {
            Log.d("Err:","err 1");
            e.printStackTrace();
        }


        // 初始化一个AipFace
        AipFace client = new AipFace(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
        //client.setHttpProxy("proxy_host", 1001);  // 设置http代理
        //client.setSocketProxy("proxy_host", 1002);  // 设置socket代理

        // 可选：设置log4j日志输出格式，若不设置，则使用默认配置
        // 也可以直接通过jvm启动参数设置此环境变量

        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");

        // 调用接口
//        String path = "test.jpg";
        //String face_field = "age,beauty,expression,face_shape,gender,glasses,landmark,race,quality";
        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("face_field", "age");
//        options.put("face_field", "beauty");
//        options.put("face_field", "expression");
        options.put("face_field", "landmark");
        // options.put("face_field", "gender");
        JSONObject res = client.detect(encoder.encodeToString(data), "BASE64",options);
        //System.out.println(res.toString(2));
        //TextView tx = (TextView) findViewById(R.id.noticeboard);
        //tx.setText(res.toString(2));

        JSONArray ja = res.getJSONObject("result").getJSONArray("face_list").getJSONObject(0)
                .getJSONArray("landmark72");

        double a[][]=new double[72][2];

        for(int i=0;i<72;i++){
            a[i][0]=Double.valueOf(ja.getJSONObject(i).get("x").toString());
            a[i][1]=Double.valueOf(ja.getJSONObject(i).get("y").toString());
        }

        a1=a;

    }




    private class myview extends View{

        public myview(Context context) {
            super(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);

            Bitmap bitmap=null;

            try {
                detectface();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                // 图片文件路径
                String filePath = sdCardPath + File.separator + "test.png";

                File f = new File(filePath);
                if(f.exists()) {

                    BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
                    mBitmapOptions.inScaled = true;
                    mBitmapOptions.inSampleSize = 4;
                    bitmap = BitmapFactory.decodeFile(filePath, mBitmapOptions);

                }
                //这里100表示不压缩，把压缩后的数据存放到baos中

            } catch (Exception e) {
                e.printStackTrace();
            }

            Paint paint=new Paint();
            canvas.drawBitmap((getBitmap(bitmap)),0,0,paint);

            paint.setColor(Color.GREEN);
            //double a[][]=new double[72][2];
            if(a1!=null) {
                for (int i = 0; i < 72; i++)
                    canvas.drawCircle((float) a1[i][0], (float) a1[i][1], 5, paint);
            }

            paint.setTextSize(100);
            canvas.drawText("abceeeee",50,50,paint);


        }
    }

    private Bitmap getBitmap(Bitmap bitmap) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        //设置想要的大小

        Matrix m = new Matrix();
        int orientationDegree = 270;
        m.setRotate(orientationDegree, (float) bitmap.getWidth() / 64, (float) bitmap.getHeight() / 64);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            bitmap=bm1;
            bm1=null;
        } catch (OutOfMemoryError ex) {
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Log.e("width","width:"+width);
        Log.e("height","height:"+height);



        DisplayMetrics dm2 = getResources().getDisplayMetrics();


        int newWidth=dm2.widthPixels;
        int newHeight=dm2.heightPixels;

        //计算压缩的比率
        float scaleWidth=((float)newWidth)/width ;
        float scaleHeight=((float)newHeight)/height ;

        //获取想要缩放的matrix
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);

        //获取新的bitmap
        bitmap=Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.getWidth();
        bitmap.getHeight();
        Log.e("newWidth","newWidth"+bitmap.getWidth());
        Log.e("newHeight","newHeight"+bitmap.getHeight());
        return bitmap;
    }






}