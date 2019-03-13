package com.example.sichunluo.fac1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class guideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView textView = findViewById(R.id.textview);
        textView.setText("This is the guide for how to use this app:" +
                "this app has 2 functions, including face recognition and " +
                "real time face detection. User can click the picture button to " +
                "take a picture or click the gallery button to choose a " +
                "image from gallery. Then the interface would display 72 landmarks and" +
                "as well as the Physiognomy analysis. By clicking the detect button" +
                "user would enter the real time face detection  part.");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
