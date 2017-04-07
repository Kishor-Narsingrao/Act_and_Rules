package com.example.samvid.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

//    SQLiteDatabase db;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Telangana Acts and Rules");
        dialog.show();
        dialog.setCancelable(false);

        new Thread(new Runnable() {
            public Intent intent;

            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    dialog.dismiss();
                    intent=new Intent(Splash.this,Home_one.class);
                    startActivity(intent);
                    Splash.this.finish();
                }
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Splash.this.finish();
    }
}
