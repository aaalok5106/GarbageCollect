package com.mridul.garbagecollect;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class BackgroundWorkerPathMaker extends AsyncTask<String, Void, String> {

    Context context;

    public BackgroundWorkerPathMaker(Context context1){
        this.context = context1;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String make_path_url = "http://172.16.176.179/optimized_path.php";

        if (type.equals("forMakingPath")){
            //connect & get string of data.
            InputStream is = null;
            String line = null;

            try {
                URL url = new URL(make_path_url);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                is = new BufferedInputStream(con.getInputStream());

                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                StringBuilder sb= new StringBuilder();


                if(br != null){
                    while((line=br.readLine()) != null){
                        sb.append(line).append('\n');
                    }
                }

                String data = sb.toString();
                Log.d("String from server : ", "" + data);

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    @Override
    protected void onPostExecute(String result) {



        Intent intent = new Intent(context,PathMaker.class);
        intent.putExtra("jsonArray",result);
        context.startActivity(intent);

    }



}
