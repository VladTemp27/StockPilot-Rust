package com.example.inventorymanagement.util;

import java.rmi.Remote;
import java.rmi.RemoteException;

// #TODO: Implement this to all other controllers
public interface ControllerInterface extends Remote {

    // Method that will fetch data from model and insert it into ui elements
    public void fetchAndUpdate() throws RemoteException;

    // Method for getting what specific panel is currently on display
    public String getObjectsUsed() throws RemoteException;
}
