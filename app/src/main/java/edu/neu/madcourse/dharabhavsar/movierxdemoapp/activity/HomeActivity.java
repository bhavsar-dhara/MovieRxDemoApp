package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.R;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.MovieResult;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.model.Movies;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.service.ServiceFactory;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.service.TMDbService;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.Constant;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.EndlessScrollListener;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkListener;
import edu.neu.madcourse.dharabhavsar.movierxdemoapp.utils.NetworkUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity
        implements NetworkListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private View parentView;
    private boolean isConnected;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    // Store a member variable for the listener
    private EndlessScrollListener scrollListener;
    private List<MovieResult> moviesList;
    private TMDbService tmDbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        parentView = findViewById(R.id.home_activity);
        isConnected = NetworkUtils.isConnected(this);
        NetworkUtils.showSnack(isConnected, parentView);

        tmDbService = ServiceFactory.createRetrofitService(TMDbService.class,
                Constant.SERVICE_ENDPOINT);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a grid layout manager
        int spanCount = 2;
        mLayoutManager = new GridLayoutManager(this, spanCount);
        mRecyclerView.setLayoutManager(mLayoutManager);

        callMovieDbApi();

        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);

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

            Observable<Movies> discover = tmDbService.getMovies("01edd08f251d3f0b1c6789182e3214e1");

            discover.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movies -> {
                        Log.e(TAG, "TotalResults = " + movies.getTotalResults().toString());
                        // specify an adapter (see also next example)
                        this.moviesList = movies.getMovieResults();
                        mAdapter = new MoviesAdapter(this.moviesList, getApplicationContext());
                        mRecyclerView.setAdapter(mAdapter);
                    });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
        NetworkUtils.showSnack(isConnected, parentView);
        callMovieDbApi();
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
        if (isConnected) {

            Observable<Movies> discover = tmDbService.getMovies("01edd08f251d3f0b1c6789182e3214e1", offset + 1);

            discover.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(movies -> {
                        Log.e(TAG, "TotalPages = " + movies.getTotalPages().toString());
                        // specify an adapter (see also next example)
                        this.moviesList.addAll(movies.getMovieResults());
                        mRecyclerView.getAdapter().notifyDataSetChanged();
                    });
        }
    }
}
