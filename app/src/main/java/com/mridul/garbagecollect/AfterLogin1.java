package com.mridul.garbagecollect;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import static com.mridul.garbagecollect.BackgroundWorkerAcountInfo.ACCOUNT_INFO_json_MOB_NO;
import static com.mridul.garbagecollect.BackgroundWorkerAcountInfo.ACCOUNT_INFO_json_NAME;
import static com.mridul.garbagecollect.BackgroundWorkerAcountInfo.ACCOUNT_INFO_json_VEHICLE_NO;
import static com.mridul.garbagecollect.BackgroundWorker.CURRENT_USER_NAME;
import static com.mridul.garbagecollect.LoginActivity.isNetworkAvailable;

public class AfterLogin1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Toolbar toolbar;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login1);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    finish(); //Calling this method to close this activity when internet is not available.
                }
            }, 2000);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("GarbageCollect");
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayFragment(R.id.navigation_home);

        /*Intent i = getIntent();
        CURRENT_USER_EMAIL = i.getStringExtra("email");*/
        View header = navigationView.getHeaderView(0);
        TextView t = (TextView)header.findViewById(R.id.textView_nav_header) ;
        t.append("\n"+"===>>  "+CURRENT_USER_NAME+"");
//        t.setText(""+CURRENT_USER_EMAIL+"");
//        Log.d("email",CURRENT_USER_EMAIL);

    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Alert !!!");
        alert.setMessage("You sure to go Back?\nYou will be Logged out.");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AfterLogin1.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.setNegativeButton("No", null);
        alert.show();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.after_login1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button
        int id = item.getItemId();

        switch(id){

            // for toolbar menu items
            case R.id.logout:
                // handle clicks here
                startActivity(new Intent(this,LoginActivity.class));
                Toast.makeText(this,"You have successfully Logged Out "+CURRENT_USER_NAME,Toast.LENGTH_LONG).show();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displayFragment(item.getItemId());
        return true;
    }


    public void displayFragment(int id){
        android.support.v4.app.Fragment fragment = null;

        switch (id){
            case R.id.navigation_home:
                //
                fragment = new FragmentHome();
                break;
            case R.id.navigation_bin_locations:
                //
                fragment = new FragmentBinMarkers();
                break;
            case R.id.navigation_pathMaker:
                //
                fragment = new FragmentPathMaker();
                break;
            case R.id.account_info:
                // handle clicks here
                Bundle bundle = new Bundle();
                bundle.putString("user_name_current_user", CURRENT_USER_NAME);
                bundle.putString("mob_no_current_user", ACCOUNT_INFO_json_MOB_NO);
                bundle.putString("name_current_user", ACCOUNT_INFO_json_NAME);
                bundle.putString("vehicle_no_current_user", ACCOUNT_INFO_json_VEHICLE_NO);

                fragment = new FragmentAccountInfo();
                fragment.setArguments(bundle);

                break;

        }


        if(fragment != null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_after_login1, fragment );
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }






}
