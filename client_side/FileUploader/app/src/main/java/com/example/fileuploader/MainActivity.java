package com.example.fileuploader;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


//https://stackoverflow.com/questions/23874842/how-to-select-a-file-with-selecting-from-gallery-or-file-manager-in-android
//https://stackoverflow.com/questions/41193219/how-to-select-file-on-android-using-intent


public class MainActivity extends AppCompatActivity {

    Button upload_button;
    String tag="findme";

    final int ACTIVITY_CHOOSE_FILE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        permission_check();

        upload_button = findViewById(R.id.upload_button);
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this, "clicked!!", Toast.LENGTH_SHORT).show();
                //onBrowse(v);
                connectServer(v);
                //selectImage(v);

                // file_check();
            }
        });

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean msg = netInfo != null && netInfo.isConnectedOrConnecting();
        Log.e("findme","internet: "+String.valueOf(msg));
    }

    public void selectImage(View v) {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 0);
    }

    void connectServer(View v){

        OkHttpClient client = new OkHttpClient();
        String baseUrl="http://192.168.0.102:8080/upload/";

        try
        {
            String file_path = "/sdcard/Download/hello.jpg";

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
                    Log.e("findme","failed to post!" + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //Log.e("findme",response.body().toString());
                    Log.e(tag,String.valueOf(response.code()));
                }
            });

        }
        catch(Exception e)
        {
            System.out.println("ee");
        }

    }

    public void onBrowse(View view) {

        Intent chooseFile, intent;

        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");

        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        String path = "";

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == ACTIVITY_CHOOSE_FILE) {
            Uri uri = data.getData();
            Log.e(tag, "UriPath  = " + uri);
            Log.e(tag, "UriPath  = " + uri.getPath());

            String FilePath = getPathVideo(uri); // should the path be here in this string
            Log.e(tag, "FilePath  = " + FilePath);
        }
    }
    private String getPathVideo(Uri contentUri) {
        String[] proj = { MediaStore.Video.Media.DATA };
//        Cursor cursor = managedQuery(contentUri, projection, null, null, null);
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    public String getPathImg(Uri contentUri) {
        String [] proj      = {MediaStore.Images.Media.DATA};
        Cursor cursor       = getContentResolver().query( contentUri, proj, null, null,null);
        if (cursor == null) return null;
        int column_index    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void file_check(){
        // File file = new File("/sdcard/Download/hello.txt");
        File file = new File("/sdcard/Download/hello.jpg");

        Log.e(tag,"existence: "+String.valueOf(file.exists()));
        Log.e(tag,"read: "+String.valueOf(file.canRead()));
        Log.e(tag,"write: "+String.valueOf(file.canWrite()));
    }
    private void permission_check(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
        }else{
            get_permission();
        }
    }
    private void get_permission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale( this,Manifest.permission.WRITE_EXTERNAL_STORAGE )){
            new AlertDialog.Builder(this).setTitle("paisi");
        }else{
            ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }
}
