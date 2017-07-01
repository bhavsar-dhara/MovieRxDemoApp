package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

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
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TMDbService tmDbService = ServiceFactory.createRetrofitService(TMDbService.class, Constant.SERVICE_ENDPOINT);

        Observable<Movies> discover = tmDbService.getMovies("01edd08f251d3f0b1c6789182e3214e1");

        discover.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movies -> {
                    Log.e(TAG, "TotalResults = " + movies.getTotalResults().toString());
                });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Search Movies...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
