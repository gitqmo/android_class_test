package com.example.simpleui.simpleui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by new on 2016/3/27.
 */
public class LogTraceActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.d("LogTrace", this.getClass().toString() + ":onCreate()");
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        Log.d("LogTrace", this.getClass().toString() + ":onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d("LogTrace", this.getClass().toString() + ":onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("LogTrace", this.getClass().toString() + ":onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("LogTrace", this.getClass().toString() + ":onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d("LogTrace", this.getClass().toString() + ":onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d("LogTrace", this.getClass().toString() + ":onDestroy()");
        super.onDestroy();
    }
}
