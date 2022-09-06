package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private Button bTakePhoto;
    private ImageView picture;
    private Uri imageUri;
    private TextView text;
    private EditText photoName;
    private Button bUpload;
    private File f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bTakePhoto = (Button) findViewById(R.id.btn_take_photo);
        picture = (ImageView) findViewById(R.id.iv_picture);
        text = (TextView) findViewById(R.id.tv_text);
        photoName = (EditText) findViewById(R.id.et_name);
        bUpload = (Button) findViewById(R.id.btn_upload);



        bTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photoTaken = new File(getExternalCacheDir(), "photo_taken.jpg");

                try{
                    if(photoTaken.exists()){
                        photoTaken.delete();
                    }
                    photoTaken.createNewFile();
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.myapplication",photoTaken);
                } else {
                    imageUri = Uri.fromFile(photoTaken);
                }
                //open camera
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                Log.i("aaa","iii");
                Log.i("url", String.valueOf(imageUri));
                startActivityForResult(intent, 1);
            }
        });

        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                f = new File(String.valueOf(imageUri));
                Toast.makeText(MainActivity.this,"开始上传"+f.getAbsolutePath(),Toast.LENGTH_LONG).show();
                try{
                    Upload.run(f);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == Activity.RESULT_OK){
                    try{
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        picture.setImageBitmap(bitmap);
                        text.setVisibility(View.VISIBLE);
                        photoName.setVisibility(View.VISIBLE);
                        bUpload.setVisibility(View.VISIBLE);
                    }catch (FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}