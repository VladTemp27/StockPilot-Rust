package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RemoveUserService {

    public static boolean process (Registry registry, ClientCallback cB , User toRemove) throws UserExistenceException, OutOfRoleException, NotLoggedInException {
        try {

            UserRequestInterface userRequest = (UserRequestInterface) registry.lookup("userRequest");

            return userRequest.removeUser(cB,toRemove);

        } catch (NotBoundException | RemoteException e){
            e.printStackTrace();
            return false;
        }


    }
}
