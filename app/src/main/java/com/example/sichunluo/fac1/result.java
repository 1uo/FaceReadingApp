package com.example.sichunluo.fac1;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.aip.face.AipFace;
import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import rorbin.q.radarview.RadarData;
import rorbin.q.radarview.RadarView;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.util.Base64.getEncoder;

public class result extends AppCompatActivity {

    public static final String APP_ID = "14321335";
    public static final String API_KEY = "XTfGD9FleBDA1CbZB1jDG7Oa";
    public static final String SECRET_KEY = "xxw7y77MRREp9dVNDhmZkfVMMNXVjqPZ";
    public  double a1[][];

    public String string="";

    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView7;
    //private TextView textView8;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        // 图片文件路径
        String filePath = sdCardPath + File.separator + "test.png";
        File f = new File(filePath);
        Bitmap bmp;
        BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
        mBitmapOptions.inScaled = true;
        mBitmapOptions.inSampleSize = 1;
        bmp=BitmapFactory.decodeFile(filePath,mBitmapOptions);

        Matrix m=new Matrix();
        m.setScale(-1, 1);
        m.setRotate(270, bmp.getWidth()/2, bmp.getHeight()/2);//90就是我们需要选择的90度
        Bitmap bmp2=Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);

        ImageView imageView = findViewById(R.id.image);
        imageView.setImageBitmap(convertBitmap(bmp2));


        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(result.this,textureActivity.class);
                startActivity(intent1);
            }
        });







        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        double a[][]=new double[72][2];



        Button textView = findViewById(R.id.text1);

        Log.d("hereS",a[0][0]+"");
        //detEyeArea(a,string);


        try {
            detectface(bitmap);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(string.length()==0){
                    string="没啥特点，中人之资；平平淡淡，一生平安";
                }

                new AlertDialog.Builder(result.this)
                        .setTitle("面相分析")
                        .setMessage(string)
                        .setPositiveButton(("OK"), null)
                        .show();
            }
        });


        List<Float> values = new ArrayList<>();
        Collections.addAll(values, 3.2f, 6.7f, 2f, 7f, 5.6f);
        RadarData data = new RadarData(values);

        RadarView mRadarView=findViewById(R.id.radarView);
        mRadarView.addData(data);
        List<String> strings = new ArrayList<>();
        //看慧在额，看名在眉，看贵在眼，看在鼻，看在嘴，看福在耳，看寿在颌
        Collections.addAll(strings,"名","禄","贵","富","慧");
        mRadarView.setVertexText(strings);
        // mRadarView.setVertexIconPosition(0);
        //mRadarView.setVertexTextOffset(0f);
        mRadarView.setMaxValue(10f);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void detectface(Bitmap bmp) throws UnsupportedEncodingException, JSONException {
        final Base64.Decoder decoder = Base64.getDecoder();
        final Base64.Encoder encoder = Base64.getEncoder();

//编码

        //System.out.println(encodedText);

        InputStream is = null;
        byte[] data = {1};
// 读取图片字节数组
        try {

            Date startDate   =   new Date(System.currentTimeMillis());



            BitmapFactory.Options mBitmapOptions=new BitmapFactory.Options();
            mBitmapOptions.inScaled = true;
            mBitmapOptions.inSampleSize = 4;
            mBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //这里100表示不压缩，把压缩后的数据存放到baos中
            bmp.compress(Bitmap.CompressFormat.JPEG,1,baos);
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());

            Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, mBitmapOptions);



            byte[] bytes = baos.toByteArray();


            Date   endDate   =   new   Date(System.currentTimeMillis());

            long diff1 = endDate.getTime() - startDate.getTime();

            Log.d("timecost5(read data)",diff1+"");



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


        System.setProperty("aip.log4j.conf", "path/to/your/log4j.properties");


        HashMap<String, String> options = new HashMap<String, String>();

        options.put("face_field", "landmark");
        // options.put("face_field", "gender");
        Date   startDate   =   new Date(System.currentTimeMillis());


        JSONObject res = client.detect(encoder.encodeToString(data), "BASE64",options);

        JSONArray ja = res.getJSONObject("result").getJSONArray("face_list").getJSONObject(0)
                .getJSONArray("landmark72");


        Date   endDate   =   new   Date(System.currentTimeMillis());

        long diff1 = endDate.getTime() - startDate.getTime();

        Log.d("timecost4(web)",diff1+"");


        double a[][]=new double[72][2];

        for(int i=0;i<72;i++){
            a[i][0]=Double.valueOf(ja.getJSONObject(i).get("x").toString());
            a[i][1]=Double.valueOf(ja.getJSONObject(i).get("y").toString());
        }

        a1=a;


       detEyeArea(a1);
       detMouthshape(a1);
    }






    private Bitmap convertBitmap(Bitmap srcBitmap) {
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //Canvas canvas = new Canvas();
        Matrix matrix = new Matrix();

        matrix.postScale(-1, 1);

        Bitmap newBitmap2 = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);

//        //canvas.drawBitmap(newBitmap2,
//                new Rect(0, 0, width, height),
//                new Rect(0, 0, width, height), null);

        return newBitmap2;


    }



    public double calAreaTri(double a[][], int m,int n,int q){
        double n1 =sqrt( pow((a[m][0]-a[n][0]),2) + pow((a[m][1]-a[n][1]),2) );
        double n2 =sqrt( pow((a[m][0]-a[q][0]),2) + pow((a[m][1]-a[q][1]),2) );
        double n5 =sqrt( pow((a[n][0]-a[q][0]),2) + pow((a[n][1]-a[q][1]),2) );
        double s1=(n1+n2+n5)/2;

        return sqrt(s1*(s1-n1)*(s1-n2)*(s1-n5));
    }

    //p & q is co-edge
    public double calAreaQua(double a[][], int m,int n,int q, int p){
        return calAreaTri(a,m,p,q)+calAreaTri(a,n,p,q);
    }

    private void detEyeArea(double a[][]){

        Log.d("hereS",string);

        double areaLeftEye = calAreaQua(a,36,40,37,41)+calAreaQua(a,37,39,38,40);
        double areaRightEye = calAreaQua(a,42,46,43,47)+calAreaQua(a,44,46,43,45);

        //target 1
        if(abs(areaLeftEye-areaRightEye)>0){
            string=string+"大小眼，此人慎密敏感，喜怒无常；/n";
        }

        if(abs(areaLeftEye+areaRightEye)>0){
            TextView tx = findViewById(R.id.textView1);
            tx.setText("眼睛大");
            string=string+"眼大而发亮,此人充满热情，才华出众；/n";
        }else

            if(abs(areaLeftEye+areaRightEye)<0){
                TextView tx = findViewById(R.id.textView1);
                tx.setText("眼睛小");
            string=string+"眼小而有神，此人精明能干，聪明伶俐；/n";
        }

        Log.d("hereS",string);



    }

    private void detMouthshape(double a[][]){
        if((a[58][1]+a[62][1])>(a[67][1]+a[70][1])){
            TextView tx = findViewById(R.id.textView2);
            tx.setText("嘴角上扬");
            string= string+" 嘴角上扬";
        }else {
            TextView tx = findViewById(R.id.textView2);
            tx.setText("嘴角下垂");
            string= string+" 嘴角下垂";
        }



    }



}
