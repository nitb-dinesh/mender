package com.app.handyman.mender.handyman.adapter;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.handyman.mender.R;
import com.app.handyman.mender.model.MaterialReceipt;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MaterialReceiptAdapter extends RecyclerView.Adapter<MaterialReceiptAdapter.ViewHolder>{

    private ArrayList<MaterialReceipt> materialReceiptArrayList;
    private String jobId;
    private int id;

    public MaterialReceiptAdapter(ArrayList<MaterialReceipt> materialReceiptArrayList, String jobId, int id){
        this.materialReceiptArrayList = materialReceiptArrayList;
        this.jobId = jobId;
        this.id = id;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId = -1;

        switch (viewType){
            case 1:
                layoutId = R.layout.rv_material_receipt_item;
                break;
            default:
                layoutId = R.layout.rv_material_purchase_receipt_yes_item;
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MaterialReceipt materialReceipt = materialReceiptArrayList.get(holder.getAdapterPosition());
        Typeface myCustomFont = Typeface.createFromAsset(holder.materialCost.getContext().getAssets(), "fonts/Quicksand Book.otf");

        holder.materialCost.setTypeface(myCustomFont);
        holder.materialDate.setTypeface(myCustomFont);

        String cost = "$"+materialReceipt.getMaterialCost();
        holder.materialCost.setText(cost);
        holder.materialDate.setText(materialReceipt.getMaterialPurchaseDate());

        FirebaseStorage.getInstance().getReferenceFromUrl("gs://mender-49a62.appspot.com").child("images/"
                + jobId + "/receipts/" + materialReceipt.getMaterialReceiptImagePath()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        try {
                            System.out.println(uri);
                            Glide.with(holder.materialReceiptImage.getContext())
                                    .load(uri) // the uri you got from Firebase
                                    .centerCrop()
                                    .into(holder.materialReceiptImage);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });







    }

    @Override
    public int getItemCount() {
        return materialReceiptArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView materialReceiptImage;
        private TextView materialDate;
        private TextView materialCost;

        public ViewHolder(View view){
            super(view);
            materialReceiptImage = (ImageView)view.findViewById(R.id.materialReceiptImage);
            materialDate = (TextView)view.findViewById(R.id.materialDate);
            materialCost = (TextView)view.findViewById(R.id.materialCost);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return id;
    }
}
