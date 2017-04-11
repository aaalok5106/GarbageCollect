package com.mridul.garbagecollect;



import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class FragmentPathMaker extends Fragment {

    Button btn_open_pathmaker;
    Button btn_flush_filled_bins;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_path_maker, container, false);
        AfterLogin1.toolbar.setTitle("Path Maker");

        btn_open_pathmaker = (Button)view.findViewById(R.id.btn_open_makepath_window);
        btn_flush_filled_bins = (Button)view.findViewById(R.id.btn_flush_bin_data);

        btn_open_pathmaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "forMakingPath";
                BackgroundWorkerPathMaker bgwork = new BackgroundWorkerPathMaker(getContext());
                bgwork.execute(type);
            }
        });


        btn_flush_filled_bins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getContext());
                alert.setTitle("Important Notice!!");
                alert.setMessage("Garbage collection Job must be done before clicking *JOB DONE* button.\nSure you have done your work?");
                alert.setPositiveButton("Work Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getContext(),"Do Flushing Here",Toast.LENGTH_LONG).show();
                        // Do flush Job here...
                    }
                });
                alert.setNegativeButton("Not Now", null);
                alert.show();
            }
        });

        return  view;
    }




}
