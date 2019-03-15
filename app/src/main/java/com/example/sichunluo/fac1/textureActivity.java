package com.example.sichunluo.fac1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import id.zelory.compressor.Compressor;

public class textureActivity extends AppCompatActivity {
    private MyTextureView myTextureView;

    public static final String APP_ID = "14321335";
    public static final String API_KEY = "XTfGD9FleBDA1CbZB1jDG7Oa";
    public static final String SECRET_KEY = "xxw7y77MRREp9dVNDhmZkfVMMNXVjqPZ";
    public  double a1[][];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getSupportActionBar().hide();
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_texture);



        final RelativeLayout test = findViewById(R.id.test);
        myTextureView = findViewById(R.id.mytextureview);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy)
            ;}

        final myview myview=new myview(this);


        TimerTask task = new TimerTask() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {

                Date   startDate1   =   new Date(System.currentTimeMillis());


                Date   startDate   =   new Date(System.currentTimeMillis());

                //detectface2();

                try {
                    detectface();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Date   endDate   =   new   Date(System.currentTimeMillis());

                long diff1 = endDate.getTime() - startDate.getTime();

                Log.d("timecost1",diff1+"");


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        test.removeView(myview);
                        test.addView(myview);


                    }
                });

            }
        };
        java.util.Timer timer = new java.util.Timer(true);
        timer.schedule(task, 1500, 70);




        findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitDialog01();
            }
        });
        findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2= new Intent(textureActivity.this,gallery.class);
                startActivity(intent2);
            }
        });
        findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myTextureView.take();
            }
        });
        findViewById(R.id.bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(textureActivity.this,result.class);
                startActivity(intent1);
            }
        });
        findViewById(R.id.bt5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(textureActivity.this,setting.class);
                startActivity(intent1);
            }
        });



    }

    private void showExitDialog01(){
        new AlertDialog.Builder(this)
                .setTitle("使用说明")
                .setMessage(this.getResources().getText(R.string.help))
                .setPositiveButton(("OK"), null)
                .show();
    }
    private void showExitDialog02(){
        new AlertDialog.Builder(this)
                .setTitle("资讯")
                .setMessage("资讯")
                .setPositiveButton("确定", null)
                .show();
    }


    private void detectface2() {
        Bitmap bmp;
        BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
        mBitmapOptions.inScaled = true;
        mBitmapOptions.inSampleSize = 8;
        mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

//            bmp=new Compressor(this).compressToBitmap(new File(filePath));;
//
//            bmp=(getBitmap(bmp));

        //bmp=BitmapFactory.decodeResource(getResources(),R.id.mytextureview,mBitmapOptions);

        bmp=myTextureView.getBitmap();
        Log.d("timecost(bitmap1)",bmp.getByteCount()+"");



        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //这里100表示不压缩，把压缩后的数据存放到baos中
        bmp.compress(Bitmap.CompressFormat.JPEG,1,baos);
        byte[] bytes = baos.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmap.compress(Bitmap.CompressFormat.JPEG,1,baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

        bitmap = BitmapFactory.decodeStream(isBm, null, mBitmapOptions);

        Log.d("timecost(bitmap2)",bitmap.getByteCount()+"");


        //bitmap.compress(Bitmap.CompressFormat.JPEG,1,baos);
        Date   startDate   =   new Date(System.currentTimeMillis());

        //detectface();





        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());
        List<VisionDetRet> results = faceDet.detect(bitmap);
        double a[][]=new double[72][2];
        int i=0;
        for (final VisionDetRet ret : results) {
            String label = ret.getLabel();
            int rectLeft = ret.getLeft();
            int rectTop = ret.getTop();
            int rectRight = ret.getRight();
            int rectBottom = ret.getBottom();
            Log.d("jieguo1",rectBottom+"");
            // Get 68 landmark points
            ArrayList<Point> landmarks = ret.getFaceLandmarks();
            for (Point point : landmarks) {
                int pointX = point.x;
                int pointY = point.y;
                a[i][0]=pointX;
                a[i][1]=pointY;
                i++;
                //Log.d("jieguo2",pointX+"-");
            }
        }

        a1=a;
        Date   endDate   =   new   Date(System.currentTimeMillis());

        long diff1 = endDate.getTime() - startDate.getTime();
        Log.d("timecost2(method2)",diff1+"");

    }

    private Handler mainHandler= new Handler()
    {
        public void dispatchMessage(android.os.Message msg) {


        };
    };

    @Override
    protected void onStart() {
        super.onStart();
        myTextureView.startPreview();
    }

    @Override
    protected void onStop() {
        myTextureView.stopPreview();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        myTextureView.releaseTextureView();
        super.onDestroy();
    }

    private class myview extends View{


        public myview(textureActivity context) {
            super(context);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);




            Paint paint=new Paint();
            //canvas.drawBitmap((getBitmap(bitmap)),0,0,paint);

            paint.setColor(Color.GREEN);
            if(a1!=null) {
            //double a[][]=new double[72][2];
            double q=a1[0][0];
            double w=a1[0][0];
            double e=a1[0][1];
            double r=a1[0][1];

                for (int i = 0; i < 72; i++) {
                    canvas.drawCircle((float) a1[i][0], (float) a1[i][1], 5, paint);

                    if(a1[i][0]>q){
                        q=a1[i][0];
                    }
                    if(a1[i][0]<w){
                        w=a1[i][0];
                    }
                    if(a1[i][1]>e){
                        e=a1[i][1];
                    }
                    if(a1[i][1]<r){
                        r=a1[i][1];
                    }

                    Log.d("chulaile",q+" "+w+" "+e+" "+r);
                }


            paint.setColor(Color.BLUE);
            paint.setStrokeWidth((float) 4.0);
            //canvas.drawRect((float)q,(float)w,(float) r,(float) e,paint);
            canvas.drawLine((float)q,(float)r,(float) q,(float) e,paint);
            canvas.drawLine((float)w,(float)r,(float) w,(float) e,paint);
            canvas.drawLine((float)w,(float)r,(float) q,(float) r,paint);
            canvas.drawLine((float)w,(float)e,(float) q,(float) e,paint);
            //canvas.drawLine((float)q,(float)w,(float) q,(float) r,paint);
          //  canvas.drawLine((float)w,(float)q,(float) w,(float) e,paint);
            //canvas.drawLine((float)e,(float)w,(float) r,(float) e,paint);

           // canvas.drawLine((float)q,(float)q,(float)r,(float)e,paint);

            //canvas.draw




        }}


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

            Date   startDate   =   new Date(System.currentTimeMillis());


            Bitmap bmp;
            BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
            mBitmapOptions.inScaled = true;
            mBitmapOptions.inSampleSize = 4;
            mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

