package com.example.pixlfreak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
//import com.example.pixlfreak.databinding.ActivityShareBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.SocketHandler;

public class ShareActivity extends AppCompatActivity {

         ImageView resultimage;
         ImageView backBtn;
         ImageView moreBtn;
         ImageView whatsappBtn;
         ImageView facebookBtn;
         ImageView instagramBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_share);


        resultimage=findViewById(R.id.resultimage);
        backBtn=findViewById(R.id.backBtn);
        moreBtn=findViewById(R.id.moreBtn);
        whatsappBtn=findViewById(R.id.whatsappBtn);
        facebookBtn=findViewById(R.id.facebookBtn);
        instagramBtn=findViewById(R.id.instagramBtn);


        resultimage.setImageURI(getIntent().getData());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri filePath = getIntent().getData();
                Intent dsPhotoEditorIntent = new Intent(ShareActivity.this, DsPhotoEditorActivity.class);

                dsPhotoEditorIntent.setData(filePath);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PixlFreak");

                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, 200);
                //startActivity(new Intent(ShareActivity.this, DsPhotoEditorActivity.class));
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                BitmapDrawable drawable=(BitmapDrawable)resultimage.getDrawable();
                if(drawable!=null){
                        Bitmap bitmap=Bitmap.createBitmap(drawable.getBitmap());
                if(bitmap!=null) {

                    File f = new File(getExternalCacheDir() + "/" + getResources().getString(R.string.app_name) + ".png");
                    Intent shareint;

                    try {
                        FileOutputStream outputStream = new FileOutputStream(f);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        outputStream.flush();
                        outputStream.close();
                        shareint = new Intent(Intent.ACTION_SEND);
                        shareint.setType("image/*");
                        shareint.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
                        shareint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    } catch (Exception e) {

                        throw new RuntimeException(e);

                    }
                    startActivity(Intent.createChooser(shareint, "share image"));
                }}
                else{
                    Toast.makeText(ShareActivity.this,"image not picked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //share to whatsapp
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri imgUri = Uri.parse("android.resource://"+getPackageName());
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShareActivity.this,"Please install Whatsapp!",Toast.LENGTH_SHORT).show();

                }


            }
        });


        facebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imgUri = Uri.parse("android.resource://"+getPackageName());
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.twitter");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShareActivity.this,"Please install Facebook!",Toast.LENGTH_SHORT).show();

                }

            }
        });



        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imgUri = Uri.parse("android.resource://"+getPackageName());
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.instagram");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
                whatsappIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
                whatsappIntent.setType("image/jpeg");
                whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ShareActivity.this,"Please install Instagram!",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }
}