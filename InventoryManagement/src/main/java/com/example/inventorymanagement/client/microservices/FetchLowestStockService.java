package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class FetchLowestStockService {

    public static LinkedList<Item> process(Registry registry, ClientCallback clientCallback) throws NotLoggedInException {
        try {
            ItemRequestInterface iRI = (ItemRequestInterface) registry.lookup("item");
            return iRI.fetchLowestStock(clientCallback);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
