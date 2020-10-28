package com.prasadmukne.android.peopleinteractive.matchmaker.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.prasadmukne.android.peopleinteractive.matchmaker.R;
import com.prasadmukne.android.peopleinteractive.matchmaker.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initialiseUI();

        runWaitThread();
    }

    private void initialiseUI() {
        mProgressBar = findViewById(R.id.progress_bar_splash);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void runWaitThread() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                finish();
            }
        }, 1000);
    }

}
