package com.app.handyman.mender.handyman.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;

import com.app.handyman.mender.handyman.adapter.HandymanRequestsAdapter;
import com.app.handyman.mender.common.fragment.HomeFragment;
import com.app.handyman.mender.model.HandymanForm;

public class HandymanRequestsActivity extends AppCompatActivity {

    private RecyclerView mRecList;
    private HandymanRequestsAdapter mAdapter;
    private ArrayList<HandymanForm> listOfRequests;

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("HandymanForm");

    private ProgressWheel mProgresWheel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handyman_requests);

        mRecList = (RecyclerView) findViewById(R.id.handyman_requests_recyclerview);
        mProgresWheel = (ProgressWheel) findViewById(R.id.progress_wheel);

        mProgresWheel.setBarColor(Color.LTGRAY);

        LinearLayoutManager llm = new LinearLayoutManager(HandymanRequestsActivity.this);
        mRecList.setLayoutManager(llm);

        listOfRequests = new ArrayList<>();

        mAdapter = new HandymanRequestsAdapter(HandymanRequestsActivity.this, listOfRequests);
        mRecList.setAdapter(mAdapter);

        mProgresWheel.spin();
        requestsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfRequests.clear();
                mAdapter.notifyDataSetChanged();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HandymanForm f = data.getValue(HandymanForm.class);
                    listOfRequests.add(f);
                     mAdapter.notifyDataSetChanged();
                }

                // Log.i("size", "onDataChange: " + listOfRequests.size());
                // Toast.makeText(HandymanRequestsActivity.this, "Size : " + listOfRequests.size(), Toast.LENGTH_SHORT).show();Confim

                mProgresWheel.stopSpinning();
                HomeFragment.onReceivedTotalJobs(listOfRequests.size());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(HandymanRequestsActivity.this, "Cancelled Request " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgresWheel.stopSpinning();
            }
        });

    }
}
