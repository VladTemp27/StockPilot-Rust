package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class FetchListOfUsersService {
    public static LinkedList<User> process(ClientCallback clientCallback, Registry registry) throws OutOfRoleException, NotLoggedInException {
        try{
            UserRequestInterface userStub = (UserRequestInterface) registry.lookup("userRequest");
            return userStub.fetchUsers(clientCallback);
        }catch(RemoteException | NotBoundException e){
            e.printStackTrace();
        }
        return null;
    }
}
