package com.example.inventorymanagement.client.admin.models;

import com.example.inventorymanagement.client.microservices.FetchActiveUsersService;
import com.example.inventorymanagement.client.microservices.FetchLowestStockService;
import com.example.inventorymanagement.client.microservices.FetchMonthlyRevenueService;
import com.example.inventorymanagement.client.microservices.FetchTransTodayService;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class DashboardAdminModel {

    private Registry registry;
    private ClientCallback clientCallback;
    private FetchMonthlyRevenueService fetchMonthlyRevenueService;
    private FetchActiveUsersService fetchActiveUsersService;
    private FetchTransTodayService fetchTransTodayService;
    private FetchLowestStockService fetchLowestStockService;

    public DashboardAdminModel(Registry registry, ClientCallback clientCallback) {
        this.registry = registry;
        this.clientCallback = clientCallback;
        this.fetchMonthlyRevenueService = new FetchMonthlyRevenueService();
        this.fetchActiveUsersService = new FetchActiveUsersService();
        this.fetchTransTodayService = new FetchTransTodayService();
        this.fetchLowestStockService = new FetchLowestStockService();
    }

    public ObservableList<XYChart.Data<String, Number>> getMonthlyRevenueChartData() throws NotLoggedInException, OutOfRoleException {
        LinkedHashMap<Integer, Float> monthlyRevenue = fetchMonthlyRevenue();
        ObservableList<XYChart.Data<String, Number>> data = FXCollections.observableArrayList();

        for (Map.Entry<Integer, Float> entry : monthlyRevenue.entrySet()) {
            data.add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
        }

        return data;
    }

    public LinkedList<User> fetchActiveUsers() throws NotLoggedInException, OutOfRoleException {
        try {
            return fetchActiveUsersService.process(registry, clientCallback);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList<ItemOrder> fetchTransactionsToday() throws NotLoggedInException {
        try {
            return fetchTransTodayService.process(registry, clientCallback);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedList<Item> fetchLowestStock() throws NotLoggedInException {
        try {
            return fetchLowestStockService.process(registry, clientCallback);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LinkedHashMap<Integer, Float> fetchMonthlyRevenue() throws NotLoggedInException, OutOfRoleException {
        try {
            return FetchMonthlyRevenueService.process(registry, clientCallback);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }
}

