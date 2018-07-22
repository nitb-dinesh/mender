package com.app.handyman.mender.user.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.app.handyman.mender.common.rest.AppConfig;
import com.app.handyman.mender.common.utils.DataManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.app.handyman.mender.model.Request;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Activity to create a new request!
 */

public class AddDetailsToNewRequestActivity extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_FROM_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    String[] cameraPermissionArray = {"android.permission.CAMERA", READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    String[] storagePermissionArray = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private Button mCreateRequest, mCancel, add_camera, add_gallery;
    private Button mAddImageUsingCamera, mAddImageUsingGallery;
    private ImageView mImage, mImage2, mImage3;
    private ProgressWheel mProgressWheel;
    private TextView title, knowMoreAboutCharges, photodesc, twoOfTwo;
    private FirebaseAuth firebaseAuth;
    Request request;

    private String userEmail;

    String keyFinal = "";
    File file;
    byte[] imageData;
    Date d = new Date();
    String a = d.getTime() + "";
    private String jobId;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_request_details);

        mAddImageUsingCamera = (Button) findViewById(R.id.add_camera);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mAddImageUsingCamera.setTypeface(myCustomFont);

        mAddImageUsingGallery = (Button) findViewById(R.id.add_gallery);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mAddImageUsingGallery.setTypeface(myCustomFont1);

        mCreateRequest = (Button) findViewById(R.id.btnAddNewReq);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mCreateRequest.setTypeface(myCustomFont2);

        mCancel = (Button) findViewById(R.id.cancel);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mCancel.setTypeface(myCustomFont3);

        title = (TextView) findViewById(R.id.title);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        title.setTypeface(myCustomFont4);

        knowMoreAboutCharges = (TextView) findViewById(R.id.know_more_about_charges);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        knowMoreAboutCharges.setTypeface(myCustomFont5);

        twoOfTwo = (TextView) findViewById(R.id.two_of_two);
        twoOfTwo.setTypeface(myCustomFont5);

        photodesc = (TextView) findViewById(R.id.photodesc);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        photodesc.setTypeface(myCustomFont6);

        knowMoreAboutCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(AddDetailsToNewRequestActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_check_rates);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView mTitle = (TextView) dialog.findViewById(R.id.title);
                final TextView mDescription = (TextView) dialog.findViewById(R.id.description);
                final Button mOk = (Button) dialog.findViewById(R.id.ok);

                Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                mTitle.setTypeface(myCustomFont19);
                mDescription.setTypeface(myCustomFont19);
                mOk.setTypeface(myCustomFont19);

                mDescription.setText("Please make sure you understand our rates. You will be charged for any services provided from this point on. \n " +
                        "\n Labor Charges = 60$ / hour \n Driving Charges = 40$ / hour \n Material Costs = Variable cost. \n\n You can find our pricing information on our Rates page.");

                mOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                    }
                });

                dialog.setCancelable(true);
                dialog.show();

            }
        });


        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(R.id.image2);
        mImage3 = (ImageView) findViewById(R.id.image3);
        ((ImageView) findViewById(R.id.cross1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request.getImageFiles().size() == 1) {
                    //request.getImageFiles().add(file);
                    //list.remove(list.size() - 1)
                    request.getImageFiles().remove(request.getImageFiles().size() - 1);
                    mImage.setImageDrawable(getResources().getDrawable(R.drawable.miniaddimage));

                } else if (request.getImageFiles().size() == 2) {

                } else if (request.getImageFiles().size() == 3) {
                }
            }
        });
        ((ImageView) findViewById(R.id.cross2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request.getImageFiles().size() == 2) {
                    //request.getImageFiles().add(file);
                    //list.remove(list.size() - 1)
                    request.getImageFiles().remove(request.getImageFiles().size() - 1);
                    mImage2.setImageDrawable(getResources().getDrawable(R.drawable.miniaddimage));

                } else if (request.getImageFiles().size() == 1) {

                } else if (request.getImageFiles().size() == 3) {
                }
            }
        });
        ((ImageView) findViewById(R.id.cross3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request.getImageFiles().size() == 3) {
                    //request.getImageFiles().add(file);
                    //list.remove(list.size() - 1)
                    request.getImageFiles().remove(request.getImageFiles().size() - 1);
                    mImage3.setImageDrawable(getResources().getDrawable(R.drawable.miniaddimage));

                } else if (request.getImageFiles().size() == 2) {

                } else if (request.getImageFiles().size() == 1) {
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("request");
        jobId = intent.getExtras().getString("id");


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setTitle(R.string.create_request);
            getSupportActionBar().setTitle(R.string.create_request);
        }

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity here
            userEmail = firebaseAuth.getCurrentUser().getEmail();
        } else {

        }

        mCreateRequest = (Button) findViewById(R.id.btnAddNewReq);
        mCancel = (Button) findViewById(R.id.cancel);
        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(R.id.image2);
        mImage3 = (ImageView) findViewById(R.id.image3);



        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        mProgressWheel.setBarColor(Color.LTGRAY);
        mProgressWheel.stopSpinning();

        DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference requestsReference = mRootReference.child("Requests");
        final String key = requestsReference.push().getKey();
        keyFinal = key;

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(AddDetailsToNewRequestActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_confirm_logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
                final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

                Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                mLogoutTitle.setTypeface(myCustomFont19);
                mConfirmLogout.setTypeface(myCustomFont19);

                mLogoutTitle.setText("Are you sure you want to cancel the job?");
                mConfirmLogout.setText("Yes");

                mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        finish();

                    }
                });

                dialog.setCancelable(true);
                dialog.show();

            }
        });

        mAddImageUsingCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraIntent();

            }
        });

        mAddImageUsingGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                galleryIntent();

            }
        });


        mCreateRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (request.getImageFiles().size() < 1) {
                    Toast.makeText(AddDetailsToNewRequestActivity.this, "Please select at-least 1 image", Toast.LENGTH_SHORT).show();
                } else {

                    // Are you sure dialog!

                    final Dialog dialog = new Dialog(AddDetailsToNewRequestActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_confirm_logout);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
                    final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

                    Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                    mLogoutTitle.setTypeface(myCustomFont19);
                    mConfirmLogout.setTypeface(myCustomFont19);

                    mLogoutTitle.setText("Are you sure you want to create this request?");
                    mConfirmLogout.setText("Yes");

                    mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(jobId.isEmpty()) {

                                request.setKey(key);
                            }else{
                                request.setKey(jobId);
                            }
                            request.setUserEmail(userEmail);

                            request.setAccepted(false);
                            request.setAssignedTo("");
                            request.setStatus(false);
                            request.setMaterialCost("0");
                            request.setDriveTimeCost("0");
                            request.setLaborTimeCost("0");
                            request.setMaterialReceiptImage("");
                            request.setTotalCost("0");
                            request.setIsPaid(false);
                            request.setIsStarted(false);

                            Map<String, Object> child = new HashMap<String, Object>();
                            if(jobId.isEmpty()){
                                child.put(key, request.toFirebase());
                            }else{
                                child.put(jobId, request.toFirebase());
                            }

                            requestsReference.updateChildren(child, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError == null) {
                                        sendNotify();
                                        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                                        DatabaseReference gameRef = mRootRef.child("Handyman");
                                        final String key2 = gameRef.push().getKey();
                                        Query qRef = gameRef.orderByValue();
                                        qRef.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                AsyncHttpClient client = new AsyncHttpClient();
                                                RequestParams params = new RequestParams();
                                                System.out.println(dataSnapshot.getValue().toString().trim() + "  :::::  :::::");
                                                params.put("regId", dataSnapshot.getValue().toString().trim());
                                                params.put("title", "New Job Posted");
                                                params.put("apiKey", "AAAAFTYEOQw:APA91bHpTZCCRTLvn4aOrMaLoqj05A4kmA0M_2p2z-nHbjUDSGTCw_VhDP-1DTyP97CwuKucEikGGc92ChY0fdpXsARDCl-5PNAXIKnIB80uyW0ayG1k57i6SCjA3b8dtaluNHoz5qnP");
                                                params.put("body", "A new job has been posted. Please check it out.");
                                                params.put("id", key);

                                                client.post("http://54.70.113.238:7001/sendMessage/", params, new AsyncHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                        System.out.println("In Success " + responseBody.toString());
                                                        Log.i("PUSH", "onSuccess: " + statusCode);

                                                        Intent intent = new Intent(AddDetailsToNewRequestActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }

                                                    @Override
                                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                        Log.i("PUSH", "onFailure: " + statusCode);

                                                        System.out.println("Error Posting Job, Please Try Again!");
                                                        Intent intent = new Intent(AddDetailsToNewRequestActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }


                                                });

                                            }

                                            @Override
                                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                                            }

                                            @Override
                                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        Toast.makeText(AddDetailsToNewRequestActivity.this, "Request has been added", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(AddDetailsToNewRequestActivity.this, "Error creating request", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();
                                    }
                                }

                            });
                        }
                    });
                    dialog.setCancelable(true);
                    dialog.show();
                }
            }
        });

    }

    private void cameraIntent() {

        // askForPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean cameraPermissiongiven = checkCameraPermission();

            if (cameraPermissiongiven) {
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
        a = d.getTime() + "";
        file = new File(Environment.getExternalStorageDirectory() + File.separator + a);
        // put Uri as extra in intent object
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        Uri photoURI = FileProvider.getUriForFile(AddDetailsToNewRequestActivity.this, getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(intent, REQUEST_CAPTURE_FROM_CAMERA);

    }

    private void galleryIntent() {

//        Intent intent = new Intent(
//                Intent.ACTION_PICK,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, ACTIVITY_SELECT_IMAGE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean storagePermissionGiven = checkStoragePermission();

            if (storagePermissionGiven) {
                selectFile();
            } else {
                requestStoragePermission();
            }

        } else {

            selectFile();

        }


    }

    private void selectFile() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_file)), SELECT_FILE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {

            //   Toast.makeText(getActivity(), "Called RESULT_ON_ACTIVITY", Toast.LENGTH_SHORT).show();

            ProgressBar progressBar2 = new ProgressBar(AddDetailsToNewRequestActivity.this, null, android.R.attr.progressBarStyleSmall);
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
                request.getImageFiles().add(file);
                if (request.getImageFiles().size() >= 3) {
                    mAddImageUsingCamera.setEnabled(false);
                    mAddImageUsingGallery.setEnabled(false);
                }
                if (request.getImageFiles().size() == 1) {
                    mImage.setImageBitmap(imageBitmap);
                } else if (request.getImageFiles().size() == 2) {
                    mImage2.setImageBitmap(imageBitmap);
                } else if (request.getImageFiles().size() == 3) {
                    mImage3.setImageBitmap(imageBitmap);
                }
                uploadImage(Uri.fromFile(new File(file.getAbsolutePath())));


            } else if (requestCode == SELECT_FILE) {


                Uri uri = data.getData();

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    // Log.d(TAG, String.valueOf(bitmap));

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    imageData = stream.toByteArray();
                    request.getImageFiles().add(new File(uri.getPath()));
                    if (request.getImageFiles().size() >= 3) {
                        mAddImageUsingCamera.setEnabled(false);
                        mAddImageUsingGallery.setEnabled(false);
                    }
                    if (request.getImageFiles().size() == 1) {
                        mImage.setImageBitmap(mBitmap);
                    } else if (request.getImageFiles().size() == 2) {
                        mImage2.setImageBitmap(mBitmap);
                    } else if (request.getImageFiles().size() == 3) {
                        mImage3.setImageBitmap(mBitmap);
                    }
                    uploadImage(uri);


                } catch (IOException e) {
                    e.printStackTrace();
                }

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

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), cameraPermissionArray[0]);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(AddDetailsToNewRequestActivity.this, cameraPermissionArray, CAMERA_PERMISSION_REQUEST_CODE);

    }

    private boolean checkStoragePermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), storagePermissionArray[0]);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), storagePermissionArray[1]);

        return (result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(AddDetailsToNewRequestActivity.this, storagePermissionArray, STORAGE_PERMISSION_REQUEST_CODE);

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
                                AddDetailsToNewRequestActivity.this.startActivity(intent);

                            }
                        }

                    }
                }
                break;

            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    // boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storagePermissionAccepted = checkStoragePermission();

                    if (storagePermissionAccepted) {
                        selectFile();
                        Toast.makeText(this, R.string.storage_permission_granted, Toast.LENGTH_SHORT).show();
                    } else {

                        // http://stackoverflow.com/questions/33162152/storage-permission-error-in-marshmallow

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showMessageOKCancel(getString(R.string.allow_storage_permission),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(storagePermissionArray,
                                                            STORAGE_PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else {
                                Toast.makeText(this, R.string.allow_storage_permission, Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                AddDetailsToNewRequestActivity.this.startActivity(intent);

                            }
                        }

                    }
                }
                break;

        }
    }

    private void uploadImage(Uri f) {

        mProgressWheel.spin();
        StorageReference imageRef;
        if(jobId.isEmpty()){
             imageRef = storageReference.child("images").child(keyFinal);
        }else{
             imageRef = storageReference.child("images").child(jobId);
        }


        int i = request.getImageFiles().size();
        StorageReference imageReference = imageRef.child(i + "");
//            Uri f1 = Uri.fromFile(new File(f.getAbsolutePath()));
        UploadTask uploadTask = imageReference.putFile(f);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("upload", " Im age Not Uploaded " + exception.getMessage());
                mProgressWheel.stopSpinning();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i("upload", "Uploaded Image");
                mProgressWheel.stopSpinning();
            }
        });


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(AddDetailsToNewRequestActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    public void sendNotify() {
        Call<ResponseBody> call = AppConfig.getLoadInterface().sendNotify();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
   /*                 try {
                        String responseData = response.body().string();
                        Log.e("Response vehicel type", responseData);
                        JSONObject object = new JSONObject(responseData);
                        Log.d("vehicellist6665", responseData);

                        if (object.getString("status").equals("200")) {
                            // Toast.makeText(BaseActivity.this, "success_city", Toast.LENGTH_LONG).show();
                            //mGetCityModel = new Gson().fromJson(responseData, GetCityModel.class);
                           // DataManager.getInstance().setGetCityModel(mGetCityModel);

                        }
                        if (object.getString("status").equals("500")) {
                            //  Toast.makeText(BaseActivity.this, "fail_city", Toast.LENGTH_LONG).show();

                        } else {


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    DataManager.getInstance().hideProgressMessage();*/
                    Toast.makeText(AddDetailsToNewRequestActivity.this, "Notified to Handyman", Toast.LENGTH_SHORT).show();
                } else {
                    //AppConfig.showToast("something went wrong");
                    DataManager.getInstance().hideProgressMessage();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
