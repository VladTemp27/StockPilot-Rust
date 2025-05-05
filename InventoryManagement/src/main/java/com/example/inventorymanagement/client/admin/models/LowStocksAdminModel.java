package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.FetchLowestStockService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class LowStocksAdminModel {
    Registry registry;
    ClientCallback clientCallback;
    FetchLowestStockService fetchLowestStockService;

    public LowStocksAdminModel(Registry registry, ClientCallback clientCallback){
        this.registry = registry;
        this.clientCallback = clientCallback;
        new FetchLowestStockService();
    }

    public LinkedList<Item> fetchLowStocks() throws NotLoggedInException {
        return FetchLowestStockService.process(registry, clientCallback);
    }
}
