package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.dmtaiwan.alexander.pm25mvp.main.utilities.StationData;

import org.json.JSONArray;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by Alexander on 5/21/2015.
 */
public class PresenterImpl implements Presenter, PM25Listener {
    private MainView mMainView;
    private Model mModel;

    public PresenterImpl(MainView mainView) {
        mMainView = mainView;
        mModel = new ModelImpl();
    }

    @Override
    public String getPreferredStation(SharedPreferences prefs) {
        return mModel.getPreferredStation(prefs, this);
    }

    @Override
    public void getStationArray(String preferredStation) {
        mModel.getStationArray(preferredStation, this);
    }

    @Override
    public void getStationData(JSONArray jsonArray, Resources resources, SharedPreferences prefs) {
        mModel.getStationData(jsonArray, resources, prefs, this);
    }

    @Override
    public void getChartArrayOfArrays(String preferredStation, Resources resources) {
        mModel.getChartArrayOfArrays(preferredStation, resources, this);
    }

    @Override
    public void loadSavedData(SharedPreferences prefs, Resources resources) {
        mModel.loadSavedData(prefs, resources, this);
    }

    @Override
    public void saveChartData(String chartDataString, SharedPreferences prefs) {
        mModel.saveChartData(chartDataString, prefs);
    }



    @Override
    public void onLoading() {
        mMainView.showProgress();
    }

    @Override
    public void onLoadingDone() {
        mMainView.hideProgress();
    }

    @Override
    public void showToast(String toast) {
        mMainView.showToast(toast);
    }

    @Override
    public void setViewsFromStation(StationData stationData) {
        mMainView.setViewsFromStation(stationData);
    }

    @Override
    public void setChartData(ColumnChartData chartData) {
        mMainView.setChartData(chartData);
    }


}
