package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import org.json.JSONArray;

/**
 * Created by Alexander on 5/22/2015.
 */
public class StationData {

    private JSONArray jsonArray;
    private String airQuality;
    private String preferredStation;
    private String pm25;
    private String time;
    private int labelColor;
    private int bgColor;

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getPreferredStation() {
        return preferredStation;
    }

    public void setPreferredStation(String preferredStation) {
        this.preferredStation = preferredStation;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(int labelColor) {
        this.labelColor = labelColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }


}