//            bmp=new Compressor(this).compressToBitmap(new File(filePath));;
//
//            bmp=(getBitmap(bmp));

            //bmp=BitmapFactory.decodeResource(getResources(),R.id.mytextureview,mBitmapOptions);

            bmp=myTextureView.getBitmap();



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //这里100表示不压缩，把压缩后的数据存放到baos中
            bmp.compress(Bitmap.CompressFormat.JPEG,1,baos);
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, mBitmapOptions);

            //bitmap.compress(Bitmap.CompressFormat.JPEG,1,baos);




            //String sdCardPath = Environment.getExternalStorageDirectory().getPath();
            // 图片文件路径
            //String filePath = sdCardPath + File.separator + "test.jpeg";
            //saveBitmapFile(bitmap,filePath);

            byte[] bytes = baos.toByteArray();


            Date   endDate   =   new   Date(System.currentTimeMillis());

            long diff1 = endDate.getTime() - startDate.getTime();

            Log.d("timecost5(read data)",diff1+"");



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



        // 调用接口
//        String path = "test.jpg";
        //String face_field = "age,beauty,expression,face_shape,gender,glasses,landmark,race,quality";
        HashMap<String, String> options = new HashMap<String, String>();
//        options.put("face_field", "age");
//        options.put("face_field", "beauty");
//        options.put("face_field", "expression");
        options.put("face_field", "landmark");
        // options.put("face_field", "gender");
        Date   startDate   =   new Date(System.currentTimeMillis());


        JSONObject res = client.detect(encoder.encodeToString(data), "BASE64",options);

        JSONArray ja = res.getJSONObject("result").getJSONArray("face_list").getJSONObject(0)
                .getJSONArray("landmark72");


        Date   endDate   =   new   Date(System.currentTimeMillis());

        long diff1 = endDate.getTime() - startDate.getTime();

        Log.d("timecost4(web)",diff1+"");

        //System.out.println(res.toString(2));
        //TextView tx = (TextView) findViewById(R.id.noticeboard);
        //tx.setText(res.toString(2));



        double a[][]=new double[72][2];

        for(int i=0;i<72;i++){
            a[i][0]=Double.valueOf(ja.getJSONObject(i).get("x").toString());
            a[i][1]=Double.valueOf(ja.getJSONObject(i).get("y").toString());
        }

        a1=a;

    }

    public static File saveBitmapFile(Bitmap bitmap, String filepath){
        File file=new File(filepath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private Bitmap getBitmap(Bitmap bitmap) {
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        //设置想要的大小

        Matrix m = new Matrix();
        int orientationDegree = 0;
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

    public static String saveBitmapToFile(File file, String newpath) {
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 2;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
//            file.createNewFile();
//
//
//            FileOutputStream outputStream = new FileOutputStream(file);
//
//            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);


            File aa = new File(newpath);

            FileOutputStream outputStream = new FileOutputStream(aa);

            //choose another format if PNG doesn't suit you

            selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);


            String filepath = aa.getAbsolutePath();
            Log.e("getAbsolutePath", aa.getAbsolutePath());

            return filepath;
        } catch (Exception e) {
            return null;
        }
    }

}


