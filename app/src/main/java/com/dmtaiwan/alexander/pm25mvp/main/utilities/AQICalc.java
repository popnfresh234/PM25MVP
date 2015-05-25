package com.dmtaiwan.alexander.pm25mvp.main.utilities;

/**
 * Created by Alexander on 5/22/2015.
 */
public class AQICalc {
    private static Double pm25;

    public AQICalc(Double pm25) {
        this.pm25 = pm25;
    }

    public Double aqiCalc() {
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
}
