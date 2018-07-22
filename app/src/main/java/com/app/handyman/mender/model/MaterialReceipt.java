package com.app.handyman.mender.model;

import java.util.HashMap;

public class MaterialReceipt {


    private String materialCost;
    private String materialPurchaseDate;
    private String materialReceiptImagePath;


    public String getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(String materialCost) {
        this.materialCost = materialCost;
    }

    public String getMaterialPurchaseDate() {
        return materialPurchaseDate;
    }

    public void setMaterialPurchaseDate(String materialPurchaseDate) {
        this.materialPurchaseDate = materialPurchaseDate;
    }

    public String getMaterialReceiptImagePath() {
        return materialReceiptImagePath;
    }

    public void setMaterialReceiptImagePath(String materialReceiptImagePath) {
        this.materialReceiptImagePath = materialReceiptImagePath;
    }

    public HashMap<String, Object> toFirebase() {
        HashMap<String, Object> f = new HashMap<>();
        f.put("materialCost", materialCost);
        f.put("materialPurchaseDate", materialPurchaseDate);
        f.put("materialReceiptImagePath", materialReceiptImagePath);

        return f;
    }
}
