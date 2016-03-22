package com.example.simpleui.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        JSONArray array = this.getData();

        //data.putExtra("result","order done");

        data.putExtra("result", array.toString());
//        data.putExtra("drinkNumber", array);
        this.setResult(RESULT_OK, data);

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

    public JSONArray getData(){
        LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.root);

        int count = rootLinearLayout.getChildCount();

        JSONArray array = new JSONArray();

        // 限制count-1，是因為不需拿最後一個LinearLayout物件
        for(int i=0; i<count-1; i++){
            LinearLayout linearLayout = (LinearLayout) rootLinearLayout.getChildAt(i);
            TextView drinkNameTextView = (TextView) linearLayout.getChildAt(0);
            Button lButton = (Button) linearLayout.getChildAt(1);
            Button mButton = (Button) linearLayout.getChildAt(2);

            String drinkName = drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());

            JSONObject object = new JSONObject();
            try {
                object.put("name", drinkName);
                object.put("lNumber", lNumber);
                object.put("mNumber", mNumber);

                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return array;
    }
}
