package com.app.handyman.mender.common.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.handyman.mender.admin.AdminConfig;
import com.app.handyman.mender.admin.fragment.AdminJobsFragment;
import com.app.handyman.mender.common.listeners.HomeSettingListener;
import com.app.handyman.mender.common.utils.DataManager;
import com.app.handyman.mender.common.utils.MySession;
import com.app.handyman.mender.handyman.fragment.MyJobsFragment;
import com.app.handyman.mender.handyman.fragment.OpenJobsFragment;
import com.app.handyman.mender.user.activity.CreateNewRequestActivity;
import com.app.handyman.mender.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.app.handyman.mender.user.adapter.RequestsAdapter;
import com.app.handyman.mender.model.Request;

import static com.app.handyman.mender.common.fragment.ProfileFragment.DUTY_OFFLINE;
import static com.app.handyman.mender.common.fragment.ProfileFragment.DUTY_ONLINE;

/**
 * Home Screen for Handyman and Clients
 * Here depending upon the user type list of requests are show or the tabs are shown!
 * If its a handyman 2 tabs of Open Jobs and My Jobs are shown!
 * If the user is a client, the list of requests by this client are shown as well as a bottom button to add a new request.
 */

public class HomeFragment extends Fragment { // implements TabLayout.OnTabSelectedListener

    private static TabLayout tabLayout;
    private ViewPager viewPager;

    private FirebaseAuth auth;

    private RecyclerView mRecList;
    private RequestsAdapter mAdapter;
    private ArrayList<Request> listOfRequests;
    private ProgressWheel mProgresWheel;

    // private FloatingActionButton mAddRequest;
    private Button mHireHandyman;
    private TextView mPostedJobs, mWelcomeText, mDescriptionText;
    private RelativeLayout mBottomRel;

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    MySession mySession;

    boolean isHandyman = false;

    Typeface typeface;

    VideoView videoView;
    public Context context;

    public static HomeSettingListener homeSettingListener = null;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        mySession = MySession.getInstance(context);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        mRecList = (RecyclerView) v.findViewById(R.id.my_requests_recyclerview);
        mBottomRel = (RelativeLayout) v.findViewById(R.id.bottom_rel);
        // mAddRequest = (FloatingActionButton) v.findViewById(R.id.create_request);
        mHireHandyman = (Button) v.findViewById(R.id.hire_handyman);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        mHireHandyman.setTypeface(typeface);

        mPostedJobs = (TextView) v.findViewById(R.id.posted_jobs);
        mPostedJobs.setTypeface(typeface);

        mWelcomeText = (TextView) v.findViewById(R.id.welcometext);
        mWelcomeText.setTypeface(typeface);

        mDescriptionText = (TextView) v.findViewById(R.id.descriptiontext);
        mDescriptionText.setTypeface(typeface);

        mProgresWheel = (ProgressWheel) v.findViewById(R.id.progress_wheel);
        mProgresWheel.setBarColor(Color.LTGRAY);

        videoView = (VideoView) v.findViewById(R.id.video_view);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");

