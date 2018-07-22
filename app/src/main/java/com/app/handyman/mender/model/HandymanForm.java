package com.app.handyman.mender.model;

import java.io.Serializable;
import java.util.HashMap;

public class HandymanForm implements Serializable {

    private String key;
    private String name;
    private String userEmail;
    private String phoneNumber;
    private String details;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Object> toFirebase() {
        HashMap<String, Object> f = new HashMap<>();
        f.put("key", key);
        f.put("name", name);
        f.put("userEmail", userEmail);
        f.put("phoneNumber", phoneNumber);
        f.put("details", details);
        return f;
    }

}
