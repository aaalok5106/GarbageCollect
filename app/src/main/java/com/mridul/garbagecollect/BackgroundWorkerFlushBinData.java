package com.mridul.garbagecollect;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.mridul.garbagecollect.BackgroundWorker.IP_MAIN;


public class BackgroundWorkerFlushBinData extends AsyncTask<String, Void, String>{

    Context context;
    ProgressDialog pd ;

    public BackgroundWorkerFlushBinData(Context context1){
        context = context1;
    }

    @Override
    protected void onPreExecute() {
        pd = new ProgressDialog(context);
        pd.setTitle("Please Wait");
        pd.setMessage("Request in Progress...");
        pd.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String flush_filled_bins = IP_MAIN + "history.php";

        if (type.equals("flushFilledBins")){

            InputStream is = null;
            String line = null;

            try {
                URL url = new URL(flush_filled_bins);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");
                is = new BufferedInputStream(con.getInputStream());

                BufferedReader br=new BufferedReader(new InputStreamReader(is));

                StringBuilder sb= new StringBuilder();


                if(br != null){
                    while((line=br.readLine()) != null){
                        sb.append(line).append('\n');
                    }
                }

                String data = "";
                data = sb.toString();


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
    protected void onPostExecute(String data) {
        pd.dismiss();

        Log.d("String from server : ", "" + data);

        if(data.trim().equals("Flushing Done")){
            Toast.makeText(context, "FLUSHING successfully Done", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context, "Some ERROR occurred During flushing . You may Try again .", Toast.LENGTH_LONG).show();
        }

    }

}
