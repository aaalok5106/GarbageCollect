package com.mridul.garbagecollect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import static com.mridul.garbagecollect.LoginActivity.isNetworkAvailable;

public class SelectStartPosition extends AppCompatActivity {

    private static final int FINE_LOCATION_REQUEST_CODE_INSTALL_BIN = 110 ;
    private static final int PLACE_PICKER_REQUEST_CODE_INSTALL_BIN = 210 ;



    Button btnInstall ;
    TextView tvPlaceName ;
    TextView tvPlaceAddress ;
    // WebView webView ;
    TextView tvDetails ;

    private Handler mHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_start_position);

        if(!isNetworkAvailable(this)) {
            Toast.makeText(this,"No Internet connection",Toast.LENGTH_LONG).show();
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    finish(); //Calling this method to close this activity when internet is not available.
                }
            }, 2000);
        }

        Toolbar tbar = (Toolbar)findViewById(R.id.tbar_start_path_position);
        tbar.setTitle("Start Position");
        tbar.setTitleTextColor(getResources().getColor(R.color.WHITE));
        setSupportActionBar(tbar);



        btnInstall = (Button) findViewById(R.id.btn_install_bins_startPos);
        tvPlaceName = (TextView) findViewById(R.id.tv_place_name_install_bins_startPos);
        tvPlaceAddress = (TextView) findViewById(R.id.tv_place_address_install_bins_startPos);
        //webView = (WebView) findViewById(R.id.webview_install_bins_startPos);
        tvDetails = (TextView) findViewById(R.id.tv_detailText_install_bins_startPos);

        permissionCheck();


        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnInstall.setClickable(false);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    Intent intent = builder.build(SelectStartPosition.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST_CODE_INSTALL_BIN);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                btnInstall.setClickable(true);
            }
        });
    }



    private void permissionCheck(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_CODE_INSTALL_BIN);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case FINE_LOCATION_REQUEST_CODE_INSTALL_BIN:
                if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this ,"This Feature requires Location Access",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PLACE_PICKER_REQUEST_CODE_INSTALL_BIN){
            if(resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(this, data);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final String name = place.getName().toString();
                final String address = place.getAddress().toString();
                final String placeId = place.getId();
                final LatLng latlng = place.getLatLng();
                Double lat = latlng.latitude;
                Double lng = latlng.longitude;


                /*String type = "pathStartingPosition";
                BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                backgroundWorker.execute(type, placeId, name, address, lat.toString(), lng.toString());*/

                String type = "pathStartingPosition";
                BackgroundWorkerStartPosition backgroundWorker = new BackgroundWorkerStartPosition(this);
                backgroundWorker.execute(type, placeId, name, address, lat.toString(), lng.toString());



                tvDetails.setText("Place selected for Start Position:-");
                tvPlaceName.setText(place.getName());
                tvPlaceAddress.setText("Address : "+place.getAddress());



                // trying to update BINSTART combinations on server.
                String type2 = "combinationsBINSTART";
                BackgroundWorkerGenerateAllCombinations bg1 = new BackgroundWorkerGenerateAllCombinations(this);
                bg1.execute(type2);

                /*if(place.getAttributions() == null){
                    webView.loadData("No Attributions found","text/html; charset=utf-8", "UTF-8");
                }else{
                    webView.loadData(place.getAttributions().toString(),"text/html; charset=utf-8", "UTF-8");
                }*/
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        finish();
    }
}
