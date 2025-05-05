package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CreateSalesInvoiceService {

    public static boolean process (Registry registry, ClientCallback cB , ItemOrder salesInvoice) throws OutOfRoleException, NotLoggedInException {
        try {

            ItemOrderRequestInterface IORequest = (ItemOrderRequestInterface) registry.lookup("itemOrder");

            return IORequest.createSalesInvoice(cB, salesInvoice);

        } catch (NotBoundException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
