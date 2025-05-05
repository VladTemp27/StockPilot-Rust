package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class FetchListOfItemsService {


    public static LinkedList<Item> process (Registry registry, ClientCallback cB ) throws NotLoggedInException{
        try {

            ItemRequestInterface ItemRequest = (ItemRequestInterface) registry.lookup("item");

            return ItemRequest.fetchLisOfItems(cB);

        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}

