package com.example.simpleui.simpleui;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

    public static byte[] urlToBytes(String urlString){
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGeoCodingUrl(String address){
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address;
        return url;
    }

    public static double[] getLatLngFromJsonString(String jsonStrng){
        try {
            JSONObject object = new JSONObject(jsonStrng);

            if (!(object.getString("status").equals("OK"))){
                return null;
            }

            JSONObject location = object.getJSONArray("results")
                                        .getJSONObject(0)
                                        .getJSONObject("geometry")
                                        .getJSONObject("location");

            double lat = location.getDouble("lat");
            double lng = location.getDouble("lng");

            return new double[]{lat, lng};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static double[] addressToLatLng(String address){
        String url = Utils.getGeoCodingUrl(address);
//                Log.d("LogTrace", "URL："+result);
        byte[] bytes = Utils.urlToBytes(url);

        String result = new String(bytes);
//                Log.d("LogTrace", result);

        double[] locations = Utils.getLatLngFromJsonString(result);

        return locations;
    }

    public static String getStaticMapUrl(double[] latlng, int zoom){
        String center = latlng[0] + "," + latlng[1];
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=" + center + "&zoom=" + zoom + "&size=640x400";
        return url;
    }
}
