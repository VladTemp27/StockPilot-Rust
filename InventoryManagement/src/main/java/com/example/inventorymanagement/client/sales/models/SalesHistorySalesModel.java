package com.example.inventorymanagement.client.sales.models;

import com.example.inventorymanagement.client.microservices.FetchItemService;
import com.example.inventorymanagement.client.microservices.FetchSalesInvoicesService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class SalesHistorySalesModel {
    private FetchSalesInvoicesService fetchSalesInvoicesService;
    private FetchItemService fetchItemService;
    private Registry registry;
    private ClientCallback callback;


    public SalesHistorySalesModel(Registry registry, ClientCallback clientCallback) {
        this.fetchItemService = new FetchItemService();
        this.fetchSalesInvoicesService = new FetchSalesInvoicesService();
        this.registry = registry;
        this.callback = clientCallback;
    }
    public LinkedList<ItemOrder> fetchItems() throws NotLoggedInException {
        try {
            // Fetch items using FetchListOfItemsService
            return fetchSalesInvoicesService.process(registry, callback);
        } catch (RuntimeException | OutOfRoleException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return new LinkedList<>(); // Or throw an exception
        }
    }
    public Item fetchItem(int ID) throws NotLoggedInException {
        return fetchItemService.process(callback, registry, ID);
    }
}

