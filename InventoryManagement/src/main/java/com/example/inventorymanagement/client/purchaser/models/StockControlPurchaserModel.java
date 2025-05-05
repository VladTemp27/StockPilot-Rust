package com.example.inventorymanagement.client.purchaser.models;

import com.example.inventorymanagement.client.microservices.FetchListOfItemsService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class StockControlPurchaserModel {
    private FetchListOfItemsService fetchListOfItems;
    private Registry registry;
    private ClientCallback callback;

    public StockControlPurchaserModel(Registry registry, ClientCallback clientCallback) {
        this.fetchListOfItems = new FetchListOfItemsService();
        this.registry = registry;
        this.callback = clientCallback;
    }

    public LinkedList<Item> fetchItems () throws NotLoggedInException {
        try {
            // Fetch items using FetchListOfItemsService
            return fetchListOfItems.process(registry, callback);
        } catch (RuntimeException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return new LinkedList<>(); // Or throw an exception
        }
    }
}

