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



public class BackgroundWorker extends AsyncTask<String, Void, String> {

    public static String CURRENT_USER_NAME;
    public static String START_POSITION_SELECTED = "noWork" ;
    public static String IP_MAIN = "http://172.16.187.133/smartbin/";

    Context context;
    //AlertDialog alertDialog;
    ProgressDialog progressDialog;


    BackgroundWorker(Context context1){
        context = context1;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        String login_url = IP_MAIN+"DriverManagement/driver_login.php";


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



    @Override
    protected void onPostExecute(String result) {

        //alertDialog.setMessage(result);
        //alertDialog.show();

        progressDialog.dismiss();


        Toast.makeText(context, result, Toast.LENGTH_LONG).show();



        if(result.trim().equals("You are successfully Logged In")) {

            START_POSITION_SELECTED = "NO" ;
            /**
             * on successful log in , opening AfterLogin1 Activity...
             */
            openAfterLogin();

        }
        else if(result.trim().equals("Sorry! Input credentials are WRONG...Please Login with valid credentials!") ){
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





}
