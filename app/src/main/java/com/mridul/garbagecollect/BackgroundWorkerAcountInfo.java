package com.mridul.garbagecollect;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import static com.mridul.garbagecollect.BackgroundWorker.IP_MAIN;

public class BackgroundWorkerAcountInfo extends AsyncTask<String, Void, String>{
    Context context;

    protected  String json_NAME;
    protected  String json_USER_NAME;
    protected  String json_MOB_NO;
    protected  String json_VEHICLE_NO;

    public BackgroundWorkerAcountInfo(Context context1){
        context = context1;
    }
    @Override
    protected String doInBackground(String... params) {

        String type = params[0];
        String accountInfo_url = IP_MAIN+"DriverManagement/driver_account_info.php";

        if (type.equals("accountInfo")){
            String userName = params[1];
            URL url = null;

            try {
                url = new URL(accountInfo_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postdata = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream is = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                StringBuilder sb= new StringBuilder();
                String line = "";
                if(br != null){
                    while((line=br.readLine()) != null){
                        sb.append(line).append('\n');
                    }
                }
                String data = sb.toString();
                Log.d("String from server : ", "" + data);

                try {
                    JSONObject jo = new JSONObject(data);

                    json_USER_NAME = jo.getString("user_name");
                    json_NAME = jo.getString("name");
                    json_MOB_NO = jo.getString("mob_no");
                    json_VEHICLE_NO = jo.getString("vehicle_no");

                    br.close();
                    is.close();
                    httpURLConnection.disconnect();

                    String result = "Your Account Info" ;
                    return result;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    @Override
    protected void onPreExecute() {

    }

    public static String ACCOUNT_INFO_json_USER_NAME = "";
    public static String ACCOUNT_INFO_json_NAME = "";
    public static String ACCOUNT_INFO_json_MOB_NO = "";
    public static String ACCOUNT_INFO_json_VEHICLE_NO = "";


    @Override
    protected void onPostExecute(String result) {

        if(result.trim().equals("Your Account Info")){
            // show information about user's account here...

            ACCOUNT_INFO_json_USER_NAME = json_USER_NAME ;
            ACCOUNT_INFO_json_NAME = json_NAME ;
            ACCOUNT_INFO_json_MOB_NO = json_MOB_NO ;
            ACCOUNT_INFO_json_VEHICLE_NO = json_VEHICLE_NO;

        }
    }
}
