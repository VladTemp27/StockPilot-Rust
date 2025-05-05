package com.example.inventorymanagement.client.microservices;

import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.requests.UserRequestInterface;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class LogOutMicroservice {
    public static void process(ClientCallback callback, Registry registry) throws NotLoggedInException {
        try{
            UserRequestInterface userStub = (UserRequestInterface) registry.lookup("userRequest");
            userStub.logout(callback);
        } catch (AccessException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
