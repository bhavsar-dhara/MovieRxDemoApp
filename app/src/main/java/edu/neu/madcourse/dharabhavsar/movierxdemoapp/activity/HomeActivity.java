package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.R;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.Movies;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.service.ServiceFactory;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.service.TMDbService;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.Constant;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkListener;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity
        implements NetworkListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    View parentView;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentView = findViewById(R.id.home_activity);
        isConnected = NetworkUtils.isConnected(this);
        NetworkUtils.showSnack(isConnected, parentView);

        callMovieDbApi();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Search Movies...", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (NetworkUtils.isMonitoring) {
                this.getApplicationContext().unregisterReceiver(NetworkUtils.getInstance().receiver);
                NetworkUtils.isMonitoring = false;
            }
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "onPause: IllegalArgumentException: ", e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MovieRxDemoApplication.getInstance().setNetworkListener(this);

        if (!NetworkUtils.isMonitoring) {
            this.registerReceiver(NetworkUtils.getInstance().receiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    private void callMovieDbApi() {
        if (isConnected) {
            TMDbService tmDbService = ServiceFactory.createRetrofitService(TMDbService.class,
                    Constant.SERVICE_ENDPOINT);

            Observable<Movies> discover = tmDbService.getMovies("01edd08f251d3f0b1c6789182e3214e1");

            discover.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movies -> {
                        Log.e(TAG, "TotalResults = " + movies.getTotalResults().toString());
                    });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
        NetworkUtils.showSnack(isConnected, parentView);
        callMovieDbApi();
    }
}
