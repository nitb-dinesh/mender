package com.app.handyman.mender.handyman.activity;

import android.content.ActivityNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import com.app.handyman.mender.model.Request;

import static com.app.handyman.mender.R.id.image2;

/**
 * This activity displays details about the job assigned to this particular Handy Man!
 */

public class OpenJobDetailsActivity extends AppCompatActivity {

    String id = "";
    TextView mJobTitle, mJobDescription, mAddress, mJobStatus, mAssignedTo;

    private ImageView mImage, mImage2, mImage3;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    String address = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_job_details);

        mJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        Typeface myCustomFont=Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobTitle.setTypeface(myCustomFont);
//
        mJobDescription = (TextView) findViewById(R.id.txtJobDesc);
        mJobDescription.setTypeface(myCustomFont);

        mAddress = (TextView) findViewById(R.id.txtAddress);
        mAddress.setTypeface(myCustomFont);

        mJobStatus = (TextView) findViewById(R.id.job_status);
        mJobStatus.setTypeface(myCustomFont);

        mAssignedTo = (TextView) findViewById(R.id.assigned_to);
        mAssignedTo.setTypeface(myCustomFont);

        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(image2);
        mImage3 = (ImageView) findViewById(R.id.image3);

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

        if (getIntent().getExtras() == null) {
            // Toast.makeText(this, "Error in intents! (Contact Developer)", Toast.LENGTH_SHORT).show();
        } else {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            // Toast.makeText(this, "Url : " + url, Toast.LENGTH_SHORT).show();
            final Request[] r = new Request[1];
            if (id != null) {

                if (!id.isEmpty()) {

                    try {

                        Toast.makeText(this, "Job Id is " + id, Toast.LENGTH_SHORT).show();
                        Log.i("JobId", "onCreate: " + id);
                        final DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference requestsReference = mRootReference.child("Requests");


                        for (int i = 1; i < 4; i++) {
                            final int finalI = i;
                            try {
                                storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        System.out.println(uri);
                                        r[0].getImageFiles().add(new File(uri.getPath()));
                                        Bitmap mBitmap;
                                        try {

                                            if (finalI == 1) {

//                                                Picasso.with(NewJobRequestActivity.this)
//                                                        .load(uri.toString())
//                                                        .resize(dpToPx(120), dpToPx(120)).centerCrop()
//                                                        .into(mImage);
//                                            mImage.setImageBitmap(mBitmap);

                                                mImage.setVisibility(View.VISIBLE);
                                                Glide.with(OpenJobDetailsActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
                                                        .into(mImage); //Y

                                            } else if (finalI == 2) {

                                                mImage2.setVisibility(View.VISIBLE);
                                                Glide.with(OpenJobDetailsActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
                                                        .into(mImage2); //Y

                                            } else if (finalI == 3) {

                                                mImage3.setVisibility(View.VISIBLE);
                                                Glide.with(OpenJobDetailsActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
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


                        Query query = requestsReference.orderByChild("key").equalTo(id);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                System.out.println("In child added method :::: " + dataSnapshot.getChildrenCount());
                                r[0] = dataSnapshot.getValue(Request.class);
                                mJobTitle.setText(r[0].getJobTitle());
                                mJobDescription.setText(r[0].getJobDescription());
                                address = r[0].getAddress();
                                mAddress.setText(address + "");

                                if( r[0].getIsStarted() && r[0].isStatus()) {
                                    if (!r[0].getIsPaid()) { // If not paid.
                                        mJobStatus.setText("Job Status : Finished and not paid");
                                    } else {
                                        mJobStatus.setText("Job Status : Finished and Paid!");
                                    }
                                }else if(r[0].getIsStarted()) {
                                    mJobStatus.setText("Job Status : In Progress");
                                }else if(r[0].isAccepted()) {
                                    mJobStatus.setText("Job Status: Accepted");
                                }else{
                                    mJobStatus.setText("Job Status: Posted");
                                }


                                /*

                                if(r[0].isStatus()) {
                                    mJobStatus.setText("Job Status : Done!");
                                }

                                if(r[0].isAccepted()) {
                                    mAssignedTo.setText("Assigned to : " + r[0].getAssignedTo());
                                }*/

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                Request r = dataSnapshot.getValue(Request.class);
                                mJobTitle.setText(r.getJobTitle());
                                mJobDescription.setText(r.getJobDescription());
                                mAddress.setText(r.getAddress());

                                if(r.isStatus()) {
                                    mJobStatus.setText("Job Status : Done!");
                                }

                                if(r.isAccepted()) {
                                    mAssignedTo.setText("Assigned to : " + r.getAssignedTo());
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


                    } catch (ActivityNotFoundException e) {

                    }


                } else {
                    // Run Animation Code!
                }

            }

        }

    }


}
