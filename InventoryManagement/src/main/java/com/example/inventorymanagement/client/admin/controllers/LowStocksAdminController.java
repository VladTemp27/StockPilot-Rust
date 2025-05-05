package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.LowStocksAdminModel;
import com.example.inventorymanagement.client.admin.views.LowStocksAdminPanel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class LowStocksAdminController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private TableView lowStockTable;
    @FXML
    private TableColumn<Item, String> productColumn;
    @FXML
    private TableColumn<Item, String> quantityColumn;

    /**
     * Controller Variables
     */
    private LowStocksAdminModel lowStocksAdminModel;
    private MainController mainController;
    boolean initialized = false;

    /**
     * Getters
     */
    public TableView getLowStockTable() {
        return lowStockTable;
    }
    public TableColumn getProductColumn() {
        return productColumn;
    }
    public TableColumn getQuantityColumn() {
        return quantityColumn;
    }

    /**
     * Default constructor for LowStocksAdminController.
     */
    public LowStocksAdminController(){
        // Default Constructor
    }

    /**
     * Constructor for LowStocksAdminController.
     * Initializes the controller with necessary services and references.
     *
     * @param clientCallback The client callback for server communication.
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @param mainController The main controller instance.
     */
    public LowStocksAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.lowStocksAdminModel = new LowStocksAdminModel(registry, clientCallback);
    }

    /**
     * For table population
     * @param items to be populated in the table
     */
    public void populateTableView(LinkedList<Item> items){
        if(lowStockTable != null && productColumn != null && quantityColumn != null){
            ObservableList<Item> observableList = FXCollections.observableArrayList(items);
            lowStockTable.setItems(observableList);

            productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));
            quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalQty()).asObject().asString());
        } else {
            System.out.println("Table or columns are null. Cannot populate");
        }
    }

    /**
     * Method to show for exceptions (for user toleration)
     * @param message to be showed to user
     */
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action event for checking low stocks.
     */
    private void handleLowStocks() {
        // TODO: Logic
    }

    /**
     * Fetches and updates data remotely.
     * This method is called to update the data displayed in the UI.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void fetchAndUpdate() throws RemoteException {
        try {
            LinkedList<Item> items = lowStocksAdminModel.fetchLowStocks();
            populateTableView(items);
        } catch (NotLoggedInException e) {
            showAlert("User not logged in");
        }
    }

    /**
     * Gets the objects used.
     * This method returns a string indicating the type of objects used by the controller.
     *
     * @return A string representing the objects used.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "item";
    }

    /**
     * Initializes the controller.
     * This method sets up the UI components and initializes the data model.
     */
    @FXML
    public void initialize(){
        lowStocksAdminModel = new LowStocksAdminModel(MainController.registry, MainController.clientCallback);
        if (!initialized){
            initialized = true;
            if (lowStockTable != null){
                try{
                    if (lowStocksAdminModel != null){
                        populateTableView(lowStocksAdminModel.fetchLowStocks());
                    } else {
                        System.out.println("Low Stocks Model is null.");
                    }
                } catch (NotLoggedInException e) {
                    showAlert("User not logged in");
                }
            } else {
                System.out.println("Table is null. Cannot Initialize");
            }
        }
    }
}
