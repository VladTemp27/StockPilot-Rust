package com.example.inventorymanagement.client.sales.models;

import com.example.inventorymanagement.client.microservices.CreateSalesInvoiceService;
import com.example.inventorymanagement.client.microservices.FetchListOfItemsService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;

import java.rmi.registry.Registry;
import java.util.LinkedList;

public class CreateSalesInvoiceSalesModel {

    private Registry registry;
    private ClientCallback clientCallback;
    private CreateSalesInvoiceService createSalesInvoiceService;
    private FetchListOfItemsService fetchListOfItemsService;

    public CreateSalesInvoiceSalesModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.createSalesInvoiceService = new CreateSalesInvoiceService();
        this.fetchListOfItemsService = new FetchListOfItemsService();
    }

    public boolean createSalesInvoice(ItemOrder purchaseOrder) throws OutOfRoleException, NotLoggedInException {
        return createSalesInvoiceService.process(registry, clientCallback, purchaseOrder);
    }

    public LinkedList<Item> fetchListOfItems() throws NotLoggedInException{
        return fetchListOfItemsService.process(registry, clientCallback);
    }
}
