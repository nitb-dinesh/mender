package com.app.handyman.mender.common.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.WindowManager;
import android.widget.TextView;

import com.app.handyman.mender.R;


/**
 * Created by  on 11/11/17.
 */
public class DataManager {

    private static final DataManager ourInstance = new DataManager();

    public static DataManager getInstance() {
        return ourInstance;
    }

    private DataManager() {
    }

    private boolean isProgressDialogRunning;
    private Dialog mDialog;
    private Activity activity;
    private String video_Play_Url;

    private String ImeiNUmber;
    private String IpAddress;
    private String mIsAdmim ="0";


    public void showProgressMessage(Activity dialogActivity) {
        try {

            if (isProgressDialogRunning) {
                hideProgressMessage();
            }
            isProgressDialogRunning = true;
            mDialog = new Dialog(dialogActivity);
            mDialog.setContentView(R.layout.dialog_loading);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            TextView textView = mDialog.findViewById(R.id.textView);
            textView.setText(Html.fromHtml("Loading"));
            WindowManager.LayoutParams lp = mDialog.getWindow().getAttributes();
            lp.dimAmount = 0.8f;
            mDialog.getWindow().setAttributes(lp);
            mDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(true);
            mDialog.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void hideProgressMessage() {
        isProgressDialogRunning = true;
        try {
            if (mDialog != null)
                mDialog.dismiss();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String getVideo_Play_Url() {
        return video_Play_Url;
    }

    public void setVideo_Play_Url(String video_Play_Url) {
        this.video_Play_Url = video_Play_Url;
    }

    public String getImeiNUmber() {
        return ImeiNUmber;
    }

    public void setImeiNUmber(String imeiNUmber) {
        this.ImeiNUmber = imeiNUmber;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.IpAddress = ipAddress;
    }


    public String getmIsAdmim() {
        return mIsAdmim;
    }

    public void setmIsAdmim(String mIsAdmim) {
        this.mIsAdmim = mIsAdmim;
    }

}
