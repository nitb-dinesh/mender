package com.app.handyman.mender.user.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.app.handyman.mender.common.activity.RaiseDisputeActivity;
import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Iterator;

import com.app.handyman.mender.model.Request;

import static com.app.handyman.mender.R.id.image2;

/**
 * This activity displays details about the job assigned to this particular Handy Man!
 */

public class RequestDetailsActivity extends AppCompatActivity {


    private final static String TAG = RequestDetailsActivity.class.getSimpleName();

    private String id = "";
    private TextView mJobTitle, mJobDescription, mAddress, mJobStatus, mAssignedTo, zipcodeTV, PhoneNoTV, managerNameTV, managerNo;

    private ImageView mImage, mImage2, mImage3;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");
    private TextView textViewJob, textViewDescription, textViewLocation, textViewAssigned, textViewStatus, txtTitle, zipcodetxt, phoneNotxt, managerNametxt, managertxt;
    private Button mCancelJob, mSetReminder, mRaiseDispute;

    private String address = "";
    private String firstName = "";


    private String userEmailAddress = "";

    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference requestsReference = mRootReference.child("Requests");

    private Request request = new Request();

    private Toolbar toolbar;
    private TextView toolbar_title;
    private ImageView edit_request;
    private Toast toast;


    private void editJobRequest() {
        Intent intent = new Intent(RequestDetailsActivity.this, CreateNewRequestActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }


    private void getUserName(String emailAddress) {

        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference gameRef = mRootRef.child("Users");
        Query queryRef = gameRef.orderByChild("userEmail").equalTo(emailAddress);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                Iterable<DataSnapshot> children = snapshot.getChildren();


                Iterator<DataSnapshot> child = children.iterator();

                while (child.hasNext()) {

                    DataSnapshot currentChild = child.next();
                    if (currentChild.getKey().equals("firstName")) {
                        mAssignedTo.setText(currentChild.getValue() + "");
                        firstName = currentChild.getValue() + "";
                    } else if (currentChild.getKey().equals("lastName")) {
                        mAssignedTo.setText(firstName + " " + currentChild.getValue());
                    }

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

    private void loadJobDetails() {
        Query query = requestsReference.orderByChild("key").equalTo(id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("In child added method :::: " + dataSnapshot.getChildrenCount());
                final Request request = dataSnapshot.getValue(Request.class);

                mJobTitle.setText(request.getJobTitle());
                mJobDescription.setText(request.getJobDescription());
                mAddress.setText(request.getAddress());

                zipcodeTV.setText(request.getZipCode());
                PhoneNoTV.setText(request.getPhoneNumber());
                managerNameTV.setText(request.getPropertyManagerName());
                managerNo.setText(request.getPropertyManagerPhoneNumber());

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

                mRaiseDispute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RequestDetailsActivity.this, RaiseDisputeActivity.class);
                        startActivity(intent);

                    }
                });

                if (request.getIsStarted() && request.isStatus()) {
                    if (!request.getIsPaid()) { // If not paid.
                        mJobStatus.setText("Finished and not paid");
                    } else {
                        mJobStatus.setText("Finished and Paid!");
                    }
                } else if (request.getIsStarted()) {
                    mJobStatus.setText("In Progress");
                } else if (request.isAccepted()) {
                    mJobStatus.setText("Accepted");
                } else {
                    mJobStatus.setText("Posted");
                }


                if (request.isAccepted()) {
                    //mAssignedTo.setText("Assigned to " + request.getAssignedTo());
                    getUserName(request.getAssignedTo());

                } else {
                    // if job is not accepted! Ability to delete a job!
                    mCancelJob.setVisibility(View.VISIBLE);

                    mCancelJob.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            final Dialog dialog = new Dialog(RequestDetailsActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(false);
                            dialog.setContentView(R.layout.dialog_incomplete_job);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            final TextView mTitle1 = (TextView) dialog.findViewById(R.id.title1);
                            final TextView mTitle2 = (TextView) dialog.findViewById(R.id.title2);

                            mTitle1.setText("Cancel Job");
                            mTitle2.setText("Are you sure you want to cancel this job?");

                            final Button mYes = (Button) dialog.findViewById(R.id.yes);

                            Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                            mTitle1.setTypeface(myCustomFont);
                            mTitle2.setTypeface(myCustomFont);
                            mYes.setTypeface(myCustomFont);

                            mYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // Delete the Request from backend

                                    Query deleteItem = requestsReference.orderByChild("key").equalTo(id);
                                    deleteItem.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                // Delete that item in the backend! To delete we need the reference to the item(s)
                                                DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                                                firstChild.getRef().removeValue();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    //TANSEER DELETE IMAGES


                                    // Go to home activity
                                    Intent intent = new Intent(RequestDetailsActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            });

                            dialog.setCancelable(true);
                            dialog.show();

                        }
                    });


                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Request request = dataSnapshot.getValue(Request.class);
                mJobTitle.setText(request.getJobTitle());
                mJobDescription.setText(request.getJobDescription());
                mAddress.setText(request.getAddress());

                zipcodeTV.setText(request.getZipCode());
                PhoneNoTV.setText(request.getPhoneNumber());
                managerNameTV.setText(request.getPropertyManagerName());
                managerNo.setText(request.getPropertyManagerPhoneNumber());

                if (request.isAccepted()) {
                    getUserName(request.getAssignedTo());
                }

                if (request.getIsStarted() && request.isStatus()) {
                    if (!request.getIsPaid()) { // If not paid.
                        mJobStatus.setText("Job Status : Finished and not paid");
                    } else {
                        mJobStatus.setText("Job Status : Finished and Paid!");
                    }
                } else if (request.getIsStarted()) {
                    mJobStatus.setText("Job Status : In Progress");
                } else if (request.isAccepted()) {
                    mJobStatus.setText("Job Status: Accepted");
                } else {
                    mJobStatus.setText("Job Status: Posted");
                }


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

    private void loadJobImages() {
        for (int i = 1; i < 4; i++) {
            final int finalI = i;
            try {
                //storageReference.child("images/" + id + "/" + 1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Log.d(TAG, uri.getPath());

                        try {

                            if (finalI == 1) {

                                mImage.setVisibility(View.VISIBLE);
                                Glide.with(RequestDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .fitCenter()
                                        .into(mImage); //Y

                            } else if (finalI == 2) {

                                mImage2.setVisibility(View.VISIBLE);
                                Glide.with(RequestDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .fitCenter()
                                        .into(mImage2); //Y

                            } else if (finalI == 3) {

                                mImage3.setVisibility(View.VISIBLE);
                                Glide.with(RequestDetailsActivity.this)
                                        .load(uri) // the uri you got from Firebase
                                        .centerCrop()
                                        .fitCenter()
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
    }

    private void jobImageDetails() {
        final ImagePopup imagePopup = new ImagePopup(this);
        imagePopup.setWindowHeight(800); // Optional
        imagePopup.setWindowWidth(800); // Optional
//        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
//        imagePopup.setHideCloseIcon(true);  // Optional
//        imagePopup.setImageOnClickClose(true);  // Optional

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

    }

    private void initViews() {
        ( (ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("My Request Details");


        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        userEmailAddress = u.getEmail();

        textViewJob = (TextView) findViewById(R.id.textViewJob);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewJob.setTypeface(myCustomFont5);

        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewDescription.setTypeface(myCustomFont6);

        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewLocation.setTypeface(myCustomFont7);


        // aditional details

        //zipcodetxt, phoneNotxt, managerNametxt, managertxt

        zipcodetxt = (TextView) findViewById(R.id.zipcodetxt);
        Typeface myCustomFont21 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        zipcodetxt.setTypeface(myCustomFont21);

        phoneNotxt = (TextView) findViewById(R.id.phoneNotxt);
        Typeface myCustomFont22 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        phoneNotxt.setTypeface(myCustomFont22);

        managerNametxt = (TextView) findViewById(R.id.managerNametxt);
        Typeface myCustomFon23 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        managerNametxt.setTypeface(myCustomFon23);

        managertxt = (TextView) findViewById(R.id.managertxt);
        Typeface myCustomFont24 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        managertxt.setTypeface(myCustomFont24);



        zipcodeTV = (TextView) findViewById(R.id.zipcodeTV);
        Typeface myCustomFont25 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        zipcodeTV.setTypeface(myCustomFont25);

        PhoneNoTV = (TextView) findViewById(R.id.PhoneNoTV);
        Typeface myCustomFont26 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        PhoneNoTV.setTypeface(myCustomFont26);

        managerNameTV = (TextView) findViewById(R.id.managerNameTV);
        Typeface myCustomFont27 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        managerNameTV.setTypeface(myCustomFont27);

        managerNo = (TextView) findViewById(R.id.managerNo);
        Typeface myCustomFont28 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        managerNo.setTypeface(myCustomFont28);




        textViewAssigned = (TextView) findViewById(R.id.textViewAssigned);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewAssigned.setTypeface(myCustomFont8);

        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        Typeface myCustomFont9 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewStatus.setTypeface(myCustomFont9);

        mJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        Typeface myCustomFont10 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobTitle.setTypeface(myCustomFont10);

        mJobDescription = (TextView) findViewById(R.id.txtJobDesc);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobDescription.setTypeface(myCustomFont1);

        mAddress = (TextView) findViewById(R.id.txtAddress);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mAddress.setTypeface(myCustomFont2);

        mJobStatus = (TextView) findViewById(R.id.job_status);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobStatus.setTypeface(myCustomFont3);

        mAssignedTo = (TextView) findViewById(R.id.assigned_to);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mAssignedTo.setTypeface(myCustomFont4);

        mSetReminder = (Button) findViewById(R.id.set_reminder);
        mSetReminder.setTypeface(myCustomFont8);

        edit_request = (ImageView) findViewById(R.id.edit_request);

        mRaiseDispute = (Button) findViewById(R.id.raise_dispute);
        mRaiseDispute.setTypeface(myCustomFont8);

        mCancelJob = (Button) findViewById(R.id.cancel_job);
        mCancelJob.setTypeface(myCustomFont4);

        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(image2);
        mImage3 = (ImageView) findViewById(R.id.image3);

        toast = Toast.makeText(RequestDetailsActivity.this, "", Toast.LENGTH_SHORT);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_detail);

        initViews();
        jobImageDetails();

        if (getIntent().getExtras() != null) {
            // Toast.makeText(this, "Error in intents! (Contact Developer)", Toast.LENGTH_SHORT).show();


            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            // Toast.makeText(this, "Url : " + url, Toast.LENGTH_SHORT).show();

            if (id != null && !id.isEmpty()) {

                // Toast.makeText(this, "Job Id is " + id, Toast.LENGTH_SHORT).show();
                Log.i("JobId", "onCreate: " + id);
                requestsReference = mRootReference.child("Requests");

                loadJobImages();
                loadJobDetails();

            }

        }
        ( (ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        edit_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id != null && !id.isEmpty()) {
                    editJobRequest();
                } else {
                    toast.setText("Not a valid Job!. Please reopen the Job");
                }
            }


        });

    }


}
