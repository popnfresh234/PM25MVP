package com.dmtaiwan.alexander.pm25mvp.main.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

/**
 * Created by Alexander on 5/21/2015.
 */
public interface SettingsPresenter {

    public ArrayAdapter<String> createAdapter(Context context, Intent intent, Resources resources);

    public Intent selectStation(Intent intent, String identifier, Context context, Integer position);

    public void setStation (Context context, String identifier, Integer position);
}
