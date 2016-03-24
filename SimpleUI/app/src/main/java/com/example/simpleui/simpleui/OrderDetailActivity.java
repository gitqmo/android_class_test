package com.example.simpleui.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {
    TextView note, storeInfo, menu;
    ImageView photo;
    String menuResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        this.note = (TextView) findViewById(R.id.noteView);
        this.storeInfo = (TextView) findViewById(R.id.storeInfoView);
        this.menu = (TextView) findViewById(R.id.menuView);
        this.photo = (ImageView) findViewById(R.id.imageView);

        this.note.setText(this.getIntent().getStringExtra("note"));
        this.storeInfo.setText(this.getIntent().getStringExtra("storeInfo"));
        this.menuResult = this.getIntent().getStringExtra("menu");

        JSONArray array;
        String text = "";
        String name, lNumber, mNumber;
        try {
            array = new JSONArray(menuResult);
            for(int i=0; i<array.length(); i++){
                JSONObject order = array.getJSONObject(i);
                name = order.getString("name");
                lNumber = String.valueOf(order.getString("lNumber"));
                mNumber = String.valueOf(order.getString("mNumber"));

                text += name+ "大杯："+ lNumber+ "、中杯："+ mNumber+ "\n";
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            array = new JSONArray(menuResult);
            for(int i=0; i<array.length(); i++){
                JSONObject order = array.getJSONObject(i);
                name = order.getString("name");
                lNumber = String.valueOf(order.getString("l"));
                mNumber = String.valueOf(order.getString("m"));

                text += name+ "大杯："+ lNumber+ "、中杯："+ mNumber+ "\n";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.menu.setText(text);

        String url = this.getIntent().getStringExtra("photoURL");
        if(url != null){
            Picasso.with(this).load(url).into(photo);
        }

//

//        this.menu.setText(this.getIntent().getStringExtra("menu"));
//        this.note.setText(this.getIntent().getStringExtra("note"));
    }
}
