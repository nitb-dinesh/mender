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
 * Created by apple on 05/12/17.
 */

public class AboutUsFragment extends Fragment {

    private TextView h1;
    private TextView t1, t2, t3, t4, t5, t6, t18;

    public AboutUsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_about_us, container, false);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        Typeface myBoldFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Bold.otf");

        h1 = (TextView) v.findViewById(R.id.h1);
        t1 = (TextView) v.findViewById(R.id.t1);
        t2 = (TextView) v.findViewById(R.id.t2);
        t3 = (TextView) v.findViewById(R.id.t3);
        t4 = (TextView) v.findViewById(R.id.t4);
        t5 = (TextView) v.findViewById(R.id.t5);
        t6 = (TextView) v.findViewById(R.id.t6);
        t18 = (TextView) v.findViewById(R.id.t18);

        h1.setTypeface(myCustomFont);

        t1.setTypeface(myCustomFont);
        t2.setTypeface(myCustomFont);
        t3.setTypeface(myCustomFont);
        t4.setTypeface(myCustomFont);
        t5.setTypeface(myCustomFont);
        t6.setTypeface(myCustomFont);
        t18.setTypeface(myCustomFont);

        return v;
    }

}
