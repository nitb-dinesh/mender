package com.app.handyman.mender.handyman.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;
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

import java.io.File;

import com.app.handyman.mender.model.Request;

import static com.app.handyman.mender.R.id.image2;

/**
 * Handyman come to this screen to view new requests which are made by the users.
 * Handyman may or may not accept these new requests.
 */


public class NewJobRequestActivity extends AppCompatActivity {

    String id = "";

    TextView txtJobTitle, txtJobDescription, txtPhoneNumber, txtAddress, txtNote, textViewJob,
            textViewLocation, textViewPhoneNumber, textViewDescription, txtTitle, txtPropertyManager, txtPropertyManagerTitle;
    Button btnAccept, btnDecline, btnLocate, btnestimate;
    private ImageView mImage, mImage2, mImage3;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    byte[] imageData;

    ImagePopup imagePopup;

    private Toolbar toolbar;
    private TextView toolbar_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job_request);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setTitle("");

        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        toolbar_title.setTypeface(myCustomFont);

        ( (ImageView) findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar_title.setText("New Job Request");

        txtJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        txtJobTitle.setTypeface(myCustomFont);
        txtJobTitle.setText("New Job Details");

        txtJobDescription = (TextView) findViewById(R.id.txtJobDesc);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtJobDescription.setTypeface(myCustomFont1);

        txtPhoneNumber = (TextView) findViewById(R.id.txtPhoneNumber);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtPhoneNumber.setTypeface(myCustomFont2);

        txtPhoneNumber.setVisibility(View.GONE);


        txtAddress = (TextView) findViewById(R.id.txtAddress);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtAddress.setTypeface(myCustomFont3);

        txtNote = (TextView) findViewById(R.id.txtNote);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        txtNote.setTypeface(myCustomFont4);

        btnAccept = (Button) findViewById(R.id.btnAccept);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btnAccept.setTypeface(myCustomFont5);

        btnDecline = (Button) findViewById(R.id.btnDecline);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btnDecline.setTypeface(myCustomFont6);

        textViewJob = (TextView) findViewById(R.id.textViewJob);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewJob.setTypeface(myCustomFont7);

        textViewDescription = (TextView) findViewById(R.id.textViewDescription);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewDescription.setTypeface(myCustomFont8);

        textViewLocation = (TextView) findViewById(R.id.textViewLocation);
        Typeface myCustomFont9 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewLocation.setTypeface(myCustomFont9);

        textViewPhoneNumber = (TextView) findViewById(R.id.textViewPhoneNumber);
        Typeface myCustomFont10 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        textViewPhoneNumber.setTypeface(myCustomFont10);

        textViewPhoneNumber.setVisibility(View.GONE);

        Typeface myCustomFont11 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        btnLocate = (Button) findViewById(R.id.locate);
        btnLocate.setTypeface(myCustomFont1);

        txtPropertyManager = (TextView) findViewById(R.id.txtPropertyManager);
        txtPropertyManager.setTypeface(myCustomFont11);

        txtPropertyManagerTitle = (TextView) findViewById(R.id.propertyManagerTitle);
        txtPropertyManagerTitle.setTypeface(myCustomFont11);

        btnestimate = (Button) findViewById(R.id.btnestimate);
        Typeface myCustomFont12 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        btnestimate.setTypeface(myCustomFont12);

        mImage = (ImageView) findViewById(R.id.image);
        mImage2 = (ImageView) findViewById(image2);
        mImage3 = (ImageView) findViewById(R.id.image3);

        imagePopup = new ImagePopup(this);
        imagePopup.setWindowHeight(1000); // Optional
        imagePopup.setWindowWidth(1000); // Optional
//        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
//        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional

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

                        // Toast.makeText(this, "Job Id is " + id, Toast.LENGTH_SHORT).show();
                        Log.i("JobId", "onCreate: " + id);
                        final DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
                        final DatabaseReference requestsReference = mRootReference.child("Requests");
                        Query query = requestsReference.orderByChild("key").equalTo(id);
                        query.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                System.out.println("In child added method :::: " + dataSnapshot.getChildrenCount());
                                r[0] = dataSnapshot.getValue(Request.class);

                                txtJobTitle.setText(r[0].getJobTitle());
                                txtJobDescription.setText(r[0].getJobDescription());
                                txtPhoneNumber.setText(r[0].getPhoneNumber());
                                String zipcode = "";
                                if (r[0].getZipCode() != null) {
                                    zipcode = r[0].getZipCode();
                                    txtAddress.setText(r[0].getAddress() /*+ "\n\nZipcode : " + zipcode*/);
                                } else {
                                    txtAddress.setText(r[0].getAddress());
                                }

                                final String address = r[0].getAddress() + "";

                                btnLocate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if (!address.isEmpty()) {

                                            String map = "http://maps.google.co.in/maps?q=" + address;
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                                            startActivity(intent);


                                        } else {
                                            Toast.makeText(NewJobRequestActivity.this, "Not able to fetch address!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                if (r[0].getPropertyManagerName() != null) {

                                    if (r[0].getPropertyManagerName().isEmpty()) {
                                        txtPropertyManager.setVisibility(View.GONE);
                                        txtPropertyManagerTitle.setVisibility(View.GONE);
                                    }

                                    txtPropertyManager.setText(r[0].getPropertyManagerName() + "");

                                    if (r[0].getPropertyManagerPhoneNumber() != null) {
                                        String prevText = r[0].getPropertyManagerName();
                                        txtPropertyManager.setText(prevText + " " + r[0].getPropertyManagerPhoneNumber() + "");
                                    }

                                }

                                if (r[0].isAccepted() == false) {
                                    btnAccept.setEnabled(true);
                                    txtNote.setVisibility(View.GONE);
                                } else {
                                    btnAccept.setEnabled(false);
                                    txtNote.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                Request r = dataSnapshot.getValue(Request.class);
                                txtJobTitle.setText(r.getJobTitle());
                                txtJobDescription.setText(r.getJobDescription());
                                txtPhoneNumber.setText(r.getPhoneNumber());
                                txtAddress.setText(r.getAddress());
                                if (r.isAccepted() == false) {
                                    btnAccept.setEnabled(true);
                                    txtNote.setVisibility(View.GONE);
                                } else {
                                    btnAccept.setEnabled(false);
                                    txtNote.setVisibility(View.VISIBLE);
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


                        for (int i = 1; i < 4; i++) {
                            final int finalI = i;
                            try {
                                storageReference.child("images/" + id + "/" + i).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        System.out.println(uri);
                                        if (r[0] != null)
                                            r[0].getImageFiles().add(new File(uri.getPath()));
                                        Bitmap mBitmap;
                                        try {

                                            if (finalI == 1) {

//                                                Picasso.with(NewJobRequestActivity.this)
//                                                        .load(uri.toString())
//                                                        .resize(dpToPx(120), dpToPx(120)).centerCrop()
//                                                        .into(mImage);
//                                            mImage.setImageBitmap(mBitmap);

                                                Glide.with(NewJobRequestActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
                                                        .listener(new RequestListener<Uri, GlideDrawable>() {
                                                            @Override
                                                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                return false;
                                                            }

                                                            @Override
                                                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                mImage.setVisibility(View.VISIBLE);

                                                                mImage.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                        imagePopup.initiatePopup(mImage.getDrawable());

                                                                    }
                                                                });


                                                                return false;
                                                            }
                                                        })
                                                        .into(mImage); //Y

                                                // imagePopup.initiatePopup(mImage.getDrawable());


                                            } else if (finalI == 2) {

                                                Glide.with(NewJobRequestActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
                                                        .listener(new RequestListener<Uri, GlideDrawable>() {
                                                            @Override
                                                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                return false;
                                                            }

                                                            @Override
                                                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                mImage2.setVisibility(View.VISIBLE);

                                                                mImage2.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                        imagePopup.initiatePopup(mImage2.getDrawable());

                                                                    }
                                                                });

                                                                return false;
                                                            }
                                                        })
                                                        .into(mImage2); //Y

                                            } else if (finalI == 3) {


                                                Glide.with(NewJobRequestActivity.this)
                                                        .load(uri) // the uri you got from Firebase
                                                        .centerCrop()
                                                        .fitCenter()
                                                        .listener(new RequestListener<Uri, GlideDrawable>() {
                                                            @Override
                                                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                                return false;
                                                            }

                                                            @Override
                                                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                                mImage3.setVisibility(View.VISIBLE);

                                                                mImage3.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View view) {

                                                                        imagePopup.initiatePopup(mImage3.getDrawable());

                                                                    }
                                                                });

                                                                return false;
                                                            }
                                                        })
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



                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference reference = mRootReference.child("Requests").child(id);
                                r[0].setAccepted(true);
                                r[0].setAssignedTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                reference.updateChildren(r[0].toFirebase(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError == null) {
                                            Toast.makeText(getApplicationContext(), "Job Accepted", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                Intent intent1 = new Intent(NewJobRequestActivity.this, HomeActivity.class);
                                startActivity(intent1);
                                btnAccept.setEnabled(false);
                            }
                        });
                        btnDecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "Job DeclinedActivity", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(NewJobRequestActivity.this, DeclinedActivity.class);
                                startActivity(intent);
                            }
                        });

                    } catch (ActivityNotFoundException e) {

                    }


                } else {
                    // Run Animation Code!
                }

            }

        }

        btnestimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewJobRequestActivity.this, EstimateJobActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}
