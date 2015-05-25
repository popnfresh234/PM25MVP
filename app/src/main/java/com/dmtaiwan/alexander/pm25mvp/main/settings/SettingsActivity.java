package com.dmtaiwan.alexander.pm25mvp.main.settings;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.dmtaiwan.alexander.pm25mvp.R;

/**
 * Created by Alexander on 5/21/2015.
 */
public class SettingsActivity extends ListActivity implements SettingsActivityView {

    public static final String CODE = "code";
    public static final String COUNTY = "county";
    public static final String STATION = "station";
    public static final String PREFERRED_STATION = "preferred_station";

    private SettingsPresenter mPresenter;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mPresenter = new SettingsPresenterImpl(this);
        setListAdapter(mPresenter.createAdapter(this, intent, getResources()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        mPresenter = new SettingsPresenterImpl(this);
        setListAdapter(mPresenter.createAdapter(this, getIntent(), getResources()));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String name = (String) l.getItemAtPosition(position);
        if (getIntent().getStringExtra(CODE).equals(COUNTY)) {
            startActivity(mPresenter.selectStation(getIntent(), name, this, position));
        }else if (getIntent().getStringExtra(CODE).equals(STATION)) {
            mPresenter.setStation(this, name, position);
            setResult(RESULT_OK);
            finish();
        }

    }
}
