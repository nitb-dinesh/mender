package com.app.handyman.mender.handyman.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import com.app.handyman.mender.model.Request;
import ng.max.slideview.SlideView;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;


/**
 * App for keeping count of the time the user takes for driving, labor and also for inputting material costs.
 */

public class TimerTestNewActivity extends AppCompatActivity {

    private static final int REQUEST_CAPTURE_FROM_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 101;

    String[] cameraPermissionArray = {"android.permission.CAMERA", READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};
    String[] storagePermissionArray = {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};

    private EditText mMaterialCost;
    private Button mSaveMaterialCost;

    private TextView mTitle, mTotalCost;
    private LinearLayout mDriveTimeLayout, mLaborTimeLayout, mReceiptsLayout;
    private Button mDriveTimeButton, mLaborTimeButton, mReceiptsButton, mJobInfo;

    private Chronometer mDriveChronometer, mLaborChronometer;
    private TextView mDriveTotal, mLaborTotal;
    private Button upload_receipt;
    private Button dtPlay, dtPause, dtReset;
    private Button ltPlay, ltPause, ltReset;

    double driveTimeCharges = 0.0, laborTimeCharges = 0.0, totalCharges = 0.0, materialCharges = 0.0;

    String id = "";

    File file;
    byte[] imageData;
    Date d = new Date();
    String a = d.getTime() + "";

    long dt_timeWhenStopped = 0;
    long lt_timeWhenStopped = 0;
//
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    int step = 0;
    boolean isTimerOn = false;

