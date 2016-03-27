package com.example.simpleui.simpleui;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by new on 2016/3/10.
 */
public class Utils {
    public static void writeFile(Context context, String fileName, String content) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            fileOutputStream.write(content.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) {
        FileInputStream fileInputStream;
        try {
            fileInputStream = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            fileInputStream.read(buffer, 0, buffer.length);
            fileInputStream.close();
            return new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Uri getPhotoUri() {
        File dir, file;

        dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!dir.exists()) {
            dir.mkdir();
        }

        file = new File(dir, "simple_photo.png");

        return Uri.fromFile(file);
    }

    public static byte[] uriToBytes(Context context, Uri uri) {
        InputStream inputStream;
        ByteArrayOutputStream byteArrayOutputStream;

        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
