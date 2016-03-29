package com.example.simpleui.simpleui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class OrderDetailActivity extends LogTraceActivity {
    private TextView note, storeInfo, menu;
    private ImageView photo;
    private String menuResult;
    private String storeInformation;
    private ImageView staticMapImageView;
    private WebView webView;
    private Switch switch1;

    private String url, address;
    private boolean switchFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order_detail);

        this.initial();
        this.setListener();

        this.showOrderDetail();
        this.showPhoto();
        this.showGoogleMap();
    }

    /**
     * 初始化
     */
    private void initial() {
        this.note = (TextView) findViewById(R.id.noteView);
        this.storeInfo = (TextView) findViewById(R.id.storeInfoView);
        this.menu = (TextView) findViewById(R.id.menuView);
        this.photo = (ImageView) findViewById(R.id.photoView);
        this.staticMapImageView = (ImageView) findViewById(R.id.staticMapImageView);
        this.webView = (WebView) findViewById(R.id.webView);
        this.switch1 = (Switch) findViewById(R.id.switch1);

        this.note.setText(this.getIntent().getStringExtra("note"));
        this.storeInfo.setText(this.getIntent().getStringExtra("storeInfo"));
        this.menuResult = this.getIntent().getStringExtra("menu");

        this.defaultSetting();
    }

    /**
     * 設定Listener
     */
    private void setListener() {
        this.switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchFlag = true;
                    staticMapImageView.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);
                    Log.d("LogTrace", "switchFlag:" + switchFlag);
                } else {
                    switchFlag = false;
                    staticMapImageView.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);
                    Log.d("LogTrace", "switchFlag:" + switchFlag);
                }
            }
        });
    }

    /**
     * 顯示訂單資訊
     */
    private void showOrderDetail() {
        //顯示被點擊的訂單資訊
        try {
            this.menu.setText(String.valueOf(Utils.countTotalCupsInHistory(new JSONArray(menuResult))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 顯示照片
     */
    private void showPhoto() {
        this.url = this.getIntent().getStringExtra("photoURL");
        if (this.url == null) {
            return;
        } else {
            //方法一：使用Picasso套件來顯示圖片
//            this.getImageByPicasso(url);

            //方法二：用Thread的方式來取的圖片，但會有問題。
//        Test.testGetImage(photo, url);

            //方法三：使用Asynchronous Thread的方式來顯示圖片
            (new ImageLoadingTask(this.photo)).execute(this.url);
        }
    }

    /**
     * 顯示Google Map
     */
    private void showGoogleMap() {
        byte[] bytes;

        bytes = null;
        this.address = storeInformation.split(",")[1];

        //方法一：使用Thread的方式來存取Google Map
//        Test.getLatLngByThread(address);

        //方法二：使用Asynchronous Thread的方式來存取Google Map
        (new GeoCodingTask(this.staticMapImageView)).execute(this.address);
    }

    /**
     * 初始設定值
     */
    private void defaultSetting() {
        this.storeInformation = this.storeInfo.getText().toString();

        this.switchFlag = false;
        this.staticMapImageView.setVisibility(View.VISIBLE);
        this.webView.setVisibility(View.GONE);
    }

    /**
     * 使用Picasso套件來顯示圖片
     *
     * @param url
     */
    private void getImageByPicasso(String url) {
        if (url != null) {
            Picasso.with(this).load(url).into(this.photo);
        }
    }

    /**
     * 使用Asynchronous Thread的方式來顯示圖片
     */
    class ImageLoadingTask extends AsyncTask<String, Void, byte[]> {
        ImageView imageView;

        public ImageLoadingTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String url = params[0];
            return Utils.urlToBytes(url);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bytes);
        }
    }

    /**
     * 使用Asynchronous Thread的方式來存取Google Map
     */
    class GeoCodingTask extends AsyncTask<String, Void, byte[]> {
        ImageView imageView;

        public GeoCodingTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String address = params[0];
            double[] location = Utils.addressToLatLng(address);
            url = Utils.getStaticMapUrl(location, 17);
            return Utils.urlToBytes(url);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            Bitmap bitmap;

            webView.loadUrl(url);
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);

//            if(switchFlag){
//                webView.setVisibility(View.GONE);
//            }else{
//                imageView.setImageResource(0);
//            }
            super.onPostExecute(bytes);
        }
    }
}
