package com.dmtaiwan.alexander.pm25mvp.main.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

import com.dmtaiwan.alexander.pm25mvp.R;

/**
 * Created by Alexander on 5/21/2015.
 */
public class SettingsModelImpl implements SettingsModel{
    private ArrayAdapter<String> mArrayAdapter;
    private Intent mIntent;

    @Override
    public ArrayAdapter<String> createAdapter(Context context, Intent intent, Resources resources) {
        String intentCode = intent.getStringExtra(SettingsActivity.CODE);
        if (intentCode.equals(SettingsActivity.COUNTY)) {
            String[] list = resources.getStringArray(R.array.counties);
            mArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);

        } else if (intentCode.equals(SettingsActivity.STATION)) {
            int county = intent.getIntExtra(SettingsActivity.STATION, -1);
            String countyName = "_"+String.valueOf(county);
            int id = resources.getIdentifier(countyName, "array", "com.dmtaiwan.alexander.pm25mvp");
            String[] list = resources.getStringArray(id);
            mArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
        } else {
            mArrayAdapter = null;
        }
        return mArrayAdapter;
    }

    @Override
    public Intent selectStation(Intent intent, String identifier, Context context, Integer position) {
        String intentCode = intent.getStringExtra(SettingsActivity.CODE);
        if (intentCode.equals(SettingsActivity.COUNTY)) {
            Intent i = new Intent(context, SettingsActivity.class);
            i.putExtra(SettingsActivity.CODE, SettingsActivity.STATION);
            i.putExtra(SettingsActivity.STATION,position);
            mIntent = i;
        }
        return mIntent;
    }

    @Override
    public void setStation(Context context, String identifier, Integer position) {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        prefs.edit().putString(SettingsActivity.PREFERRED_STATION, identifier).apply();
    }

}
