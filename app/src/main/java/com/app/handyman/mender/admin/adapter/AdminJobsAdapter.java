package com.app.handyman.mender.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.Icon_Manager;
import com.app.handyman.mender.handyman.activity.MyJobDetailsActivity;
import com.app.handyman.mender.model.Request;
import com.app.handyman.mender.user.activity.RequestDetailsActivity;
import com.bumptech.glide.Glide;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminJobsAdapter extends RecyclerView.Adapter<AdminJobsAdapter.RV_ViewHolder> {

    private List<Request> requestList;
    private Context mContext;
    View itemView;
    Icon_Manager icon_manager;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReferenceFromUrl("gs://mender-49a62.appspot.com");

    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference requestsReference = mRootReference.child("Requests");

    public AdminJobsAdapter(Context context, ArrayList<Request> requestList) {
        this.mContext = context;
        this.requestList = requestList;
        icon_manager = new Icon_Manager();

    }

    @Override
    public RV_ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_my_requests_item, parent, false); // link to xml
        return new RV_ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RV_ViewHolder holder, int position) {
        final Request request = requestList.get(position);

        holder.mJobTitle.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", mContext));
        holder.mJobDescription.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", mContext));
        holder.mJobStatus.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", mContext));
        holder.mJobLocation.setTypeface(icon_manager.get_icons("fonts/Icons.ttf", mContext));

        holder.mJobTitle.setText(capitalizeFirstLetter(request.getJobTitle()));
        holder.mJobDescription.setText(capitalizeFirstLetter(request.getJobDescription()));
        holder.mJobLocation.setText(capitalizeFirstLetter(request.getAddress()));




        final String id = request.getKey();

        holder.mMasterLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent intent = new Intent(mContext, RequestDetailsActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);*/
                Intent intent = new Intent(mContext, MyJobDetailsActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });

        try {

            Log.i("TANNIREQUEST", request.toString());

            storageReference.child("images/" + id + "/" + 1).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    System.out.println(uri);
                    Log.i("image", "onBindViewHolder: " + uri);


                    Glide.with(mContext)
                            .load(uri) // the uri you got from Firebase
                            .centerCrop()
                            .fitCenter()
                            .into(holder.mRequestImage);
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("image", "onBindViewHolder: " + e.getMessage());
        }

        final ImagePopup imagePopup = new ImagePopup(mContext);
        imagePopup.setWindowHeight(2500); // Optional
        imagePopup.setWindowWidth(1500); // Optional
        imagePopup.setBackgroundColor(Color.BLACK);  // Optional
        imagePopup.setHideCloseIcon(true);  // Optional
        imagePopup.setImageOnClickClose(true);  // Optional

        holder.mRequestImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePopup.initiatePopup(holder.mRequestImage.getDrawable());
            }
        });



        if( request.getIsStarted() && request.isStatus()) {
            if (!request.getIsPaid()) { // If not paid.
                holder.mJobStatus.setText("Job Status : Finished and not paid");
            } else {
                holder.mJobStatus.setText("Job Status : Finished and Paid!");
            }
        }else if(request.getIsStarted()) {
            holder.mJobStatus.setText("Job Status : In Progress");
        }else if(request.isAccepted()) {
            holder.mJobStatus.setText("Job Status: Accepted");
        }else{
            holder.mJobStatus.setText("Job Status: Posted");
        }

    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    public class RV_ViewHolder extends RecyclerView.ViewHolder {

        protected LinearLayout mMasterLin;
        protected TextView mJobTitle, mJobDescription, mJobLocation, mJobStatus;
        protected ImageView mRequestImage;
        protected LinearLayout mViewLine;
        // protected Button mMakePayment;

        public RV_ViewHolder(View itemView) {
            super(itemView);

            mMasterLin = (LinearLayout) itemView.findViewById(R.id.master_lin);
            mJobTitle = (TextView) itemView.findViewById(R.id.job_title);
            mJobDescription = (TextView) itemView.findViewById(R.id.job_description);
            mRequestImage = (ImageView) itemView.findViewById(R.id.request_image);
            mJobLocation = (TextView) itemView.findViewById(R.id.job_location);
            mViewLine = (LinearLayout) itemView.findViewById(R.id.view_line);
            // mMakePayment = (Button) itemView.findViewById(R.id.make_payment);
            mJobStatus = (TextView) itemView.findViewById(R.id.job_status);

        }
    }

    private Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


}

