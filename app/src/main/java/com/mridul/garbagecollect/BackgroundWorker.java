package com.mridul.garbagecollect;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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


/**
 * Presently this class is used to fetch data from server for login and user_registration & much more.
 */

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    public static String CURRENT_USER_NAME;

    Context context;
    //AlertDialog alertDialog;
    ProgressDialog progressDialog;

    protected  String json_NAME;
    protected  String json_USER_NAME;
    protected  String json_MOB_NO;
    protected  String json_VEHICLE_NO;


    BackgroundWorker(Context context1){
        context = context1;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        String login_url = "http://172.16.176.179/DriverManagement/driver_login.php";
        String accountInfo_url = "http://172.16.176.179/DriverManagement/driver_account_info.php";


        if (type.equals("login")) {
            String userName = params[1];
            CURRENT_USER_NAME = userName;
            String password = params[2];
            URL url = null;
            try {
                url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String postdata = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
                bufferedWriter.write(postdata);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else if (type.equals("accountInfo")){
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

        return null ;
    }

    @Override
    protected void onPreExecute() {

        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Status");


        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Request in Progress...");
        progressDialog.show();

    }


    public static String ACCOUNT_INFO_json_USER_NAME = "";
    public static String ACCOUNT_INFO_json_NAME = "";
    public static String ACCOUNT_INFO_json_MOB_NO = "";
    public static String ACCOUNT_INFO_json_VEHICLE_NO = "";

    @Override
    protected void onPostExecute(String result) {

        //alertDialog.setMessage(result);
        //alertDialog.show();

        progressDialog.dismiss();

        if( !result.equals("Your Account Info") ) {
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }

        if(result.equals("Your Account Info")){
            // show information about user's account here...
            //gotoAccountInfoLayout();

            ACCOUNT_INFO_json_USER_NAME = json_USER_NAME ;
            ACCOUNT_INFO_json_NAME = json_NAME ;
            ACCOUNT_INFO_json_MOB_NO = json_MOB_NO ;
            ACCOUNT_INFO_json_VEHICLE_NO = json_VEHICLE_NO;

        }
        else if(result.equals("You are successfully Logged In")) {
            /**
             * on successful log in , opening AfterLogin1 Activity...
             */
            openAfterLogin();

        }
        else if(result.equals("Sorry! Input credentials are WRONG...Please Login with valid credentials!") ){
            /**
             * else returning to login page again...
             * After registration , also , returning to login activity...
             */
            gotoLoginLayout();
        }else {
            // Do nothing.
        }
        //alertDialog.dismiss();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    private void openAfterLogin() {
        Intent intent = new Intent(context,AfterLogin1.class);
        /*intent.putExtra("email",EMAIL);*/
        context.startActivity(intent);
    }

    private void gotoLoginLayout() {
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    private void gotoAccountInfoLayout(){
        //Intent intent = new Intent(context,AccountInfo.class);
       /* Intent intent = new Intent(context,AccountInfo.class);
        intent.putExtra("email",json_EMAIL);
        intent.putExtra("name",json_NAME);
        intent.putExtra("mob_no",json_MOB_NO);
        context.startActivity(intent);*/
    }



}
