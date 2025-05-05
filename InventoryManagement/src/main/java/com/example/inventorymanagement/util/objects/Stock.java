package com.example.inventorymanagement.util.objects;

import java.io.Serializable;

public class Stock implements Serializable {
    private String batchNo;
    private int qty;
    private float price;
    private float cost;
    private String supplier;
    private String date;

    /**
     * Constructor
     * @param batchNo       batchNo is given at runtime
     * @param qty           qty of the stock, usually taken from orderdetail units
     * @param price         selling price for item, for simplicity’s sake this is cost+cost*.20
     * @param cost          cost per units, can be taken from the batchNo
     * @param supplier      Supplier of this batch, can be taken from the batchNo as well
     * @param date          Date when the batch was bought, can be taken from batchNo as well
     */
    public Stock(String batchNo, int qty, float price, float cost, String supplier, String date) {
        this.batchNo = batchNo;
        this.qty = qty;
        this.price = price;
        this.cost = cost;
        this.supplier = supplier;
        this.date = date;
    }

    public Stock(){
        this.batchNo = null;
        this.qty = 0;
        this.price = 0;
        this.cost = 0;
        this.supplier = null;
        this.date = null;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
