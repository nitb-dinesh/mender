package com.app.handyman.mender.common.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.EditProfileActivity;
import com.app.handyman.mender.common.utils.DataManager;
import com.app.handyman.mender.common.utils.MySession;
import com.app.handyman.mender.model.Request;
import com.app.handyman.mender.user.adapter.RequestsAdapter;
import com.app.handyman.mender.user.fragment.PaymentMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Fragment to manage user profile!
 */

public class ProfileFragment extends Fragment {

    public static final String DUTY_OFFLINE = "10";
    public static final String DUTY_ONLINE = "11" ;
    private static final String TAG ="ProfileFragment" ;
    private static final String HANDYMAN = "Handyman" ;
    private RecyclerView mRecList;
    private RequestsAdapter mAdapter;
    private ArrayList<Request> listOfRequests = new ArrayList<>();
    private Button paymentmethods;
    private TextView mName, mEmail, mPhoneNumber, mAddress, previousjobs, previousjobs2, mCityAndState;
    private ProgressBar progressBar;

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    private RelativeLayout   availabilityHandyRL;

    private Button mEditProfile;

    private FirebaseAuth auth;

    private MySession mySession;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mySession = MySession.getInstance(getActivity());

        // Setup Widgets
        mRecList = (RecyclerView) v.findViewById(R.id.my_requests_recyclerview);

        mName = (TextView) v.findViewById(R.id.name);
        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mName.setTypeface(myCustomFont);


        mEmail = (TextView) v.findViewById(R.id.email);
        Typeface myCustomFont1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mEmail.setTypeface(myCustomFont1);

        mPhoneNumber = (TextView) v.findViewById(R.id.phone_number);
        Typeface myCustomFont2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mPhoneNumber.setTypeface(myCustomFont2);

        mAddress = (TextView) v.findViewById(R.id.address);
        Typeface myCustomFont3 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mAddress.setTypeface(myCustomFont3);

        mCityAndState = (TextView) v.findViewById(R.id.city_and_state);
        Typeface myCustomFont4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mCityAndState.setTypeface(myCustomFont4);

        mEditProfile = (Button) v.findViewById(R.id.edit_profile);
        Typeface myCustomFont5 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mEditProfile.setTypeface(myCustomFont5);

        paymentmethods = (Button) v.findViewById(R.id.paymentmethods);
        Typeface myCustomFont7 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        paymentmethods.setTypeface(myCustomFont7);

        previousjobs = (TextView) v.findViewById(R.id.previousjobs);
        Typeface myCustomFont6 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        previousjobs.setTypeface(myCustomFont6);

