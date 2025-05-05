package com.example.inventorymanagement.client.model;


import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.objects.User;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

// #TODO: Populate with actual logic, discuss with team what to do with ui call to remotely change ui elements
public class ClientCallbackImpl extends UnicastRemoteObject implements ClientCallback, Serializable {
    private Object returnedObject;
    private User user;
    private ControllerInterface currentController;

    public ClientCallbackImpl(User user) throws RemoteException {
        super();
        this.user = user;
    }

    @Deprecated
    @Override
    public void objectCall(Object objectReturn) throws RemoteException {
        returnedObject = objectReturn;
    }

    @Override
    public void uiCall() throws RemoteException {

    }

    @Deprecated
    @Override
    public Object getObject() {
        return returnedObject;
    }

    @Override
    public void setUser(User user) throws RemoteException {
        this.user = user;
    }

    @Override
    public User getUser() throws RemoteException {
        return this.user;
    }


    @Override
    public String getObjectsUsedByPanel() throws RemoteException {
        return currentController.getObjectsUsed();
    }


    @Override
    public void updateUICall() throws RemoteException{
        System.out.println("Updating UI");
        currentController.fetchAndUpdate();
    }

    @Override
    public void setCurrentPanel(ControllerInterface controller) throws RemoteException {
        currentController = controller;
    }

}
