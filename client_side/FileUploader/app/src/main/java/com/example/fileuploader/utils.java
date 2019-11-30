package com.example.fileuploader;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;

public class utils {

    private static String tag = MainActivity.tag;

    private void onBrowse(View view) {

        Intent chooseFile, intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
       // startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }
    private void internet_check(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean msg = netInfo != null && netInfo.isConnectedOrConnecting();
        Log.e("findme","internet: "+String.valueOf(msg));
    }
    private void file_check(){
        // File file = new File("/sdcard/Download/hello.txt");
        File file = new File("/sdcard/Download/hello.jpg");

        Log.e(tag,"existence: "+String.valueOf(file.exists()));
        Log.e(tag,"read: "+String.valueOf(file.canRead()));
        Log.e(tag,"write: "+String.valueOf(file.canWrite()));
    }
//    private void get_permission(Context context){
//        if(ActivityCompat.shouldShowRequestPermissionRationale( context,Manifest.permission.WRITE_EXTERNAL_STORAGE )){
//            new AlertDialog.Builder(context).setTitle("paisi");
//        }else{
//            ActivityCompat.requestPermissions( MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
//        }
//    }
    private void permission_check(Context context){

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show();
        }else{
            //get_permission(context);
        }
    }



}
