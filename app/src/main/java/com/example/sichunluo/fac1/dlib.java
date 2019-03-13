package com.example.sichunluo.fac1;

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

public class dlib extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlib);






        String sdCardPath = Environment.getExternalStorageDirectory().getPath();
        // 图片文件路径
        String filePath = sdCardPath + File.separator + "test.png";
        Log.d("jieguo",123+"");

        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());
            List<VisionDetRet> results = faceDet.detect(bitmap);
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
                    TextView textView = findViewById(R.id.test1);
                    textView.setText(pointX + " " + pointY);
                    Log.d("jieguo",pointX+"");
                }
            }
        }


}
