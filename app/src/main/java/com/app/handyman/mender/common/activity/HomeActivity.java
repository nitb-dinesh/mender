package com.app.handyman.mender.common.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.admin.fragment.AdminJobsFragment;
import com.app.handyman.mender.common.fragment.HomeFragment;
import com.app.handyman.mender.common.fragment.ProfileFragment;
import com.app.handyman.mender.common.fragment.RatesFragment;
import com.app.handyman.mender.common.listeners.HomeSettingListener;
import com.app.handyman.mender.common.utils.CustomTypefaceSpan;
import com.app.handyman.mender.handyman.activity.NewJobRequestActivity;
import com.app.handyman.mender.myapp.fragment.ContactUsFragment;
import com.app.handyman.mender.myapp.onboarding.WelcomeOneAvtivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Home Activity of the app! First screen after user logs in!
 * Shows Navigation Drawer, initially which takes the user to the HomeFragment
 */

public class HomeActivity extends AppCompatActivity {

    private static final String MY_PREFS_NAME = "Preferences";
    private static final String HAS_WATCHED_TUTORIAL = "HasWatchedTutorial";

    String id = "";

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAnalytics mFirebaseAnalytics;

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // For Push Notification.
        if (getIntent().getExtras() == null) {
             //Toast.makeText(this, "Error in intents! (Contact Developer)", Toast.LENGTH_SHORT).show();
        } else {

            Bundle extras = getIntent().getExtras();
            id = extras.getString("id");
            // Toast.makeText(this, "Url : " + url, Toast.LENGTH_SHORT).show();

            if (id != null) {
                if (!id.isEmpty()) {
                    try {
                        Intent intent = new Intent(HomeActivity.this, NewJobRequestActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                    }
                } else {
                    // Run Animation Code!
                }
            }
        }

        hasActiveInternetConnection();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Home screen - 1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home screen - 2");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Main Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        load();
//
//    }

    private void load() {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        boolean hasWatchedTutorial = prefs.getBoolean(HAS_WATCHED_TUTORIAL, false);

        // Toast.makeText(this, "Watched : " + hasWatchedTutorial, Toast.LENGTH_SHORT).show();

        if (!hasWatchedTutorial) {
            goToOnboardingActivity();
        } else {

            auth = FirebaseAuth.getInstance();

            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();

                    if (u == null) {

                        Toast.makeText(HomeActivity.this, "Please Sign Up / Login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();

                    } else {
                       // Toast.makeText(HomeActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();

                        // Initializing Toolbar and setting it as the actionbar
                        toolbar = (Toolbar) findViewById(R.id.toolbar);
                        setSupportActionBar(toolbar);
                        getSupportActionBar().setTitle("");

                        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
                        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                        toolbar_title.setTypeface(myCustomFont);

                        //Initializing NavigationView
                        navigationView = (NavigationView) findViewById(R.id.navigation_view);
                        // Initializing Drawer Layout and ActionBarToggle
                        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

                        HomeSettingListener homeSettingListener = new HomeSettingListener() {
                            @Override
                            public void adminSetup() {

                               // navigationView = (NavigationView) findViewById(R.id.nav_view);
                                Menu nav_Menu = navigationView.getMenu();
                                nav_Menu.findItem(R.id.admin).setVisible(true);
                            }
                        };
                        HomeFragment.homeSettingListener = homeSettingListener;

                        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
                        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                            // This method will trigger on item Click of navigation menu
                            @Override
                            public boolean onNavigationItemSelected(MenuItem menuItem) {


                                if (menuItem.isChecked()) menuItem.setChecked(false);
                                else menuItem.setChecked(true);

                                drawerLayout.closeDrawers();

                                switch (menuItem.getItemId()) {

                                    case R.id.home:
                                        toolbar_title.setText("Home");

                                        HomeFragment fragment = new HomeFragment();
                                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        fragmentTransaction.replace(R.id.frame, fragment);
                                        // fragmentTransaction.addToBackStack("a");
                                        fragmentTransaction.commit();
                                        return true;

                                    case R.id.profile:
                                        toolbar_title.setText("Account");
                                        ProfileFragment profileFragment = new ProfileFragment();
                                        android.support.v4.app.FragmentTransaction profileFragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        profileFragmentTransaction.replace(R.id.frame, profileFragment);
                                        profileFragmentTransaction.commit();
//                        Toast.makeText(getApplicationContext(),"Pending : Profile!",Toast.LENGTH_SHORT).show();
//                        Intent intent1 = new Intent(HomeActivity.this, UserProfile.class);
//                        startActivity(intent1);
                                        return true;

                                    case R.id.admin:

                                        // Toast.makeText(getApplicationContext(), "Pending : About Us", Toast.LENGTH_SHORT).show();
                                        toolbar_title.setText("Admin Section");

                                        AdminJobsFragment adminJobsFragment = new AdminJobsFragment();
                                        android.support.v4.app.FragmentTransaction aboutUSFragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        aboutUSFragmentTransaction.replace(R.id.frame, adminJobsFragment);
                                        aboutUSFragmentTransaction.commit();
                                        return true;

//                                    case R.id.apply_as_handyman:
//                                        toolbar_title.setText("Apply as Handyman");
//
//                                        ApplyAsHandyman applyAsHandymanFragment = new ApplyAsHandyman();
//                                        android.support.v4.app.FragmentTransaction applyAsHandymanFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                                        applyAsHandymanFragmentTransaction.replace(R.id.frame, applyAsHandymanFragment);
//                                        applyAsHandymanFragmentTransaction.commit();
//                                        return true;
//
                                    case R.id.rates:
                                        toolbar_title.setText("Rates");

                                        RatesFragment ratesFragment = new RatesFragment();
                                        android.support.v4.app.FragmentTransaction ratesFragmentTransaction = getSupportFragmentManager().beginTransaction();
                                        ratesFragmentTransaction.replace(R.id.frame, ratesFragment);
                                        ratesFragmentTransaction.commit();
                                        return true;
//
//                                    case R.id.services:
//                                        toolbar_title.setText("Services Provided");
//
//                                        ServicesFragment servicesFragment = new ServicesFragment();
//                                        android.support.v4.app.FragmentTransaction servicesFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                                        servicesFragmentTransaction.replace(R.id.frame, servicesFragment);
//                                        servicesFragmentTransaction.commit();
//                                        return true;

                                    case R.id.contact_us:
                                        toolbar_title.setText("Contact Us");

                                        ContactUsFragment contactUsFragment = new ContactUsFragment();
                                        android.support.v4.app.FragmentTransaction contactUsFragmenttransaction = getSupportFragmentManager().beginTransaction();
                                        contactUsFragmenttransaction.replace(R.id.frame, contactUsFragment);
                                        contactUsFragmenttransaction.commit();
                                        return true;

                                    /*case R.id.terms_and_conditions:

                                        // Toast.makeText(getApplicationContext(), "Pending : About Us", Toast.LENGTH_SHORT).show();
                                        toolbar_title.setText("Terms of use");

                                        TermsAndConditionsFragment termsAndConditionsFragment = new TermsAndConditionsFragment();
                                        android.support.v4.app.FragmentTransaction termsAndConditionsTransaction = getSupportFragmentManager().beginTransaction();
                                        termsAndConditionsTransaction.replace(R.id.frame, termsAndConditionsFragment);
                                        termsAndConditionsTransaction.commit();
                                        return true;*/

                                    case R.id.share:

                                        try {
                                            Intent i = new Intent(Intent.ACTION_SEND);
                                            i.setType("text/plain");
                                            i.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                                            String sAux = "\n" + "Mender Handyman Services, Download Now. " + "\n";
                                            sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName();
                                            i.putExtra(Intent.EXTRA_TEXT, sAux);
                                            startActivity(Intent.createChooser(i, getString(R.string.share)));
                                        } catch (Exception e) {
                                            e.toString();
                                        }

                                        return true;
                                    case R.id.logout:



                                        final Dialog dialog = new Dialog(HomeActivity.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setCancelable(false);
                                        dialog.setContentView(R.layout.dialog_confirm_logout);
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                                        final TextView mLogoutTitle = (TextView) dialog.findViewById(R.id.logout_title);
                                        final Button mConfirmLogout = (Button) dialog.findViewById(R.id.logout);

                                        Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
                                        mLogoutTitle.setTypeface(myCustomFont19);
                                        mConfirmLogout.setTypeface(myCustomFont19);

                                        mConfirmLogout.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {


                                                FirebaseMessaging.getInstance().unsubscribeFromTopic("job")
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                String msg = getString(R.string.msg_unsubscribed);
                                                                if (!task.isSuccessful()) {
                                                                    msg = getString(R.string.msg_unsubscribe_failed);
                                                                }
                                                                //Log.d(TAG, msg);
                                                               // Toast.makeText(HomeActivity.this, msg, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                dialog.dismiss();
                                                FirebaseAuth.getInstance().signOut();

                                                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }
                                        });

                                        dialog.setCancelable(true);
                                        dialog.show();

                                        return true;
                                    default:
                                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                                        return true;

                                }
                            }
                        });

                        navigationView.getMenu().getItem(0).setChecked(true);
                        navigationView.setCheckedItem(R.id.home);

                        Menu m = navigationView.getMenu();
                        for (int i=0;i<m.size();i++) {
                            MenuItem mi = m.getItem(i);

                            //for aapplying a font to subMenu ...
                            SubMenu subMenu = mi.getSubMenu();
                            if (subMenu!=null && subMenu.size() >0 ) {
                                for (int j=0; j <subMenu.size();j++) {
                                    MenuItem subMenuItem = subMenu.getItem(j);
                                    applyFontToMenuItem(subMenuItem);
                                }
                            }

                            //the method we have create in activity
                            applyFontToMenuItem(mi);
                        }

                        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

                            @Override
                            public void onDrawerClosed(View drawerView) {
                                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                                super.onDrawerClosed(drawerView);
                            }

                            @Override
                            public void onDrawerOpened(View drawerView) {
                                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                                super.onDrawerOpened(drawerView);
                            }
                        };

                        //Setting the actionbarToggle to drawer layout
                        drawerLayout.setDrawerListener(actionBarDrawerToggle);

                        //calling sync state is necessay or else your hamburger icon wont show up
                        actionBarDrawerToggle.syncState();


                        if(!isFinishing()) {
                            HomeFragment fragment = new HomeFragment();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }
            };
        }

    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        //No call for super(). Bug on API Level > 11.
//    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        // toolbar_title.setText("Home");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(toolbar_title != null) {

            toolbar_title.setText("Home");
        }

        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }

    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();

    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Toast.makeText(HomeActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(HomeActivity.this, "No Network Available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void goToOnboardingActivity() {

        Intent intent = new Intent(HomeActivity.this, WelcomeOneAvtivity.class);
        startActivity(intent);
        finish();

    }


}
