package com.example.inventorymanagement.client.purchaser.models;

import com.example.inventorymanagement.client.microservices.*;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class ProfileManagementPurchaserModel {
    private ChangeUserRoleService changeUserRoleService;
    private FetchListOfUsersService fetchListOfUsersService;
    private Registry registry;
    private ClientCallback clientCallback;

    public ProfileManagementPurchaserModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.changeUserRoleService = new ChangeUserRoleService();
        this.fetchListOfUsersService = new FetchListOfUsersService(); // Initialize the new service;
    }

    public boolean changeUserRole(User user, String newRole) throws UserExistenceException, OutOfRoleException, NotLoggedInException {
        try {
            return changeUserRoleService.process(registry, clientCallback, user, newRole);
        } catch (RuntimeException e) {
            // Handle any runtime exceptions
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