    Request request;

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_test_new);

        try {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            getRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Drive Time Costs
        mDriveChronometer = (Chronometer) findViewById(R.id.drive_chronometer);
        // mDriveChronometer.setFormat("MM:SS");
        mDriveChronometer.setText("00:00");
        mDriveTotal = (TextView) findViewById(R.id.drive_total);

        dtPlay = (Button) findViewById(R.id.dt_play);
        dtPause = (Button) findViewById(R.id.dt_pause);
        dtReset = (Button) findViewById(R.id.dt_reset);

        // Labor Costs
        mLaborChronometer = (Chronometer) findViewById(R.id.labor_chronometer);
        // mLaborChronometer.setFormat("MM:SS");
        mLaborChronometer.setText("00:00");

        mLaborTotal = (TextView) findViewById(R.id.labor_total);

        ltPlay = (Button) findViewById(R.id.lt_play);
        ltPause = (Button) findViewById(R.id.lt_pause);
        ltReset = (Button) findViewById(R.id.lt_reset);

        // Material Costs
        mSaveMaterialCost = (Button) findViewById(R.id.save_material_cost);
        mMaterialCost = (EditText) findViewById(R.id.material_cost);

        upload_receipt = (Button) findViewById(R.id.upload_receipt);

        mTitle = (TextView) findViewById(R.id.title);
        mTotalCost = (TextView) findViewById(R.id.total_cost);

        mDriveTimeLayout = (LinearLayout) findViewById(R.id.drive_time_layout);
        mLaborTimeLayout = (LinearLayout) findViewById(R.id.labor_time_layout);
        mReceiptsLayout = (LinearLayout) findViewById(R.id.receipts_layout);

        mDriveTimeButton = (Button) findViewById(R.id.drive_time_button);
        mLaborTimeButton = (Button) findViewById(R.id.labor_time_button);
        mReceiptsButton = (Button) findViewById(R.id.receipts_button);
        mJobInfo = (Button) findViewById(R.id.job_info_button);

        mDriveTimeButton.setEnabled(true);
        mDriveTimeButton.setClickable(false);

        mLaborTimeButton.setEnabled(false);
        mLaborTimeButton.setClickable(false);

        mReceiptsButton.setEnabled(false);
        mReceiptsButton.setClickable(false);

        mJobInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(TimerTestNewActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_job_info);

                TextView txtJobTitle, txtJobDescription, txtPhoneNumber, txtAddress;
                final ImageView mImage, mImage2, mImage3;

                txtJobTitle = (TextView) dialog.findViewById(R.id.txtJobTitle);
                txtJobTitle.setText(request.getJobTitle());

                txtJobDescription = (TextView) dialog.findViewById(R.id.txtJobDesc);
                txtJobDescription.setText(request.getJobDescription());

                txtPhoneNumber = (TextView) dialog.findViewById(R.id.txtPhoneNumber);
                txtPhoneNumber.setText(request.getPhoneNumber());

                txtAddress = (TextView) dialog.findViewById(R.id.txtAddress);
                txtAddress.setText(request.getPhoneNumber());

                mImage = (ImageView) dialog.findViewById(R.id.image);
                mImage2 = (ImageView) dialog.findViewById(R.id.image2);
                mImage3 = (ImageView) dialog.findViewById(R.id.image3);

                for (int i = 1; i < 4; i++) {
                    final int finalI = i;
                    try {

                        storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                try {

                                    System.out.println(uri);
                                    request.getImageFiles().add(new File(uri.getPath()));
                                    if (finalI == 1) {

//                                                Picasso.with(NewJobRequestActivity.this)
//                                                        .load(uri.toString())
//                                                        .resize(dpToPx(120), dpToPx(120)).centerCrop()
//                                                        .into(mImage);
//                                            mImage.setImageBitmap(mBitmap);

                                        mImage.setVisibility(View.VISIBLE);
                                        Glide.with(TimerTestNewActivity.this)
                                                .load(uri) // the uri you got from Firebase
                                                .centerCrop()
                                                .into(mImage); //Y

                                    } else if (finalI == 2) {

                                        mImage2.setVisibility(View.VISIBLE);
                                        Glide.with(TimerTestNewActivity.this)
                                                .load(uri) // the uri you got from Firebase
                                                .centerCrop()
                                                .into(mImage2); //Y

                                    } else if (finalI == 3) {

                                        mImage3.setVisibility(View.VISIBLE);
                                        Glide.with(TimerTestNewActivity.this)
                                                .load(uri) // the uri you got from Firebase
                                                .centerCrop()
                                                .into(mImage3); //Y

                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

//                                    ImageList.add(uri);
//                                    mSelectImagesRecyclerView.setAdapter(mGalleryAdapter);
//                                    mGalleryAdapter.notifyDataSetChanged();
//                                    mSelectImagesRecyclerView.scrollToPosition(0);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                dialog.setCancelable(true);
                dialog.show();


            }
        });

        upload_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == upload_receipt) {

//                    Intent intent = new Intent(TimerTest.this, EnterReceiptsActivity.class);
//                    intent.putExtra("id", id);
//                    startActivity(intent);
                    cameraIntent();

                }
            }
        });

        ((SlideView) findViewById(R.id.end_job_button)).setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                // Check which step it is and based on it show appropriate lienar layout.
                // First Step Enabled by Default

                if (step == 0) {
                    // Disable Drive Time Linear Layout and Make it invisible + set alpha of drive time button to .5f
                    // and make Labor Time Buttons alpha to 1f

                    if (isTimerOn) {
                        Toast.makeText(TimerTestNewActivity.this, "Pause the time first", Toast.LENGTH_SHORT).show();
                    } else {

                        mDriveTimeLayout.setVisibility(View.GONE);
                        mLaborTimeLayout.setVisibility(View.VISIBLE);

                        mDriveTimeButton.setAlpha(.5f);
                        mLaborTimeButton.setAlpha(1f);


                        // Update Total Cost + Update Title
                        mTitle.setText("Labor Time");
                        totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;
                        mTotalCost.setText("$" + totalCharges);

                    }

                } else if (step == 1) {

                    if (isTimerOn) {
                        Toast.makeText(TimerTestNewActivity.this, "Pause the time first", Toast.LENGTH_SHORT).show();
                    } else {

                        mLaborTimeLayout.setVisibility(View.GONE);
                        mReceiptsLayout.setVisibility(View.VISIBLE);

                        mLaborTimeButton.setAlpha(.5f);
                        mReceiptsButton.setAlpha(1f);


                        // Update Total Cost + Update Title
                        mTitle.setText("Add ReceiptActivity Charges");
                        totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;
                        mTotalCost.setText("$" + totalCharges);

                    }


                } else if (step == 2) {

                    totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                    // Update Backend with task done and the respective charges! Set status as done.

                    request.setStatus(true);
                    request.setTotalCost("" + totalCharges);

//                    Map<String, Object> child = new HashMap<String, Object>();
//                    child.put(id, request.toFirebase());

                    HashMap<String, Object> map = request.toFirebase();

                    requestsReference.child(id).updateChildren(map);

                    Intent intent = new Intent(TimerTestNewActivity.this, HomeActivity.class);
                    startActivity(intent);

                } else if (step == 3) {

                    Toast.makeText(TimerTestNewActivity.this, "Check Programming logic!", Toast.LENGTH_SHORT).show();

                }

                // Increment Step
                step++;

