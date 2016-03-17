package com.example.simpleui.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    CheckBox hideCheckBox;
    ListView listView;
    Spinner spinner;

    String menuResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("debug", "main menu onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        hideCheckBox = (CheckBox)findViewById(R.id.checkBox);

        sp = getSharedPreferences("setting", Context.MODE_PRIVATE); // 定義setting裡面的東西，供之後使用
        editor = sp.edit();
        listView = (ListView) findViewById(R.id.listView);
        spinner = (Spinner) findViewById(R.id.spinner);

        editText.setText(sp.getString("editText", ""));             // 取出sp裡面editText的內容，存回到editText

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editor.putString("editText", editText.getText().toString());//把所key入的內容寫入到editText裡面，然後存起來
                editor.apply();

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {    //前面定義按下去的按鈕為enter，後面定義時機
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {       // 設定可以偵測虛擬鍵盤的輸入
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit(v);
                    return true;
                }

                return false;
            }
        });

        //抓取畫面上hidecheckbox物件的值，如果有被勾選就帶入true，如果沒有勾選就是預設為false，再存回sp裡面的hidecheckbox
        hideCheckBox.setChecked(sp.getBoolean("hideCheckbox", false));

        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckbox", hideCheckBox.isChecked());
                editor.apply();
            }
        });

        this.setListView();
        this.setSpinner();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

        ParseObject testObject;
        //testObject = new ParseObject("TestObject");
        testObject = new ParseObject("HomeworkParse");
        //testObject.put("Android Class Parse Test", "hello");
        //testObject.put("him", "榮榮要結婚了，請大家來多多捧場！！！");
        //testObject.put("hi", "hello");

        testObject.put("sid", "And26312");
        testObject.put("email", "yien@taipower.com");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.d("Gradle Debug Message:", e.toString());
                }
            }
        });

    }

    public void setListView(){
        String[] data = Utils.readFile(this, "history.txt").split("\n");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, data);
        listView.setAdapter(adapter);
    }

    public void setSpinner(){
        //String[] data = {"1","2","3","4","5"};
        String[] data = getResources().getStringArray(R.array.storeInfo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
    }

    public void submit(View view)
    {
        //Toast.makeText(this, "Hello DUDE!!!", Toast.LENGTH_LONG).show();// 第一個參數把自己傳入，第二個是要顯示的東西，第三個是顯示的時間
        //textView.setText("Test Test Test!!!");

        String text = editText.getText().toString();
        ParseObject orderObject = new ParseObject("Order");

        orderObject.put("note", text);
        orderObject.put("storeInfo", spinner.getSelectedItem());
        orderObject.put("menu", menuResult);

        orderObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(MainActivity.this, "Submit OK", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Submit Fail", Toast.LENGTH_LONG).show();
                }
            }
        });

        Utils.writeFile(this, "history.txt", text + '\n');


        if (hideCheckBox.isChecked())
        {
            Toast.makeText(this,text,Toast.LENGTH_LONG).show();
            textView.setText("**********");
            editText.setText("**********");
            return;
        }
        editText.setText("");
        textView.setText(text);

    }

    public void goToMenu(View view){
        //方法一
        Intent intent = new Intent(this, DrinkMenuAvtivity.class);

        //方法二
//        Intent intent = new Intent(MainActivity.this, DrinkMenuAvtivity.class);


        //方法三
//        Intent intent = new Intent();
//        intent.setClass(this, DrinkMenuAvtivity.class);

        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("debug", "main menu onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_MENU_ACTIVITY){
            if(resultCode == RESULT_OK){
                JSONArray array;

                menuResult = data.getStringExtra("result");
                try {
                    String text = "";
                    array = new JSONArray(menuResult);
                    for(int i=0; i<array.length(); i++){
                        String name, lNumber, mNumber;
                        JSONObject order = array.getJSONObject(i);

                        name = order.getString("name");
                        lNumber = String.valueOf(order.getString("lNumber"));
                        mNumber = String.valueOf(order.getString("mNumber"));

                        text += name+ "大杯："+ lNumber+ "、中杯："+ mNumber+ "\n";
                    }

                    textView.setText(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //textView.setText(data.getStringExtra("result"));
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d("debug", "main menu onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("debug", "main menu onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d("debug", "main menu onPause");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d("debug", "main menu onStop");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d("debug", "main menu onRestart");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("debug", "main menu onDestroy");
    }
}
