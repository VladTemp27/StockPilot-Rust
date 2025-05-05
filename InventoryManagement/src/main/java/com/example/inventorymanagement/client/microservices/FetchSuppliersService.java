package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

public class FetchSuppliersService {

    public static ArrayList<String> process (Registry registry, ClientCallback cB ) throws NotLoggedInException {
        try {

            ItemOrderRequestInterface IORequest = (ItemOrderRequestInterface) registry.lookup("itemOrder");

            return IORequest.fetchSuppliers(cB);

        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
