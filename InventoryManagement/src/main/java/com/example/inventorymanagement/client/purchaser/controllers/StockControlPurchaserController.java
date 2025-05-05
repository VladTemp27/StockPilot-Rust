package com.example.inventorymanagement.client.purchaser.controllers;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
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
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class StockControlPurchaserController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private BorderPane borderPaneStockControlPurchaser;
    @FXML
    private Button lowStocksButtonPurchaser;
    @FXML
    private Button addItemButtonPurchaser;
    @FXML
    private TextField searchFieldPurchaser;
    @FXML
    private TableView stockControlPurchaserTable;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> totalQtyColumn;

    /**
     * Controller Variables
     */
    private StockControlPurchaserModel stockControlPurchaserModel;
    private MainController mainController;
    boolean initialized = false;

    /**
     * Getters
     */
    @FXML
    public BorderPane getBorderPaneStockControlPurchaser() {
        return borderPaneStockControlPurchaser;
    }
    @FXML
    public Button getLowStocksButton() {
        return lowStocksButtonPurchaser;
    }
    @FXML
    public Button getAddItemButton() { return addItemButtonPurchaser;}
    @FXML
    public TextField getSearchFieldPurchaser() { return searchFieldPurchaser; }
    @FXML
    public TableView getStockControlPurchaserTable() { return stockControlPurchaserTable; }

    /**
     * Default constructor for StockControlPurchaserController.
     */
    public StockControlPurchaserController() {
        // Default Constructor
    }

    /**
     * Constructor for StockControlPurchaserController.
     * Initializes the controller with necessary services and references.
     *
     * @param clientCallback The client callback for server communication.
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @param mainController The main controller instance.
     */
    public StockControlPurchaserController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.stockControlPurchaserModel = new StockControlPurchaserModel(registry, clientCallback);
        this.mainController = mainController;
    }

    /**
     * Populates the table view with the given list of items.
     *
     * @param items The list of items to populate the table with.
     */
    private void populateTableView(LinkedList<Item> items) {
        if (stockControlPurchaserTable != null && itemNameColumn != null && totalQtyColumn != null) {
            ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
            stockControlPurchaserTable.setItems(observableItems);

            // Make sure the cell value factories are set for the table columns
            itemNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));
            totalQtyColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalQty()).asObject());
        } else {
            System.out.println("Error: Table or columns are null. Cannot populate table.");
        }
    }

    /**
     * Sets the main controller instance.
     *
     * @param mainController The main controller instance.
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Adds hover effect to the given button.
     *
     * @param button The button to add hover effect to.
     */
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    /**
     * Shows an alert dialog with the given message.
     *
     * @param message The message to display in the alert dialog.
     */
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action event for adding an item.
     */
    @FXML
    private void handleAddItem() {
        if (mainController != null) {
            mainController.openAddItemPurchaserPanel();
        } else {
            System.out.println("MainController is not set.");
        }
    }

    /**
     * Handles the action event for checking low stocks.
     */
    @FXML
    private void handleLowStocks() {
        if (mainController != null) {
            mainController.openLowStocksPurchaserPanel();
        } else {
            System.out.println("MainController is not set.");
        }
    }

    /**
     * Initializes the controller.
     * This method sets up the UI components and initializes the data model.
     */
    @FXML
    public void initialize() { // initialize components -> better approach is to initialize just the components and let nav___bar buttons handle the population of data/realtime
        addHoverEffect(lowStocksButtonPurchaser);
        addHoverEffect(addItemButtonPurchaser);

        lowStocksButtonPurchaser.setOnAction(event -> handleLowStocks());
        addItemButtonPurchaser.setOnAction(event -> handleAddItem());

        stockControlPurchaserModel = new StockControlPurchaserModel(MainController.registry, MainController.clientCallback);
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (stockControlPurchaserTable != null && addItemButtonPurchaser != null & lowStocksButtonPurchaser != null) {
                addHoverEffect(addItemButtonPurchaser);
                addHoverEffect(lowStocksButtonPurchaser);
                lowStocksButtonPurchaser.setOnAction(event -> handleLowStocks());
                addItemButtonPurchaser.setOnAction(event -> handleAddItem());

                try {
                    if (stockControlPurchaserModel != null) {
                        populateTableView(stockControlPurchaserModel.fetchItems());
                    } else {
                        // Handle the case where stockControlPurchaserModel is null
                        System.out.println("Stock Control Purchaser Model is null.");
                    }
                } catch (NotLoggedInException e) {
                    // Show prompt to user not logged in
                    System.out.println("User is not logged in.");
                }
            } else {
                // Handle the case where UI components are null
                System.out.println("Error: Table or button is null. Cannot initialize.");
            }
        }
        try {
            MainController.clientCallback.setCurrentPanel(this);
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (NotLoggedInException e){
            showAlert("User is not logged in");
        } catch (RemoteException e) {
            System.out.println(e.getMessage());
        }
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
            LinkedList<Item> items = stockControlPurchaserModel.fetchItems();
            populateTableView(items);
        } catch (NotLoggedInException e) {
            showAlert("Error occurred while fetching items: " + e.getMessage());
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
        return "item,itemorder";
    }
}
