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

public class ChangePasswordService {
   public static boolean process(Registry registry, ClientCallback cB, User toChange, String newPassword) throws UserExistenceException, OutOfRoleException, NotLoggedInException {

       try {

           UserRequestInterface userRequest = (UserRequestInterface) registry.lookup("userRequest");

          return userRequest.changePassword(cB,toChange, newPassword);

       } catch (RemoteException | NotBoundException e) {
           throw new RuntimeException(e);
       }

   }
}
