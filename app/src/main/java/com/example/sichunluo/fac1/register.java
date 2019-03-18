package com.example.sichunluo.fac1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final SQLiteDatabase mSQLiteDatabase = this.openOrCreateDatabase("myuser.db", MODE_PRIVATE, null);
        String sql = "create table if not exists " + "User" + " (username text primary key, password text)";
        mSQLiteDatabase.execSQL(sql);

        final EditText ed1 = findViewById(R.id.et_userName);
        final EditText ed2 = findViewById(R.id.et_password);


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( "".equals(ed1.getText().toString()) || "".equals(ed2.getText().toString()) ){
                    AlertDialog.Builder builder  = new AlertDialog.Builder(register.this);
                    builder.setTitle("Error" ) ;
                    builder.setMessage("用户名密码不能为空" ) ;
                    builder.setPositiveButton("OK" ,  null );
                    builder.show();

                    ed1.setText("");
                    ed2.setText("");
                }

                String  INSERT_DATA = "INSERT INTO User (username, password) values ('" + ed1.getText().toString()  + "', '"+ ed2.getText().toString()  +"')" ;

                mSQLiteDatabase.execSQL(INSERT_DATA);

                Intent intent1 = new Intent(register.this, textureActivity.class);
                startActivity(intent1);

            }
        });

    }
}
