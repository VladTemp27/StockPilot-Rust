package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.CreateItemListingService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;

import java.rmi.registry.Registry;

public class AddListingAdminModel {
    private Registry registry;
    private ClientCallback clientCallback;
    private CreateItemListingService createItemListingService;

    public AddListingAdminModel(Registry registry, ClientCallback clientCallback){
        this.registry = registry;
        this.clientCallback = clientCallback;
        new  CreateItemListingService();
    }

    public void addListing(Item item) throws NotLoggedInException, OutOfRoleException {
        createItemListingService.process(registry, clientCallback,item);
    }
}
