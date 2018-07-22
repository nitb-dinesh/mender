package com.app.handyman.mender.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.app.handyman.mender.common.activity.HomeActivity;
import com.app.handyman.mender.handyman.activity.StartJobActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pnikosis.materialishprogress.ProgressWheel;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Helper {


    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    public static void uploadImage(Context context, Uri f, String id) {


        Log.d("TestUploadImage", id + f.getPath());

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://mender-49a62.appspot.com");
        DatabaseReference requestsReference = FirebaseDatabase.getInstance().getReference().child("Requests");

        final ProgressWheel wheel = new ProgressWheel(context);
        wheel.setBarColor(Color.BLUE);
        wheel.spin();

        StorageReference imageRef = storageReference.child("images").child(id).child("receipts");

        // int i = 4; // First 3 images are by the customer. 4th is Receipts Image!
        String newReceiptsKey = requestsReference.push().getKey();
        StorageReference imageReference = imageRef.child(newReceiptsKey + "");
        // Uri f1 = Uri.fromFile(new File(f.getAbsolutePath()));
        UploadTask uploadTask = imageReference.putFile(f);

        // Register observers to listen for when the download is done or if it fails

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i("upload", " Image Not Uploaded " + exception.getMessage());
                wheel.stopSpinning();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("upload", "Uploaded Image");
                wheel.stopSpinning();
            }
        });

    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "No Network Available", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
