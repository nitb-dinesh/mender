package com.app.handyman.mender.common.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.handyman.mender.R;

/**
 * For Handyman
 * This Fragment would show list of jobs assigned to the particular handyman who is logged in.
 */

public class RatesFragment extends Fragment {


    private TextView h3;
    private TextView t11, t12, t13, t14, t15, t16, t17, t18;

    public RatesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rates, container, false);

        h3 = (TextView) v.findViewById(R.id.h3);

        t11 = (TextView) v.findViewById(R.id.t11);
        t12 = (TextView) v.findViewById(R.id.t12);
        t13 = (TextView) v.findViewById(R.id.t13);
        t14 = (TextView) v.findViewById(R.id.t14);
        t15 = (TextView) v.findViewById(R.id.t15);
        t16 = (TextView) v.findViewById(R.id.t16);
        t17 = (TextView) v.findViewById(R.id.t17);
        t18 = (TextView) v.findViewById(R.id.t18);

        Typeface myCustomFont = Typeface.createFromAsset (getActivity().getAssets(), "fonts/Quicksand Book.otf");
        Typeface myCustomFont2 = Typeface.createFromAsset (getActivity().getAssets(), "fonts/Quicksand Bold Oblique.otf");

        h3.setTypeface(myCustomFont);

        t11.setTypeface(myCustomFont);
        t12.setTypeface(myCustomFont2);
        t13.setTypeface(myCustomFont);
        t14.setTypeface(myCustomFont2);
        t15.setTypeface(myCustomFont);
        t16.setTypeface(myCustomFont2);
        t17.setTypeface(myCustomFont);
        t18.setTypeface(myCustomFont);

        return v;
    }


}
