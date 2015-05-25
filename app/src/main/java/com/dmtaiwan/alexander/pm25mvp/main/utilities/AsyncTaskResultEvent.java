package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import org.json.JSONArray;

/**
 * Created by Alexander on 5/22/2015.
 */
public class AsyncTaskResultEvent {
    private JSONArray jsonArray;

    public AsyncTaskResultEvent(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }
    public JSONArray getJsonArray() {
        return jsonArray;
    }
}
