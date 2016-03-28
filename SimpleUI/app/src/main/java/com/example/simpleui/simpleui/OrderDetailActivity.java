package com.example.simpleui.simpleui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends LogTraceActivity {
    private String url;
    private String address;

    TextView note, storeInfo, menu;
    ImageView photo;
    String menuResult;
    String storeInformation;
    ImageView staticMapImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        this.note = (TextView) findViewById(R.id.noteView);
        this.storeInfo = (TextView) findViewById(R.id.storeInfoView);
        this.menu = (TextView) findViewById(R.id.menuView);
        this.photo = (ImageView) findViewById(R.id.imageView);
        this.staticMapImageView = (ImageView) findViewById(R.id.staticMapImageView);

        this.note.setText(this.getIntent().getStringExtra("note"));
        this.storeInfo.setText(this.getIntent().getStringExtra("storeInfo"));
        this.menuResult = this.getIntent().getStringExtra("menu");

        this.storeInformation = this.storeInfo.getText().toString();

        JSONArray array;
        String text = "";
        String name, lNumber, mNumber;

        //取lNumber、mNumber的訂單
        try {
            array = new JSONArray(menuResult);
            for (int i = 0; i < array.length(); i++) {
                JSONObject order = array.getJSONObject(i);
                name = order.getString("name");
                lNumber = String.valueOf(order.getString("lNumber"));
                mNumber = String.valueOf(order.getString("mNumber"));

                text += name + "大杯：" + lNumber + "、中杯：" + mNumber + "\n";
            }
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        //取l、m的訂單
        try {

            array = new JSONArray(menuResult);
            for (int i = 0; i < array.length(); i++) {
                JSONObject order = array.getJSONObject(i);
                name = order.getString("name");
                lNumber = String.valueOf(order.getString("l"));
                mNumber = String.valueOf(order.getString("m"));

                text += name + "大杯：" + lNumber + "、中杯：" + mNumber + "\n";
            }
        } catch (JSONException e) {
//            e.printStackTrace();
        }

        this.menu.setText(text);

        address = storeInformation.split(",")[1];

        double[] locations = Utils.addressToLatLng(address);
        (new ImageLoadingTask(this.photo)).execute(Utils.getStaticMapUrl(locations, 17));

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                double[] locations = Utils.addressToLatLng(address);
//
//                if(locations == null){
//                    Log.d("LogTrace", "locations is null");
//                }else{
//                    String debugLog = "lat:" + String.valueOf(locations[0])
//                            + " lng:" + String.valueOf(locations[1]);
//                    Log.d("LogTrace", debugLog);
//                }
//            }
//        });
//        thread.start();

        url = this.getIntent().getStringExtra("photoURL");
        if(url==null){
            return;
        }

//        if (url != null) {
//            Picasso.with(this).load(url).into(photo);
//        }

//        this.menu.setText(this.getIntent().getStringExtra("menu"));
//        this.note.setText(this.getIntent().getStringExtra("note"));


//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] bytes = Utils.urlToBytes(url);
//                String result = new String(bytes);
//                Log.d("LogTrace", "URL："+result);
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                photo.setImageBitmap(bitmap);
//            }
//        });
//        thread2.start();


        if (url != null) {
            (new ImageLoadingTask(this.photo)).execute();
        }

//        new AsyncTask<String, Void, byte[]>(){
//            @Override
//            protected byte[] doInBackground(String... parms){
//                String url = parms[0];
//                return Utils.urlToBytes(url);
//            }
//
//            @Override
//            protected void onPostExecute(byte[] bytes) {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                photo.setImageBitmap(bitmap);
//                super.onPostExecute(bytes);
//            }
//        }.execute(url);
    }


    class ImageLoadingTask extends AsyncTask<String, Void, byte[]>{
        ImageView imageView;

        public ImageLoadingTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected byte[] doInBackground(String... parms){
            String url = parms[0];
            return Utils.urlToBytes(url);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bytes);
        }
    }
}
