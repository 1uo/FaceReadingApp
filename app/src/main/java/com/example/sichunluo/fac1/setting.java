package com.example.sichunluo.fac1;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        TextView tx = findViewById(R.id.t1);
        Button bn = findViewById(R.id.b4);
        String sta=getResources().getConfiguration().locale.getLanguage();
        if(sta.equals("zh")){
            tx.setText("当前语言：简体中文");
            bn.setText("返回");

        }else{
            tx.setText("Current language: English");
            bn.setText("Return");
        }

        bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1= new Intent(setting.this,textureActivity.class);
                startActivity(intent1);
            }
        });

        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale.setDefault(Locale.CHINESE);
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = Locale.CHINESE;
                getBaseContext().getResources().updateConfiguration(config
                        , getBaseContext().getResources().getDisplayMetrics());
                refreshSelf();
            }
        });

        findViewById(R.id.b3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Locale.setDefault(Locale.ENGLISH);
                Configuration config = getBaseContext().getResources().getConfiguration();
                config.locale = Locale.ENGLISH;
                getBaseContext().getResources().updateConfiguration(config
                        , getBaseContext().getResources().getDisplayMetrics());
                refreshSelf();
            }
        });

    }

    public void refreshSelf(){
        Intent intent=new Intent(this,setting.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = findViewById(R.id.button);
        bt.setText(getResources().getString(R.string.aaa));
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sta=getResources().getConfiguration().locale.getLanguage();
                shiftLanguage(sta);

            }
        });

    }

    public void shiftLanguage(String sta){

        if(sta.equals("zh")){
            Locale.setDefault(Locale.ENGLISH);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.ENGLISH;
            getBaseContext().getResources().updateConfiguration(config
                    , getBaseContext().getResources().getDisplayMetrics());
            refreshSelf();
        }else{
            Locale.setDefault(Locale.CHINESE);
            Configuration config = getBaseContext().getResources().getConfiguration();
            config.locale = Locale.CHINESE;
            getBaseContext().getResources().updateConfiguration(config
                    , getBaseContext().getResources().getDisplayMetrics());
            refreshSelf();
        }
    }
    //refresh self
    public void refreshSelf(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    */
}
