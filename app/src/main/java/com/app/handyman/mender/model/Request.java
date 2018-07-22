package com.app.handyman.mender.model;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Request implements Serializable {

    private String jobTitle;
    private String jobDescription;
    private String key;
    private String userEmail;
    private String address;
    private String phoneNumber;
    // private String customerId; // For Stripe!

    private List<File> imageFiles = new ArrayList<>();

    private boolean accepted;
    private String assignedTo; // ID of HandyMan
    private boolean status;

    private boolean isPaid;
    private String materialCost;
    private String driveTimeCost;
    private String laborTimeCost;
    private String propertyManagerName;
    private String propertyManagerPhoneNumber;

    private Date driveTimeStartDate;
    private Date labourTimeStartDate;
    private String driveTime;
    private String labourTime;
    private boolean isStarted;
    private String zipCode;


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }



    public Date getDriveTimeStartDate() {
        return driveTimeStartDate;
    }

    public void setDriveTimeStartDate(Date driveTimeStartDate) {
        this.driveTimeStartDate = driveTimeStartDate;
    }

    public Date getLabourTimeStartDate() {
        return labourTimeStartDate;
    }

    public void setLabourTimeStartDate(Date labourTimeStartDate) {
        this.labourTimeStartDate = labourTimeStartDate;
    }

    public String getDriveTime() {
        return driveTime;
    }

    public void setDriveTime(String driveTime) {
        this.driveTime = driveTime;
    }

    public String getLabourTime() {
        return labourTime;
    }

    public void setLabourTime(String labourTime) {
        this.labourTime = labourTime;
    }


    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    private String totalCost;
    private String materialReceiptImage;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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


    public List<File> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(String materialCost) {
        this.materialCost = materialCost;
    }

    public String getDriveTimeCost() {
        return driveTimeCost;
    }

    public void setDriveTimeCost(String driveTimeCost) {
        this.driveTimeCost = driveTimeCost;
    }

    public String getLaborTimeCost() {
        return laborTimeCost;
    }

    public void setLaborTimeCost(String laborTimeCost) {
        this.laborTimeCost = laborTimeCost;
    }

    public String getMaterialReceiptImage() {
        return materialReceiptImage;
    }

    public void setMaterialReceiptImage(String materialReceiptImage) {
        this.materialReceiptImage = materialReceiptImage;
    }

    public String getPropertyManagerName() {
        return propertyManagerName;
    }

    public void setPropertyManagerName(String propertyManagerName) {
        this.propertyManagerName = propertyManagerName;
    }

    public String getPropertyManagerPhoneNumber() {
        return propertyManagerPhoneNumber;
    }

    public void setPropertyManagerPhoneNumber(String propertyManagerPhoneNumber) {
        this.propertyManagerPhoneNumber = propertyManagerPhoneNumber;
    }


    public boolean getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(boolean started) {
        isStarted = started;
    }


//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    public HashMap<String, Object> toFirebase() {
        HashMap<String, Object> f = new HashMap<>();
        f.put("jobTitle", jobTitle);
        f.put("jobDescription", jobDescription);
        f.put("key", key);
        f.put("userEmail", userEmail);
        f.put("address", address);
        f.put("phoneNumber", phoneNumber);
        f.put("accepted", accepted);
        f.put("assignedTo", assignedTo);
        f.put("status", status);
        f.put("materialCost", materialCost); //10
        f.put("driveTimeCost", driveTimeCost);
        f.put("laborTimeCost", laborTimeCost);
        f.put("materialReceiptImage", materialReceiptImage);
        f.put("totalCost", totalCost);
        f.put("isPaid", isPaid);
        f.put("propertyManagerName", propertyManagerName);
        f.put("propertyManagerPhoneNumber", propertyManagerPhoneNumber);
        f.put("driveTimeStartDate", driveTimeStartDate);
        f.put("labourTimeStartDate", labourTimeStartDate);
        f.put("driveTime", driveTime); //20
        f.put("labourTime", labourTime);
        f.put("isStarted", isStarted);
        f.put("zipCode", zipCode);

        return f;
    }

}
