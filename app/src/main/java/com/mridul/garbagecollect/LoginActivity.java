package com.mridul.garbagecollect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is for opening first activity just after you opens the app.
 * it will open registration activity if you want to register.
 */

public class LoginActivity extends AppCompatActivity {

    EditText userName, password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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



}