        previousjobs2 = (TextView) v.findViewById(R.id.previousjobs2);
        previousjobs2.setTypeface(myCustomFont6);

        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);

        paymentmethods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), PaymentMethods.class);
                startActivity(intent);

            }
        });

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);

            }
        });


        // Getting Users Jobs List.
        try {
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            mRecList.setLayoutManager(llm);

            listOfRequests = new ArrayList<>();

            mAdapter = new RequestsAdapter(getActivity(), listOfRequests);
            mRecList.setAdapter(mAdapter);


            getUserDetails();
        } catch (Exception e) {
            e.printStackTrace();
        }



        // switch setup

       final TextView titletext =  v.findViewById(R.id.availableTV);
       availabilityHandyRL =  v.findViewById(R.id.availabilityHandyRL);
        Typeface myCustomFontAb = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        titletext.setTypeface(myCustomFontAb);

        Switch dutyStatusSwt = (Switch) v. findViewById(R.id.availableSwt);

        try {
            dutyStatusSwt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        // new ChgStatus().execute(); on
                       // titletext.setText(R.string.availability);
                       // mySession.setDutyStatus(DUTY_ONLINE);
                       // FirebaseMessaging.getInstance().subscribeToTopic("handyman");

                        FirebaseMessaging.getInstance().subscribeToTopic("job")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                     /*   String msg = getString(R.string.msg_subscribed);
                                        if (!task.isSuccessful()) {
                                            msg = getString(R.string.msg_subscribe_failed);
                                        }
                                        Log.d(TAG, msg);*/
                                       // Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                        if (task.isSuccessful()) {
                                            try {
                                                mySession.setDutyStatus(DUTY_ONLINE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                    }
                                });
                    }else {
                       // updateDutyStatus(DUTY_OFFLINE);
                        //unsubscribeFromTopic
                       // mySession.setDutyStatus(DUTY_OFFLINE);
                       // titletext.setText(R.string.availability);
                        try {
                            mySession.setDutyStatus(DUTY_OFFLINE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        FirebaseMessaging.getInstance().unsubscribeFromTopic("job")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                  /*      String msg = getString(R.string.msg_unsubscribed);
                                        if (!task.isSuccessful()) {
                                            msg = getString(R.string.msg_unsubscribe_failed);
                                        }
                                        Log.d(TAG, msg);*/
                                        if (task.isSuccessful()) {
                                            try {
                                                mySession.setDutyStatus(DUTY_OFFLINE);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        // Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    }
                                });


                    }


                    /*
                    * if (s.equalsIgnoreCase(DUTY_OFFLINE)){
                                    MySession.getInstance(getApplicationContext()).setDutyStatus(DUTY_OFFLINE);
                                }
                                else if (s.equalsIgnoreCase(DUTY_ONLINE)){
                                    MySession.getInstance(getApplicationContext()).setDutyStatus(DUTY_ONLINE);
                                }*/

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (( MySession.getInstance(getActivity()).getDutyStatus()).equalsIgnoreCase(DUTY_OFFLINE)){
            dutyStatusSwt.setChecked(false);
           // titletext.setText(R.string.offline);
           // updateDutyStatus(DUTY_OFFLINE);
        }
        else if (( MySession.getInstance(getActivity()).getDutyStatus()).equalsIgnoreCase(DUTY_ONLINE)){
            dutyStatusSwt.setChecked(true);
           // titletext.setText(R.string.online);
           // updateDutyStatus(DUTY_ONLINE);
        }


        return v;
    }




    private void getJobRequests() {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            Query usersRequestReference = requestsReference.orderByChild("userEmail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

            usersRequestReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot == null) return;

                    listOfRequests.clear();
                    mAdapter.notifyDataSetChanged();

                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Request f = data.getValue(Request.class);
                        listOfRequests.add(f);
                        mAdapter.notifyDataSetChanged();
                    }

                    if (listOfRequests.size() == 0) {
                        previousjobs2.setVisibility(View.VISIBLE);
                    } else {
                        previousjobs2.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Toast.makeText(getContext(), "Cancelled Request", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void getUserDetails() {

        DataManager.getInstance().showProgressMessage(getActivity());
        auth = FirebaseAuth.getInstance();
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {
            try {
                getJobRequests();
            } catch (Exception e) {
                e.printStackTrace();
            }

            final String email = auth.getCurrentUser().getEmail();
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gameRef = mRootRef.child("Users");
            Query queryRef = gameRef.orderByChild("userEmail").equalTo(email);
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    DataManager.getInstance().hideProgressMessage();
                    if (snapshot == null) return;
                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    progressBar.setVisibility(View.GONE);

                    if (children == null) return;
                    Iterator<DataSnapshot> child = children.iterator();

                    while (child.hasNext()) {


                        DataSnapshot currentChild = child.next();
                        if(currentChild == null) continue;
                        if (currentChild.getKey().equals("firstName")) {
                            mName.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("lastName")) {
                            String firstName = mName.getText().toString();
                            mName.setText(""+firstName + " " + currentChild.getValue());
                        } else if (currentChild.getKey().equals("address")) {
                            mAddress.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("cityState")) {
                            mCityAndState.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("userEmail")) {
                            mEmail.setText("" + currentChild.getValue());
                        } else if (currentChild.getKey().equals("phonenumber")) {
                            mPhoneNumber.setText("" + currentChild.getValue());
                        }else if (currentChild.getKey().equals("userType")) {
                           // mPhoneNumber.setText("" + currentChild.getValue());
                            if ((""+currentChild.getValue()).equalsIgnoreCase(HANDYMAN)){
                                availabilityHandyRL.setVisibility(View.VISIBLE);
                            }else {
                                availabilityHandyRL.setVisibility(View.GONE);
                            }
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

}
