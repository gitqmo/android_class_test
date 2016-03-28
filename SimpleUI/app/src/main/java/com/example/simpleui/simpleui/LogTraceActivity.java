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
        Log.d("LogTrace", this.getLocalClassName() + ":onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        Log.d("LogTrace", this.getLocalClassName() + ":onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("LogTrace", this.getLocalClassName() + ":onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d("LogTrace", this.getLocalClassName() + ":onRestart()");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.d("LogTrace", this.getLocalClassName() + ":onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("LogTrace", this.getLocalClassName() + ":onResume()");
        super.onResume();
    }

    @Override
    protected void onStart() {
        Log.d("LogTrace", this.getLocalClassName() + ":onStart()");
        super.onStart();
    }
}
