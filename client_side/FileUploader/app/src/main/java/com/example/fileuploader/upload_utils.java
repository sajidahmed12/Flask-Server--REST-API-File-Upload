package com.example.fileuploader;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import android.content.ContentResolver;

public class upload_utils {

    private static String tag = MainActivity.tag;

    private static  String getPath(Context context, Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public static String getFileName(Context context, Uri uri){

        String[] projection = { MediaStore.Video.Media.DISPLAY_NAME };

        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "img", null);
        return Uri.parse(path);
    }

    public static byte[] uriToByteArray(Context context, Uri uri) throws IOException {
        // this dynamically extends to take the bytes you read
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }
    public static void upload_file(Context context,Uri uri){

        String file_path= upload_utils.getPath(context,uri);

        Log.e(tag,file_path);

        OkHttpClient client = new OkHttpClient();
        String baseUrl = "http://192.168.0.103:8080/upload/";
        Log.e(tag,"in connect server");

        try
        {

            MediaType MEDIA_TYPE_ALL = MediaType.parse("*/*");

            File file = new File(file_path);
            byte[] fileContent = Files.readAllBytes(file.toPath());

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).
                    addFormDataPart("file_type","video").
                    addFormDataPart("file", file_path, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();

            Request request = new Request.Builder().url(baseUrl).header("Accept", "application/json").
                    header("Content-Type", "multipart/form-data").post(body).build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(tag,"failed to post!" + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    try {
                        JSONObject Jobject = new JSONObject(jsonData);
                        Log.e(tag,Jobject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("findme",response.body().toString());
                    // Log.e(tag,String.valueOf(response.code()));
                }
            });

        }
        catch(Exception e)
        {
            System.out.println("ee");
        }

    }
    public static void upload_file_byte(Context context, Uri uri){

        String file_name = getFileName(context,uri);
        byte[] fileContent = new byte[0];
        try {
            fileContent = uriToByteArray(context,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "http://192.168.0.103:8080/upload/";
        Log.e(tag,"in connect server");

        try
        {

            MediaType MEDIA_TYPE_ALL = MediaType.parse("*/*");

            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM).
                    addFormDataPart("file_type","video").
                    addFormDataPart("file", file_name, RequestBody.create(MEDIA_TYPE_ALL, fileContent)).build();

            Request request = new Request.Builder().url(baseUrl).header("Accept", "application/json").
                    header("Content-Type", "multipart/form-data").post(body).build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(tag,"failed to post!" + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String jsonData = response.body().string();
                    try {
                        JSONObject Jobject = new JSONObject(jsonData);
                        Log.e(tag,Jobject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("findme",response.body().toString());
                    // Log.e(tag,String.valueOf(response.code()));
                }
            });

        }
        catch(Exception e)
        {
            System.out.println("ee");
        }

    }

}
