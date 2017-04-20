package com.mridul.garbagecollect;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This class is for opening first activity just after you opens the app.
 * it will open registration activity if you want to register.
 */

public class LoginActivity extends AppCompatActivity {

    EditText userName, password ;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    finish(); //Calling this method to close this activity when internet is not available.
                }
            }, 2000);
        }

        userName = (EditText)findViewById(R.id.login_userName);
        password = (EditText)findViewById(R.id.login_password);

        Button button = (Button)findViewById(R.id.login_signin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("Notification...");
                    alert.setMessage("All Fields are Required.");
                    alert.setPositiveButton("OK", null);
                    alert.show();
                }else{
                    String type = "login";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                    backgroundWorker.execute(type,userName.getText().toString(), password.getText().toString());
                    userName.getText().clear();
                    password.getText().clear();
                }
            }
        });
    }




    public void forgotPasswordLayout(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hello User...");
        alert.setMessage("If you Forgot your Password, contact Admin to Get a Password again.");
        alert.setPositiveButton("OK", null);
        alert.show();

    }

    @Override
    public void onBackPressed() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
                System.exit(0);
            }
        }, 1);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan.getActiveNetworkInfo() != null && conMan.getActiveNetworkInfo().isConnected())
            return true;
        else
            return false;
    }
}

