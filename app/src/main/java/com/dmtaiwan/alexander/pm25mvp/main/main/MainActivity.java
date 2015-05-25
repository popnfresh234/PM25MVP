package com.dmtaiwan.alexander.pm25mvp.main.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dmtaiwan.alexander.pm25mvp.R;
import com.dmtaiwan.alexander.pm25mvp.main.settings.SettingsActivity;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.AsyncTaskResultEvent;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.EventBus;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.ParseResultEvent;
import com.dmtaiwan.alexander.pm25mvp.main.utilities.StationData;
import com.squareup.otto.Subscribe;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;


public class MainActivity extends ActionBarActivity implements MainView {

    public static final String PREF_STATION_DATA = "pref_station_data";
    public static final String PREF_CHART_DATA = "pref_chart_data";
    public static final String URL = "http://opendata.epa.gov.tw/ws/Data/AQX/?$orderby=County&$skip=0&$top=1000&format=json";
    private static final int CLEAR_CHART_CODE = 1;

    private Context mContext = this;

    //SharedPrefs;
    private SharedPreferences mSharedPrefs;

    private Boolean mIsParseLoading = false;
    private Boolean mIsHttpLoading = false;

    //Toolbar Items
    private Toolbar mToolbar;
    private ProgressBar mProgressBar;

    //Views
    private TextView mPM25TextView;
    private TextView mSiteTextView;
    private TextView mTimeTextView;
    private TextView mQualityTextView;
    private Button mGetDataButton;
    private Button mSettingsButton;
    private LinearLayout mLinearLayout;
    private GradientDrawable mGradientDrawable;

    private Presenter mPresenter;

    //Chart Items
    private ColumnChartView mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EventBus.getInstance().register(this);
        mPresenter = new PresenterImpl(this);
        //Setup Views
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        mProgressBar = (ProgressBar) findViewById(R.id.toolbar_progress_indicator);
        mLinearLayout = (LinearLayout) findViewById(R.id.linearBackground);
        mGradientDrawable = (GradientDrawable) mLinearLayout.getBackground();
        mPM25TextView = (TextView) findViewById(R.id.text_view_pm25);
        mSiteTextView = (TextView) findViewById(R.id.text_view_site);
        mTimeTextView = (TextView) findViewById(R.id.text_view_time);
        mQualityTextView = (TextView) findViewById(R.id.text_view_quality);

        //CHART
        mChart = (ColumnChartView) findViewById(R.id.columnChart);
        mChart.setOnValueTouchListener(new ValueTouchListener());

        //Setup Listeners

        mGetDataButton = (Button) findViewById(R.id.button_get_data);
        mGetDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                mIsHttpLoading = true;
                mIsParseLoading = true;
                String preferredStation = mPresenter.getPreferredStation(mSharedPrefs);
                mPresenter.getStationArray(preferredStation);
                //TODO preferred Station
                mPresenter.getChartArrayOfArrays(preferredStation, getResources());
            }
        });

        mSettingsButton = (Button) findViewById(R.id.button_settings);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, SettingsActivity.class);
                i.putExtra(SettingsActivity.CODE, SettingsActivity.COUNTY);
                startActivityForResult(i, CLEAR_CHART_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Null out prefs
        if(resultCode == RESULT_OK) {
            mSharedPrefs.edit().putString(PREF_STATION_DATA, "NONE").apply();
            mSharedPrefs.edit().putString(PREF_CHART_DATA, "NONE").apply();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSharedPrefs = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        mPresenter.loadSavedData(mSharedPrefs, getResources());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }



    @Subscribe
    public void onAsyncTaskResult(AsyncTaskResultEvent event) {
        mIsHttpLoading = false;
        if (!mIsParseLoading) {
            hideProgress();
        }
        mPresenter.getStationData(event.getJsonArray(), getResources(), this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE));
    }

    @Subscribe
    public void onParseResult(ParseResultEvent event) {
        mIsParseLoading = false;
        if (!mIsHttpLoading) {
            hideProgress();
        }
        mChart.setColumnChartData(event.getChartData());
        mPresenter.saveChartData(event.getChartDataString(), mSharedPrefs);

    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setViewsFromStation(StationData stationData) {
        mQualityTextView.setText(stationData.getAirQuality());
        mQualityTextView.setBackgroundColor(stationData.getLabelColor());
        mGradientDrawable.setColor(stationData.getBgColor());
        mSiteTextView.setText(stationData.getPreferredStation());
        mPM25TextView.setText(stationData.getPm25());
        mTimeTextView.setText(stationData.getTime());
    }

    @Override
    public void setChartData(ColumnChartData chartData) {
        mChart.setColumnChartData(chartData);
    }

    private class ValueTouchListener implements ColumnChartOnValueSelectListener {

        @Override
        public void onValueSelected(int i, int i1, SubcolumnValue subcolumnValue) {
            String value = String.valueOf(subcolumnValue.getValue());
            Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
