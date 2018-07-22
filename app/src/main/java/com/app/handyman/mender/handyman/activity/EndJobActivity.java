package com.app.handyman.mender.handyman.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EndJobActivity extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_FROM_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    String[] cameraPermissionArray = {"android.permission.CAMERA", READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    String[] storagePermissionArray = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};


    private ImageView mDoneImage1;
    private StorageReference storageReference10;
    private Uri filePath;

    private Button btnback;
    private Button btendjob;
    private EditText etwhat;
    private TextView tvtitle;
    private TextView tvrate;
    private TextView TextView11;

    String mCurrentPhotoPath = "";

    String id = "";

    File file;
    byte[] imageData;
    Date d = new Date();
    String a =  d.getTime() + "";

    ProgressDialog mProgress;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_job);

        storageReference10 = FirebaseStorage.getInstance().getReference();

        // get id from intent.
        try {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");

        } catch (Exception e) {
            e.printStackTrace();
        }

        mProgress = new ProgressDialog(EndJobActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);


        mDoneImage1 = (ImageView) findViewById(R.id.done_image_1);

        mDoneImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Take Image and save it to Firebase database with reference of 4!

                cameraIntent();

            }
        });


        etwhat = (EditText) findViewById(R.id.etwhat);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        etwhat.setTypeface(myCustomFont2);

        tvrate = (TextView) findViewById(R.id.tvrate);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvrate.setTypeface(myCustomFont4);

        tvtitle = (TextView) findViewById(R.id.tvtitle);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvtitle.setTypeface(myCustomFont3);

        btendjob = (Button) findViewById(R.id.btendjob);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btendjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EndJobActivity.this, "Successfully Job Ended", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EndJobActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btendjob.setTypeface(myCustomFont);

        btnback = (Button) findViewById(R.id.btnback);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        // btnback.setOnClickListener(this);
        btnback.setTypeface(myCustomFont1);

        TextView11 = (TextView) findViewById(R.id.TextView11);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        TextView11.setTypeface(myCustomFont5);

    }


    private void cameraIntent() {

        // askForPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean cameraPermissiongiven = checkCameraPermission();

            if(cameraPermissiongiven) {
                takePicture();
            } else {
                requestCameraPermission();
            }

        } else {

            takePicture();

        }

    }

    private void takePicture() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        d = new Date();
        a =  d.getTime() + "";
        file = new File(Environment.getExternalStorageDirectory() + File.separator + a);
        // put Uri as extra in intent object
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        Uri photoURI = FileProvider.getUriForFile(EndJobActivity.this, getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(intent, REQUEST_CAPTURE_FROM_CAMERA);

    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), cameraPermissionArray[0]);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(EndJobActivity.this, cameraPermissionArray, CAMERA_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    // boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = checkCameraPermission();

                    if (cameraAccepted) {
                        takePicture();
                        Toast.makeText(this, R.string.camera_permission_granted, Toast.LENGTH_SHORT).show();
                    } else {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel(getString(R.string.allow_camera_permission),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(cameraPermissionArray,
                                                            CAMERA_PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else {
                                Toast.makeText(this, R.string.allow_camera_permission, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                EndJobActivity.this.startActivity(intent);

                            }
                        }

                    }
                }
                break;
        }
    }

    private void uploadImage(Uri f){

        mProgress.show();
        StorageReference imageRef = storageReference.child("images").child(id);

        int i = 5; // First 3 images are by the customer. 5th is Done Image!
        StorageReference imageReference = imageRef.child(i+"");
//            Uri f1 = Uri.fromFile(new File(f.getAbsolutePath()));
        UploadTask uploadTask = imageReference.putFile(f);


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("upload", " Im age Not Uploaded " + exception.getMessage());
                mProgress.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i("upload", "Uploaded Image");
                mProgress.dismiss();
            }
        });




    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(EndJobActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //   Toast.makeText(getActivity(), "Called RESULT_ON_ACTIVITY", Toast.LENGTH_SHORT).show();

            ProgressBar progressBar2 = new ProgressBar(EndJobActivity.this, null, android.R.attr.progressBarStyleSmall);
            progressBar2.setVisibility(View.VISIBLE);

            Bitmap mBitmap;
            if (requestCode == REQUEST_CAPTURE_FROM_CAMERA) {

                progressBar2.setVisibility(View.GONE);

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + a);
                Bitmap imageBitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);
                // mProfilePic.setImageBitmap(imageBitmap);

                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(file.getAbsolutePath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageBitmap = rotateImage(imageBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageBitmap = rotateImage(imageBitmap, 180);
                            break;
                        // etc.
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //   Toast.makeText(getActivity(), "Called RESULT_ON_ACTIVITY", Toast.LENGTH_SHORT).show();


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);

                imageData = stream.toByteArray();
                mDoneImage1.setImageBitmap(imageBitmap);

                uploadImage(Uri.fromFile(new File(file.getAbsolutePath())));

            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

}