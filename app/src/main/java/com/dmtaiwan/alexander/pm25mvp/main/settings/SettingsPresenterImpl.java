package com.dmtaiwan.alexander.pm25mvp.main.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.ArrayAdapter;

/**
 * Created by Alexander on 5/21/2015.
 */
public class SettingsPresenterImpl implements SettingsPresenter {
    private SettingsActivityView mSettingsActivityView;
    private SettingsModel mModel;

    public SettingsPresenterImpl(SettingsActivityView settingsActivityView) {
        mSettingsActivityView = settingsActivityView;
        mModel = new SettingsModelImpl();
    }

    @Override
    public ArrayAdapter<String> createAdapter(Context context, Intent intent, Resources resources) {
        return mModel.createAdapter(context, intent, resources);
    }

    @Override
    public Intent selectStation(Intent intent, String identifier, Context context, Integer position) {
        return mModel.selectStation(intent, identifier, context, position);
    }

    @Override
    public void setStation(Context context, String identifier, Integer position) {
        mModel.setStation(context, identifier, position);
    }


}
