package com.app.handyman.mender.handyman.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.handyman.mender.R;
import com.app.handyman.mender.common.activity.HomeActivity;

public class ConfirmActivity extends AppCompatActivity {

    //I will probably forget this, so if you are in here, I want to have the 25.00 TextField be
    //invisible unless the Dump Fee Radio Button is clicked.

    private Button btnback;
    private Button btnconfirm;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView textView8;
    private TextView textView9;
    private TextView textView11, textView12, textView17, textView18;

    private EditText mEditedDriveTime, mEditedLaborTime, mEditedMaterialCost;

    private double laborTimeCharges, driveTimeCharges, materialCostCharges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        Bundle extras = getIntent().getExtras();
        laborTimeCharges = extras.getDouble("drive_time");
        driveTimeCharges = extras.getDouble("labor_time");
        materialCostCharges = extras.getDouble("material_cost");

        Typeface boldFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Bold.otf");
        Typeface normalFont = Typeface.createFromAsset(getAssets(), "fonts/Quicksand Book.otf");

        textView1 = (TextView) findViewById(R.id.textView1);
        textView1.setTypeface(normalFont);

        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setTypeface(normalFont);

        textView3 = (TextView) findViewById(R.id.textView3);
        textView3.setTypeface(boldFont);

        textView4 = (TextView) findViewById(R.id.textView4);
        textView4.setTypeface(boldFont);

        textView5 = (TextView) findViewById(R.id.textView5);
        textView5.setTypeface(normalFont);

        textView6 = (TextView) findViewById(R.id.textView6);
        textView6.setTypeface(normalFont);

        mEditedDriveTime = (EditText) findViewById(R.id.edited_drive_time);
        mEditedDriveTime.setTypeface(normalFont);

        mEditedLaborTime = (EditText) findViewById(R.id.edited_labor_time);
        mEditedLaborTime.setTypeface(normalFont);

        mEditedMaterialCost = (EditText) findViewById(R.id.edited_material_cost);
        mEditedMaterialCost.setTypeface(normalFont);

        textView8 = (TextView) findViewById(R.id.textView8);
        textView8.setTypeface(normalFont);

        textView9 = (TextView) findViewById(R.id.textView9);
        textView9.setTypeface(normalFont);

        textView11 = (TextView) findViewById(R.id.textView11);
        textView11.setTypeface(normalFont);

        textView12 = (TextView) findViewById(R.id.textView12);
        textView12.setTypeface(normalFont);

        textView18 = (TextView) findViewById(R.id.textView18);
        textView18.setTypeface(boldFont);

        textView17 = (TextView) findViewById(R.id.textView17);
        textView17.setTypeface(boldFont);

        btnback = (Button) findViewById(R.id.btnback);

        btnback.setTypeface(normalFont);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        textView6.setText(driveTimeCharges + "");
        textView9.setText(laborTimeCharges + "");
        textView12.setText(materialCostCharges + "");

        btnconfirm = (Button) findViewById(R.id.btnconfirm);
        btnconfirm.setTypeface(normalFont);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String driveTime = mEditedDriveTime.getText().toString();
                String laborTime = mEditedLaborTime.getText().toString();
                String materialCost = mEditedMaterialCost.getText().toString();

                if (driveTime.isEmpty() || laborTime.isEmpty() || materialCost.isEmpty()) {
                    Toast.makeText(ConfirmActivity.this, "Please enter revised values for all the three fields", Toast.LENGTH_SHORT).show();
                } else {

                    // TODO : Get Double Values

                    // TODO : Save Values in Backend with Task Id

                    Intent intent = new Intent(ConfirmActivity.this, HomeActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
