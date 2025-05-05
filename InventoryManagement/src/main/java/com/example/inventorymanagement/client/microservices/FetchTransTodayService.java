package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class FetchTransTodayService {

    public static LinkedList<ItemOrder> process(Registry registry, ClientCallback callback) throws NotLoggedInException{
        try {
            ItemOrderRequestInterface iORequest = (ItemOrderRequestInterface) registry.lookup("itemOrder");
            return iORequest.fetchTransactionsToday(callback);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
