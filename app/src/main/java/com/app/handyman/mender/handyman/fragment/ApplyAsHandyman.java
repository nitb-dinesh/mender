package com.app.handyman.mender.handyman.fragment;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.handyman.activity.HandymanRequestsActivity;
import com.app.handyman.mender.common.activity.HomeActivity;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.app.handyman.mender.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import com.app.handyman.mender.model.HandymanForm;

/**
 * Fragment to manage user profile!
 */

public class ApplyAsHandyman extends Fragment {

    private EditText mHandymanName, mHandymanPhone, mHandymanEmail, mHandymanInfo, mEnterCode;
    private TextView mInterestedTV, mWelcomeTextTV, textView1, textView2, textView3, contractor; // mContractorsInformation
    private Button mApplyAsHandyman, mSeeRequests;
    private AssetManager assets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_apply_as_handyman, container, false);

        Icon_Manager icon_manager = new Icon_Manager();
        ((TextView) v.findViewById(R.id.textView1)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", getActivity()));
        ((TextView) v.findViewById(R.id.textView2)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", getActivity()));
        ((TextView) v.findViewById(R.id.textView3)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", getActivity()));

        // mApply = (TextView) v.findViewById(R.id.apply);
        mHandymanName = (EditText) v.findViewById(R.id.handyman_name);
        mHandymanPhone = (EditText) v.findViewById(R.id.handyman_phone);
        mHandymanEmail = (EditText) v.findViewById(R.id.handyman_email);
        mHandymanInfo = (EditText) v.findViewById(R.id.handyman_info);
        mEnterCode = (EditText) v.findViewById(R.id.enter_code);
        mSeeRequests = (Button) v.findViewById(R.id.see_requests);
        mApplyAsHandyman = (Button) v.findViewById(R.id.apply_as_handyman);
        textView1 = (TextView) v.findViewById(R.id.textView1);
        textView2 = (TextView) v.findViewById(R.id.textView2);
        textView3 = (TextView) v.findViewById(R.id.textView3);
        mInterestedTV = (TextView) v.findViewById(R.id.interested);
        mWelcomeTextTV = (TextView) v.findViewById(R.id.welcometext);
        contractor = (TextView) v.findViewById(R.id.contractor);

        Typeface customFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        // mApply.setTypeface(customFont);
        mHandymanName.setTypeface(customFont);
        mHandymanPhone.setTypeface(customFont);
        mHandymanEmail.setTypeface(customFont);
        mHandymanInfo.setTypeface(customFont);
        mEnterCode.setTypeface(customFont);
        mSeeRequests.setTypeface(customFont);
        mApplyAsHandyman.setTypeface(customFont);

        mInterestedTV.setTypeface(customFont);
        mWelcomeTextTV.setTypeface(customFont);
        contractor.setTypeface(customFont);

        DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference handymanFormReference = mRootReference.child("HandymanForm");
        final String key = handymanFormReference.push().getKey();




        mEnterCode.setVisibility(View.GONE);
        mSeeRequests.setVisibility(View.GONE);

        mSeeRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = mEnterCode.getText().toString();

                if(code.equals("secretarea")) {
                    Intent intent = new Intent(getActivity(), HandymanRequestsActivity.class);
                    startActivity(intent);
                }

            }
        });

        mApplyAsHandyman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = mHandymanName.getText().toString().trim();
                String phone = mHandymanPhone.getText().toString().trim();
                String email = mHandymanEmail.getText().toString().trim();
                String info = mHandymanInfo.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || info.isEmpty()) {
                    Toast.makeText(getActivity(), "Make sure no fields are empty!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Request Successful, we'll reach out to you soon!", Toast.LENGTH_SHORT).show();

                    HandymanForm hF = new HandymanForm();
                    hF.setName(name);
                    hF.setPhoneNumber(phone);
                    hF.setUserEmail(email);
                    hF.setDetails(info);

                    Map<String, Object> child = new HashMap<String, Object>();
                    child.put(key, hF.toFirebase());

                    handymanFormReference.updateChildren(child, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null) {


                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);

                            } else {

                                Toast.makeText(getActivity(), "Error uploading form.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                }


            }
        });


        return v;
    }


    public AssetManager getAssets() {
        return assets;
    }
}
