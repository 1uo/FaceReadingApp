package com.example.sichunluo.fac1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class gallery extends AppCompatActivity {

    Bitmap bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);

        findViewById(R.id.rt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(gallery.this,textureActivity.class);
                startActivity(intent1);
            }
        });

        findViewById(R.id.db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImageView mImageView = findViewById(R.id.iv_photo);
//                Bitmap bitmap = ((BitmapDrawable) mImageView.getBackground()).getBitmap();

                Canvas canvas = new Canvas(bp);
                Paint paint = new Paint();
                //设置画笔颜色
                paint.setColor(Color.GREEN);
                paint.setStrokeWidth(10);

                FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());
                List<VisionDetRet> results = faceDet.detect(bp);
                for (final VisionDetRet ret : results) {
                    String label = ret.getLabel();
                    int rectLeft = ret.getLeft();
                    int rectTop = ret.getTop();
                    int rectRight = ret.getRight();
                    int rectBottom = ret.getBottom();
                    // Get 68 landmark points
                    ArrayList<Point> landmarks = ret.getFaceLandmarks();
                    for (Point point : landmarks) {
                        int pointX = point.x;
                        int pointY = point.y;
                        canvas.drawPoint(pointX,pointY,paint);

                        Log.d("jieguo",pointX+" "+rectLeft);
                    }
                }


                ImageView iv_photo=findViewById(R.id.iv_photo);
                iv_photo.setImageBitmap(bp);



            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case 1://这里的requestCode是我自己设置的，就是确定返回到那个Activity的标志
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        Bitmap bitmap = loadBitmap(path,true);

                        bp = bitmap;

//                        Log.d("rrr",1+" "+2);
//
//
//                        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());
//                        Log.d("rrr",1+" "+3);
//                        List<VisionDetRet> results = faceDet.detect(bitmap);
//
//                        Log.d("rrr",1+" "+4);
//                        for (final VisionDetRet ret : results) {
//                            String label = ret.getLabel();
//                            int rectLeft = ret.getLeft();
//                            int rectTop = ret.getTop();
//                            int rectRight = ret.getRight();
//                            int rectBottom = ret.getBottom();
//                            // Get 68 landmark points
//                            ArrayList<Point> landmarks = ret.getFaceLandmarks();
//                            for (Point point : landmarks) {
//                                int pointX = point.x;
//                                int pointY = point.y;
//
//                                Log.d("rrr",pointX+" "+rectLeft);
//                            }
//                        }




                        ImageView iv_photo=findViewById(R.id.iv_photo);
                        iv_photo.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        // TODO Auto-generatedcatch block
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }

    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap bm = loadBitmap(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                        bm.getHeight(), m, true);
            }
            return bm;
        }
    }

//    protected void onDraw(Canvas canvas) {
//        canvas.d
//    }

}
