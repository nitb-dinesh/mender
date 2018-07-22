package com.app.handyman.mender.user.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import java.util.Iterator;

import com.app.handyman.mender.model.Request;

/**
 * Activity to create a new request!
 */

public class CreateNewRequestActivity extends AppCompatActivity {

    private Button mNext, mCancel;
    private EditText mJobTitle;
    private EditText mJobDescription;
    private EditText mPhoneNumber;
    private EditText mAddress, mZipCode;
    private TextView propman;
    private FirebaseAuth firebaseAuth;
    private String userEmail;
    private EditText mPropertyManagerName, mPropertyManagerPhone;

    private Toolbar toolbar;
    private TextView toolbar_title;

    private String customerId = "";

    private FirebaseAuth auth;
    private String id;

    Request request = new Request();


    private void getJobDetails(){
        DatabaseReference jobReference = FirebaseDatabase.getInstance().getReference().
                child("Requests").child(id);

        jobReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    Request request = dataSnapshot.getValue(Request.class);


                    mJobTitle.setText(request.getJobTitle());
                    mJobDescription.setText(request.getJobDescription());
                    mPhoneNumber.setText(request.getPhoneNumber());
                    mAddress.setText(request.getAddress());
                    mPropertyManagerName.setText(request.getPropertyManagerName());
                    mPropertyManagerPhone.setText(request.getPropertyManagerPhoneNumber());
                    mZipCode.setText(request.getZipCode());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_request);

        //Icon Manager
        Icon_Manager icon_manager = new Icon_Manager();
        ((TextView) findViewById(R.id.textView12)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView13)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView14)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView15)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView16)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView17)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView18)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setTypeface(myCustomFont);

        toolbar_title.setText("Create Job Request");


        //Typeface
        mJobTitle = (EditText) findViewById(R.id.job_title);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobTitle.setTypeface(myCustomFont3);

        mJobDescription = (EditText) findViewById(R.id.job_description);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mJobDescription.setTypeface(myCustomFont4);

        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        Typeface myCustomFont5 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mPhoneNumber.setTypeface(myCustomFont5);

        mAddress = (EditText) findViewById(R.id.address);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mAddress.setTypeface(myCustomFont6);

        mZipCode = (EditText) findViewById(R.id.zipcode);
        mZipCode.setTypeface(myCustomFont6);

        mNext = (Button) findViewById(R.id.next);
        Typeface myCustomFont12 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mNext.setTypeface(myCustomFont12);

        mCancel = (Button) findViewById(R.id.cancel);
        Typeface myCustomFont13 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mCancel.setTypeface(myCustomFont13);

        propman = (TextView) findViewById(R.id.propman);
        Typeface myCustomFont14 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        propman.setTypeface(myCustomFont14);

        mPropertyManagerName = (EditText) findViewById(R.id.property_manager_name);
        mPropertyManagerPhone = (EditText) findViewById(R.id.property_manager_phone);

        mPropertyManagerName.setTypeface(myCustomFont13);
        mPropertyManagerPhone.setTypeface(myCustomFont13);

        firebaseAuth = FirebaseAuth.getInstance();


        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            id = bundle.getString("id");
            getJobDetails();
        }else{
            id = "";
        }



        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity here
            userEmail = firebaseAuth.getCurrentUser().getEmail();
            getCustomerDetails();





        } else {

        }


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String jobTitle = mJobTitle.getText().toString().trim();
                final String jobDescription = mJobDescription.getText().toString().trim();
                final String phoneNumber = mPhoneNumber.getText().toString().trim();
                final String address = mAddress.getText().toString().trim();
                final String propertyManagerName = mPropertyManagerName.getText().toString().trim();
                final String propertyManagerPhone = mPropertyManagerPhone.getText().toString().trim();
                final String zipcode = mZipCode.getText().toString().trim();

                Log.d("TAGTAGATAG", jobTitle + jobDescription + phoneNumber + address + zipcode);

                if (TextUtils.isEmpty(jobTitle) || TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(address) || TextUtils.isEmpty(jobDescription) || TextUtils.isEmpty(zipcode)) {

                    Toast.makeText(CreateNewRequestActivity.this, "Please enter all the required data.", Toast.LENGTH_SHORT).show();

                } else if(phoneNumber.length() < 10) {
                    Toast.makeText(CreateNewRequestActivity.this, "Please enter phone number properly!", Toast.LENGTH_SHORT).show();
                } else {

                    request.setJobTitle(jobTitle);
                    request.setJobDescription(jobDescription);
                    request.setUserEmail(userEmail);
                    request.setPhoneNumber(phoneNumber);
                    request.setAddress(address);
                    request.setZipCode(zipcode);
                    request.setPropertyManagerName(propertyManagerName);
                    request.setPropertyManagerPhoneNumber(propertyManagerPhone);

                    Intent intent = new Intent(CreateNewRequestActivity.this, AddDetailsToNewRequestActivity.class);
                    intent.putExtra("request", request);
                    intent.putExtra("id",id );
                    startActivity(intent);

                }
            }
        });

    }

    private void getCustomerDetails() {

        auth = FirebaseAuth.getInstance();
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {

            final String email = auth.getCurrentUser().getEmail();
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gameRef = mRootRef.child("Users");
            Query queryRef = gameRef.orderByChild("userEmail").equalTo(email);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    Iterable<DataSnapshot> children = snapshot.getChildren();


                    Iterator<DataSnapshot> child = children.iterator();

                    while (child.hasNext()) {

                        DataSnapshot currentChild = child.next();
//                        key = currentChild.getKey();

                        if (currentChild.getKey().equals("customerId")) {
                            customerId = currentChild.getValue() + "";
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


}
