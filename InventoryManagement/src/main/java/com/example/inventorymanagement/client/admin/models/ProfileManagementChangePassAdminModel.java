package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.ChangePasswordService;
import com.example.inventorymanagement.client.microservices.FetchListOfUsersService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class ProfileManagementChangePassAdminModel {
    private ChangePasswordService changePasswordService;
    private FetchListOfUsersService fetchListOfUsersService;
    private Registry registry;
    private ClientCallback clientCallback;

    public ProfileManagementChangePassAdminModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.changePasswordService = new ChangePasswordService();
        this.fetchListOfUsersService = new FetchListOfUsersService();
    }

    public boolean changePassword(User user, String newPassword) throws UserExistenceException, OutOfRoleException, NotLoggedInException {
        try {
            return changePasswordService.process(registry, clientCallback, user, newPassword);
        } catch (RuntimeException e) {
            // Handle any runtime exceptions
            e.printStackTrace();
            return false;
        }
    }

    @Deprecated
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
