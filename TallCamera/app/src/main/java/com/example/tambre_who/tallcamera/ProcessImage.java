package com.example.tambre_who.tallcamera;

/**
 * Created by tambre-who on 10/20/19.
 */

import android.os.AsyncTask;
import android.widget.TextView;
import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.*;
import java.net.*;
import java.io.*;


public class ProcessImage extends AsyncTask<File, Void, String> {

    MainActivity.ListViewAdapter list;

    public ProcessImage(MainActivity.ListViewAdapter list) {
        this.list = list;
    }

    public String multipartRequest(String urlTo, File file)  {
        String fileMimeType = ".jpg";
        String filefield = "file";
        HttpURLConnection connection = null;
        DataOutputStream outputStream = null;
        InputStream inputStream = null;

        String twoHyphens = "--";
        String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
        String lineEnd = "\r\n";

        String result = "";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        // String[] q = filepath.split("/");
        // int idx = q.length - 1;

        try {

            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlTo);
            connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + file.getName() + "\"" + lineEnd);
            outputStream.writeBytes("Content-Type: " + fileMimeType + lineEnd);
            outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);

            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);

            // // Upload POST Data
            // Iterator<String> keys = parmas.keySet().iterator();
            // while (keys.hasNext()) {
            //     String key = keys.next();
            //     String value = parmas.get(key);

            //     outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            //     outputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd);
            //     outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
            //     outputStream.writeBytes(lineEnd);
            //     outputStream.writeBytes(value);
            //     outputStream.writeBytes(lineEnd);
            // }

            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            outputStream.flush();

            if (200 != connection.getResponseCode()) {
                // throw new CustomException("Failed to upload code:" + connection.getResponseCode() + " " + connection.getResponseMessage());
            }

            inputStream = connection.getInputStream();

            result = this.convertStreamToString(inputStream);

            fileInputStream.close();
            inputStream.close();
            outputStream.flush();
            outputStream.close();


            return result;
        } catch (Exception e) {
            e.printStackTrace();
            // throw new CustomException(e);
            return null;
        }

    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    @Override
    protected String doInBackground(File... files) {
        return multipartRequest("http://10.138.35.52:5000/image",files[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result == null) return;
        Log.e("Downloaded ", result + " bytes");
        JsonParser jsp = new JsonParser();
        JsonElement jsresult = jsp.parse(result);
        JsonObject food = jsresult.getAsJsonObject();
        JsonElement foods = food.get("food");
        JsonArray foodsss = foods.getAsJsonArray();
        for (JsonElement item:foodsss) {
            list.addFood(new ListViewCell(item.getAsJsonObject().get("name").getAsString(), item.getAsJsonObject().get("date").getAsString()));
            // Log.e("2", item.getAsString());
        }

        //list.add(new ListViewCell("grapefruit", "8"));
    }
}
