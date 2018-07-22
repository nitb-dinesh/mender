package com.app.handyman.mender.common.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.CustomOnItemSelectedListener;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Activity to edit user details
 */

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mUpdateProfile;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private EditText et4;
    private EditText et5;
    private Spinner et6;
    private String token, dateOfBirth, userType;

    private ProgressWheel mProgressWheel;
    private FirebaseAuth auth;

    String key = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();

        Icon_Manager icon_manager = new Icon_Manager();
        ((TextView)findViewById(R.id.tvicon)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvaddress)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvName)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvlastName)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvaddress)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvmap)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView)findViewById(R.id.tvcity)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        et1 = (EditText) findViewById(R.id.et1);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et1.setTypeface(myCustomFont);

        et2 = (EditText) findViewById(R.id.et2);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et2.setTypeface(myCustomFont1);

        et3 = (EditText) findViewById(R.id.et3);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et3.setTypeface(myCustomFont2);

        et4 = (EditText) findViewById(R.id.et4);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et4.setTypeface(myCustomFont3);

        et5 = (EditText) findViewById(R.id.et5);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et5.setTypeface(myCustomFont4);

        et6 = (Spinner) findViewById(R.id.et6);

        mUpdateProfile = (Button) findViewById(R.id.update_profile);
        Typeface myCustomFont16 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        mUpdateProfile.setTypeface(myCustomFont16);
        mUpdateProfile.setOnClickListener(this);

        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        mProgressWheel.setBarColor(Color.LTGRAY);


        getUserDetails();

    }


    // add items into spinner dynamically
    public void addItemsOnSpinner() {

        Spinner spinner = (Spinner) findViewById(R.id.et6);
        List<String> list = new ArrayList<String>();
        list.add("WA");
        list.add("HI");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.state_spinner, list);
        dataAdapter.setDropDownViewResource(R.layout.spinner_list_item);
        spinner.setAdapter(dataAdapter);
    } //

    public void addListenerOnSpinnerItemSelection() {
        Spinner spinner = (Spinner) findViewById(R.id.et6);
        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

    }
// signUpButton is "register"


    private void updateUser() {

        final String firstname = et1.getText().toString().trim();
        final String lastname = et2.getText().toString().trim();
        final String phonenumber = et3.getText().toString().trim();
        final String city = et4.getText().toString().trim();
        final String address = et5.getText().toString().trim();
        final String state = et6.getContext().toString().trim();
        System.out.println("key ::: " + key + " :::: name ::: " + firstname);

        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "Please enter your First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Please enter your Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(state)) {Toast.makeText(this, "Please chose a State", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enter your Physical Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please enter the City", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressWheel.spin();
        // ToDo : Update user details!

        final String email = auth.getCurrentUser().getEmail();
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = mRootRef.child("Users").child(key);
        // Query queryRef = userRef.child("userEmail").equalTo(email);

        // Query queryRef = gameRef.orderByChild("userEmail").equalTo(email);
        HashMap<String, Object> f = new HashMap<>();
        f.put("firstName", firstname);
        f.put("lastName", lastname);
        f.put("userEmail", email);
        f.put("cityState", city);
        f.put("key", key);
        f.put("token", token);
        f.put("dateofbirth", dateOfBirth);
        f.put("userType", userType);
        f.put("address", address);
        f.put("phonenumber", phonenumber);

//        Map<String, Object> child = new HashMap<String, Object>();

        userRef.updateChildren(f, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }

    @Override
    public void onClick(View view) {
        if (view == mUpdateProfile) {
            updateUser();
        }
    }

    private void getUserDetails() {

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
                        if (currentChild.getKey().equals("firstName")) {
                            et1.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("lastName")) {
                            et2.setText(" " + currentChild.getValue());
                        } else if (currentChild.getKey().equals("cityState")) {
                            et4.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("phonenumber")) {
                            et3.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("address")) {
                            et5.setText("" + currentChild.getValue());
                        } else if(currentChild.getKey().equalsIgnoreCase("key")) {
                            key = currentChild.getValue().toString();
                            System.out.println("key :::: " + key);
                        }
                        else if (currentChild.getKey().equals("dateofbirth")) {
                            dateOfBirth = currentChild.getValue().toString();
                        }
                        else if (currentChild.getKey().equals("token")) {
                            token =  "" + currentChild.getValue();
                        }
                        else if (currentChild.getKey().equals("userType")) {
                            userType = "" + currentChild.getValue();
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




