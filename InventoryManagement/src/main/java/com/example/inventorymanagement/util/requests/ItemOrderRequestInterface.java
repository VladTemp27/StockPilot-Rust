package com.example.inventorymanagement.util.requests;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface ItemOrderRequestInterface extends Remote {

    public boolean createSalesInvoice(ClientCallback clientCallback, ItemOrder salesInvoice) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public boolean createPurchaseOrder(ClientCallback clientCallback, ItemOrder purchaseOrder) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public LinkedList<ItemOrder> fetchSalesInvoices(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public float fetchRevenueToday(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public float fetchCostToday(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public LinkedHashMap<Integer, Float> fetchMonthlyRevenue(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public LinkedHashMap<Integer, Float> fetchMonthlyCost(ClientCallback clientCallback) throws RemoteException, OutOfRoleException, NotLoggedInException;

    public ArrayList<String> fetchSuppliers(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public LinkedList<ItemOrder> fetchTransactionsToday(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public void checkIfValidPerm(User user) throws RemoteException, OutOfRoleException;

    public void checkIfLoggedIn(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public String getCurrentDate() throws RemoteException;

    public void callUpdate(String panel) throws RemoteException;
}
