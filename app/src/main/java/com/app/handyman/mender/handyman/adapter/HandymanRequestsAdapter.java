package com.app.handyman.mender.handyman.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.handyman.mender.common.utils.Icon_Manager;
import com.app.handyman.mender.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.app.handyman.mender.model.HandymanForm;

public class HandymanRequestsAdapter extends RecyclerView.Adapter<HandymanRequestsAdapter.RV_ViewHolder> {

    private List<HandymanForm> listOfJobs;
    private Context mContext;
    View itemView;
    Icon_Manager icon_manager;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    public HandymanRequestsAdapter(Context context, ArrayList<HandymanForm> listOfJobs) {
        this.mContext = context;
        this.listOfJobs = listOfJobs;
        icon_manager = new Icon_Manager();
    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_handyman_requests_item, parent, false); // link to xml
        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RV_ViewHolder holder, int position) {
        final HandymanForm hF = listOfJobs.get(position);

        holder.mName.setText(capitalizeFirstLetter(hF.getName()));
        holder.mEmail.setText(hF.getUserEmail());
        holder.mPhone.setText(hF.getPhoneNumber());
        holder.mDetails.setText(hF.getDetails());

        holder.mTextHandyman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("smsto:" + hF.getPhoneNumber());
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "Enter your message here");
                mContext.startActivity(intent);

            }
        });

        holder.mCallHandyman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uri = "tel:" + hF.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                mContext.startActivity(intent);

            }
        });

    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Override
    public int getItemCount() {
        return listOfJobs.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout mMasterLin;
        protected TextView mName, mPhone, mEmail, mDetails;
        protected Button mCallHandyman, mTextHandyman;

        public RV_ViewHolder(View itemView) {
            super(itemView);

            mMasterLin = (LinearLayout) itemView.findViewById(R.id.master_lin);
            mName = (TextView) itemView.findViewById(R.id.name);
            mPhone = (TextView) itemView.findViewById(R.id.phone);
            mEmail = (TextView) itemView.findViewById(R.id.email);
            mDetails = (TextView) itemView.findViewById(R.id.details);
            mCallHandyman = (Button) itemView.findViewById(R.id.call_handyman);
            mTextHandyman = (Button) itemView.findViewById(R.id.text_handyman);

        }


    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}
