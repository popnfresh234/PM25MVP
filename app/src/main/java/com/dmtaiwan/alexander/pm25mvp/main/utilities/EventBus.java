package com.dmtaiwan.alexander.pm25mvp.main.utilities;

import com.squareup.otto.Bus;

/**
 * Created by Alexander on 5/22/2015.
 */
public class EventBus {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}
