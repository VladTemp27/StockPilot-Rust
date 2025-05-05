package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.AddUserService;
import com.example.inventorymanagement.client.microservices.FetchListOfUsersService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class AddUserAdminModel {
    private AddUserService addUserService;
    private FetchListOfUsersService fetchListOfUsersService;
    private Registry registry;
    private ClientCallback clientCallback;

    public AddUserAdminModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.addUserService = new AddUserService();
        this.fetchListOfUsersService = new FetchListOfUsersService();
    }


    public boolean addUserService(User newUser) {
        try {
            // Call the AddUserService microservice to add the user
            return addUserService.process(registry, clientCallback, newUser);
        } catch (UserExistenceException | OutOfRoleException | NotLoggedInException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return false;
        }
    }
    public LinkedList<User> fetchListOfUsers() throws NotLoggedInException, OutOfRoleException {
        try {
            return fetchListOfUsersService.process(clientCallback, registry);
        } catch (RuntimeException e) {
            // Handle any runtime exceptions
            e.printStackTrace();
            return null;
        }
    }
}
