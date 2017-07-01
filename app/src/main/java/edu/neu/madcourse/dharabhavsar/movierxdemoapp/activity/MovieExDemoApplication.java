package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkUtils;

/**
 * Created by Dhara on 7/1/2017.
 */

public class MovieExDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        getApplicationContext().registerReceiver(NetworkUtils.getmInstance().receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        getApplicationContext().unregisterReceiver(NetworkUtils.getmInstance().receiver);
        NetworkUtils.isMonitoring = false;
    }
}
