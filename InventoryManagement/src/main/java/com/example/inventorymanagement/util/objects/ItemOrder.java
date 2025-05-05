package com.example.inventorymanagement.util.objects;

import java.io.Serializable;
import java.util.LinkedList;

public class ItemOrder implements Serializable {
    private int orderID;
    private String byUser;
    private String date;
    private LinkedList<OrderDetail> orderDetails = new LinkedList<>();

    /**
     * Default constructor
     */
    public ItemOrder(){
        orderID = 0;
        byUser = null;
        date = null;
    }

    /**
     * Constructor method for the class
     * @param orderId       ID of order, would be set by the server side
     * @param byUser        Object of User that created this item order
     * @param date          Date when the order was created
     * @param orderDetails  LinkedList of object OrderDetails, to store details regarding this order
     */
    public ItemOrder(int orderId, String byUser, String date, LinkedList<OrderDetail> orderDetails) {
        this.orderID = orderId;
        this.byUser = byUser;
        this.date = date;
        this.orderDetails = orderDetails;
    }

    /**
     * Method to add orderDetail to orderDetails
     * @param orderDetail object of OrderDetail
     */
    public void addOrderDetail(OrderDetail orderDetail){
        orderDetails.addLast(orderDetail);
    }

    // Getter and setter below
    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderId) {
        this.orderID = orderID;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public LinkedList<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(LinkedList<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
