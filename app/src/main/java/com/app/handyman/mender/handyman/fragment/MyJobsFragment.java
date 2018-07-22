package com.app.handyman.mender.handyman.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.fragment.HomeFragment;
import com.app.handyman.mender.handyman.adapter.MyJobsAdapter;
import com.app.handyman.mender.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * For Handyman
 * This Fragment would show list of jobs assigned to the particular handyman who is logged in.
 */

public class MyJobsFragment extends Fragment {

    private RecyclerView mRecList;
    private MyJobsAdapter mAdapter;
    private ArrayList<Request> listOfRequests;
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    private ProgressWheel mProgresWheel;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mNoJobs, mNoJobs2;


    public MyJobsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_jobs, container, false);

        mRecList = (RecyclerView) v.findViewById(R.id.my_jobs_recyclerview);

        mProgresWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        mProgresWheel.setBarColor(Color.LTGRAY);

        mNoJobs = (TextView) v.findViewById(R.id.no_jobs);
        mNoJobs2 = (TextView) v.findViewById(R.id.no_jobs_WELCOME);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mNoJobs.setTypeface(myCustomFont);
        mNoJobs2.setTypeface(myCustomFont);


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRecList.setLayoutManager(llm);

        listOfRequests = new ArrayList<>();

        mAdapter = new MyJobsAdapter(getActivity(), listOfRequests);
        mRecList.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                reloadList();
            }
        });


        getJobs();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getJobs();

    }

    private void getJobs() {

        Query unassignedJobsReference = requestsReference.orderByChild("assignedTo").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        mProgresWheel.spin();
        unassignedJobsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfRequests.clear();
                mAdapter.notifyDataSetChanged();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Request f = data.getValue(Request.class);

                    Log.d("COmpletedCOmp", f.isStatus() + "");
                    if(!f.isStatus()){
                        listOfRequests.add(f);
                    }

                    // mAdapter.notifyDataSetChanged();
                }

                Collections.reverse(listOfRequests);
                mAdapter.notifyDataSetChanged();

                mProgresWheel.stopSpinning();
                HomeFragment.onReceivedTotalJobs(listOfRequests.size());


                if(listOfRequests.size() == 0) {
                    mNoJobs.setVisibility(View.VISIBLE);
                    mNoJobs2.setVisibility(View.VISIBLE);
                } else {
                    mNoJobs.setVisibility(View.GONE);
                    mNoJobs2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Toast.makeText(getActivity(), "Cancelled Request", Toast.LENGTH_SHORT).show();
                mProgresWheel.stopSpinning();
            }
        });

    }

    private void reloadList() {
        Query unassignedJobsReference = requestsReference.orderByChild("assignedTo").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        unassignedJobsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOfRequests.clear();
                mAdapter.notifyDataSetChanged();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Request f = data.getValue(Request.class);

                    if(!f.isStatus()) {
                        listOfRequests.add(f);
                    }

                }

                mProgresWheel.stopSpinning();
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

        HomeFragment.onReceivedTotalJobs(listOfRequests.size());

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public interface OnReceivedTotalJobs {
        void onReceivedTotalJobs(int count);
    }

}