        checkIfUserIsHandyMan();
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyJobsFragment(), "My Jobs");
        adapter.addFragment(new OpenJobsFragment(), "Open Jobs");
        viewPager.setAdapter(adapter);
    }

    public static void onReceivedTotalJobs(int count) {
        // tabLayout.getTabAt(0).setText("My Jobs (" + count + ")");
    }

    public static void onReceivedTotalOpenJobs(int count) {
        // tabLayout.getTabAt(1).setText("Open Jobs (" + count + ")");
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        checkIfUserIsHandyMan();

    }

    @Override
    public void onResume() {
        super.onResume();

        checkIfUserIsHandyMan();

    }

    private boolean checkIfUserIsHandyMan() {

//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TextView tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            tv.setTypeface(typeface);
            tabLayout.getTabAt(i).setCustomView(tv);
        }

        auth = FirebaseAuth.getInstance();
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        if (u != null) {

            DataManager.getInstance().showProgressMessage(getActivity());

            final String email = auth.getCurrentUser().getEmail();
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference gameRef = mRootRef.child("Users");
            Query queryRef = gameRef.orderByChild("userEmail").equalTo(email);

            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    DataManager.getInstance().hideProgressMessage();

                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    Iterator<DataSnapshot> child = children.iterator();

                    while (child.hasNext()) {
                        DataSnapshot currentChild = child.next();
                        if (currentChild.getKey().equals("userType")) {

                            if (currentChild.getValue().equals("Handyman")) {


                                if (email.equalsIgnoreCase(AdminConfig.ADMIN_EMAIL)) {
                                    // do something here if admin login
                                    if (homeSettingListener != null)
                                        homeSettingListener.adminSetup();

                                 /*    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                     ft.replace(R.id.frame, new AdminJobsFragment(), "Admin Jobs");
                                     ft.commit();*/

                                }

                                if (mySession.getDutyStatus().equalsIgnoreCase(DUTY_ONLINE)){
                                    try {
                                        FirebaseMessaging.getInstance().subscribeToTopic("job")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        //  String msg = getString(R.string.msg_subscribed);
                                                        if (task.isSuccessful()) {
                                                            try {
                                                                if (mySession != null)
                                                                    mySession.setDutyStatus(DUTY_ONLINE);
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                    }
                                                });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("job")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {
                                                        try {
                                                            if (mySession != null)
                                                                mySession.setDutyStatus(DUTY_OFFLINE);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }
                                            });

                                }

                                setupViewPager(viewPager);
                                tabLayout.setupWithViewPager(viewPager);
                                // Toast.makeText(getContext(), "user is handyman", Toast.LENGTH_SHORT).show();
                                isHandyman = true;

                                viewPager.setVisibility(View.VISIBLE);
                                tabLayout.setVisibility(View.VISIBLE);
                                mRecList.setVisibility(View.GONE);
                                // mAddRequest.setVisibility(View.GONE);
                                mHireHandyman.setVisibility(View.GONE);
                                mBottomRel.setVisibility(View.GONE);
                                mWelcomeText.setVisibility(View.GONE);
                                mDescriptionText.setVisibility(View.GONE);
                                videoView.setVisibility(View.GONE);
                                mPostedJobs.setVisibility(View.GONE);

                            } else {
                                if (mySession != null)
                                    mySession.setDutyStatus(DUTY_OFFLINE);

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


                                // Toast.makeText(getContext(), "user is not handyman", Toast.LENGTH_SHORT).show();
                                isHandyman = false;

                                viewPager.setVisibility(View.GONE);
                                tabLayout.setVisibility(View.GONE);
                                mRecList.setVisibility(View.VISIBLE);
                                // mAddRequest.setVisibility(View.VISIBLE);
                                mHireHandyman.setVisibility(View.VISIBLE);
                                mBottomRel.setVisibility(View.VISIBLE);
                                mPostedJobs.setVisibility(View.VISIBLE);

                                if (getActivity() != null) {

                                    MediaController mediaController = new MediaController(getContext());
                                    mediaController.setAnchorView(videoView);

                                    String path = "android.resource://com.app.handyman.mender/" + R.raw.hire_handyman_2;
                                    videoView.setMediaController(mediaController);
                                    videoView.setVideoURI(Uri.parse(path));
                                    videoView.requestFocus();
                                    videoView.start();

                                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                        @Override
                                        public void onPrepared(MediaPlayer mp) {
                                            mp.setLooping(true);
                                        }
                                    });

                                }


                                mHireHandyman.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        SharedPreferences sp = getActivity().getSharedPreferences("your_prefs", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        int myIntValue = sp.getInt("click_count", 0);

                                        if (myIntValue > 3) {
                                            startActivity(new Intent(getActivity(), CreateNewRequestActivity.class));
                                        } else {

                                            // Show user prices dialog if they are new to the app and for the first 3 times they click the hire handyman button.

                                            editor.putInt("click_count", ++myIntValue);
                                            editor.apply();

                                            final Dialog dialog = new Dialog(getActivity());
                                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog.setCancelable(false);
                                            dialog.setContentView(R.layout.dialog_check_rates);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                            final TextView mTitle = (TextView) dialog.findViewById(R.id.title);
                                            final TextView mDescription = (TextView) dialog.findViewById(R.id.description);
                                            final Button mOk = (Button) dialog.findViewById(R.id.ok);

                                            Typeface myCustomFont19 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
                                            mTitle.setTypeface(myCustomFont19);
                                            mDescription.setTypeface(myCustomFont19);
                                            mOk.setTypeface(myCustomFont19);

                                            mDescription.setText("We charge $75/hr for labor and $40/hr for store runs. By posting this job you agree to these rates. For more information about our rates, please view our Rates page");

                                            mOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    dialog.dismiss();
                                                    startActivity(new Intent(getActivity(), CreateNewRequestActivity.class));

                                                }
                                            });

                                            dialog.setCancelable(true);
                                            dialog.show();

                                        }

                                    }
                                });

                                // Toast.makeText(getActivity(), "User is a client", Toast.LENGTH_SHORT).show();
                                mProgresWheel.spin();

                                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                                mRecList.setLayoutManager(llm);

                                listOfRequests = new ArrayList<>();

                                mAdapter = new RequestsAdapter(getActivity(), listOfRequests);
                                mRecList.setAdapter(mAdapter);

                                FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();

                                if (u != null) {
                                    Query usersRequestReference = requestsReference.orderByChild("userEmail").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                                    usersRequestReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            listOfRequests.clear();
                                            mAdapter.notifyDataSetChanged();

                                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                                Request f = data.getValue(Request.class);

                                                // Toast.makeText(getActivity(), f.getJobTitle() + " " + f.getIsPaid(), Toast.LENGTH_SHORT).show();
                                                Log.i("paid", "onDataChange: " + f.getIsPaid());

                                                if (f.getIsPaid()) {
                                                    // listOfRequests.add(f);
                                                } else {
                                                    listOfRequests.add(f);
                                                }
                                                mAdapter.notifyDataSetChanged();

                                                if (listOfRequests.size() == 0) {
                                                    mWelcomeText.setVisibility(View.VISIBLE);
                                                    mDescriptionText.setVisibility(View.VISIBLE);
                                                } else {
                                                    mWelcomeText.setVisibility(View.GONE);
                                                    mDescriptionText.setVisibility(View.GONE);
                                                }

                                            }

                                            Collections.reverse(listOfRequests);

                                            mProgresWheel.stopSpinning();
                                            // Toast.makeText(getActivity(), "Got Data Now!", Toast.LENGTH_SHORT).show()
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            mProgresWheel.stopSpinning();
                                            // Toast.makeText(getContext(), "Cancelled Request", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            }
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

        return isHandyman;
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(getActivity());
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }


    public class Pager extends FragmentStatePagerAdapter {

        //integer to count number of tabs
        int tabCount;

        //Constructor to the class
        public Pager(FragmentManager fm, int tabCount) {
            super(fm);
            //Initializing tab count
            this.tabCount = tabCount;
        }

        //Overriding method getItem
        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    MyJobsFragment myJobsFragment = new MyJobsFragment();
                    return myJobsFragment;
                case 1:
                    OpenJobsFragment openJobsFragment = new OpenJobsFragment();
                    return openJobsFragment;
                default:
                    return null;
            }
        }

        //Overriden method getCount to get the number of tabs
        @Override
        public int getCount() {
            return tabCount;
        }
    }


}




