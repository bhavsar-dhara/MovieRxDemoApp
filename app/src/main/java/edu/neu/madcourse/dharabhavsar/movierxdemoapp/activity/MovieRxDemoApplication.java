package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.squareup.leakcanary.LeakCanary;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkListener;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkUtils;

/**
 * Created by Dhara on 7/1/2017.
 */

public class MovieRxDemoApplication extends Application {

    private static MovieRxDemoApplication mInstance;

    public static synchronized MovieRxDemoApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        // Normal app init code...
        mInstance = this;

        getApplicationContext().registerReceiver(NetworkUtils.getInstance().receiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        getApplicationContext().unregisterReceiver(NetworkUtils.getInstance().receiver);
        NetworkUtils.isMonitoring = false;
    }

    public void setNetworkListener(NetworkListener listener) {
        NetworkUtils.networkListener = listener;
    }
}
