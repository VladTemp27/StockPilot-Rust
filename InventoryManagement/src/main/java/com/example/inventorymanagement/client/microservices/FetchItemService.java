package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class FetchItemService {
    public static Item process(ClientCallback clientCallback, Registry registry, int id) throws NotLoggedInException{
        try{
            ItemRequestInterface itemStub = (ItemRequestInterface) registry.lookup("item");
            return itemStub.fetchItem(clientCallback, id);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
