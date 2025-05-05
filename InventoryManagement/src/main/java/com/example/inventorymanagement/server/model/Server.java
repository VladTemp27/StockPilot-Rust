package com.example.inventorymanagement.server.model;

import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    public void run() throws RemoteException {
        System.setProperty("java.rmi.server.hostname","serverMachine");
        System.out.println(System.getenv("java.rmi.server.hostname"));
        UserRequestInterfaceImplementation userStub  = new UserRequestInterfaceImplementation();
        ItemOrderRequestImpl itemOrderStub = new ItemOrderRequestImpl();
        ItemRequestInterface itemStub = new ItemRequestImpl();
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("userRequest", userStub);
        reg.rebind("itemOrder",itemOrderStub);
        reg.rebind("item", itemStub);
        System.out.println("Server has started");
    }

    public static void main (String[]args){
        try {
            Server server = new Server();
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
