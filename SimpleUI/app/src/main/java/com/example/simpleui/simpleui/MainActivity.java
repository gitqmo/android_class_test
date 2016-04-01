package com.example.simpleui.simpleui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.util.jar.Manifest;

public class MainActivity extends LogTraceActivity {
    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    private static final int REQUEST_CODE_CAMERA = 1;

    //GUI元件宣告
    private TextView textView;
    private EditText editText;
    private CheckBox hideCheckBox;
    private ListView historyListView;
    private Spinner spinner;
    private ImageView photoView;
    private ProgressDialog progressDialog;
    private ProgressBar progressBar;
    //暫存資料變數宣告
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    //自定變數
    private boolean test;
    private boolean hasPhoto;               // 儲存是否有照片存在
    private String menuResult;              // 儲存一筆訂單全部資訊
    private List<ParseObject> queryResults; // 儲存歷史訂單資訊


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.initial();
        this.setListener();
        this.restoreData();

        this.test = false;
        if (!test) {
            this.setStoreInfos();
            this.setHistoryList();
        } else {
            Test.testSetSpinner(this, this.spinner);
            Test.testSetListView(this, this.historyListView);
//            Test.testParseServer();
        }
    }

    /**
     * 初始化
     */
    private void initial() {
        this.textView = (TextView) this.findViewById(R.id.textView);
        this.editText = (EditText) this.findViewById(R.id.editText);
        this.hideCheckBox = (CheckBox) this.findViewById(R.id.checkBox);

        this.historyListView = (ListView) this.findViewById(R.id.listView);
        this.historyListView.setVisibility(View.GONE);
        this.historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetailOrder(position);
            }
        });

        this.spinner = (Spinner) this.findViewById(R.id.spinner);
        this.photoView = (ImageView) this.findViewById(R.id.imageView);

        this.progressDialog = new ProgressDialog(this);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.setVisibility(View.GONE);

        this.sharedPreferences = this.getSharedPreferences("setting", Context.MODE_PRIVATE); // 定義setting裡面的東西，供之後使用
        this.editor = this.sharedPreferences.edit();

        this.hasPhoto = false;
        this.menuResult = "";
    }

    /**
     * 設定Listener
     */
    private void setListener() {
        // 設定可以偵測實體鍵盤的輸入
        this.editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editor.putString("editText", editText.getText().toString());//把所key入的內容寫入到editText裡面，然後存起來
                editor.apply();

                if (keyCode == KeyEvent.KEYCODE_ENTER &&                //定義按鈕為【Enter】
                        event.getAction() == KeyEvent.ACTION_DOWN) {    //定義動作為【按下去】
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        // 設定可以偵測虛擬鍵盤的輸入
        this.editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });


        this.hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckbox", hideCheckBox.isChecked());
                editor.apply();

                if (isChecked) {
                    photoView.setVisibility(View.GONE);
                } else {
                    photoView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 從sharedPreferences中先前所暫存的資料，回復到畫面中
     */
    private void restoreData() {
        // 取出sharedPreferences裡面editText的內容，存回到editText
        this.editText.setText(this.sharedPreferences.getString("editText", ""));

        /*
         * 抓取畫面上hidecheckbox物件的值(如果有被勾選就帶入true，如果沒有勾選就是預設為false)，
         * 再存回sharedPreferences裡面的hidecheckbox
         */
        this.hideCheckBox.setChecked(sharedPreferences.getBoolean("hideCheckbox", false));
    }

    /**
     * 從先前的thread中，取出符合【StoreInfo】的ParseQuery，並把值填入下拉式選單中(spinner)
     */
    private void setStoreInfos() {
        ParseQuery<ParseObject> query = new ParseQuery<>("StoreInfo");

        // Retrieves a list of ParseObjects that satisfy this query from the source in a background thread.
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                String[] stores = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ParseObject object = list.get(i);
                    stores[i] = object.getString("name") + ", " + object.getString("address");
                }

                ArrayAdapter<String> storeAdapter = new ArrayAdapter<>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_1,
                        stores);

                spinner.setAdapter(storeAdapter);
            }
        });
    }

    private void setHistoryList() {
        ParseQuery<ParseObject> query;
        progressBar.setVisibility(View.VISIBLE);

        query = new ParseQuery<>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

                if (e != null) {
                    // 第一個參數把自己傳入、第二個是要顯示的東西、第三個是顯示的時間
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                queryResults = list;
                List<Map<String, String>> data = new ArrayList<>();
                for (int i = 0; i < queryResults.size(); i++) {
                    ParseObject object;
                    String note, storeInfo, menu;

                    object = queryResults.get(i);

                    note = object.getString("note");
                    storeInfo = object.getString("storeInfo");
                    menu = null;
//                    menu = object.getString("menu");
                    try {
//                        String temp = object.getString("menu");
//                        JSONArray jsonArray = new JSONArray(temp);
//                        int test = countTotalCupsInHistory(jsonArray);
                        menu = String.valueOf(Utils.countTotalCupsInHistory(new JSONArray(object.getString("menu"))));
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    Map<String, String> item = new HashMap<>();
                    item.put("note", note);
                    item.put("storeInfo", storeInfo);
                    item.put("drinkNumber", menu);

                    data.add(item);
                }

                String[] from = {"note", "storeInfo", "drinkNumber"};
                int[] to = {R.id.note, R.id.storeInfo, R.id.drinkNumber};

                SimpleAdapter simpleAdapter = new SimpleAdapter(
                        MainActivity.this, data, R.layout.listview_item, from, to);

                historyListView.setAdapter(simpleAdapter);
                historyListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 顯示歷史選單的訂單資訊
     *
     * @param posotion
     */
    private void goToDetailOrder(int posotion) {
        Intent intent = new Intent();
        ParseObject object = queryResults.get(posotion);

        intent.setClass(this, OrderDetailActivity.class);

        intent.putExtra("note", object.getString("note"));
        intent.putExtra("storeInfo", object.getString("storeInfo"));
        intent.putExtra("menu", object.getString("menu"));

        if (object.getParseFile("photo") != null) {
            intent.putExtra("photoURL", object.getParseFile("photo").getUrl());
        }

        startActivity(intent);
    }

    public void submit(View view) {
        if (!test) {
            //實際把資料上傳到Parse Server
            this.uploadToParseServer();
        } else {
            //實際把資料儲存到指定檔案裡面
            Test.saveData(this, this.editText.getText().toString());
            Test.submitByCheckbox(this, this.textView, this.editText, this.hideCheckBox);
        }
    }

    /**
     * 把訂單資料上傳到Parse Server
     */
    private void uploadToParseServer() {
        String text;
        ParseObject orderObject;

        //在資料尚未上傳完成前，顯示【Loading...】告知使用者，目前系統資料處理中
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        text = editText.getText().toString();
        orderObject = new ParseObject("Order");

        orderObject.put("note", text);
        orderObject.put("storeInfo", spinner.getSelectedItem());
        orderObject.put("menu", menuResult);

        if (this.hasPhoto) {
            Uri uri = Utils.getPhotoUri();
            ParseFile file = new ParseFile("photo.png", Utils.uriToBytes(this, uri));

            orderObject.put("photo", file);
        }

        orderObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                //表示系統資料處理結束，準備顯示其結果。
                progressDialog.dismiss();
                if (e == null) {
                    Toast.makeText(MainActivity.this, "Submit OK", Toast.LENGTH_LONG).show();
                    hasPhoto = false;
                    photoView.setImageResource(0);
//                    photoView.setImageDrawable(null);

                    editText.setText("");
                    textView.setText("");
                    setHistoryList();
                } else {
                    Toast.makeText(MainActivity.this, "Submit Fail", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 點選右上角Menu的選單
     *
     * @param view
     */
    public void goToMenu(View view) {
        //方法一
        Intent intent = new Intent(this, DrinkMenuAvtivity.class);

        //方法二
//        Intent intent = new Intent(MainActivity.this, DrinkMenuAvtivity.class);


        //方法三
//        Intent intent = new Intent();
//        intent.setClass(this, DrinkMenuAvtivity.class);

        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("LogTrace", this.getLocalClassName() + ":onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                //處理從DrinkMenuActivity中回傳的一筆訂單資料描述
                this.getMenuResultDescription(data);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == RESULT_OK) {
                this.hasPhoto = true;
                photoView.setImageURI(Utils.getPhotoUri());
            }
        }
    }

    /**
     * 處理從DrinkMenuActivity中回傳的一筆訂單資料描述
     *
     * @param data
     */
    private void getMenuResultDescription(Intent data) {
        JSONArray array;

        this.menuResult = data.getStringExtra("result");
        try {
            String text = "";
            array = new JSONArray(menuResult);
            for (int i = 0; i < array.length(); i++) {
                String name, lNumber, mNumber;
                JSONObject order = array.getJSONObject(i);

                name = order.getString("name");
                lNumber = String.valueOf(order.getString("lNumber"));
                mNumber = String.valueOf(order.getString("mNumber"));

                text += name + "大杯：" + lNumber + "、中杯：" + mNumber + "\n";
            }

            textView.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("LogTrace", this.getLocalClassName() + ":onCreateOptionsMenu()");
        this.getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("LogTrace", this.getLocalClassName() + ":onOptionsItemSelected()");
        int id = item.getItemId();

        if (id == R.id.action_take_photo) {
            Toast.makeText(MainActivity.this, "take Photo", Toast.LENGTH_LONG).show();
            this.goToCamera();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 進入相機功能操作
     */
    private void goToCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            //方法一：
//            if(this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED){
//                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
//                return;
//            }

            //方法二：
            if (this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                return;
            }
        }
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoUri());
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
