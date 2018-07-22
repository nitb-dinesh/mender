package com.app.handyman.mender.handyman.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.fragment.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;

import com.app.handyman.mender.handyman.adapter.OpenJobsAdapter;
import com.app.handyman.mender.model.Request;

/**
 * For Handyman
 * This Fragment would show list of jobs which are unassigned and up for grabs for the handyman!
 */

public class OpenJobsFragment extends Fragment {

    private RecyclerView mRecList;
    private OpenJobsAdapter mAdapter;
    private ArrayList<Request> listOfRequests;

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressWheel mProgresWheel;

    public OpenJobsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_open_jobs, container, false);

        mRecList = (RecyclerView) v.findViewById(R.id.open_jobs_recyclerview);

        mProgresWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        mProgresWheel.setBarColor(Color.LTGRAY);
        mProgresWheel.spin();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecList.setLayoutManager(llm);

        listOfRequests = new ArrayList<>();

        mAdapter = new OpenJobsAdapter(getActivity(), listOfRequests);
        mRecList.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                reloadList();
            }
        });

        return v;
    }

    private void getJobs() {

        Query unassignedJobsReference = requestsReference.orderByChild("assignedTo").equalTo("");

        unassignedJobsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfRequests.clear();
                mAdapter.notifyDataSetChanged();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Request f = data.getValue(Request.class);
                    listOfRequests.add(f);
                    // mAdapter.notifyDataSetChanged();
                }

                Collections.reverse(listOfRequests);
                mAdapter.notifyDataSetChanged();

                HomeFragment.onReceivedTotalOpenJobs(listOfRequests.size());
                mProgresWheel.stopSpinning();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(getActivity(), "Cancelled Request", Toast.LENGTH_SHORT).show();
                mProgresWheel.stopSpinning();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        getJobs();

    }
    private void reloadList() {
        Query unassignedJobsReference = requestsReference.orderByChild("assignedTo").equalTo("");

        unassignedJobsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfRequests.clear();
                mAdapter.notifyDataSetChanged();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Request f = data.getValue(Request.class);
                    listOfRequests.add(f);
                }

                onItemsLoadComplete();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(getActivity(), "Cancelled Request", Toast.LENGTH_SHORT).show();
                onItemsLoadComplete();
            }
        });
    }

    void onItemsLoadComplete() {
        // Update the com.app.handyman.mender.adapter and notify data set changed

        Collections.reverse(listOfRequests);
        mAdapter.notifyDataSetChanged();

        mProgresWheel.stopSpinning();
        HomeFragment.onReceivedTotalOpenJobs(listOfRequests.size());

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);

    }

}
