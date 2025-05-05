package com.example.inventorymanagement.util;

import com.example.inventorymanagement.util.objects.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallback extends Remote {

    public void objectCall(Object objectReturn) throws RemoteException; // do not make it abstract!!

    public void uiCall() throws RemoteException;

    public Object getObject() throws RemoteException;
    // method to get the current user of the callback/session

    public void setUser(User user) throws RemoteException;

    public User getUser() throws RemoteException;


    public void updateUICall() throws RemoteException;

    public void setCurrentPanel(ControllerInterface controller)throws RemoteException;

    public String getObjectsUsedByPanel() throws RemoteException;

}
