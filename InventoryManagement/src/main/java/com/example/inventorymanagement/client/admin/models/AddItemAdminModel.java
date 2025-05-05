package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.CreatePurchaseOrderService;
import com.example.inventorymanagement.client.microservices.FetchListOfItemsService;
import com.example.inventorymanagement.client.microservices.FetchSuppliersService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;

import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;

public class AddItemAdminModel {
    private Registry registry;
    private ClientCallback clientCallback;
    private CreatePurchaseOrderService createPurchaseOrderService;
    private FetchListOfItemsService fetchListOfItemsService;
    private FetchSuppliersService fetchSuppliersService;

    public AddItemAdminModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.createPurchaseOrderService = new CreatePurchaseOrderService();
        this.fetchListOfItemsService = new FetchListOfItemsService();
        this.fetchSuppliersService = new FetchSuppliersService();
    }

    public boolean createPurchaseOrder(ItemOrder purchaseOrder) throws OutOfRoleException, NotLoggedInException {
        return createPurchaseOrderService.process(registry, clientCallback, purchaseOrder);
    }

    public LinkedList<Item> fetchListOfItems() throws NotLoggedInException {
        return fetchListOfItemsService.process(registry, clientCallback);
    }

    public ArrayList<String> fetchSuppliers() throws NotLoggedInException {
        return fetchSuppliersService.process(registry, clientCallback);
    }
}