//                totalCharges = laborTimeCharges + driveTimeCharges + materialCharges;
//
//                Toast.makeText(TimerTestNewActivity.this, "Ending Job! Total Charges " + totalCharges + " (Payment Gateway Pending) ", Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(TimerTestNewActivity.this, EndJobActivity.class);
//                intent.putExtra("id", id);
//                startActivity(intent);
            }
        });


        dtPlay.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                mDriveChronometer.setBase(SystemClock.elapsedRealtime());
//                mDriveChronometer.start();

                mDriveChronometer.setBase(SystemClock.elapsedRealtime() + dt_timeWhenStopped);
                mDriveChronometer.start();

                isTimerOn = true;

            }
        });

        dtPause.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

//                long timeElapsed = SystemClock.elapsedRealtime() - mDriveChronometer.getBase();
//
//                int hours = (int) (timeElapsed / 3600000);
//                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
//
//                driveTimeCharges = (double) minutes * 0.67;
//
//                String time = mDriveChronometer.getText().toString();
//                Log.i("time", "DT TIME :" + mDriveChronometer.getText().toString());
//                mDriveTotal.setText("Drive Time Total :  " + minutes + " minutes "  + " Charges : " + driveTimeCharges);
//                mDriveChronometer.stop();

                dt_timeWhenStopped = mDriveChronometer.getBase() - SystemClock.elapsedRealtime();

                //long timeElapsed = SystemClock.elapsedRealtime() - mDriveChronometer.getBase();


                long timeElapsed = dt_timeWhenStopped;
                int hours = (int) (timeElapsed / 3600000);
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;

                if (minutes < 0) {
                    minutes = minutes * -1;
                }

                driveTimeCharges = (double) minutes * 0.67;

                String time = mDriveChronometer.getText().toString();
                Log.i("time", "DT TIME :" + mDriveChronometer.getText().toString());
                mDriveTotal.setText("$" + driveTimeCharges);

                isTimerOn = false;

                mDriveChronometer.stop();

            }
        });

        dtReset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TimerTestNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TimerTestNewActivity.this);
                }
                builder.setTitle("Reset Timer")
                        .setMessage("Are you sure you want to reset the timer?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mDriveChronometer.setBase(SystemClock.elapsedRealtime());
                                dt_timeWhenStopped = 0;
                                driveTimeCharges = 0.0;
                                mDriveTotal.setText("$" + driveTimeCharges);
                                totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;
                                isTimerOn = false;
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

            }
        });

        ltPlay.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mLaborChronometer.setBase(SystemClock.elapsedRealtime() + lt_timeWhenStopped);
                mLaborChronometer.start();

            }
        });

        ltPause.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
