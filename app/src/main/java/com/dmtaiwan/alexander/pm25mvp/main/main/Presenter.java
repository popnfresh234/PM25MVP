package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.json.JSONArray;

/**
 * Created by Alexander on 5/21/2015.
 */
public interface Presenter {
    public String getPreferredStation(SharedPreferences prefs);

    public void getStationArray(String preferredStation);

    public void getStationData(JSONArray jsonArray, Resources resources, SharedPreferences prefs);

    public void getChartArrayOfArrays(String preferredStation, Resources resource);

    public void loadSavedData(SharedPreferences prefs, Resources resources);

    public void saveChartData(String chartDataString, SharedPreferences prefs);

}
