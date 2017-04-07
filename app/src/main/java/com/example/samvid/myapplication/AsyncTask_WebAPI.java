package com.example.samvid.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AsyncTask_WebAPI extends AsyncTask<String,Void,String>
{
    public Context context;
    ProgressDialog dialog;
    JSONObject postJson;
    String strURL;

    ResultCallBack resultCallBack;

    public AsyncTask_WebAPI(Context context,String strURL, ResultCallBack resultCallBack)
    {
        this.context = context;
        this.strURL=strURL;
        this.resultCallBack=resultCallBack;
    }

    @Override
    protected void onPreExecute() {
        dialog=new ProgressDialog(context);
        dialog.setMessage("Please Wait...");
        dialog.show();
        dialog.setCancelable(false);
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String result =null;

        InputStream inputStream = null;

        try {

//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(strURL);

//            String json = "";

//            Log.v("JSON", json);

//            StringEntity se = new StringEntity(json);
//            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,"application/json"));
//            httppost.setEntity(se);

//            httppost.setHeader("Accept", "application/json");
//            httppost.setHeader("Content-type", "application/json");

//            HttpResponse httpResponse = httpclient.execute(httppost);

//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            URL url = new URL(strURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            inputStream =conn.getInputStream();/* httpResponse.getEntity().getContent();*/

            if (inputStream != null) {

                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();
            } else {
                Log.v("result", "Did not work !");
            }

        }
        catch(Exception e)
        {
            //result="false";
            Log.d("inputStream", e.getLocalizedMessage());
        }
//        }
        return result;
    }

    @Override
    protected void onPostExecute(String jsonObject) {
        dialog.dismiss();
        super.onPostExecute(jsonObject);
        resultCallBack.onResultListener(jsonObject);
    }
}
