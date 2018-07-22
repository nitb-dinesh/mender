package com.app.handyman.mender.misc;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.handyman.mender.R;

/**
 * Created by apple on 13/12/17.
 */

public class TermsAndConditionsFragment extends Fragment {

    private TextView t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11,t12;

    public TermsAndConditionsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        // Typeface myBoldFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Bold.otf");

        t1 = (TextView) v.findViewById(R.id.t1);
        t2 = (TextView) v.findViewById(R.id.t2);
        t3 = (TextView) v.findViewById(R.id.t3);
        t4 = (TextView) v.findViewById(R.id.t4);
        t5 = (TextView) v.findViewById(R.id.t5);
        t6 = (TextView) v.findViewById(R.id.t6);
        t7 = (TextView) v.findViewById(R.id.t7);
        t8 = (TextView) v.findViewById(R.id.t8);
        t9 = (TextView) v.findViewById(R.id.t9);
        t10 = (TextView) v.findViewById(R.id.t10);
        t11 = (TextView) v.findViewById(R.id.t11);
        t12 = (TextView) v.findViewById(R.id.t12);

        t1.setTypeface(myCustomFont);
        t2.setTypeface(myCustomFont);
        t3.setTypeface(myCustomFont);
        t4.setTypeface(myCustomFont);
        t5.setTypeface(myCustomFont);
        t6.setTypeface(myCustomFont);
        t7.setTypeface(myCustomFont);
        t8.setTypeface(myCustomFont);
        t9.setTypeface(myCustomFont);
        t10.setTypeface(myCustomFont);
        t11.setTypeface(myCustomFont);
        t12.setTypeface(myCustomFont);

        return v;
    }

}
