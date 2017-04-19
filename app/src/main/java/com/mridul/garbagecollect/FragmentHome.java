package com.mridul.garbagecollect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import static com.mridul.garbagecollect.BackgroundWorker.CURRENT_USER_NAME;


public class FragmentHome extends Fragment implements  View.OnClickListener{


    ImageView iv_filled_bins ;
    TextView tv_filled_bins;
    ImageView iv_make_path;
    TextView tv_make_path;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        AfterLogin1.toolbar.setTitle("Home");


        //Log.d("current email", ""+email_user);
        String type="accountInfo";
        BackgroundWorkerAcountInfo backgroundWorker = new BackgroundWorkerAcountInfo(getContext());
        backgroundWorker.execute(type, CURRENT_USER_NAME);

        iv_filled_bins = (ImageView) view.findViewById(R.id.iv1);
        tv_filled_bins = (TextView) view.findViewById(R.id.tv1);
        iv_make_path = (ImageView) view.findViewById(R.id.iv2);
        tv_make_path = (TextView) view.findViewById(R.id.tv2);

        iv_filled_bins.setOnClickListener(this);
        iv_make_path.setOnClickListener(this);
        tv_filled_bins.setOnClickListener(this);
        tv_make_path.setOnClickListener(this);

        return view ;
    }

    @Override
    public void onClick(View v) {

        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.iv1:
                fragment = new FragmentBinMarkers();
                replaceFragment(fragment);
                break;

            case R.id.tv1:
                fragment = new FragmentBinMarkers();
                replaceFragment(fragment);
                break;
            case R.id.iv2:
                fragment = new FragmentPathMaker();
                replaceFragment(fragment);
                break;

            case R.id.tv2:
                fragment = new FragmentPathMaker();
                replaceFragment(fragment);
                break;
        }
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_after_login1, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
