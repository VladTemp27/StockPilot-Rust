package com.example.inventorymanagement.util.objects;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private int itemID;
    private int qty;
    private float unitPrice;
    private String batchNo;

    /**
     * Constructor
     * @param itemID        Id of the Item
     * @param qty           qty of said item
     * @param unitPrice     unit price of per item
     * @param batchNo       batchNo, given if the order is a sale, generated if it is purchase using cost, supplier, and date
     */
    public OrderDetail(int itemID, int qty, float unitPrice, String batchNo) {
        this.itemID = itemID;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.batchNo = batchNo;
    }

    public int getItemId() {
        return itemID;
    }

    public void setItemId(int itemID) {
        this.itemID = itemID;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
