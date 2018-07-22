package com.app.handyman.mender.handyman.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.widget.Toast.makeText;



public class EnterReceiptsActivity extends AppCompatActivity implements View.OnClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PICK_IMAGE_REQUEST = 123;
    private static final int REQUEST_TAKE_PHOTO = 1;
    private Button bteditreceipt;
    private Button btupload1;
    private Button btChoosefromgallery;
    private Button btpicture;
    private ImageView receiptpreview;
    private TextView tv15;
    private Uri filePath;
    private StorageReference storageReference1;
    String mCurrentPhotoPath;


    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_receipts);

        try {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        storageReference1 = FirebaseStorage.getInstance().getReference();

        tv15 = (TextView) findViewById(R.id.tv15);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tv15.setTypeface(myCustomFont4);


        bteditreceipt = (Button) findViewById(R.id.bteditreceipt);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");
        bteditreceipt.setOnClickListener(this);
        bteditreceipt.setTypeface(myCustomFont);

        btChoosefromgallery = (Button) findViewById(R.id.btChoosefromgallery);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");
        btChoosefromgallery.setOnClickListener(this);
        btChoosefromgallery.setTypeface(myCustomFont1);


        btpicture = (Button) findViewById(R.id.btpicture);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");
        btpicture.setOnClickListener(this);
        btpicture.setTypeface(myCustomFont2);

        btupload1 = (Button) findViewById(R.id.btupload1);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");
        btupload1.setOnClickListener(this);
        btupload1.setTypeface(myCustomFont3);



        receiptpreview = (ImageView) findViewById(R.id.receiptpreview);

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an Image"), PICK_IMAGE_REQUEST);
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


                }
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            receiptpreview.setImageBitmap(imageBitmap);
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                receiptpreview.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
        }
    }
    private void handleSmallCameraPhoto(Intent intent) {
        Bundle extras = intent.getExtras();
        Bitmap mImageBitmap = (Bitmap) extras.get("data");
        receiptpreview.setImageBitmap(mImageBitmap);
        receiptpreview.setVisibility(View.VISIBLE);
    }
    private void uploadFile() {

        if (filePath != null) {

            final ProgressDialog progressdialog = new ProgressDialog(this);
            progressdialog.setTitle("Uploading...");
            progressdialog.show();
            Toast.makeText(this, "Upload Complete", Toast.LENGTH_SHORT).show();

            StorageReference riversRef = storageReference1.child("images/Bill.jpg");

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressdialog.dismiss();
                            makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_LONG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressdialog.dismiss();
                            makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG);

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot){

                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v == bteditreceipt){
            startActivity(new Intent(this, ReceiptActivity.class));
        }if (v == btChoosefromgallery) {
            //open file chooser
            showFileChooser();
            uploadFile();
        }if (v == btupload1){
            uploadFile();
            // Check permission for CAMERA
        }if (v == btpicture){
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivity(intent);
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        15);
                uploadFile();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
                // Now user should be able to use camera
            }
            else {
                Toast.makeText(this, "Cannot invoke Camera Permissions",Toast.LENGTH_LONG).show();
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }

}