//                long timeElapsed = SystemClock.elapsedRealtime() - mLaborChronometer.getBase();
//
//                int hours = (int) (timeElapsed / 3600000);
//                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
//
//                if (minutes <= 60) {
//                    laborTimeCharges = 75.0;
//                } else {
//                    double overMinutes = minutes - 60;
//                    laborTimeCharges = 75.0 + (1.25 * overMinutes);
//                }
//
//                String time = mLaborChronometer.getText().toString();
//                Log.i("time", "LT TIME :" + mDriveChronometer.getText().toString());
//                mLaborTotal.setText("Labor Work Total : " + minutes + " Minutes. Charges : " + laborTimeCharges) ;
//                mLaborChronometer.stop();

                lt_timeWhenStopped = mLaborChronometer.getBase() - SystemClock.elapsedRealtime();

                //long timeElapsed = SystemClock.elapsedRealtime() - mLaborChronometer.getBase();

                long timeElapsed = lt_timeWhenStopped;

                int hours = (int) (timeElapsed / 3600000);
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;

                if (minutes <= 60) {
                    laborTimeCharges = 75.0;
                } else {
                    double overMinutes = minutes - 60;
                    laborTimeCharges = 75.0 + (1.25 * overMinutes);
                }

                String time = mLaborChronometer.getText().toString();
                Log.i("time", "LT TIME :" + time);
                mLaborTotal.setText("$" + laborTimeCharges);


                mLaborChronometer.stop();


            }
        });

        ltReset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(TimerTestNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(TimerTestNewActivity.this);
                }
                builder.setTitle("Reset Timer")
                        .setMessage("Are you sure you want to reset the timer?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                mLaborChronometer.setBase(SystemClock.elapsedRealtime());
                                lt_timeWhenStopped = 0;

                                laborTimeCharges = 0.0;
                                mDriveTotal.setText("$" + driveTimeCharges);
                                totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;
                                isTimerOn = false;

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();

            }
        });

        mSaveMaterialCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String materialCost = mMaterialCost.getText().toString();

                if (materialCost.isEmpty()) {

                    Toast.makeText(TimerTestNewActivity.this, "No Cost Added!", Toast.LENGTH_SHORT).show();

                } else {

                    materialCharges = Double.valueOf(materialCost);
                    mSaveMaterialCost.setText("Saved!");
                    Toast.makeText(TimerTestNewActivity.this, "Saved!", Toast.LENGTH_SHORT).show();

                    totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;
                    mTotalCost.setText("$" + totalCharges);

                }

            }
        });


    }

    private void getRequest() {

        Query jobReference = requestsReference.orderByChild("key").equalTo(id);

        jobReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    request = data.getValue(Request.class);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(getActivity(), "Cancelled Request", Toast.LENGTH_SHORT).show();
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

        Uri photoURI = FileProvider.getUriForFile(TimerTestNewActivity.this, getApplicationContext().getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        startActivityForResult(intent, REQUEST_CAPTURE_FROM_CAMERA);

    }

    private boolean checkCameraPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), cameraPermissionArray[0]);

        return result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(TimerTestNewActivity.this, cameraPermissionArray, CAMERA_PERMISSION_REQUEST_CODE);

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
                                TimerTestNewActivity.this.startActivity(intent);

                            }
                        }

                    }
                }
                break;
        }
    }

    private void uploadImage(Uri f) {

        final ProgressWheel wheel = new ProgressWheel(TimerTestNewActivity.this);
        wheel.setBarColor(Color.BLUE);
        wheel.spin();

        StorageReference imageRef = storageReference.child("images").child(id);

        int i = 4; // First 3 images are by the customer. 4th is Receipts Image!
        StorageReference imageReference = imageRef.child(i + "");
//            Uri f1 = Uri.fromFile(new File(f.getAbsolutePath()));
        UploadTask uploadTask = imageReference.putFile(f);


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("upload", " Im age Not Uploaded " + exception.getMessage());
                wheel.stopSpinning();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Log.i("upload", "Uploaded Image");
                wheel.stopSpinning();
            }
        });

    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(TimerTestNewActivity.this)
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

            if (requestCode == REQUEST_CAPTURE_FROM_CAMERA) {

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + a);
                Bitmap imageBitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);

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

                uploadImage(Uri.fromFile(new File(file.getAbsolutePath())));

            }
        }
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

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(TimerTestNewActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(TimerTestNewActivity.this);
        }

        builder.setTitle("Cancel")
                .setMessage("Are you sure you want to cancel and go back?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(TimerTestNewActivity.this, HomeActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }
}
