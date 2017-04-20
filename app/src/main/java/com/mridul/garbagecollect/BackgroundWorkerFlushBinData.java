package com.mridul.garbagecollect;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
        String flush_filled_bins = IP_MAIN + "/mailer/history_driver.php";

        if (type.equals("flushFilledBins")){
            String userName = params[1];

            URL url = null;
            try {
                url = new URL(flush_filled_bins);
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
