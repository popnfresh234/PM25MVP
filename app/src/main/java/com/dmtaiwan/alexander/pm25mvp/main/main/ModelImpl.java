package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.dmtaiwan.alexander.pm25mvp.R;
import com.dmtaiwan.alexander.pm25mvp.main.settings.SettingsActivity;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.AQICalc;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.EventBus;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.HttpAsyncTask;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.ParseResultEvent;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.StationData;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.Stations;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * Created by Alexander on 5/21/2015.
 */
public class ModelImpl implements Model {

    @Override
    public String getPreferredStation(SharedPreferences prefs, PM25Listener listener) {
        if (!prefs.getString(SettingsActivity.PREFERRED_STATION, "NONE").equals("NONE")) {
            String preferredStation = prefs.getString(SettingsActivity.PREFERRED_STATION, "NONE");
            return preferredStation;
        } else {
            listener.showToast("Please Choose a Station");
            return "NONE";
        }
    }

    @Override
    public void getStationArray(String preferredStation, PM25Listener listener) {

        if (!preferredStation.equals("NONE")) {
            new HttpAsyncTask().execute(MainActivity.URL);
        }else {
            listener.onLoadingDone();
        }
    }

    @Override
    public void getStationData(JSONArray jsonArray, Resources resources, SharedPreferences prefs, PM25Listener listener) {

        int green = resources.getColor(R.color.green);
        int green100 = resources.getColor(R.color.green100);
        int yellow = resources.getColor(R.color.yellow);
        int yellow100 = resources.getColor(R.color.yellow100);
        int orange = resources.getColor(R.color.orange);
        int orange100 = resources.getColor(R.color.orange100);
        int red = resources.getColor(R.color.red);
        int red100 = resources.getColor(R.color.red100);
        int purple = resources.getColor(R.color.purple);
        int purple100 = resources.getColor(R.color.purple100);

        Stations stations = new Stations(jsonArray, green, green100, yellow, yellow100, orange, orange100, red, red100, purple, purple100);
        //TODO get station name from prefs

        if (!prefs.getString(SettingsActivity.PREFERRED_STATION, "NONE").equals("NONE")) {
            String preferredStation = prefs.getString(SettingsActivity.PREFERRED_STATION, "NONE");
            StationData stationData = stations.createStationData(preferredStation);
            listener.setViewsFromStation(stationData);
            //Save stationData to shared prefs
            Gson gson = new Gson();
            String stationDataString = gson.toJson(stationData);
            prefs.edit().putString(MainActivity.PREF_STATION_DATA, stationDataString).apply();
        } else {
            listener.showToast("Please Select a Station");
            listener.onLoadingDone();
        }

    }

