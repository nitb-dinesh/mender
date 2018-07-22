package com.app.handyman.mender.handyman.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.handyman.mender.R;
import com.app.handyman.mender.model.Request;
import com.bumptech.glide.Glide;
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

import java.io.File;
import java.util.Iterator;

public class EstimateJobActivity extends AppCompatActivity {

    Button jobdetailsbtn, btnfirstoption, btnsecondoption, btnthirdoption, btnfourthoption;
    private String jobId, jobtitle;
    private static final String TAG = MyJobDetailsActivity.class.getSimpleName();
    Request request;
    String id = "";
    private String mName = "";

    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");
    DatabaseReference requestsReference = mRootReference.child("Requests");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detailswith_fab);
        try {
           /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);*/

            getUserDetails();

            ( (ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            Bundle extras = getIntent().getExtras();
            jobId = id = extras.getString("id");
            loadJobDetails(jobId);

            ( (ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

            TextView toolbar_title = (TextView) findViewById(R.id.toolbar_title);
            toolbar_title.setTypeface(myCustomFont);

            toolbar_title.setText("Job Estimate");

            getRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });

            Button jobdetails = (Button) findViewById(R.id.jobdetailsbtn);
            jobdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (request == null)return;

                    final Dialog dialog = new Dialog(EstimateJobActivity.this);
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
                                            Glide.with(EstimateJobActivity.this)
                                                    .load(uri) // the uri you got from Firebase
                                                    .centerCrop()
                                                    .into(mImage); //Y

                                        } else if (finalI == 2) {

                                            mImage2.setVisibility(View.VISIBLE);
                                            Glide.with(EstimateJobActivity.this)
                                                    .load(uri) // the uri you got from Firebase
                                                    .centerCrop()
                                                    .into(mImage2); //Y

                                        } else if (finalI == 3) {

                                            mImage3.setVisibility(View.VISIBLE);
                                            Glide.with(EstimateJobActivity.this)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getUserDetails() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {


            final String email = auth.getCurrentUser().getEmail();
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gameRef = mRootRef.child("Users");
            Query queryRef = gameRef.orderByChild("userEmail").equalTo(email);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    if (snapshot == null) return;
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                  //  progressBar.setVisibility(View.GONE);

                    if (children == null) return;
                    Iterator<DataSnapshot> child = children.iterator();

                    while (child.hasNext()) {


                        DataSnapshot currentChild = child.next();
                        if(currentChild == null) continue;
                        if (currentChild.getKey().equals("firstName")) {
                            mName =""+ currentChild.getValue();
                           // mName.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("lastName")) {
                           // String firstName = mName.getText().toString();
                           // mName.setText(""+firstName + " " + currentChild.getValue());
                        } else if (currentChild.getKey().equals("address")) {
                           // mAddress.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("cityState")) {
                           // mCityAndState.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("userEmail")) {
                           // mEmail.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("phonenumber")) {
                           // mPhoneNumber.setText("" + currentChild.getValue());
                        } else {

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
    }




    private void loadJobDetails(final String id) {


        mRootReference.child("Requests").child(jobId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("In child added method :::: " + dataSnapshot.getChildrenCount());
                final Request request = dataSnapshot.getValue(Request.class);


                Button btnfirst = (Button) findViewById(R.id.btnfirstoption);
                btnfirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("smsto:" + request.getPhoneNumber());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "Hello, this is " + /*request.getAssignedTo()*/mName + " I'm a handyman with Mender Services. The estimated time to completed your project is 1 - 2 hours. If you approve of this estimate please reply with the word approve");
                        startActivity(intent);
                        finish();
                    }
                });
                Button btnsecond = (Button) findViewById(R.id.btnsecondoption);
                btnsecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("smsto:" + request.getPhoneNumber());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        //intent.putExtra("sms_body", "Hello, this is" + request.getAssignedTo() + " I'm a handyman with Mender Services. The estimated time to completed your project is 2 - 4 hours. If you approve of this estimate please reply with the word approve");
                        intent.putExtra("sms_body", "Hello, this is " + /*request.getAssignedTo()*/mName + " I'm a handyman with Mender Services. The estimated time to completed your project is 2 - 4 hours. If you approve of this estimate please reply with the word approve");
                        startActivity(intent);
                        finish();
                    }
                });
                Button btnthird = (Button) findViewById(R.id.btnthirdoption);
                btnthird.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("smsto:" + request.getPhoneNumber());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "Hello, this is " + /*request.getAssignedTo()*/mName + " I'm a handyman with Mender Services. The estimated time to completed your project is 4 - 6 hours. If you approve of this estimate please reply with the word approve");
                        startActivity(intent);
                        finish();
                    }
                });
                Button btnfourth = (Button) findViewById(R.id.btnfourthoption);
                btnfourth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Uri uri = Uri.parse("smsto:" + request.getPhoneNumber());
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "Hello, this is " + /*request.getAssignedTo()*/mName + " I'm a handyman with Mender Services. The estimated time to completed your project is 6 hours or more. If you approve of this estimate please reply with the word approve");
                        startActivity(intent);
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
}
