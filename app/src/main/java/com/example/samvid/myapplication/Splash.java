package com.example.samvid.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class Splash extends AppCompatActivity implements ResultCallBack{

    SQLiteDatabase db;
    ProgressDialog dialog;
    com.example.samvid.myapplication.DatabaseAccess da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dialog=new ProgressDialog(this);
        dialog.setMessage("Telangana Acts and Rules");
        dialog.show();
        dialog.setCancelable(false);

        da = com.example.samvid.myapplication.DatabaseAccess.getInstance(this);

        db = da.open();
        Cursor cursor = db.rawQuery("select * from Books", null);
        int count=cursor.getCount();

        if(count <= 0) {
            if(isNetworkAvailable()) {
                String BookInformationURL = "http://172.168.20.31:84/api/TreasuryBooks?bookInfoId=null&bookIndexId=null&paarentInfoId=null&typeId=null";
                String BookIndexURL = "http://172.168.20.31:84/api/TreasuryBooks?bookIndexId=null&bookId=null";
                String BooksURL = "http://172.168.20.31:84/api/TreasuryBooks?BookId=Null";

                AsyncTask_WebAPI BookasyncTask = new AsyncTask_WebAPI(Splash.this, BooksURL, Splash.this);
                BookasyncTask.execute();

                AsyncTask_WebAPI BookIndexasyncTask = new AsyncTask_WebAPI(Splash.this, BookIndexURL, Splash.this);
                BookIndexasyncTask.execute();

                AsyncTask_WebAPI BookInformationasyncTask = new AsyncTask_WebAPI(Splash.this, BookInformationURL, Splash.this);
                BookInformationasyncTask.execute();
            }else
            {
                Toast.makeText(Splash.this,"Please Check for Internet Connectivity",Toast.LENGTH_LONG).show();
            }
        }
        Intent intent;
       /* new Thread(new Runnable() {


            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {*/
                    dialog.dismiss();

                    intent=new Intent(Splash.this,Home_one.class);
                    startActivity(intent);
                    Splash.this.finish();
                /*}
            }
        }).start();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Splash.this.finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onResultListener(String object) {

        try {
            JSONObject jsonObjects = new JSONObject(object);
//            JSONArray jsonArray = new JSONArray(jsonObjects);
            if(jsonObjects.has("Books")){

                Log.v("Books","Executed");

                 JSONArray jsonArray = jsonObjects.getJSONArray("Books");
            if (jsonArray.length() != 0) {
                db = da.open();
                Cursor cursor = db.rawQuery("delete from Books", null);
                cursor.getCount();
                cursor.close();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
//                int strId = jsonObjectChild.getInt("$id");
                    int strBookId = jsonObjectChild.getInt("BookId");
                    String strBookName = jsonObjectChild.getString("BookName");

                    Cursor c = db.rawQuery("insert into Books values('" + strBookId + "','" + strBookName + "',1)", null);
                    c.getCount();
                }
                db.close();
            }
            }
            else if(jsonObjects.has("BookIndex"))
            {
                Log.v("BookIndex","Executed");

                JSONArray jsonArray = jsonObjects.getJSONArray("BookIndex");
//                    JSONArray jsonArray = new JSONArray(object);
                    db=da.open();

                    Cursor cursor=db.rawQuery("delete from BookIndex",null);
                    cursor.getCount();
                    cursor.close();
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObjectChild=jsonArray.getJSONObject(i);

                        int iBookId=jsonObjectChild.getInt("BookId");
                        int iBookIndexId=jsonObjectChild.getInt("BookIndexId");
                        String strBookName=jsonObjectChild.getString("BookIndexName");

//                globalList.setBookIndex(new BookIndexModel(iBookId,iBookIndexId,strBookName));
                        Cursor c=db.rawQuery("insert into BookIndex values('"+iBookId+"','"+iBookIndexId+"','"+strBookName+"',1)",null);
                        c.getCount();
                    }
                    db.close();

            }
            else
            {
                Log.v("BookInformation","Executed");

//                JSONArray jsonArray = new JSONArray(object);
                JSONArray jsonArray = jsonObjects.getJSONArray("BookInformation");
                db = da.open();

                Cursor cursor=db.rawQuery("delete from BookInformation",null);
                cursor.getCount();
                cursor.close();
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObjectChild = jsonArray.getJSONObject(i);

                    int iBookInfoId = jsonObjectChild.getInt("BookInfoId");
                    int iBookIndexId = jsonObjectChild.getInt("BookIndexId");
                    String strBookInfoName = jsonObjectChild.getString("BookInfoName");
                    int iParentBookInfoId = jsonObjectChild.getInt("ParentBookInfoId");
                    int iTypeId=jsonObjectChild.getInt("TypeId");

                    Cursor c = db.rawQuery("insert into BookInformation values('" + iBookInfoId + "','" + iBookIndexId + "','" + strBookInfoName + "','" + iParentBookInfoId + "','" + iTypeId + "',1)", null);
                    c.getCount();
                }
                db.close();

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
