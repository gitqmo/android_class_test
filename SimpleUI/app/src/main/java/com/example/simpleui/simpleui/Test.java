package com.example.simpleui.simpleui;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by new on 2016/3/27.
 */
public class Test {
    /**
     * 用來簡單測試與ParseServer連線，傳送訊息。
     */
    public static void testParseServer() {
        ParseObject testObject;
        //testObject = new ParseObject("TestObject");
        testObject = new ParseObject("HomeworkParse");

        testObject.put("sid", "And26312");
        testObject.put("email", "yien@taipower.com");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.d("Gradle Debug Message:", e.toString());
                }
            }
        });
    }

    /**
     * 簡單應用Spinner(店家資訊在array.xml中定義)
     */
    public static void testSetSpinner(Context context, Spinner spinner) {
        String[] data;
        ArrayAdapter<String> adapter;
        //String[] data = {"1","2","3","4","5"};

        data = context.getResources().getStringArray(R.array.storeInfo);
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
    }

    /**
     * 簡單從sharedPreferences中讀取資料顯示到TextView上
     */
    public static void testSetListView(Context context, ListView listView) {
        String[] data;
        ArrayAdapter<String> adapter;

        data = Utils.readFile(context, "history.txt").split("\n");
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, data);
        listView.setAdapter(adapter);
    }

    /**
     * 簡單把傳入的文字儲存到指定檔案中。
     *
     * @param context
     * @param text
     */
    public static void saveData(Context context, String text) {
        Utils.writeFile(context, "history.txt", text + '\n');
    }

    /**
     * 依據checkBox值來決定是否隱藏資訊
     *
     * @param context
     * @param textView
     * @param editText
     * @param checkBox
     */
    public static void submitByCheckbox(Context context,
                                        TextView textView,
                                        EditText editText,
                                        CheckBox checkBox) {
        if (checkBox.isChecked()) {
            Toast.makeText(context, editText.getText().toString(), Toast.LENGTH_LONG).show();
            textView.setText("**********");
            editText.setText("**********");
            return;
        }
        textView.setText(editText.getText().toString());
        editText.setText("");
    }
}
