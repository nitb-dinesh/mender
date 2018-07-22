package com.app.handyman.mender.handyman.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;

import com.app.handyman.mender.model.Request;

import static com.app.handyman.mender.R.id.image2;

/**
 * App for keeping count of the time the user takes for driving, labor and also for inputting material costs.
 */

public class StartJobActivity extends AppCompatActivity implements View.OnClickListener{


    private final static String TAG = StartJobActivity.class.getSimpleName();
    private static final int REQUEST_CAPTURE_FROM_CAMERA = 1;

    private TextView mTitle, mTotalCost, mcharges, charges2;
    private LinearLayout mDriveTimeLayout, mLaborTimeLayout;
    private Button mDriveTimeButton, mLaborTimeButton, mReceiptsButton, mJobInfo, mEndJob, mIncompleteJob;

    private Chronometer mDriveChronometer, mLaborChronometer;
    private TextView mDriveTotal, mLaborTotal, drivetime1, labortime1;
    private Button dtPlay, dtPause, dtReset;
    private Button ltPlay, ltPause, ltReset;

    private double driveTimeCharges = 00.00, laborTimeCharges = 00.00, totalCharges = 00.00, materialCharges = 00.00;

    private String id = "";

    private static final String FORMAT = "%02d:%02d";

    private File file;
    private byte[] imageData;
    private Date d = new Date();
    private String a = d.getTime() + "";

    private long dt_timeWhenStopped = 0;
    private long lt_timeWhenStopped = 0;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    private int step = 0;
    private boolean isTimerOn = false;

    private Request request;

    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference requestsReference = mRootReference.child("Requests");
    private Toast toast;



    private void handymanDrive(){
        if (isTimerOn) {
            Toast.makeText(StartJobActivity.this, "Pause the timer first.", Toast.LENGTH_SHORT).show();
        } else {

            // Show the correct view!

            mDriveTimeLayout.setVisibility(View.VISIBLE);
            mLaborTimeLayout.setVisibility(View.GONE);

            mDriveTimeButton.setAlpha(1f);
            mLaborTimeButton.setAlpha(.5f);
            mReceiptsButton.setAlpha(.5f);

            mDriveTimeButton.setTextColor(Color.parseColor("#2ec4b6"));
            mLaborTimeButton.setTextColor(Color.parseColor("#000000"));
            mReceiptsButton.setTextColor(Color.parseColor("#000000"));


        }

    }

