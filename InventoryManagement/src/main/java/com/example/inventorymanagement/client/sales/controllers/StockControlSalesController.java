package com.example.inventorymanagement.client.sales.controllers;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.client.sales.models.StockControlSalesModel;
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

public class StockControlSalesController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private BorderPane borderPaneStockControlSales;
    @FXML
    private Button createSalesInvoiceButtonSales;
    @FXML
    private TextField searchFieldSales;
    @FXML
    private TableView stockControlSalesTable;
    @FXML
    private TableColumn<Item, String> itemNameColumn;
    @FXML
    private TableColumn<Item, Integer> totalQtyColumn;

    /**
     * Controller Variables
     */
    private StockControlSalesModel stockControlSalesModel;
    private MainController mainController;
    boolean initialized = false;

    /**
     * Getters
     */
    @FXML
    public BorderPane getBorderPaneStockControlSales() {
        return borderPaneStockControlSales;
    }
    @FXML
    public Button getCreateSalesInvoiceButtonSales() { return createSalesInvoiceButtonSales;}
    @FXML
    public TextField getSearchFieldSales() { return searchFieldSales; }
    @FXML
    public TableView getStockControlSalesTable() { return stockControlSalesTable; }

    /**
     * Default constructor for StockControlSalesController.
     */
    public StockControlSalesController(){
        // Default Constructor
    }

    /**
     * Constructor for StockControlSalesController.
     * Initializes the controller with necessary services and references.
     *
     * @param clientCallback The client callback for server communication.
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @param mainController The main controller instance.
     */
    public StockControlSalesController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.stockControlSalesModel = new StockControlSalesModel(registry, clientCallback);
        this.mainController = mainController;
    }

    /**
     * Populates the table view with the given list of items.
     *
     * @param items The list of items to populate the table with.
     */
    private void populateTableView(LinkedList<Item> items) {
        if (stockControlSalesTable != null && itemNameColumn != null && totalQtyColumn != null) {
            ObservableList<Item> observableItems = FXCollections.observableArrayList(items);
            stockControlSalesTable.setItems(observableItems);

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
     * Handles the action event for creating a sales invoice.
     */
    @FXML
    private void handleCreateSalesInvoice() {
        if (mainController != null) {
            mainController.openSalesInvoiceSalesPanel();
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
        addHoverEffect(createSalesInvoiceButtonSales);

        createSalesInvoiceButtonSales.setOnAction(event -> handleCreateSalesInvoice());
        stockControlSalesModel = new StockControlSalesModel(MainController.registry, MainController.clientCallback);
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (stockControlSalesTable != null && createSalesInvoiceButtonSales != null) {
                addHoverEffect(createSalesInvoiceButtonSales);
                createSalesInvoiceButtonSales.setOnAction(event -> handleCreateSalesInvoice());

                try {
                    if (stockControlSalesModel != null) {
                        populateTableView(stockControlSalesModel.fetchItems());
                    } else {
                        // Handle the case where stockControlSalesModel is null
                        System.out.println("Stock Control Sales Model is null.");
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
            LinkedList<Item> items = stockControlSalesModel.fetchItems();
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
