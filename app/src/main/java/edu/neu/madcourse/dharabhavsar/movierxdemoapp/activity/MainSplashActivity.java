package edu.neu.madcourse.dharabhavsar.movierxdemoapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import edu.neu.madcourse.dharabhavsar.movierxdemoapp.R;

public class MainSplashActivity extends AppCompatActivity {

    private static final String TAG = MainSplashActivity.class.getSimpleName();
    private static final int LOADING_TIME = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: " + TAG);
        setContentView(R.layout.activity_main_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//	              Creating an Intent that will re-direct the Main-Reader-Activity class
                Intent mainIntent = new Intent(MainSplashActivity.this, HomeActivity.class);
                MainSplashActivity.this.startActivity(mainIntent);
                MainSplashActivity.this.finish();
            }
        }, LOADING_TIME);
    }
}
