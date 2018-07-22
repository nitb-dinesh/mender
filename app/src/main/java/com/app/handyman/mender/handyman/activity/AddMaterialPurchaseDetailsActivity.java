package com.app.handyman.mender.handyman.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.utils.GenericFileProvider;
import com.app.handyman.mender.common.utils.GlobalValues;
import com.app.handyman.mender.handyman.adapter.MaterialReceiptTwoAdapter;
import com.app.handyman.mender.model.MaterialReceipt;
import com.app.handyman.mender.model.MaterialReceiptTwo;
import com.app.handyman.mender.model.Request;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.app.handyman.mender.common.utils.Helper.decodeSampledBitmapFromFile;
import static com.app.handyman.mender.common.utils.Helper.rotateImage;

public class AddMaterialPurchaseDetailsActivity extends AppCompatActivity implements
        View.OnClickListener, MaterialReceiptTwoAdapter.SelectReceiptListener {

    private final static String TAG = AddMaterialPurchaseDetailsActivity.class.getSimpleName();
    private String jobId;

    private RecyclerView rvMaterialRecipt;
    private double materialCharges = 0;
    private double driveCost, laborCost, totalCost;
    private static final int REQUEST_CAPTURE_FROM_CAMERA = 1;
    public static final int REQUEST_SELECT_IMAGE_GALLERY = 2;
    private Date d = new Date();
    private String a = String.valueOf(d.getTime() );
    private Toast toast;
    private DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference requestsReference = mRootReference.child("Requests");
    private DatabaseReference materialReciptReference;
    private long materialReceiptCount = 0;
    private File file;
    private boolean isImageAdded = false;
    private MaterialReceiptTwoAdapter materialReceiptTwoAdapter;
    private ArrayList<MaterialReceiptTwo> materialReceiptArrayList;
    private FloatingActionButton addReceipt;
    private TextView materialPurchaseCost;

    private EditText materialCost;
    private ImageView cancelUpload ;
    private ImageView reciptImage ;
    private Button clickReciptImage ;
    private Button galleryReceiptImage ;
    private Button saveMaterialReceipt ;
    private Uri galleryUri;
    private ImageView deleteReceipts;
    private ImageView selectAllReceipts;
    private LinearLayout receiptActions;
    private boolean buttonPressed = false;
    private StorageReference storageReference;
    private ArrayList<MaterialReceiptTwo> deleteList;
ImageView getDeleteReceipts;
    @Override
    public void onClick(int position) {

        if(receiptActions.getVisibility() == View.VISIBLE){
            toast.setText("" + position);
            toast.show();
            if(materialReceiptArrayList.get(position).getMaterialReceiptSelect()){
                materialReceiptArrayList.get(position).setMaterialReceiptSelect(false);
                String receiptImagePath = materialReceiptArrayList.get(position).getMaterialReceiptImagePath();
                for (int i = 0; i < deleteList.size(); i++) {
                    // Find the item to remove and then remove it by index
                    if (deleteList.get(i).getMaterialReceiptImagePath().equals(receiptImagePath)) {
                        deleteList.remove(i);
                        break;
                    }
                }
            }else{
                materialReceiptArrayList.get(position).setMaterialReceiptSelect(true);
                deleteList.add(materialReceiptArrayList.get(position));
            }

            materialReceiptTwoAdapter.notifyItemChanged(position);

        }

    }

    @Override
    public void onLongClick(int position) {
        receiptActions.setVisibility(View.VISIBLE);
        if(materialReceiptArrayList.get(position).getMaterialReceiptSelect()){
            materialReceiptArrayList.get(position).setMaterialReceiptSelect(false);
            String receiptImagePath = materialReceiptArrayList.get(position).getMaterialReceiptImagePath();
            for (int i = 0; i < deleteList.size(); i++) {
                // Find the item to remove and then remove it by index
                if (deleteList.get(i).getMaterialReceiptImagePath().equals(receiptImagePath)) {
                    deleteList.remove(i);
                    break;
                }
            }
        }else{
            materialReceiptArrayList.get(position).setMaterialReceiptSelect(true);
            deleteList.add(materialReceiptArrayList.get(position));
        }
        materialReceiptTwoAdapter.notifyItemChanged(position);
    }

    /*
    private void enableAddImage(){
        isImageAdded = false;
        imageUploadLayout.setVisibility(View.GONE);
        addReceipt.setVisibility(View.VISIBLE);
        cancelUpload.setVisibility(View.VISIBLE);
        reciptImage.setVisibility(View.GONE);

    }
    */

    private void uploadImage(Uri f) {


        Log.d("TestUploadImage", f.getPath());


        //DatabaseReference requestsReference = FirebaseDatabase.getInstance().getReference().child("Requests");



//        final ProgressWheel wheel = new ProgressWheel(AddMaterialPurchaseDetailsActivity.this);
//        wheel.setBarColor(Color.RED);
//        wheel.spin();
        final ProgressDialog progressDialog = new ProgressDialog(AddMaterialPurchaseDetailsActivity.this, R.style.DialogStyle);
        progressDialog.setMessage("Uploading Receipts");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StorageReference imageRef = storageReference.child("images").child(jobId).child("receipts");

        // int i = 4; // First 3 images are by the customer. 4th is Receipts Image!
        final String newReceiptsKey = requestsReference.push().getKey();
        StorageReference imageReference = imageRef.child(newReceiptsKey + "");
        // Uri f1 = Uri.fromFile(new File(f.getAbsolutePath()));
        UploadTask uploadTask = imageReference.putFile(f);

        // Register observers to listen for when the download is done or if it fails

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("upload", " Image Not Uploaded " + exception.getMessage());
                //wheel.stopSpinning();


            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("upload", "Uploaded Image");
                isImageAdded = false;
                MaterialReceipt materialReceipt = new MaterialReceipt();
                materialReceipt.setMaterialCost(materialCost.getText().toString());
                materialReceipt.setMaterialPurchaseDate(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                materialReceipt.setMaterialReceiptImagePath(newReceiptsKey);






                materialReciptReference.child(newReceiptsKey).setValue(materialReceipt).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("onSuccess", " onSuccess " );

                        materialCost.setText("");
                        reciptImage.setImageBitmap(null);
                        //wheel.stopSpinning();

                        progressDialog.cancel();

                        cancelUpload.setVisibility(View.VISIBLE);
                        reciptImage.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("onFailure", e.getLocalizedMessage() );

                        progressDialog.cancel();
                        //wheel.stopSpinning();
                    }
                });


            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

      /*  if (data == null) {
            toast.setText(getResources().getString(R.string.loading_image_error));
            toast.show();
            return;
        }*/


        if (requestCode == REQUEST_SELECT_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {

            try {

                try {
                    Uri imageUri = data.getData();
                    reciptImage.setColorFilter(null);
                    isImageAdded = true;
                    reciptImage.setVisibility(View.VISIBLE);
                    cancelUpload.setVisibility(View.GONE);
                    reciptImage.setImageURI(imageUri);
                    galleryUri = imageUri;

                } catch (Exception e) {
                    e.printStackTrace();
                    /*toast.setText(getResources().getString(R.string.loading_image_error));
                    toast.show();*/
                }

            } catch (Exception e) {
                e.printStackTrace();
                toast.setText(e.getLocalizedMessage());
                toast.show();
            }


        }else if (requestCode == REQUEST_CAPTURE_FROM_CAMERA && resultCode == Activity.RESULT_OK) {
            try {

                galleryUri = null;
                file = new File(Environment.getExternalStorageDirectory() + File.separator + a);
                Bitmap imageBitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);

                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(file.getAbsolutePath());
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            imageBitmap = rotateImage(imageBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            imageBitmap = rotateImage(imageBitmap, 180);
                            break;
                        // etc.
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 30, stream);

                isImageAdded = true;
                reciptImage.setVisibility(View.VISIBLE);
                cancelUpload.setVisibility(View.GONE);
                reciptImage.setImageBitmap(imageBitmap);

            } catch (Exception e) {
                e.printStackTrace();
                toast.setText(e.getLocalizedMessage());
                toast.show();
            }
        }
    }

    void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_IMAGE_GALLERY);
    }




    private void takePicture() {
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file;
        Uri newPictureUri;
        file = new File(Environment.getExternalStorageDirectory() + File.separator + a);

        newPictureUri = GenericFileProvider.getUriFromFile(AddMaterialPurchaseDetailsActivity.this, file);

        List<ResolveInfo> resInfoList = AddMaterialPurchaseDetailsActivity.this.getPackageManager().queryIntentActivities(imageIntent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            AddMaterialPurchaseDetailsActivity.this.grantUriPermission(packageName, newPictureUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, newPictureUri);
        startActivityForResult(imageIntent, REQUEST_CAPTURE_FROM_CAMERA);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case GlobalValues.ExternalStorageAndCameraPermissionsRequest:
                if (grantResults.length == 3
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    addMaterialReceiptDetails();

                } else {
                    Toast.makeText(AddMaterialPurchaseDetailsActivity.this, R.string.external_storage_and_camera_permissions_required, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(AddMaterialPurchaseDetailsActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddMaterialPurchaseDetailsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(AddMaterialPurchaseDetailsActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                ) {
            ActivityCompat.requestPermissions(
                    AddMaterialPurchaseDetailsActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA
                    },
                    GlobalValues.ExternalStorageAndCameraPermissionsRequest
            );
            return ;
        }


        addMaterialReceiptDetails();

    }



    private void clickMaterialReciptImage() {
        takePicture();

    }



    private void saveReceiptinDb(Uri f){
        String materialCostText = materialCost.getText().toString();

        if (materialCostText.isEmpty()) {
            Toast.makeText(AddMaterialPurchaseDetailsActivity.this, "No Cost Added!", Toast.LENGTH_SHORT).show();

        } else {
            if( isImageAdded ) {
                if(f != null ){
                    uploadImage(f);
                } else if (file != null ) {
                    uploadImage(Uri.fromFile(new File(file.getAbsolutePath())));
                }
            }else{
                Toast.makeText(AddMaterialPurchaseDetailsActivity.this, "Please add receipt image!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addMaterialReceiptDetails(){
        final Dialog dialog = new Dialog(AddMaterialPurchaseDetailsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_receipt);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final TextView imageDetails = (TextView) dialog.findViewById(R.id.imageDetails);
        materialCost = (EditText) dialog.findViewById(R.id.materialCost);
        cancelUpload = (ImageView) dialog.findViewById(R.id.cancelUpload);
        reciptImage = (ImageView) dialog.findViewById(R.id.reciptImage);
        clickReciptImage = (Button) dialog.findViewById(R.id.clickReciptImage);
        galleryReceiptImage = (Button) dialog.findViewById(R.id.galleryReceiptImage);
        saveMaterialReceipt = (Button) dialog.findViewById(R.id.saveMaterialReceipt);


        Typeface myCustomFont19 = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        imageDetails.setTypeface(myCustomFont19);
        materialCost.setTypeface(myCustomFont19);
        clickReciptImage.setTypeface(myCustomFont19);
        galleryReceiptImage.setTypeface(myCustomFont19);
        saveMaterialReceipt.setTypeface(myCustomFont19);


        clickReciptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickMaterialReciptImage();
            }
        });

        saveMaterialReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReceiptinDb(galleryUri);
            }
        });

        cancelUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        galleryReceiptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });




        /*

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        /*
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        */



        dialog.setCancelable(true);
        dialog.show();
    }

    private void deleteReceipts(final ArrayList<MaterialReceiptTwo> tempList){
        DatabaseReference deleteReference = mRootReference.child("Requests").child(jobId).child("receipts");


        if(tempList.size() > 0) {
            final ProgressDialog progressDialog = new ProgressDialog(AddMaterialPurchaseDetailsActivity.this, R.style.DialogStyle);
            progressDialog.setMessage("Deleting Receipts");
            progressDialog.setCancelable(false);
            progressDialog.show();
            for (int i = 0; i < tempList.size(); i++) {
                final int t = i;
                final String receiptNameinDb = tempList.get(i).getMaterialReceiptImagePath();
                deleteReference.child(receiptNameinDb).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        // Create a storage reference from our app
                        materialReceiptTwoAdapter.notifyDataSetChanged();
                        StorageReference imageRef = storageReference.child("images").child(jobId).child("receipts");

                        // Create a reference to the file to delete
                        StorageReference desertRef = imageRef.child(receiptNameinDb);

                        // Delete the file
                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                toast.setText("File deleted successfully");
                                toast.show();

                                if(t == tempList.size() -1){
                                    receiptActions.setVisibility(View.GONE);
                                    progressDialog.cancel();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Uh-oh, an error occurred!
                                toast.setText(exception.getMessage());
                                toast.show();
                                if(t == tempList.size() -1){
                                    receiptActions.setVisibility(View.GONE);
                                    progressDialog.cancel();
                                }
                            }
                        });
                    }
                });


            }
        }else{

            receiptActions.setVisibility(View.GONE);

            toast.setText("Please select receipts to delete");
            toast.show();
        }



    }



    @Override
    public void onClick(View view) {

        toast.setText("button click" + materialReceiptCount);
        toast.show();


        switch (view.getId()) {

            case R.id.addReceipt:

                if(materialReceiptCount<5) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkCameraPermission();
                    } else {
                        addMaterialReceiptDetails();
                    }
                }else{
                    toast.setText( "Only 5 receipts are accepted");
                    toast.show();
                }
                break;

            case R.id.selectAllReceipts:

                buttonPressed = !buttonPressed;

                for(int i =0; i< materialReceiptArrayList.size(); i++){
                    materialReceiptArrayList.get(i).setMaterialReceiptSelect(buttonPressed);
                }
                materialReceiptTwoAdapter.notifyDataSetChanged();
                break;

            case R.id.deleteReceipts:
                deleteReceipts(deleteList);

        }

    }

    private void getCostInfo(){
        DatabaseReference costReference = mRootReference.child("Requests").child(jobId);
        costReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                laborCost = 0;
                driveCost = 0;
                if(dataSnapshot.hasChildren()) {
                    Request request = dataSnapshot.getValue(Request.class);

                    laborCost = Double.parseDouble(request.getLaborTimeCost());
                    driveCost = Double.parseDouble(request.getDriveTimeCost());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMaterialReceiptInfo(){
        materialReciptReference = mRootReference.child("Requests").child(jobId).child("receipts");

        materialReciptReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChildren()){
                    Log.d("REMOVALREMOVAL", "onChildRemoved");
                    MaterialReceiptTwo materialReceipt = dataSnapshot.getValue(MaterialReceiptTwo.class);
                    materialReceipt.setMaterialReceiptSelect(false);
                    materialReceiptArrayList.add(materialReceipt);
                    materialReceiptCount++;
                    materialReceiptTwoAdapter.notifyDataSetChanged();


                    Log.d(TAG, materialReciptReference.toString());

                    materialCharges = materialCharges + Double.parseDouble(materialReceipt.getMaterialCost());

                    totalCost = driveCost + laborCost + materialCharges;

                    String totalMaterialPurchaseCost = "$" + String.valueOf(materialCharges);


                    materialPurchaseCost.setText(totalMaterialPurchaseCost);

                    HashMap<String, Object> request = new HashMap<>();
                    request.put("materialCost",String.valueOf(materialCharges) );

                    request.put("totalCost",  String.valueOf(totalCost));
                    requestsReference.child(jobId).updateChildren(request);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d("REMOVALREMOVAL", "onChildRemoved");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("REMOVALREMOVAL", "onChildRemoved");
                if(dataSnapshot.hasChildren()){
                    Log.d("REMOVALREMOVAL", "onChildRemoved123");
                    MaterialReceiptTwo materialReceipt = dataSnapshot.getValue(MaterialReceiptTwo.class);



                    String receiptImagePath = materialReceipt.getMaterialReceiptImagePath();

                    for (int i = 0; i < materialReceiptArrayList.size(); i++) {
                        // Find the item to remove and then remove it by index
                        if (materialReceiptArrayList.get(i).getMaterialReceiptImagePath().equals(receiptImagePath)) {
                            materialReceiptArrayList.remove(i);

                            materialCharges = materialCharges - Double.parseDouble(materialReceipt.getMaterialCost());
                            totalCost = driveCost + laborCost + materialCharges;

                            String totalMaterialPurchaseCost = "$" + String.valueOf(materialCharges);


                            materialPurchaseCost.setText(totalMaterialPurchaseCost);

                            HashMap<String, Object> request = new HashMap<>();
                            request.put("materialCost",String.valueOf(materialCharges) );

                            request.put("totalCost",  String.valueOf(totalCost));
                            requestsReference.child(jobId).updateChildren(request);
                            break;
                        }
                    }

                    materialReceiptTwoAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("REMOVALREMOVAL", "onChildRemoved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("REMOVALREMOVAL", "onChildRemoved");

            }
        });


    }





    private void initViews(){
        rvMaterialRecipt = (RecyclerView) findViewById(R.id.rvMaterialRecipt);
        addReceipt = (FloatingActionButton) findViewById(R.id.addReceipt);
        materialPurchaseCost = (TextView) findViewById(R.id.materialPurchaseCost);


        deleteReceipts =  (ImageView )findViewById(R.id.deleteReceipts);
        selectAllReceipts = (ImageView )findViewById(R.id.selectAllReceipts);
        receiptActions =  (LinearLayout)findViewById(R.id.receiptActions);

        addReceipt.setVisibility(View.VISIBLE);



        Typeface typeface = Typeface.createFromAsset(AddMaterialPurchaseDetailsActivity.this.getAssets(), "fonts/Quicksand Book.otf");

        materialReceiptArrayList = new ArrayList<>();
        deleteList = new ArrayList<>();

        materialReceiptTwoAdapter = new MaterialReceiptTwoAdapter(materialReceiptArrayList, jobId, this);
        rvMaterialRecipt.setLayoutManager(new GridLayoutManager(AddMaterialPurchaseDetailsActivity.this,2));
        rvMaterialRecipt.setHasFixedSize(true);
        rvMaterialRecipt.setAdapter(materialReceiptTwoAdapter);

        addReceipt.setOnClickListener(this);
        selectAllReceipts.setOnClickListener(this);
        deleteReceipts.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mender-49a62.appspot.com");


        toast = Toast.makeText(AddMaterialPurchaseDetailsActivity.this, "", Toast.LENGTH_LONG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material_purchase_details);

        try {
            Bundle extras = getIntent().getExtras();
            jobId = extras.getString("id");
            Log.d(TAG, jobId);

            initViews();

            getMaterialReceiptInfo();
            getCostInfo();

        } catch (Exception e) {
            e.printStackTrace();
        }




    }
}
