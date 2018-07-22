package com.app.handyman.mender.misc;

import java.io.Serializable;
import java.util.HashMap;

public class Dispute implements Serializable {

    private String key;
    private String name;
    private String userEmail;
    private String phoneNumber;
    private String query;
    private String assignedTo;
    private String jobTitle;
    private String jobDescription;
    private String jobKey;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getJobKey() {
        return jobKey;
    }

    public void setJobKey(String jobKey) {
        this.jobKey = jobKey;
    }

    public HashMap<String, Object> toFirebase() {
        HashMap<String, Object> f = new HashMap<>();
        f.put("key", key);
        f.put("name", name);
        f.put("userEmail", userEmail);
        f.put("phoneNumber", phoneNumber);
        f.put("query", query);
        f.put("assignedTo", assignedTo);
        f.put("jobTitle", jobTitle);
        f.put("jobDescription", jobDescription);
        f.put("jobKey", jobKey);
        return f;
    }


}
