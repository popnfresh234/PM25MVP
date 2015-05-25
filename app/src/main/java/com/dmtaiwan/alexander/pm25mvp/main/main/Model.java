package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.content.SharedPreferences;
import android.content.res.Resources;

import org.json.JSONArray;

/**
 * Created by Alexander on 5/21/2015.
 */
public interface Model {
    public String getPreferredStation(SharedPreferences prefs, PM25Listener listener);

    public void getStationArray(String preferredStation, PM25Listener listener);

    public void getStationData(JSONArray jsonArray, Resources resources, SharedPreferences prefs, PM25Listener listener);

    public void getChartArrayOfArrays(String preferredStation, Resources resources, PM25Listener listener);

    public void loadSavedData(SharedPreferences prefs,Resources resources, PM25Listener listener);

    public void saveChartData(String chartDataString, SharedPreferences prefs);

}
