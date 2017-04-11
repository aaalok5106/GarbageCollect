package com.mridul.garbagecollect.MakingPath;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by Mridul on 05-03-2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