    private void handymanLabor(){
        if (isTimerOn) {
            Toast.makeText(StartJobActivity.this, "Pause the timer first.", Toast.LENGTH_SHORT).show();
        } else {

            // Show the correct view!

            mDriveTimeLayout.setVisibility(View.GONE);
            mLaborTimeLayout.setVisibility(View.VISIBLE);

            mDriveTimeButton.setAlpha(.5f);
            mLaborTimeButton.setAlpha(1f);
            mReceiptsButton.setAlpha(.5f);

            mDriveTimeButton.setTextColor(Color.parseColor("#000000"));
            mLaborTimeButton.setTextColor(Color.parseColor("#2ec4b6"));
            mReceiptsButton.setTextColor(Color.parseColor("#000000"));

        }

    }
    private void handymanReceipts(){
        if (isTimerOn) {
            toast.setText("Pause the timer first.");
        } else {
            Intent intent = new Intent(StartJobActivity.this, AddMaterialPurchaseDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }

    }

    private void handymanIncompleteJob(){
        final Dialog dialog = new Dialog(StartJobActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_incomplete_job);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView mTitle1 = (TextView) dialog.findViewById(R.id.title1);
        final TextView mTitle2 = (TextView) dialog.findViewById(R.id.title2);
        final Button mYes = (Button) dialog.findViewById(R.id.yes);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mTitle1.setTypeface(myCustomFont);
        mTitle2.setTypeface(myCustomFont);
        mYes.setTypeface(myCustomFont);

        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                request.setDriveTimeCost("0");
                request.setDriveTime(null);

                request.setLabourTime(null);
                request.setLaborTimeCost("0");

                request.setStatus(false);
                request.setTotalCost("0");
                request.setAssignedTo("");

                request.setAccepted(false);
                request.setIsStarted(false);

                request.setMaterialCost("0");



                HashMap<String, Object> map = request.toFirebase();
                requestsReference.child(id).updateChildren(map);
                requestsReference.child(id).child("receipts").removeValue();

                Intent intent = new Intent(StartJobActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        dialog.setCancelable(true);
        dialog.show();

    }

    private void handymanEndJob(){
        if (isTimerOn) {
            Toast.makeText(StartJobActivity.this, "Pause the timer first.", Toast.LENGTH_SHORT).show();
        } else {

            // Use Custom Dialog

            final Dialog dialog = new Dialog(StartJobActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_confirm_end_job);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final Button mEndJob = (Button) dialog.findViewById(R.id.end_job_button);
            final Button mDisputeTotal = (Button) dialog.findViewById(R.id.dispute_total);
            final Button mBack = (Button) dialog.findViewById(R.id.back);

            final TextView mDescDriving, mDescLabor, mDescMaterial, mDrivingCost, mLaborCost, mMaterialCost;
            TextView mTitle1, mTitle2, mTitle3, mTitle4, mTitle5;

            mTitle1 = (TextView) dialog.findViewById(R.id.title1);
            mTitle2 = (TextView) dialog.findViewById(R.id.title2);
            mTitle3 = (TextView) dialog.findViewById(R.id.title3);
            mTitle4 = (TextView) dialog.findViewById(R.id.title4);
            mTitle5 = (TextView) dialog.findViewById(R.id.title5);

            Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

            mDescDriving = (TextView) dialog.findViewById(R.id.desc_driving_cost);
            mDescLabor = (TextView) dialog.findViewById(R.id.desc_labor_cost);
            mDescMaterial = (TextView) dialog.findViewById(R.id.desc_material_cost);

            mDrivingCost = (TextView) dialog.findViewById(R.id.driving_cost);
            mLaborCost = (TextView) dialog.findViewById(R.id.labor_cost);
            mMaterialCost = (TextView) dialog.findViewById(R.id.material_cost);

            mDescDriving.setTypeface(myCustomFont);
            mDescLabor.setTypeface(myCustomFont);
            mDescMaterial.setTypeface(myCustomFont);

            mDrivingCost.setTypeface(myCustomFont);
            mLaborCost.setTypeface(myCustomFont);
            mMaterialCost.setTypeface(myCustomFont);

            mEndJob.setTypeface(myCustomFont);
            mDisputeTotal.setTypeface(myCustomFont);
            mBack.setTypeface(myCustomFont);

            mTitle1.setTypeface(myCustomFont);
            mTitle2.setTypeface(myCustomFont);
            mTitle3.setTypeface(myCustomFont);
            mTitle4.setTypeface(myCustomFont);
            mTitle5.setTypeface(myCustomFont);

            mDisputeTotal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // End Job
                    totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                    // Update Backend with task done and the respective charges! Set status as done.

                    if (totalCharges <= 0) {

                        request.setDriveTimeCost("0");
                        request.setDriveTime(null);

                        request.setLabourTime(null);
                        request.setLaborTimeCost("0");

                        request.setStatus(false);
                        request.setTotalCost("0");
                        request.setAssignedTo("");

                        request.setAccepted(false);
                        request.setIsStarted(false);

                        request.setMaterialCost("0");




                        HashMap<String, Object> map = request.toFirebase();
                        requestsReference.child(id).updateChildren(map);

                        Intent intent = new Intent(StartJobActivity.this, ConfirmActivity.class);
                        intent.putExtra("drive_time", driveTimeCharges);
                        intent.putExtra("labor_time", laborTimeCharges);
                        intent.putExtra("material_cost", materialCharges);
                        startActivity(intent);

                    } else {

                        request.setStatus(true);
                        request.setTotalCost(String.valueOf(totalCharges));
                        request.setDriveTimeCost(String.valueOf(driveTimeCharges));
                        request.setLaborTimeCost(String.valueOf(laborTimeCharges));
                        request.setMaterialCost(String.valueOf(materialCharges ));

                        HashMap<String, Object> map = request.toFirebase();
                        requestsReference.child(id).updateChildren(map);
                    }

                }
            });

            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            LinearLayout l1, l2, l3;
            l1 = (LinearLayout) dialog.findViewById(R.id.l1);
            l2 = (LinearLayout) dialog.findViewById(R.id.l2);
            l3 = (LinearLayout) dialog.findViewById(R.id.l3);

            l1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mDescDriving.getVisibility() == View.VISIBLE) {
                        mDescDriving.setVisibility(View.GONE);
                    } else {
                        mDescDriving.setVisibility(View.VISIBLE);
                    }

                }
            });

            l2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mDescLabor.getVisibility() == View.VISIBLE) {
                        mDescLabor.setVisibility(View.GONE);
                    } else {
                        mDescLabor.setVisibility(View.VISIBLE);
                    }

                }
            });

            l3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mDescMaterial.getVisibility() == View.VISIBLE) {
                        mDescMaterial.setVisibility(View.GONE);
                    } else {
                        mDescMaterial.setVisibility(View.VISIBLE);
                    }

                }
            });


            final TextView mTotalCost = (TextView) dialog.findViewById(R.id.total_payment);
            mTotalCost.setTypeface(myCustomFont);




            totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

            mTotalCost.setText(String.format( "$%.2f", totalCharges));
            mDrivingCost.setText(String.format( "$%.2f", driveTimeCharges));
            mLaborCost.setText(String.format( "$%.2f", laborTimeCharges));
            mMaterialCost.setText(String.format( "$%.2f", materialCharges));

            mEndJob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // End Job



                    totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                    // Update Backend with task done and the respective charges! Set status as done.

                    if (totalCharges <= 0) {

                        request.setDriveTimeCost("0");
                        request.setDriveTime(null);

                        request.setLabourTime(null);
                        request.setLaborTimeCost("0");

                        request.setStatus(false);
                        request.setTotalCost("0");
                        request.setAssignedTo("");

                        request.setAccepted(false);
                        request.setIsStarted(false);


//                    Map<String, Object> child = new HashMap<String, Object>();
//                    child.put(id, request.toFirebase());

                        HashMap<String, Object> map = request.toFirebase();
                        requestsReference.child(id).updateChildren(map);

                    } else {

                        request.setStatus(true);
                        request.setTotalCost("" + totalCharges);
                        request.setDriveTimeCost(String.valueOf(driveTimeCharges ));
                        request.setLaborTimeCost(String.valueOf(laborTimeCharges  ));
                        request.setMaterialCost(String.valueOf(materialCharges ));

//                    Map<String, Object> child = new HashMap<String, Object>();
//                    child.put(id, request.toFirebase());

                        HashMap<String, Object> map = request.toFirebase();
                        requestsReference.child(id).updateChildren(map);

                    }

                    Intent intent = new Intent(StartJobActivity.this, HomeActivity.class);
                    startActivity(intent);

                }
            });

            dialog.setCancelable(true);
            dialog.show();


        }

    }

    private void handymanJobInfo(){
        final Dialog dialog = new Dialog(StartJobActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_job_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtJobTitle, txtJobDescription, txtPhoneNum, txtPhoneNumber, txtAddress, txtLocation;
        final ImageView mImage, mImage2, mImage3;
        // final Button mCall;

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        txtJobTitle = (TextView) dialog.findViewById(R.id.txtJobTitle);
        txtJobTitle.setText(request.getJobTitle());
        txtJobTitle.setTypeface(myCustomFont);

        txtJobDescription = (TextView) dialog.findViewById(R.id.txtJobDesc);
        txtJobDescription.setText(request.getJobDescription());
        txtJobDescription.setTypeface(myCustomFont);

        txtPhoneNumber = (TextView) dialog.findViewById(R.id.txtPhoneNumber);
        txtPhoneNumber.setText(request.getPhoneNumber());
        txtPhoneNumber.setTypeface(myCustomFont);

        txtPhoneNum = (TextView) dialog.findViewById(R.id.txtPhoneNum);
        txtPhoneNum.setTypeface(myCustomFont);


//

        txtAddress = (TextView) dialog.findViewById(R.id.txtAddress);
        txtAddress.setText(request.getAddress());
        txtAddress.setTypeface(myCustomFont);



        txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                Intent geoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
                        + request.getAddress().toString()));
                startActivity(geoIntent);

            }
        });

        txtLocation = (TextView) dialog.findViewById(R.id.txtLocation);
        txtLocation.setTypeface(myCustomFont);

        mImage = (ImageView) dialog.findViewById(R.id.image);
        mImage2 = (ImageView) dialog.findViewById(image2);
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


                                mImage.setVisibility(View.VISIBLE);
                                Glide.with(StartJobActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage); //Y

                            } else if (finalI == 2) {

                                mImage2.setVisibility(View.VISIBLE);
                                Glide.with(StartJobActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage2); //Y

                            } else if (finalI == 3) {

                                mImage3.setVisibility(View.VISIBLE);
                                Glide.with(StartJobActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage3); //Y

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dialog.setCancelable(true);
        dialog.show();

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.drive_time_button:

                handymanDrive();

                break;

            case R.id.labor_time_button:
                handymanLabor();

                break;

            case R.id.receipts_button:
                handymanReceipts();
                break;

            case R.id.job_info_button:
                handymanJobInfo();
                break;

            case R.id.end_job_button:
                handymanEndJob();
                break;

            case R.id.incomplete_job_button:
                handymanIncompleteJob();
                break;

        }
    }

    private void initViews(){
        toast = Toast.makeText(StartJobActivity.this, "", Toast.LENGTH_LONG);
        // Drive Time Costs
        mDriveChronometer = (Chronometer) findViewById(R.id.drive_chronometer);
        // mDriveChronometer.setFormat("MM:SS");
        //mDriveChronometer.setText("00:00");
        Typeface myCustomFont21 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mDriveChronometer.setTypeface(myCustomFont21);

        mDriveTotal = (TextView) findViewById(R.id.drive_total);

        dtPlay = (Button) findViewById(R.id.dt_play);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        dtPlay.setTypeface(myCustomFont);
        mDriveTotal.setTypeface(myCustomFont);

        dtPause = (Button) findViewById(R.id.dt_pause);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        dtPause.setTypeface(myCustomFont1);

        dtReset = (Button) findViewById(R.id.dt_reset);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        dtReset.setTypeface(myCustomFont2);

        // Labor Costs
        mLaborChronometer = (Chronometer) findViewById(R.id.labor_chronometer);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLaborChronometer.setTypeface(myCustomFont3);
        // mLaborChronometer.setFormat("MM:SS");
        mLaborChronometer.setText("00:00");
        mLaborChronometer.setTypeface(myCustomFont);

        mLaborTotal = (TextView) findViewById(R.id.labor_total);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLaborTotal.setTypeface(myCustomFont4);

        ltPlay = (Button) findViewById(R.id.lt_play);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        ltPlay.setTypeface(myCustomFont5);

        ltPause = (Button) findViewById(R.id.lt_pause);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        ltPause.setTypeface(myCustomFont6);

        ltReset = (Button) findViewById(R.id.lt_reset);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        ltReset.setTypeface(myCustomFont7);

        mTitle = (TextView) findViewById(R.id.title);
        Typeface myCustomFont11 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mTitle.setTypeface(myCustomFont11);

        mTotalCost = (TextView) findViewById(R.id.total_cost);
        Typeface myCustomFont12 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mTotalCost.setTypeface(myCustomFont12);

        //Layouts
        mDriveTimeLayout = (LinearLayout) findViewById(R.id.drive_time_layout);
        mLaborTimeLayout = (LinearLayout) findViewById(R.id.labor_time_layout);


        //Buttons
        mDriveTimeButton = (Button) findViewById(R.id.drive_time_button);
        Typeface myCustomFont13 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mDriveTimeButton.setTypeface(myCustomFont13);

        mLaborTimeButton = (Button) findViewById(R.id.labor_time_button);
        Typeface myCustomFont14 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLaborTimeButton.setTypeface(myCustomFont14);

        mReceiptsButton = (Button) findViewById(R.id.receipts_button);
        Typeface myCustomFont15 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mReceiptsButton.setTypeface(myCustomFont15);

        mJobInfo = (Button) findViewById(R.id.job_info_button);
        Typeface myCustomFont16 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobInfo.setTypeface(myCustomFont16);

        mEndJob = (Button) findViewById(R.id.end_job_button);
        Typeface myCustomFont17 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mEndJob.setTypeface(myCustomFont17);

        mIncompleteJob = (Button) findViewById(R.id.incomplete_job_button);
        mIncompleteJob.setTypeface(myCustomFont17);

        mLaborTimeButton.setClickable(false);
        Typeface myCustomFont18 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLaborTimeButton.setTypeface(myCustomFont18);

        mReceiptsButton.setClickable(false);
        Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mReceiptsButton.setTypeface(myCustomFont19);

        drivetime1 = (TextView) findViewById(R.id.drivetime1);
        Typeface myCustomFont20 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        drivetime1.setTypeface(myCustomFont20);

        mcharges = (TextView) findViewById(R.id.mcharges);
        Typeface myCustomFont22 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mcharges.setTypeface(myCustomFont22);

        charges2 = (TextView) findViewById(R.id.charges2);
        Typeface myCustomFont24 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        charges2.setTypeface(myCustomFont24);

        labortime1 = (TextView) findViewById(R.id.labortime1);
        Typeface myCustomFont23 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        labortime1.setTypeface(myCustomFont23);



        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
//        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
//        imagePopup.setHideCloseIcon(true);  // Optional
//        imagePopup.setImageOnClickClose(true);  // Optional



        mDriveTimeButton.setPaintFlags(mDriveTimeButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mLaborTimeButton.setPaintFlags(mDriveTimeButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mReceiptsButton.setPaintFlags(mDriveTimeButton.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void getRequest() {

        Query jobReference = requestsReference.orderByChild("key").equalTo(id);

        jobReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    request = data.getValue(Request.class);

                    // customerId = request.getCustomerId();

                    int currentDriveTime = 0;
                    if (request.getDriveTime() != null) {
                        try {
                            currentDriveTime = Integer.parseInt(request.getDriveTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    mDriveChronometer.setBase(SystemClock.elapsedRealtime() + currentDriveTime);
                    // mDriveChronometer.start();
                    // mDriveChronometer.stop();

                    int currentLabourTime = 0;
                    if (request.getLabourTime() != null) {
                        try {
                            currentLabourTime = Integer.parseInt(request.getLabourTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                    mLaborChronometer.setBase(SystemClock.elapsedRealtime() + currentLabourTime);
                    // mLaborChronometer.start();
                    // mLaborChronometer.stop();

                    Log.d("TESTEST1", request.getTotalCost());

                    Log.d("TESTEST2", String.format( "$%.2f", 0.67));





                    if(request.getDriveTimeCost() != null){
                        driveTimeCharges = Double.parseDouble(request.getDriveTimeCost());
                        mDriveTotal.setText(String.format( "$%.2f", driveTimeCharges));
                    }else{
                        driveTimeCharges = 0;
                    }

                    if(request.getLaborTimeCost() != null){
                        laborTimeCharges = Double.parseDouble(request.getLaborTimeCost());
                        mLaborTotal.setText(String.format( "$%.2f", laborTimeCharges));
                    }else{
                        laborTimeCharges = 0;
                    }

                    if(request.getMaterialCost() != null){
                        materialCharges = Double.parseDouble(request.getMaterialCost());

                    }else{
                        materialCharges = 0;
                    }

                    /*
                    if(request.getTotalCost() != null){
                        mTotalCost.setText(String.format( "$%.2f", Double.parseDouble(request.getTotalCost())));
                    }
                    */

                    Double mTotalCostTemp = driveTimeCharges + laborTimeCharges + materialCharges;

                    mTotalCost.setText(String.format( "$%.2f", mTotalCostTemp));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(getActivity(), "Cancelled Request", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_job);

        try {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            getRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        initViews();


        mDriveTimeButton.setOnClickListener(this);
        mLaborTimeButton.setOnClickListener(this);
        mReceiptsButton.setOnClickListener(this);
        mJobInfo.setOnClickListener(this);
        mEndJob.setOnClickListener(this);
        mIncompleteJob.setOnClickListener(this);


        dtPause.setEnabled(false);

        dtPlay.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                mDriveChronometer.setBase(SystemClock.elapsedRealtime());
//                mDriveChronometer.start();

                int currentDriveTime = 0;
                if (request.getDriveTime() != null) {
                    try {
                        currentDriveTime = Integer.parseInt(request.getDriveTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dtPlay.setEnabled(false);
                dtPause.setEnabled(true);
                mDriveChronometer.setBase(SystemClock.elapsedRealtime() + currentDriveTime);
                mDriveChronometer.start();
                request.setDriveTimeStartDate(new Date());
                requestsReference.child(id).updateChildren(request.toFirebase());
                isTimerOn = true;

            }
        });

        dtPause.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                dt_timeWhenStopped = mDriveChronometer.getBase() - SystemClock.elapsedRealtime();

                //long timeElapsed = SystemClock.elapsedRealtime() - mDriveChronometer.getBase();

                dtPlay.setEnabled(true);
                dtPause.setEnabled(false);

                long timeElapsed = dt_timeWhenStopped;
                request.setDriveTime(timeElapsed + "");

                if(timeElapsed <0){
                    timeElapsed = timeElapsed * -1;
                }
                int hours = (int) (timeElapsed / 3600000);
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;
                if (minutes < 0) {
                    minutes = minutes * -1;
                }

                driveTimeCharges = (double) ((hours * 60) + minutes )* 0.67;
                //driveTimeCharges = (double)  minutes * 0.67;

                totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;



                Log.d("driveTimeCharges", String.format("%.02f", driveTimeCharges));


                request.setDriveTimeCost(String.valueOf(driveTimeCharges));
                request.setTotalCost(String.valueOf(totalCharges));
                requestsReference.child(id).updateChildren(request.toFirebase());
                isTimerOn = false;
                mDriveChronometer.stop();

            }
        });

        dtReset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(StartJobActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_confirm_logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
                final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

                Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                mLogoutTitle.setTypeface(myCustomFont19);
                mConfirmLogout.setTypeface(myCustomFont19);

                mLogoutTitle.setText("Are your sure you want to reset the timer?");
                mConfirmLogout.setText("Yes");

                mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mDriveChronometer.setBase(SystemClock.elapsedRealtime());
                        dt_timeWhenStopped = 0;
                        driveTimeCharges = 0.0;

                        request.setDriveTimeCost("0");
                        request.setDriveTime(null);
                        totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                        request.setTotalCost(String.valueOf(totalCharges));
                        requestsReference.child(id).updateChildren(request.toFirebase());


                        isTimerOn = false;
                        dtPlay.setEnabled(true);
                        dtPause.setEnabled(false);

                        dialog.dismiss();

                    }
                });

                dialog.setCancelable(true);
                dialog.show();

            }
        });

        ltPause.setEnabled(false);

        ltPlay.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                int currentLabourTime = 0;
                if (request.getLabourTime() != null) {
                    try {
                        currentLabourTime = Integer.parseInt(request.getLabourTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                ltPlay.setEnabled(false);
                ltPause.setEnabled(true);
                mLaborChronometer.setBase(SystemClock.elapsedRealtime() + currentLabourTime);
                mLaborChronometer.start();
                request.setLabourTimeStartDate(new Date());
                requestsReference.child(id).updateChildren(request.toFirebase());
                isTimerOn = true;

            }
        });

        ltPause.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub


                lt_timeWhenStopped = mLaborChronometer.getBase() - SystemClock.elapsedRealtime();

                //long timeElapsed = SystemClock.elapsedRealtime() - mLaborChronometer.getBase();
                ltPlay.setEnabled(true);
                ltPause.setEnabled(false);
                long timeElapsed = lt_timeWhenStopped;
                request.setLabourTime(timeElapsed + "");

                int hours = (int) (timeElapsed / 3600000);
                int minutes = (int) (timeElapsed - hours * 3600000) / 60000;

                if (minutes <= 60) {
                    laborTimeCharges = 75.0;
                } else {
                    double overMinutes = minutes - 60;
                    laborTimeCharges = 75.0 + (1.25 * overMinutes);
                }

                totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                request.setLaborTimeCost(String.valueOf(laborTimeCharges));
                request.setTotalCost(String.valueOf(totalCharges));
                requestsReference.child(id).updateChildren(request.toFirebase());
                isTimerOn = false;
                mLaborChronometer.stop();

            }
        });

        ltReset.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                final Dialog dialog = new Dialog(StartJobActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_confirm_logout);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
                final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

                Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                mLogoutTitle.setTypeface(myCustomFont19);
                mConfirmLogout.setTypeface(myCustomFont19);

                mLogoutTitle.setText("Are your sure you want to reset the timer?");
                mConfirmLogout.setText("Yes");

                mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mLaborChronometer.setBase(SystemClock.elapsedRealtime());
                        lt_timeWhenStopped = 0;
                        laborTimeCharges = 0.0;

                        request.setLabourTime(null);
                        request.setLaborTimeCost("0");
                        totalCharges = driveTimeCharges + laborTimeCharges + materialCharges;

                        request.setTotalCost(String.valueOf(totalCharges));

                        requestsReference.child(id).updateChildren(request.toFirebase());


                        isTimerOn = false;
                        ltPlay.setEnabled(true);
                        ltPause.setEnabled(false);

                        dialog.dismiss();

                    }
                });

                dialog.setCancelable(true);
                dialog.show();

            }
        });


    }




    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(StartJobActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {

        if (isTimerOn) {
            Toast.makeText(this, "Please stop the timer first.", Toast.LENGTH_SHORT).show();
        } else {

            final Dialog dialog = new Dialog(StartJobActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_confirm_logout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
            final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

            Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

            mLogoutTitle.setText("Are you sure you want to cancel and go back?");
            mConfirmLogout.setText("Yes");

            mLogoutTitle.setTypeface(myCustomFont19);
            mConfirmLogout.setTypeface(myCustomFont19);

            mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(StartJobActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            dialog.setCancelable(true);
            dialog.show();

        }

    }



}
