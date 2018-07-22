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
import com.app.handyman.mender.model.MaterialReceiptTwo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class MaterialReceiptTwoAdapter extends RecyclerView.Adapter<MaterialReceiptTwoAdapter.ViewHolder>{

    private ArrayList<MaterialReceiptTwo> materialReceiptArrayList;
    private String jobId;
    private SelectReceiptListener selectReceiptListener;
    private static final int LAYOUT_SELECT_YES = 0;
    private static final int LAYOUT_SELECT_NO = 1;


    public interface SelectReceiptListener{
        void onClick(int position);
        void onLongClick(int position);
    }


    public MaterialReceiptTwoAdapter(ArrayList<MaterialReceiptTwo> materialReceiptArrayList,
                                     String jobId, SelectReceiptListener selectReceiptListener){
        this.materialReceiptArrayList = materialReceiptArrayList;
        this.jobId = jobId;
        this.selectReceiptListener = selectReceiptListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layoutId = -1;

        switch (viewType){
            case LAYOUT_SELECT_YES:
                layoutId = R.layout.rv_material_purchase_receipt_yes_item;
                break;

            case LAYOUT_SELECT_NO:
                layoutId = R.layout.rv_material_purchase_receipt_no_item;
                break;

        }


        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent,false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        MaterialReceiptTwo materialReceipt = materialReceiptArrayList.get(holder.getAdapterPosition());
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


        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectReceiptListener.onClick(holder.getAdapterPosition());
            }
        });

        holder.mainLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                selectReceiptListener.onLongClick(holder.getAdapterPosition());
                return true;
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
        private View mainLayout;
        public ViewHolder(View view){
            super(view);
           materialReceiptImage =(ImageView) view.findViewById(R.id.materialReceiptImage);
            materialDate = (TextView)view.findViewById(R.id.materialDate);
            materialCost = (TextView)view.findViewById(R.id.materialCost);
            mainLayout = view;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(materialReceiptArrayList.get(position).getMaterialReceiptSelect()){
            return LAYOUT_SELECT_YES;
        }else{
            return LAYOUT_SELECT_NO;
        }
    }
}
