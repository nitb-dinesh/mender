package com.app.handyman.mender.common.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.AppUtils;
import com.app.handyman.mender.common.utils.CustomOnItemSelectedListener;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Registration Activity!
public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private TextView et4;
    private EditText et5;
    // private Spinner et6;
    private EditText et7;
    private EditText et8;
    private EditText et9;
    private EditText tvicon;
    private TextView tvaddress, photodesc;
    private TextView tvName;
    private EditText mZipCode;
    private TextView tvlastName, tvpassword, tvmap, tvemail, tvRegisterTitle, tvRegisterAs, titletext, mTermsText, mPaymentInformation;
    // private RadioGroup radioUserGroup;
    // private RadioButton radioUserButton, radioClient, radioHandyman;
    private ProgressWheel mProgressWheel;
    private FirebaseAuth firebaseAuth;

    private CheckBox mAcceptTerms;

//    Card cardToSave;
//    Context mContext;
//    Stripe stripe;
//    Token mToken;
//    String customerId;

    boolean error;
    String firstname = "";
    String lastname = "";
    String phonenumber = "";
    String city = "";
    String address = "";
    String email = "";
    String password = "";
    String confirmPassword = "";
    String zipcode = "";
    FirebaseAuth firebaseauth;
    private DatabaseReference mDatabase;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //addItemsOnSpinner();
        //addListenerOnSpinnerItemSelection();
        firebaseauth = FirebaseAuth.getInstance();

        mRegProgress = new ProgressDialog(this);
        Icon_Manager icon_manager = new Icon_Manager();
        ((TextView) findViewById(R.id.tvicon)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvaddress)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvName)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvlastName)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvaddress)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvpassword)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvmap)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvemail)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvcity)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.tvConfirmPassword)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));
        ((TextView) findViewById(R.id.textView13)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", this));

        firebaseAuth = FirebaseAuth.getInstance();

        et1 = (EditText) findViewById(R.id.et1);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et1.setTypeface(myCustomFont);

        et2 = (EditText) findViewById(R.id.et2);
        Typeface myCustomFont1 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et2.setTypeface(myCustomFont1);

        et3 = (EditText) findViewById(R.id.et3);
        Typeface myCustomFont2 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et3.setTypeface(myCustomFont2);

        et4 = (TextView) findViewById(R.id.et4);
        Typeface myCustomFont3 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et4.setTypeface(myCustomFont3);

        et5 = (EditText) findViewById(R.id.et5);
        Typeface myCustomFont4 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et5.setTypeface(myCustomFont4);

        // et6 = (Spinner) findViewById(R.id.et6);

        et7 = (EditText) findViewById(R.id.et7);
        Typeface myCustomFont6 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et7.setTypeface(myCustomFont6);

        et8 = (EditText) findViewById(R.id.et8);
        Typeface myCustomFont7 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et8.setTypeface(myCustomFont7);

        et9 = (EditText) findViewById(R.id.et9);
        Typeface myCustomFont8 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        et9.setTypeface(myCustomFont8);


        mZipCode = (EditText) findViewById(R.id.zipcode);
        mZipCode.setTypeface(myCustomFont6);


        mAcceptTerms = (CheckBox) findViewById(R.id.accept_terms);
        mTermsText = (TextView) findViewById(R.id.terms_text);

        mTermsText.setTypeface(myCustomFont8);

        mAcceptTerms.setText("");
        mTermsText.setText(Html.fromHtml("I have read and agree to the " +
                "<a href='com.app.david.mender.TermsOfUse://'>Terms of Use</a> and <a href='com.app.david.mender.TermsOfServiceActivity://'>Terms of Service</a>"));
        mTermsText.setClickable(true);
        mTermsText.setMovementMethod(LinkMovementMethod.getInstance());

        mAcceptTerms.setTypeface(myCustomFont2);

        tvRegisterAs = (TextView) findViewById(R.id.tvRegisterAs);
        Typeface myCustomFont12 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        tvRegisterAs.setTypeface(myCustomFont12);

//        radioClient = (RadioButton) findViewById(radioClient);
//        Typeface myCustomFont13 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
//        radioClient.setTypeface(myCustomFont13);
//
//        radioHandyman = (RadioButton) findViewById(radioHandyman);
//        Typeface myCustomFont14 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
//        radioHandyman.setTypeface(myCustomFont14);

        titletext = (TextView) findViewById(R.id.titletext);
        Typeface myCustomFont15 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        titletext.setTypeface(myCustomFont15);

//        photodesc = (TextView) findViewById(R.id.photodesc);
//        Typeface myCustomFont16 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
//        photodesc.setTypeface(myCustomFont16);

        signUpButton = (Button) findViewById(R.id.signUpButton);
        Typeface myCustomFont17 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");
        signUpButton.setTypeface(myCustomFont17);
        signUpButton.setOnClickListener(this);

        // radioUserGroup = (RadioGroup) findViewById(R.id.radioUserType);
        mProgressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        mProgressWheel.setBarColor(Color.LTGRAY);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
//signUpButton is "register"


    private void registerUser() {

        firstname = et1.getText().toString().trim();
        lastname = et2.getText().toString().trim();
        phonenumber = et3.getText().toString().trim();
        city = et4.getText().toString().trim();
        address = et5.getText().toString().trim();
        email = et7.getText().toString().trim();
        password = et8.getText().toString().trim();
        confirmPassword = et9.getText().toString().trim();
        zipcode = mZipCode.getText().toString().trim();


//        int selectedId = radioUserGroup.getCheckedRadioButtonId();
//        radioUserButton = (RadioButton) findViewById(selectedId);
//        final String userType = radioUserButton.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "Please enter your First Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Please enter your Last Name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phonenumber) || phonenumber.length() < 10) {
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(state)) {
//            Toast.makeText(this, "Please chose a State", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enter your Physical Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please enter the City", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(this, "Please enter Zipcode", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (TextUtils.isEmpty(userType)) {
//            Toast.makeText(this, "Please select a User Type", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (confirmPassword != password) {
//            Toast.makeText(this, "Make sure both passwords you entered are same.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (!mAcceptTerms.isChecked()) {
            Toast.makeText(this, "Please accept the terms of use and terms of service.", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (cardToSave == null) {
//            Toast.makeText(SignUp.this, "Invalid Card Data", Toast.LENGTH_LONG).show();
//            return;
//        } else {
//            validateCard();
//        }

        // ToDo : Signup User!

        mProgressWheel.spin();


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task1) {



                         /*   firebaseauth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {*/
                                   /*     @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {*/
                                            if (task1.isSuccessful()) {

                                                // Save Customer Details in Firebase Backend.
                                                DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
                                                DatabaseReference usersReference = mRootReference.child("Users");
                                                // TODO : For push notifications!
                                                // DatabaseReference handyManReference = mRootReference.child("Handyman");

                                                // Saving user details in database after registration!
                                                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                                                String key = usersReference.push().getKey();
                                                HashMap<String, Object> f = new HashMap<>();

                                                f.put("firstName", firstname);
                                                f.put("lastName", lastname);
                                                f.put("userEmail", email);
                                                f.put("cityState", city);
                                                f.put("address", address);
                                                f.put("key", key);
                                                f.put("zipcode", zipcode);
                                                f.put("userType", "Client");
                                                f.put("phonenumber", phonenumber);
                                                f.put("token", refreshedToken);
                                                //f.put("dateofbirth", state);

                                                Map<String, Object> child = new HashMap<String, Object>();
                                                child.put(key, f);

                                                Map<String, Object> map = new HashMap<>();
                                                map.put(key, refreshedToken);

                                                // TODO : For Push Notifications!
                                                // handyManReference.updateChildren(map);

                                                usersReference.updateChildren(child, new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        if (databaseError == null) {
                                                            Intent intent = new Intent(SignUp.this, HomeActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                });
                                                // Toast.makeText(SignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                                mProgressWheel.stopSpinning();


                                            }

                        else {
                            mProgressWheel.stopSpinning();
                            Toast.makeText(SignUp.this, "Could Not Complete Registration ", Toast.LENGTH_SHORT).show();
                        }


                    }


                });



    }

    private void register_user(final String display_name, final String email, String password) {

        if(!error) {
            mRegProgress.show();
            firebaseauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {


                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = current_user.getUid();

                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                        String key =  mDatabase.push().getKey();
                        String device_token = FirebaseInstanceId.getInstance().getToken();

                        HashMap<String, String> userMap = new HashMap<>();
                        userMap.put("firstName", firstname);
                        userMap.put("lastName", lastname);
                        userMap.put("userEmail", email);
                        userMap.put("cityState", city);
                        userMap.put("address", address);
                        userMap.put("key", key);
                        userMap.put("zipcode", zipcode);
                        userMap.put("userType", "Client");
                        userMap.put("phonenumber", phonenumber);
                        userMap.put("token", device_token);

                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    mRegProgress.dismiss();

                                    Intent intent = new Intent(SignUp.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(SignUp.this, " Complete Registration ", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });


                    } else {

                        mRegProgress.hide();
                        // Toast.makeText(SignUp.this, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();
                        if (!task.isSuccessful()) {
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(SignUp.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            //  message.hide();
                            return;
                        }
                    }

                }
            });
        }
        else {

        }

    }

    @Override
    public void onClick(View view) {
        if (view == signUpButton) {
           // registerUser();
            input();

            if (AppUtils.isConnected(getApplicationContext())) {
                register_user(firstname, email, password);
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void input(){
        firstname = et1.getText().toString().trim();
        lastname = et2.getText().toString().trim();
        phonenumber = et3.getText().toString().trim();
        city = et4.getText().toString().trim();
        address = et5.getText().toString().trim();
        email = et7.getText().toString().trim();
        password = et8.getText().toString().trim();
        confirmPassword = et9.getText().toString().trim();
        zipcode = mZipCode.getText().toString().trim();


//        int selectedId = radioUserGroup.getCheckedRadioButtonId();
//        radioUserButton = (RadioButton) findViewById(selectedId);
//        final String userType = radioUserButton.getText().toString();
        if (TextUtils.isEmpty(firstname)) {
            Toast.makeText(this, "Please enter your First Name", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(lastname)) {
            Toast.makeText(this, "Please enter your Last Name", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(phonenumber) || phonenumber.length() < 10) {
            Toast.makeText(this, "Please enter your Phone Number", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
//        if (TextUtils.isEmpty(state)) {
//            Toast.makeText(this, "Please chose a State", Toast.LENGTH_SHORT).show();
//            return;
//        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please enter your Physical Address", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(city)) {
            Toast.makeText(this, "Please enter the City", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your Email Address", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a Password", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }

        if (TextUtils.isEmpty(zipcode)) {
            Toast.makeText(this, "Please enter Zipcode", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
//        if (TextUtils.isEmpty(userType)) {
//            Toast.makeText(this, "Please select a User Type", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        if (confirmPassword != password) {
//            Toast.makeText(this, "Make sure both passwords you entered are same.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (!mAcceptTerms.isChecked()) {
            Toast.makeText(this, "Please accept the terms of use and terms of service.", Toast.LENGTH_SHORT).show();
            error = true;
            return;
        }
        else {
            error = false;
        }
    }
}




