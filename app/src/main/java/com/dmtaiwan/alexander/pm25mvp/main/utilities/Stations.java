package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by Alexander on 5/22/2015.
 */
public class Stations {
    private static final DecimalFormat mDecimalFormat = new DecimalFormat("0.#");
    private static JSONArray jsonArray;
    public static int green;
    public static int green100;
    public static int orange;
    public static int orange100;
    public static int yellow;
    public static int yellow100;
    public static int purple;
    public static int purple100;
    public static int red;
    public static int red100;

    public Stations(JSONArray jsonArray, Integer green, Integer green100, Integer yellow, Integer yellow100, Integer orange, Integer orange100, Integer red, Integer red100, Integer purple, Integer purple100) {
        this.jsonArray = jsonArray;
        this.green = green;
        this.green100 = green100;
        this.yellow = yellow;
        this.yellow100 = yellow100;
        this.orange = orange;
        this.orange100 = orange100;
        this.red = red;
        this.red100 = red100;
        this.purple = purple;
        this.purple100 = purple100;
    }


    public StationData createStationData(String preferredStation) {
        StationData preferredStationData = new StationData();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject station = jsonArray.getJSONObject(i);
                if (station.getString("SiteName").equals(preferredStation)) {
                    preferredStationData.setPreferredStation(preferredStation);
                    //get PM25 concentration as string from JSON data
                    String pm25String = station.getString("PM2.5");
                    //convert to double
                    if (pm25String.equals("")) {
                        pm25String = "0";
                    }
                    Double pm25 = Double.valueOf(pm25String);
                    //calculate AQI
                    Double aqi = aqiCalc(pm25);
                    //set quality text view
                    preferredStationData = setQualityText(aqi, preferredStationData);
                    //format double and get String
                    String text = mDecimalFormat.format(aqi);
                    preferredStationData.setPm25(text);
                    String time = station.getString("PublishTime");
                    String newTime = "";
                    for (int j = (time.length() - 5); j < time.length(); j++) {
                        newTime = newTime + time.charAt(j);
                    }
                    preferredStationData.setTime(newTime);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return preferredStationData;
    }

    private Double aqiCalc(double pm25) {
        Double c = Math.floor((10 * pm25) / 10);
        Double AQI = null;
        if (c >= 0 && c < 12.1) {
            AQI = linear(50.0, 0.0, 12.0, 0.0, c);
        } else if (c >= 12.1 && c < 35.5) {
            AQI = linear(100.0, 51.0, 35.4, 12.1, c);
        } else if (c >= 35.5 && c < 55.5) {
            AQI = linear(150.0, 101.0, 55.4, 35.5, c);
        } else if (c >= 55.5 && c < 150.5) {
            AQI = linear(200.0, 151.0, 150.4, 55.5, c);
        } else if (c >= 150.5 && c < 250.5) {
            AQI = linear(300.0, 201.0, 250.4, 150.5, c);
        } else if (c >= 250.5 && c < 350.5) {
            AQI = linear(400.0, 301.0, 350.4, 250.5, c);
        } else if (c >= 350.5 && c < 500.5) {
            AQI = linear(500.0, 401.0, 500.4, 350.5, c);
        } else {
            AQI = -1.0;
        }
        return AQI;


    }

    private Double linear(Double AQIHigh, Double AQILow, Double concHigh, Double concLow, Double Concentration) {
        Double linear;
        Double a = ((Concentration - concLow) / (concHigh - concLow)) * (AQIHigh - AQILow) + AQILow;
        linear = Double.valueOf(Math.round(a));
        return linear;
    }

    private StationData setQualityText(Double aqi, StationData preferredStationData) {
        if (aqi <= 50) {
            preferredStationData.setAirQuality("GOOD");
            preferredStationData.setLabelColor(green);
            preferredStationData.setBgColor(green100);

        }
        if (aqi > 51 && aqi <= 100) {
            preferredStationData.setAirQuality("MODERATE");
            preferredStationData.setLabelColor(yellow);
            preferredStationData.setBgColor(yellow100);
        }
        if (aqi > 101 && aqi <= 150) {
            preferredStationData.setAirQuality("UNHEALTHY");
            preferredStationData.setLabelColor(orange);
            preferredStationData.setBgColor(orange100);
        }
        if (aqi > 151 && aqi <= 200) {
            preferredStationData.setAirQuality("DANGEROUS");
            preferredStationData.setLabelColor(red);
            preferredStationData.setBgColor(red100);
        }
        if (aqi > 201) {
            preferredStationData.setAirQuality("DEADLY");
            preferredStationData.setLabelColor(purple);
            preferredStationData.setBgColor(purple100);
        }
        return preferredStationData;
    }
}

