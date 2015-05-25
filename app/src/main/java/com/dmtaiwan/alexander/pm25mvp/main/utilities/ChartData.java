package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by Alexander on 5/23/2015.
 */
public class ChartData {
    private ColumnChartData chartData;

    public ChartData(ColumnChartData chartData) {
        this.chartData = chartData;
    }

    public ColumnChartData getChartData() {
        return this.chartData;
    }
}
