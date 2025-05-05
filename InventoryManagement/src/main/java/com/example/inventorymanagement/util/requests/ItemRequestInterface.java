package com.example.inventorymanagement.util.requests;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.LinkedList;

public interface ItemRequestInterface extends Remote {

    public LinkedList<Item> fetchLisOfItems(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public boolean createItemListing(ClientCallback clientCallback, Item item) throws RemoteException, NotLoggedInException, OutOfRoleException;

    public boolean removeItemListing(ClientCallback clientCallback, Item item) throws RemoteException, NotLoggedInException,OutOfRoleException;

    public LinkedList<Item> fetchLowestStock(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public Item fetchItem(ClientCallback clientCallback,int id) throws RemoteException, NotLoggedInException;

    public void checkIfLoggedIn(ClientCallback clientCallback) throws RemoteException, NotLoggedInException;

    public void callUpdate(String panel) throws RemoteException;


}
