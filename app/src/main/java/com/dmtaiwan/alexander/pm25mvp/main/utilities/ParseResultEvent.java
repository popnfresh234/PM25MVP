package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by Alexander on 5/22/2015.
 */
public class ParseResultEvent {
    private ColumnChartData chartData;
    private String chartDataString;

    public ParseResultEvent(ColumnChartData chartData, String chartDataString) {
        this.chartData = chartData;
        this.chartDataString = chartDataString;
    }
    public ColumnChartData getChartData() {
        return chartData;
    }

    public String getChartDataString() {
        return chartDataString;
    }
}
