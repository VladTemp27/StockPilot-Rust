package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class UpdateCallback {
    public static boolean process(ClientCallback clientCallback, Registry registry)throws NotLoggedInException {
        try{
            UserRequestInterface userStub = (UserRequestInterface) registry.lookup("userRequest");

            return userStub.updateCallback(clientCallback.getUser(),clientCallback);

        }catch(RemoteException e){

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
