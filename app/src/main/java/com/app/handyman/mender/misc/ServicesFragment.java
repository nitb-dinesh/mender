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
 * For Handyman
 * This Fragment would show list of jobs assigned to the particular handyman who is logged in.
 */

public class ServicesFragment extends Fragment {


    private TextView t7, t8, t9, t10;

    public ServicesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_services, container, false);

        t7 = (TextView) v.findViewById(R.id.t7);
        t8 = (TextView) v.findViewById(R.id.t8);
        t9 = (TextView) v.findViewById(R.id.t9);
        t10 = (TextView) v.findViewById(R.id.t10);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");

        t7.setTypeface(myCustomFont);
        t8.setTypeface(myCustomFont);
        t9.setTypeface(myCustomFont);
        t10.setTypeface(myCustomFont);

        return v;
    }


}
