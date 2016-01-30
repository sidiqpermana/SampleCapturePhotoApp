package com.sidiq.samplecapturephotoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.img_photo)
    ImageView imgPhoto;
    @Bind(R.id.fab_camera)
    FloatingActionButton fabCamera;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static Uri mLocationForPhotos = null;

    private String fileName = "temp.jpg";
    private String KEY_PHOTO = "photo";
    private String temp = Environment.getExternalStorageDirectory() + "/" + fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.fab_camera)
    public void capturePhoto(){
        Uri tempImgUri = createCameraTempFile();
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempImgUri);
        startActivityForResult(cameraIntent,
                REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setUpImage(temp);
        }
    }

    private void setUpImage(String path){
        Glide.with(MainActivity.this)
                .load(new File(path))
                .into(imgPhoto);
    }

    public Uri createCameraTempFile() {

        try {
            File tempFile = new File(temp);

            tempFile.createNewFile();

            return Uri.fromFile(tempFile);
        } catch (Exception e) {
            Log.v("Jet Driver", "Can't create file to take picture!");
            return Uri.EMPTY;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_PHOTO, temp);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        setUpImage(savedInstanceState.getString(KEY_PHOTO));
        super.onRestoreInstanceState(savedInstanceState);
    }
}
