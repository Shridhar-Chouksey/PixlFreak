package com.example.pixlfreak;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity;
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants;
import com.google.android.material.snackbar.Snackbar;
//import com.example.pixlfreak.databinding.ActivityMainBinding;

import java.io.ByteArrayOutputStream;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class MainActivity extends AppCompatActivity implements InAppUpdateManager.InAppUpdateHandler {

    private int STORAGE_PERMISSION_CODE=1;
    int IMAGE_REQUEST_CODE=45;
    int CAMERA_REQUEST_CODE=5;
    int SHARE_CODE=200;
    ImageView editBtn;
    ImageView cameraBtn;
    InAppUpdateManager inAppUpdateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

                  setContentView(R.layout.activity_main);

                  editBtn=findViewById(R.id.editBtn);
                  cameraBtn=findViewById(R.id.cameraBtn);



        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ==PackageManager.PERMISSION_GRANTED)
                {

                    Intent intent = new Intent();
                    intent.setAction(intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }else{
                    requestStoragePermission();
                }


//                Intent intent = new Intent();
//                intent.setAction(intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//
//                startActivityForResult(intent, IMAGE_REQUEST_CODE);

                //    "*/*" -all types of files
                //    "video/*" -for videos ,similarly for image
            }
        });

          cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 10);

                } else {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);


                }
            }
        });

          //Update method code
          inAppUpdateManager=InAppUpdateManager.Builder(this,101)
                  .resumeUpdates(true)
                  .mode(Constants.UpdateMode.FLEXIBLE)
                  .snackBarAction("An update has just been downloaded.")
                  .snackBarAction("Restart")
                  .handler(this);

          inAppUpdateManager.checkForAppUpdate();

    }

      private void requestStoragePermission() {
          if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

              new AlertDialog.Builder(this)
                      .setTitle("Permission needed")
                      .setMessage("Allow Permissions to access photos on your device?")
                      .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                              Intent intent = new Intent();
                              intent.setAction(intent.ACTION_GET_CONTENT);
                              intent.setType("image/*");

                              startActivityForResult(intent, IMAGE_REQUEST_CODE);
                          }
                      })
                      .setNegativeButton("DENY", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                              dialog.dismiss();
                          }
                      })
                      .create().show();


          } else {
              ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

          }
      }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE){

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
             Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();

            }

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_CODE) {

            if (data.getData() != null) {

                Uri filePath = data.getData();
                Intent dsPhotoEditorIntent = new Intent(this, DsPhotoEditorActivity.class);

                dsPhotoEditorIntent.setData(filePath);
                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PixlFreak");

                int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};

                dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);

                startActivityForResult(dsPhotoEditorIntent, SHARE_CODE);

            }
        }

        if (requestCode == SHARE_CODE) {

            Intent intent = new Intent(MainActivity.this, ShareActivity.class);
            intent.setData(data.getData());
            startActivity(intent);
        }
        
        if(requestCode==CAMERA_REQUEST_CODE){

            Bitmap photo = (Bitmap) data.getExtras().get("data");
                Uri uri = getImageUri(photo);
            Intent dsPhotoEditorIntent = new Intent(MainActivity.this, DsPhotoEditorActivity.class);
            dsPhotoEditorIntent.setData(uri);
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "PixlFreak");
            int[] toolsToHide = {DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_CROP};
            dsPhotoEditorIntent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, toolsToHide);
            startActivityForResult(dsPhotoEditorIntent, SHARE_CODE);
        }

    }

    public Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onInAppUpdateError(int code, Throwable error) {

    }

    @Override
    public void onInAppUpdateStatus(InAppUpdateStatus status) {

        if(status.isDownloaded())
        {
          View view=getWindow().getDecorView().findViewById(android.R.id.content);
            Snackbar snackbar=Snackbar.make(view,
                    "An update has just been downloaded.",
                    Snackbar.LENGTH_INDEFINITE);

            snackbar.setAction("", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     inAppUpdateManager.completeUpdate();

                }
            });

            snackbar.show();

        }

    }
}