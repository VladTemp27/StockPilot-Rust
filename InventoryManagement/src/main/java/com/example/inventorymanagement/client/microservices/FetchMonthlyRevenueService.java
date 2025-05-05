package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;

public class FetchMonthlyRevenueService {

    public static LinkedHashMap<Integer, Float> process (Registry registry, ClientCallback cB ) throws NotLoggedInException, OutOfRoleException{
        try {

            ItemOrderRequestInterface IORequest = (ItemOrderRequestInterface) registry.lookup("itemOrder");

            return IORequest.fetchMonthlyRevenue(cB);


        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }

    }
}