    @Override
    public void getChartArrayOfArrays(final String preferredStation, final Resources resources, PM25Listener listener) {
        if (!preferredStation.equals("NONE")) {


            //Initialize Array
            final ArrayList<JSONArray> jsonArray = new ArrayList<JSONArray>();
            for (int i = 0; i < 24; i++) {
                jsonArray.add(null);
            }
            //Set loading
            listener.onLoading();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Data");
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        for (int i = 0; i < 24; i++) {
                            String data = parseObject.getString("data" + String.valueOf(i));
                            if (!data.equals("0")) {
                                try {
                                    JSONArray stationData = new JSONArray(data);
                                    jsonArray.set(i, stationData);
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                jsonArray.set(i, null);
                            }
                        }

                        //Create ArrayList float for chartData
                        ArrayList<Float> floatArray = new ArrayList<>();
                        for (int k = 0; k < 24; k++) {
                            floatArray.add(Float.valueOf("0"));
                        }
                        for (int j = 0; j < jsonArray.size(); j++) {
                            JSONArray currentArray = jsonArray.get(j);
                            if (currentArray != null) {
                                try {
                                    for (int i = 0; i < currentArray.length(); i++) {
                                        JSONObject station = currentArray.getJSONObject(i);
                                        if (station.getString("SiteName").equals(preferredStation)) {
                                            //get PM25 concentration as string from JSON data
                                            String pm25String = station.getString("PM2.5");
                                            if (pm25String.equals("")) {
                                                pm25String = "0";
                                            }
                                            //convert to double
                                            Double pm25 = Double.valueOf(pm25String);
                                            //calculate AQI
                                            AQICalc aqiCalc = new AQICalc(pm25);
                                            Double aqi = aqiCalc.aqiCalc();
                                            String doubleValue = String.valueOf(aqi);
                                            floatArray.set(j, Float.valueOf(doubleValue));
                                        }
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            } else {
                                floatArray.set(j, Float.valueOf("0"));
                            }
                            if (currentArray != null) {
                            }
                        }
                        ColumnChartData chartData = returnChartData(floatArray, resources);
                        //Save chart data to prefs and save to EventBus for saving to SharedPref
                        Gson gson = new Gson();
                        String chartDataString = gson.toJson(floatArray);
                        //Send event via EventBus
                        ParseResultEvent event = new ParseResultEvent(chartData, chartDataString);
                        EventBus.getInstance().post(event);
                    } else {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            listener.onLoadingDone();
        }
    }

    @Override
    public void loadSavedData(SharedPreferences prefs, Resources resources, PM25Listener listener) {

        if (!prefs.getString(MainActivity.PREF_STATION_DATA, "NONE").equals("NONE")) {
            Gson gson = new Gson();
            String stationDataString = prefs.getString(MainActivity.PREF_STATION_DATA, "NONE");
            StationData stationData = gson.fromJson(stationDataString, StationData.class);
            listener.setViewsFromStation(stationData);
        }

        if (!prefs.getString(MainActivity.PREF_CHART_DATA, "NONE").equals("NONE")) {
            Gson gson = new Gson();
            Type type = new TypeToken<Collection<Float>>(){}.getType();
            String chartDataString = prefs.getString(MainActivity.PREF_CHART_DATA, "NONE");
            ArrayList<Float> floatArray = gson.fromJson(chartDataString, type);
            Log.i("FloatArray", floatArray.toString());
            ColumnChartData chartData = returnChartData(floatArray, resources);
            listener.setChartData(chartData);
        }
        //if no data, null out fields
        if (prefs.getString(MainActivity.PREF_STATION_DATA, "NONE").equals("NONE")) {
            StationData stationData= new StationData();
            stationData.setAirQuality(resources.getString(R.string.text_view_air_quality_label));
            stationData.setLabelColor(resources.getColor(android.R.color.white));
            stationData.setPreferredStation(prefs.getString(SettingsActivity.PREFERRED_STATION, "Select Station"));
            stationData.setPm25("");
            stationData.setTime("");
            stationData.setBgColor(resources.getColor(R.color.transparentWhite));
            listener.setViewsFromStation(stationData);
        }
        if (prefs.getString(MainActivity.PREF_CHART_DATA, "NONE").equals("NONE")) {
            ColumnChartData chartData= new ColumnChartData();
            listener.setChartData(chartData);
        }
    }

    @Override
    public void saveChartData(String chartDataString, SharedPreferences prefs) {
        prefs.edit().putString(MainActivity.PREF_CHART_DATA, chartDataString).apply();
    }



    public ColumnChartData returnChartData(ArrayList<Float> floatArray, Resources resources) {
        final boolean hasAxes = true;
        final boolean hasAxesNames = true;
        final boolean hasLabels = false;
        final boolean hasLabelForSelected = false;
        //Generate ChartData from floatArray
        int numColumns = 24;
        List<Column> columns = new ArrayList<>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; i++) {
            values = new ArrayList<>();
            values.add(new SubcolumnValue(floatArray.get(i), resources.getColor(R.color.purple100)));
            Column column = new Column(values);
            column.setHasLabels(hasLabels);
            column.setHasLabelsOnlyForSelected(hasLabelForSelected);
            columns.add(column);
        }
        ColumnChartData chartData = new ColumnChartData(columns);
        if (hasAxes) {
            Axis axisX = new Axis();
            axisX.setTextColor(resources.getColor(android.R.color.black));
            List<AxisValue> axisValues = new ArrayList<>();
            for (int i = 0; i <= 24; i++) {
                AxisValue axisValue = new AxisValue(i);
                axisValues.add(axisValue);
            }
            axisX.setValues(axisValues);
            axisX.setAutoGenerated(false);
            axisX.setMaxLabelChars(1);
            axisX.setTextSize(6);
            Axis axisY = new Axis().setHasLines(true);
            axisY.setTextColor(resources.getColor(android.R.color.black));
            if (hasAxesNames) {
                axisX.setName("Time");
                axisY.setName("PM25");
            }
            chartData.setAxisXBottom(axisX);
            chartData.setAxisYLeft(axisY);
        } else {
            chartData.setAxisXBottom(null);
            chartData.setAxisYLeft(null);
        }
        return chartData;
    }

}
