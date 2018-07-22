package com.app.handyman.mender.common.utils;

/**
 * Created by Dinesh on 12/6/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;


import java.util.HashMap;

/**
 * Created by user on 11/04/2016.
 */
public class MySession {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "MyPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TOKEN_AUTH = "token";
    public static final String KEY_ID = "id";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USERNAME = "username";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private static final String DUTY_STATUS = "duty_status";
    private static final String USER_TYPE = "user_type";

    private static MySession mySession;

    // fname , lname, email, username

    public MySession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static MySession getInstance(Context context) {
        if (mySession != null) {
            return mySession;
        } else {
            return (new MySession(context));
        }
    }

    public void createLoginSession(String id, String token, String first_name, String email, String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_TOKEN_AUTH, token);
        editor.putString(KEY_FIRSTNAME, first_name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public void createLoginSession(String token) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN_AUTH, token);

        editor.commit();
    }

    public void createLoginSession(String token, int type) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN_AUTH, token);
        editor.putInt(USER_TYPE, type);
        editor.commit();
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_ID, pref.getString(KEY_ID, ""));
        user.put(KEY_FIRSTNAME, pref.getString(KEY_FIRSTNAME, ""));
        user.put(KEY_LASTNAME, pref.getString(KEY_LASTNAME, ""));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, ""));
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, ""));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public boolean IsLoggedIn()

    {
        return pref.getBoolean(IS_LOGIN, false);
    }


    public int getUserType() {
        return pref.getInt(USER_TYPE, 0);
    }

    public String getFirstName() {
        return pref.getString(KEY_FIRSTNAME, null);
    }

    public String getLastName() {
        return pref.getString(KEY_LASTNAME, null);
    }

    public String getUsername() {
        return pref.getString(KEY_USERNAME, null);
    }


    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }


    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public String getKeyTokenAuth() {
        return pref.getString(KEY_TOKEN_AUTH, null);
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public String getDutyStatus() {

        return pref.getString(DUTY_STATUS, "11");
       // return "";

    }

    public void setDutyStatus(String dutyStatus) {

        editor.putString(DUTY_STATUS, dutyStatus);
        editor.commit();

    }
}
