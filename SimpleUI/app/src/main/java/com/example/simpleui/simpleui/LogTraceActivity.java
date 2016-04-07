package com.example.simpleui.simpleui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by new on 2016/3/27.
 */
public class LogTraceActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonPostCreate()");
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonResume()");
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonDestroy()");
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonPrepareOptionsMenu()");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonOptionsItemSelected()");
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LogTrace", this.getLocalClassName() + ":\tonActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
