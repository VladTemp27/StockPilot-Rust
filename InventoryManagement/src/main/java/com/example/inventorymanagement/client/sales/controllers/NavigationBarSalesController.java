package com.example.inventorymanagement.client.sales.controllers;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.client.sales.views.StockControlSalesPanel;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class NavigationBarSalesController implements ControllerInterface {
    @FXML
    private BorderPane borderPaneNavigationBarSales;
    @FXML
    private Button stockControlButtonSales;
    @FXML
    private Button salesHistoryButtonSales;
    @FXML
    private Button profileButtonSales;

    // Reference to the main BorderPane
    private BorderPane mainPane;
    private MainController mainController;
    public NavigationBarSalesController() {

    }
    public NavigationBarSalesController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.mainController = mainController;
    }

    // Setter for main BorderPane
    public void setMainPane(BorderPane mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public void fetchAndUpdate() throws RemoteException {
        // No implementation needed in this controller
    }

    @Override
    public String getObjectsUsed() throws RemoteException {
        return null;
    }

    @FXML
    private void initialize() {
        // Handle button clicks
        stockControlButtonSales.setOnAction(event -> loadStockControlPanel());
        salesHistoryButtonSales.setOnAction(event -> loadSalesHistoryPanel());
        profileButtonSales.setOnAction(event -> loadProfileManagementPanel());

        addHoverEffect(stockControlButtonSales);
        addHoverEffect(salesHistoryButtonSales);
        addHoverEffect(profileButtonSales);
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #967373;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    private void loadStockControlPanel() {
        try {
            MainController.getStockControlSalesController().fetchAndUpdate(); // triggered when btn stock control is click
            mainPane.setRight(MainController.getStockControlSalesPanel()); // get the refresh components of stock control of sales
            MainController.clientCallback.setCurrentPanel(MainController.getStockControlSalesController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSalesHistoryPanel() {
        try {
            MainController.getSalesHistorySalesController().fetchAndUpdate();
            mainPane.setRight(MainController.getSalesHistorySalesPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getSalesHistorySalesController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadProfileManagementPanel() {
        try {
            MainController.getProfileManagementSalesController().fetchAndUpdate();
            mainPane.setRight(MainController.getProfileManagementSalesPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getProfileManagementSalesController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public BorderPane getBorderPaneNavigationBarSales() { return borderPaneNavigationBarSales;}

    @FXML
    public Button getStockControlButtonSales() { return stockControlButtonSales; }

    @FXML
    public Button getSalesHistoryButtonSales() { return salesHistoryButtonSales;}

    @FXML
    public Button getProfileButtonSales() { return profileButtonSales; }
}
