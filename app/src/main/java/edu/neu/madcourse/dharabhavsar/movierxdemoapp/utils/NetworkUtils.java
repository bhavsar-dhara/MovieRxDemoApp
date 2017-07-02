package edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Dhara on 7/1/2017.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static NetworkUtils mInstance;
    private static Snackbar snackbar;

    public static NetworkListener networkListener;
    public static boolean isMonitoring = false;

    public static BroadcastReceiver receiver = null;

    public static synchronized NetworkUtils getInstance() {
        if(mInstance == null) {
            mInstance = new NetworkUtils();
            isMonitoring = true;
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    boolean isConnected = isConnected(context);
                    Log.e(TAG, "onReceive: " + isConnected);
                    if (networkListener != null) {
                        networkListener.onNetworkConnectionChanged(isConnected);
                    }
                }
            };
        }
        return mInstance;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void showSnack(boolean isConnected, View parentView) {
        String message;
        int color;
        int duration;
        if (isConnected) {
            message = "Connected to Internet";
            color = Color.WHITE;
            duration = Snackbar.LENGTH_SHORT;
        } else {
            message = "No Internet Connection";
            color = Color.RED;
            duration = Snackbar.LENGTH_INDEFINITE;
        }

        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
        }

        snackbar = Snackbar.make(parentView, message, duration);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

}
