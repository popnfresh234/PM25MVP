package com.dmtaiwan.alexander.pm25mvp.main.main;

import com.dmtaiwan.alexander.pm25mvp.main.utilities.StationData;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by Alexander on 5/21/2015.
 */
public interface MainView {
    public void showProgress();

    public void hideProgress();

    public void showToast(String toast);

    public void setViewsFromStation(StationData stationData);

    public void setChartData(ColumnChartData chartData);

}
