package com.mridul.garbagecollect.MakingPath;


import java.util.ArrayList;
import java.util.List;

import com.mridul.garbagecollect.MakingPath.Route;


public interface DirectionFinderListener {

    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route, int dist, int time, String origin_id_of_bin);

}
