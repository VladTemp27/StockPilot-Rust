package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class FetchActiveUsersService {

    public static LinkedList<User> process (Registry registry, ClientCallback cB) throws NotLoggedInException, OutOfRoleException {
        try {

            UserRequestInterface userRequest = (UserRequestInterface) registry.lookup("userRequest");

            return userRequest.getActiveUser(cB);

        } catch (RemoteException | NotBoundException e){
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
}
