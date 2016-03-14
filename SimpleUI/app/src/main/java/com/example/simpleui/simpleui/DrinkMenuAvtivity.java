package com.example.simpleui.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DrinkMenuAvtivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "drink menu onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu_avtivity);
    }

    public void add(View view){
        Button button = (Button)view;
        int number = Integer.parseInt(button.getText().toString());

        number++;
        ((Button) view).setText(String.valueOf(number));
    }

    public void cancel(View view){
        finish();
    }

    public void done(View view){
        Intent data = new Intent();
        data.putExtra("result","order done");
        setResult(RESULT_OK, data);

        Log.d("debug", "main menu done before");
        finish();   //不要這個頁面
        Log.d("debug", "main menu done after");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug", "drink menu onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("debug", "drink menu onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("debug", "drink menu onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("debug", "drink menu onStop");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("debug", "drink menu onRestart");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("debug", "drink menu onDestroy");
    }
}
