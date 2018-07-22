package com.app.handyman.mender.myapp.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.common.utils.Icon_Manager;
import com.app.handyman.mender.R;

public class ContactUsFragment extends Fragment {

    private EditText editemail;
    private EditText editphone;
    private EditText editthank;

    private TextView mContactUs;

    private Button submit;

    public ContactUsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_contact_us, container, false);

        Typeface myCustomFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");

        Icon_Manager icon_manager = new Icon_Manager();

        ((TextView) v.findViewById(R.id.textView1)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", getActivity()));
        ((TextView) v.findViewById(R.id.textView2)).setTypeface(icon_manager.get_icons("fonts/Icons.ttf", getActivity()));

        editemail = (EditText) v.findViewById(R.id.editemail);
        editemail.setTypeface(myCustomFont);

        editphone = (EditText) v.findViewById(R.id.editphone);
        Typeface myCustomFont1 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        editphone.setTypeface(myCustomFont1);

        editthank = (EditText) v.findViewById(R.id.editthank);
        Typeface myCustomFont2 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        editthank.setTypeface(myCustomFont2);

        submit = (Button) v.findViewById(R.id.submit);
        Typeface myCustomFont4 = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Quicksand Book.otf");
        submit.setTypeface(myCustomFont4);

        mContactUs = (TextView) v.findViewById(R.id.contact_us);
        mContactUs.setTypeface(myCustomFont);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editemail.getText().toString();
                String phone = editphone.getText().toString();
                String text = editthank.getText().toString();

                if(email.isEmpty() || phone.isEmpty() || text.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter all the details presented to you.", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    String[] to = {"customerservice@menderservices.com", ""};
                    intent.putExtra(Intent.EXTRA_EMAIL, to);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Mender Android Query");
                    intent.putExtra(Intent.EXTRA_TEXT, "Email : " + email + " \n Phone : " + phone + "\n " + text);
                    intent.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(intent, "Send email");
                    startActivity(chooser);

                }


            }
        });


        return v;
    }


}
