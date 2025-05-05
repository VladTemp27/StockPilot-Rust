package com.example.inventorymanagement.client.purchaser.controllers;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.purchaser.models.LowStocksPurchaserModel;
import com.example.inventorymanagement.client.purchaser.models.StockControlPurchaserModel;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class LowStocksPurchaserController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private TextField searchField;
    @FXML
    private TableView lowStocksTable;
    @FXML
    private TableColumn<Item, String> productColumn;
    @FXML
    private TableColumn<Item, String> quantityLeftColumn;

    /**
     * Controller Variables
     */
    private LowStocksPurchaserModel lowStocksPurchaserModel;
    private MainController mainController;
    boolean initialized=false;

    /**
     * Getters
     */
    public TextField getSearchField() { return searchField;}
    public TableView getLowStocksTable() { return lowStocksTable;}
    public TableColumn getProductColumn() { return productColumn;}
    public TableColumn getQuantityLeftColumn() { return quantityLeftColumn;}

    /**
     * Default constructor for LowStocksPurchaserController.
     */
    public LowStocksPurchaserController(){
        // Default Constructor
    }

    /**
     * Constructor for LowStocksPurchaserController.
     * Initializes the controller with necessary services and references.
     *
     * @param clientCallback The client callback for server communication.
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @param mainController The main controller instance.
     */
    public LowStocksPurchaserController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.lowStocksPurchaserModel = new LowStocksPurchaserModel(registry, clientCallback);
    }

    /**
     * For table population
     * @param items to be populated in the table
     */
    private void populateTableView(LinkedList<Item> items) {
        if (lowStocksTable != null && productColumn != null && quantityLeftColumn != null) {
            ObservableList<Item> observableList = FXCollections.observableArrayList(items);
            lowStocksTable.setItems(observableList);

            productColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));
            quantityLeftColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalQty()).asObject().asString());
        } else {
            System.out.println("Error: Table and/or Columns are null. Populate canceled");
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
            LinkedList<Item> items = lowStocksPurchaserModel.fetchLowStocks();
            populateTableView(items);
        } catch (NotLoggedInException e) {
            showAlert("User not logged in.");
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
        lowStocksPurchaserModel = new LowStocksPurchaserModel(MainController.registry, MainController.clientCallback);
        if (!initialized){
            initialized = true;
            if (lowStocksTable != null){
                try{
                    if (lowStocksPurchaserModel != null){
                        populateTableView(lowStocksPurchaserModel.fetchLowStocks());
                    } else {
                        System.out.println("Low Stocks Model is null.");
                    }
                } catch (NotLoggedInException e) {
                    showAlert("User not logged in");
                }
            } else {
                System.out.println("Low Stocks Table is null");
            }
        }
    }
}
