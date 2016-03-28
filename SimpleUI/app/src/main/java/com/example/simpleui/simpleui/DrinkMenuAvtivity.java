package com.example.simpleui.simpleui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuAvtivity extends LogTraceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu_avtivity);
    }

    public void add(View view) {
        Button button = (Button) view;
        int number = Integer.parseInt(button.getText().toString());

        number++;
        ((Button) view).setText(String.valueOf(number));
    }

    public void cancel(View view) {
        finish();       //不要這個頁面(Call this when your activity is done and should be closed.)
    }

    public void done(View view) {
        Intent data = new Intent();
        JSONArray drinkOrder = this.getOrderData();

        //回傳總訂單資料描述
        data.putExtra("result", drinkOrder.toString());
        this.setResult(RESULT_OK, data);
        this.finish();   //不要這個頁面(Call this when your activity is done and should be closed.)
    }

    /**
     * 從選單頁面讀取訂單資訊，並以JSONArray的格式回傳
     *
     * @return
     */
    public JSONArray getOrderData() {
        LinearLayout rootLinearLayout = (LinearLayout) findViewById(R.id.root);

        int count = rootLinearLayout.getChildCount();

        JSONArray array = new JSONArray();

        // 限制count-1，是因為不需拿最後一個LinearLayout物件
        for (int i = 0; i < count - 1; i++) {
            LinearLayout linearLayout = (LinearLayout) rootLinearLayout.getChildAt(i);
            TextView drinkNameTextView = (TextView) linearLayout.getChildAt(0);
            Button lButton = (Button) linearLayout.getChildAt(1);
            Button mButton = (Button) linearLayout.getChildAt(2);

            String drinkName = drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());

            JSONObject object = this.setJSONObject(
                    "name", drinkName,
                    "lNumber", lNumber,
                    "mNumber", mNumber);
            array.put(object);
        }

        return array;
    }

    /**
     * 把資料存入到JSONObject
     *
     * @param nameKey
     * @param name
     * @param lNumberKey
     * @param lNumber
     * @param mNumberKey
     * @param mNumber
     * @return
     */
    private JSONObject setJSONObject(String nameKey, String name,
                                     String lNumberKey, int lNumber,
                                     String mNumberKey, int mNumber) {
        JSONObject object = new JSONObject();

        try {
            object.put(nameKey, name);
            object.put(lNumberKey, lNumber);
            object.put(mNumberKey, mNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }
}
