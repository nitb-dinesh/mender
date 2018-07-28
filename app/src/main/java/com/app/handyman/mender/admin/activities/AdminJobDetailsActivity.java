package com.app.handyman.mender.admin.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.RaiseDisputeActivity;
import com.app.handyman.mender.handyman.activity.AddMaterialPurchaseDetailsActivity;
import com.app.handyman.mender.handyman.activity.MyJobDetailsActivity;
import com.app.handyman.mender.handyman.activity.StartJobActivity;
import com.app.handyman.mender.handyman.adapter.MaterialReceiptAdapter;
import com.app.handyman.mender.model.MaterialReceipt;
import com.app.handyman.mender.model.Request;
import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import static com.app.handyman.mender.R.id.image2;

public class AdminJobDetailsActivity extends AppCompatActivity {

/*   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_job_details);
    }*/

    /*  @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_admin_job_details);
        }*/

    private static final String TAG = AdminJobDetailsActivity.class.getSimpleName();

    private TextView txtJobTitle, txtJobDescription, txtPhoneNumber, txtAddress,
            txtLocation, txtPhoneNum, jobdes, jobtitle1,
            propertyMngr, propertyMngrTV, assigned_to, assignToEmailTV, userEmail, userEmailTV;
    private TextView txtDrivingHoursValue, txtWorkingHoursValue, txtMaterialPurchasedValue;


    private ImageView mImage, mImage2, mImage3;
    private Button mLocate, callpropman, textmessage, mCall, mSetReminder, start_job, mRaiseDispute;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    private String address = "";
    private String jobId;

    private TextView toolbar_title;
    private RecyclerView rvMaterialRecipt;
    private MaterialReceiptAdapter materialReceiptAdapter;
    private ArrayList<MaterialReceipt> materialReceiptArrayList;

    private DatabaseReference mRootReference;
    private DatabaseReference materialReciptReference;
    private DatabaseReference jobDetailsReference;
    private double materialCharges;
    private Toast toast;
    private boolean isJobStarted;


