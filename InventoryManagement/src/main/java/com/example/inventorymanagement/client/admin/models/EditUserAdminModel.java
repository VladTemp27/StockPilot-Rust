package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.*;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class EditUserAdminModel {

    private final Registry registry;
    private final ClientCallback clientCallback;
    private RemoveUserService removeUserService;
    private FetchListOfUsersService fetchListOfUsersService;

    public EditUserAdminModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.removeUserService = new RemoveUserService();
        this.fetchListOfUsersService = new FetchListOfUsersService();
    }

    public boolean changePassword(User user, String newPassword) throws RemoteException {
        ChangePasswordService changePasswordService = new ChangePasswordService();
        try {
            return changePasswordService.process(registry, clientCallback, user, newPassword);
        } catch (UserExistenceException | OutOfRoleException | NotLoggedInException e) {
            e.printStackTrace(); // Handle exception appropriately
            return false;
        }
    }

    public boolean changeUserRole(User user, String newRole) throws RemoteException {
        try {
            // Check if the user has the necessary role to change another user's role
            if (!clientCallback.getUser().getRole().equals("admin")) {
                throw new OutOfRoleException("Insufficient permission to change user role");
            }

            // Call the method from the microservice to change the user's role
            boolean success = ChangeUserRoleService.process(registry, clientCallback, user, newRole);
            return success;
        } catch (RemoteException | OutOfRoleException | NotLoggedInException e) {
            // Handle exceptions appropriately
            e.printStackTrace(); // Log or handle the exception appropriately
            return false;
        } catch (UserExistenceException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeUser(User userToRemove) throws RemoteException {
        try {
            // Check if the user has the necessary role to remove a user
            if (!clientCallback.getUser().getRole().equals("admin")) {
                throw new OutOfRoleException("Insufficient permission to remove user");
            }

            // Once validation is successful, proceed with removing the user
            boolean success = removeUserService.process(registry, clientCallback, userToRemove);
            return success;
        } catch (RemoteException | OutOfRoleException | NotLoggedInException e) {
            // Handle exceptions related to RMI registry lookup
            e.printStackTrace(); // Log or handle the exception appropriately
            throw new RemoteException("Failed to communicate with server", e); // Throw a custom exception or handle the error
        } catch (UserExistenceException e) {
            throw new RuntimeException(e);
        }
    }

    public LinkedList<User> fetchUsers() throws RemoteException, OutOfRoleException, NotLoggedInException {
        try {
            // Call the fetchListOfUsersService to get the list of users
            return fetchListOfUsersService.process(clientCallback, registry);
        } catch (OutOfRoleException | NotLoggedInException e) {
            // Re-throw the exception to be handled by the caller
            throw e;
        } catch (Exception e) {
            // Handle any other exceptions and return null or handle appropriately
            e.printStackTrace();
            return null;
        }
    }
}

