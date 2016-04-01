package com.example.simpleui.simpleui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by new on 2016/3/27.
 */
public class LogTraceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LogTrace", this.getLocalClassName() + ":onCreate()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LogTrace", this.getLocalClassName() + ":onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LogTrace", this.getLocalClassName() + ":onDestroy()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LogTrace", this.getLocalClassName() + ":onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LogTrace", this.getLocalClassName() + ":onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LogTrace", this.getLocalClassName() + ":onResume()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LogTrace", this.getLocalClassName() + ":onStart()");
    }
}