    private void getMaterialReceiptInfo(String id) {
        materialReciptReference = mRootReference.child("Requests").child(id).child("receipts");

        materialReciptReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.hasChildren()) {
                    MaterialReceipt materialReceipt = dataSnapshot.getValue(MaterialReceipt.class);
                    materialReceiptArrayList.add(materialReceipt);
                    materialReceiptAdapter.notifyDataSetChanged();

                    Log.d(TAG, materialReciptReference.toString());
//                    materialCharges = materialCharges + Double.parseDouble(materialReceipt.getMaterialCost());
//                    HashMap<String, Object> request = new HashMap<>();
//                    request.put("materialCost",String.valueOf(materialCharges) );
//                    jobDetailsReference.child(jobId).updateChildren(request);
                }
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


    }


    private void loadJobDetails(final String id) {


        mRootReference.child("Requests").child(jobId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("In child added method :::: " + dataSnapshot.getChildrenCount());
                final Request request = dataSnapshot.getValue(Request.class);
                txtJobTitle.setText(request.getJobTitle());
                txtJobDescription.setText(request.getJobDescription());
                txtPhoneNumber.setText(request.getPhoneNumber());

                propertyMngrTV.setText(request.getPropertyManagerName());
                assignToEmailTV.setText(request.getAssignedTo());
                userEmailTV.setText(request.getUserEmail());
                address = request.getAddress();
                txtAddress.setText(address);

                Log.d("REquestStatus", request.getIsStarted() + "'" + id);

                //txtMaterialPurchasedValue.setText(totalMaterialPurchaseCost);
                txtMaterialPurchasedValue.setText(String.format("$%.2f", Double.valueOf(request.getMaterialCost())));
                isJobStarted = request.getIsStarted();


                if (Double.parseDouble(request.getMaterialCost()) > 0) {
                    getMaterialReceiptInfo(id);
                }


                txtMaterialPurchasedValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AdminJobDetailsActivity.this, AddMaterialPurchaseDetailsActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });


                String labourTime = "0";
                String driveTime = "0";


                if (request.getDriveTime() != null) {
                    try {
                        driveTime = convertTimeIntToString(request.getDriveTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    driveTime = "00 : 00 Hrs";
                }

                if (request.getLabourTime() != null) {
                    try {
                        labourTime = convertTimeIntToString(request.getLabourTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    labourTime = "00 : 00 Hrs";
                }


                Log.d("TIMINGSTIMINGS", driveTime + "_" + labourTime);

                txtDrivingHoursValue.setText(driveTime);
                txtWorkingHoursValue.setText(labourTime);


                if (request.isStatus()) {
                    mSetReminder.setVisibility(View.GONE);

                } else {
                    mSetReminder.setVisibility(View.VISIBLE);
                    mSetReminder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Calendar cal = Calendar.getInstance();
                            Intent intent = new Intent(Intent.ACTION_EDIT);
                            intent.setType("vnd.android.cursor.item/event");
                            intent.putExtra("title", "Mender - " + request.getJobTitle());
                            startActivity(intent);

                        }
                    });
                }


                mRaiseDispute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(AdminJobDetailsActivity.this, RaiseDisputeActivity.class);
                        // intent.putExtra("request", r[0]);
                        startActivity(intent);

                    }
                });

                if (request.isStatus()) {
                    ((Button) findViewById(R.id.start_job)).setVisibility(View.GONE);
                } else {
                    ((Button) findViewById(R.id.start_job)).setVisibility(View.VISIBLE);
                }

                txtPhoneNumber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uri = "tel:" + request.getPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);

                    }
                });

                txtAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!address.isEmpty()) {

                            String map = "http://maps.google.co.in/maps?q=" + address;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                            startActivity(intent);

                        } else {
                            Toast.makeText(AdminJobDetailsActivity.this, "Not able to fetch address!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                mCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String uri = "tel:" + request.getPhoneNumber();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(uri));
                        startActivity(intent);

                    }
                });

                textmessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("smsto:" + request.getPhoneNumber());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "Enter your message here");
                        startActivity(intent);


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void loadImages(String id) {
        for (int i = 1; i < 4; i++) {
            final int finalI = i;
            try {
                // storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                    @Override
                    public void onSuccess(Uri uri) {

                        try {


                            System.out.println(uri);

                            if (finalI == 1) {

                                mImage.setVisibility(View.VISIBLE);
                                Glide.with(AdminJobDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .fitCenter()
                                        .into(mImage); //Y
                                /*Glide.with(AdminJobDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage); //Y*/

                            } else if (finalI == 2) {

                                mImage2.setVisibility(View.VISIBLE);
                                Glide.with(AdminJobDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage2); //Y

                            } else if (finalI == 3) {

                                mImage3.setVisibility(View.VISIBLE);
                                Glide.with(AdminJobDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .into(mImage3); //Y

                            }


                            final ImagePopup imagePopup = new ImagePopup(AdminJobDetailsActivity.this);
                            imagePopup.setWindowHeight(2000); // Optional
                            imagePopup.setWindowWidth(1500); // Optional
                            // imagePopup.setBackgroundColor(Color.BLACK);  // Optional
                            imagePopup.setHideCloseIcon(true);  // Optional
                            imagePopup.setImageOnClickClose(true);  // Optional

                            mImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    imagePopup.initiatePopup(mImage.getDrawable());

                                }
                            });
                            mImage2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    imagePopup.initiatePopup(mImage2.getDrawable());

                                }
                            });
                            mImage3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    imagePopup.initiatePopup(mImage3.getDrawable());

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void getJobDetails(final String id) {
        if (id != null && !id.isEmpty()) {
            try {
                loadImages(id);
                loadJobDetails(id);
            } catch (ActivityNotFoundException e) {
                Log.i(TAG, e.getMessage());
            }

        } else {
            Log.e(TAG, "Invalid Job Id");
        }
    }

    private void initViews() {
        ((ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        // getSupportActionBar().se
//        toolbar.setTitle("");

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("Job Details");

        txtJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        txtJobTitle.setTypeface(myCustomFont);

        txtJobDescription = (TextView) findViewById(R.id.txtJobDesc);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtJobDescription.setTypeface(myCustomFont1);

        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtPhoneNumber.setTypeface(myCustomFont3);

        txtPhoneNum = (TextView) findViewById(R.id.txtPhoneNum);
        Typeface myCustomFont11 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtPhoneNum.setTypeface(myCustomFont11);



        propertyMngr = (TextView) findViewById(R.id.propertyMngr);
        Typeface myCustomFont21 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        propertyMngr.setTypeface(myCustomFont21);

        propertyMngrTV = (TextView) findViewById(R.id.propertyMngrTV);
        Typeface myCustomFont22 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        propertyMngrTV.setTypeface(myCustomFont22);

        assigned_to = (TextView) findViewById(R.id.assigned_to);
        Typeface myCustomFont23 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        assigned_to.setTypeface(myCustomFont23);

        assignToEmailTV = (TextView) findViewById(R.id.assignToEmailTV);
        Typeface myCustomFont24 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        assignToEmailTV.setTypeface(myCustomFont24);

        userEmail = (TextView) findViewById(R.id.userEmail);
        Typeface myCustomFont25 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        userEmail.setTypeface(myCustomFont25);

        userEmailTV = (TextView) findViewById(R.id.userEmailTV);
        Typeface myCustomFont26 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        userEmailTV.setTypeface(myCustomFont26);



        txtAddress = (TextView) findViewById(R.id.txtAddress);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtAddress.setTypeface(myCustomFont4);

        txtLocation = (TextView) findViewById(R.id.txtLocation);
        Typeface myCustomFont10 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtLocation.setTypeface(myCustomFont10);

        mLocate = (Button) findViewById(R.id.locate);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mLocate.setTypeface(myCustomFont5);

        callpropman = (Button) findViewById(R.id.callpropman);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        callpropman.setTypeface(myCustomFont6);

        textmessage = (Button) findViewById(R.id.textmessage);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textmessage.setTypeface(myCustomFont7);

        mCall = (Button) findViewById(R.id.call);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mCall.setTypeface(myCustomFont8);

        mSetReminder = (Button) findViewById(R.id.set_reminder);
        mSetReminder.setTypeface(myCustomFont8);

        mRaiseDispute = (Button) findViewById(R.id.raise_dispute);
        mRaiseDispute.setTypeface(myCustomFont8);

        jobdes = (TextView) findViewById(R.id.jobdes);
        Typeface myCustomFont9 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        jobdes.setTypeface(myCustomFont9);

        jobtitle1 = (TextView) findViewById(R.id.jobtitle1);
        Typeface myCustomFont12 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        jobtitle1.setTypeface(myCustomFont12);

        start_job = (Button) findViewById(R.id.start_job);
        Typeface myCustomFont13 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        start_job.setTypeface(myCustomFont13);

        txtDrivingHoursValue = (TextView) findViewById(R.id.txtDrivingHoursValue);
        Typeface myCustomFont14 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtDrivingHoursValue.setTypeface(myCustomFont14);

        txtWorkingHoursValue = (TextView) findViewById(R.id.txtWorkingHoursValue);
        Typeface myCustomFont15 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtWorkingHoursValue.setTypeface(myCustomFont15);

        txtMaterialPurchasedValue = (TextView) findViewById(R.id.txtMaterialPurchasedValue);
        Typeface myCustomFont16 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtMaterialPurchasedValue.setTypeface(myCustomFont16);

        rvMaterialRecipt = findViewById(R.id.rvMaterialRecipt);

        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(image2);
        mImage3 = (ImageView) findViewById(R.id.image3);

        toast = Toast.makeText(AdminJobDetailsActivity.this, "", Toast.LENGTH_SHORT);

        start_job.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to a new activity

                if (isJobStarted) {
                    goToNextActivity();
                } else {
                    startJobConfirmation();
                }

            }
        });

        materialReceiptArrayList = new ArrayList<>();

        materialReceiptAdapter = new MaterialReceiptAdapter(materialReceiptArrayList, jobId, 1);
        rvMaterialRecipt.setLayoutManager(new LinearLayoutManager(AdminJobDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rvMaterialRecipt.setHasFixedSize(true);
        rvMaterialRecipt.setAdapter(materialReceiptAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_job_details);

        try {
            mRootReference = FirebaseDatabase.getInstance().getReference();
            jobDetailsReference = mRootReference.child("Requests");
            materialCharges = 0;


            if (getIntent().getExtras() != null) {
                Bundle extras = getIntent().getExtras();
                jobId = extras.getString("id");
                Log.d(TAG, jobId);
                initViews();
                getJobDetails(jobId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String convertTimeIntToString(String time) {

        int sTime = Integer.parseInt(time);

        if (sTime < 0) {
            sTime = sTime * -1;
        }

        int hours = (int) (sTime / 3600000);
        int minutes = (int) (sTime - hours * 3600000) / 60000;


        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " Hrs";


    }

    private void startJobConfirmation() {
        final Dialog dialog = new Dialog(AdminJobDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_confirm_startjob);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView title = (TextView) dialog.findViewById(R.id.title);
        final Button buttonYes = (Button) dialog.findViewById(R.id.buttonYes);
        final Button buttonNo = (Button) dialog.findViewById(R.id.buttonNo);

        Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        title.setTypeface(myCustomFont19);
        buttonYes.setTypeface(myCustomFont19);
        buttonNo.setTypeface(myCustomFont19);

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatusinDb(dialog);
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        dialog.setCancelable(true);
        dialog.show();
    }

    private void updateStatusinDb(final Dialog dialog) {

        HashMap<String, Object> request = new HashMap<>();
        request.put("isStarted", true);

        mRootReference.child("Requests").child(jobId).updateChildren(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialog.dismiss();
                toast.setText("Job Started!");
                goToNextActivity();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                toast.setText("Job could not be started, try again!");
            }
        });

    }

    private void goToNextActivity() {
        Intent intent = new Intent(AdminJobDetailsActivity.this, StartJobActivity.class);
        intent.putExtra("id", jobId);
        startActivity(intent);
    }


    /*


    new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Request r = dataSnapshot.getValue(Request.class);

                txtJobTitle.setText(r.getJobTitle());
                txtJobDescription.setText(r.getJobDescription());
                txtPhoneNumber.setText(r.getPhoneNumber());
                txtAddress.setText(r.getAddress());

                //txtMaterialPurchasedValue.setText(totalMaterialPurchaseCost);
                txtMaterialPurchasedValue.setText(String.format( "$%.2f", Double.valueOf(r.getMaterialCost())));
                Log.d("REquestStatus", r.isStarted() + "'");
                isJobStarted = r.isStarted();

                if(Double.parseDouble(r.getMaterialCost()) >0){
                    getMaterialReceiptInfo(id);
                }


                String labourTime = "0";
                String driveTime = "0";



                if(r.getDriveTime() == null){
                    try {
                        driveTime = convertTimeIntToString(r.getDriveTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (r.getLabourTime() != null) {
                    try {
                        labourTime = convertTimeIntToString(r.getLabourTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                Log.d("TIMINGSTIMINGS", driveTime + "_" + labourTime);

                txtDrivingHoursValue.setText(driveTime );
                txtWorkingHoursValue.setText(labourTime);
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
     */
}
