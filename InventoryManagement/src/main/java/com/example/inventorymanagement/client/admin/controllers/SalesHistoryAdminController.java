package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.SalesHistoryAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.objects.Stock;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.beans.property.SimpleFloatProperty;
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

public class SalesHistoryAdminController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private BorderPane borderPaneSalesHistoryAdmin;
    @FXML
    private Button createSalesInvoiceAdminButton;
    @FXML
    private TextField searchFieldAdmin;
    @FXML
    private TableView salesHistoryAdminTable;
    @FXML
    private TableColumn<ItemOrder, Integer> orderIDColumn;
    @FXML
    private TableColumn<ItemOrder, String> dateColumn;
    @FXML
    private TableColumn<ItemOrder, String> productColumn;
    @FXML
    private TableColumn<ItemOrder, String> priceColumn;
    @FXML
    private TableColumn<ItemOrder, String> quantityColumn;
    @FXML
    private TableColumn<ItemOrder, String> totalSalesColumn;

    /**
     * Controller Variables
     */
    private MainController mainController;
    private SalesHistoryAdminModel salesHistoryAdminModel;
    boolean initialized = false;

    /**
     * Default constructor for SalesHistorySalesController.
     */
    public SalesHistoryAdminController() {
        // Default Constructor
    }

    /**
     * Constructor for SalesHistoryAdminController.
     * Initializes the controller with necessary services and references.
     *
     * @param clientCallback The client callback for server communication.
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @param mainController The main controller instance.
     */
    public SalesHistoryAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.salesHistoryAdminModel = new SalesHistoryAdminModel(registry, clientCallback);
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
     * Getters
     */
    @FXML
    public BorderPane getBorderPaneSalesHistoryAdmin() {
        return borderPaneSalesHistoryAdmin;
    }
    @FXML
    public Button getcreateSalesInvoiceAdminButton() {
        return createSalesInvoiceAdminButton;
    }
    @FXML
    public TextField getSearchFieldAdmin() {
        return searchFieldAdmin;
    }
    @FXML
    public TableView getsalesHistoryAdminTable() {
        return salesHistoryAdminTable;
    }
    public TableColumn getDateColumn() { return dateColumn;}
    public TableColumn getProductColumn() { return productColumn;}
    public TableColumn getPriceColumn() { return priceColumn;}
    public TableColumn getQuantityColumn() { return quantityColumn;}
    public TableColumn getTotalSalesColumn() { return totalSalesColumn;}

    //Controller Implementation

    /**
     * Fetches and updates data remotely.
     * This method is called to update the data displayed in the UI.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void fetchAndUpdate() throws RemoteException {
        try {
            LinkedList<ItemOrder> itemOrders = salesHistoryAdminModel.fetchItems();
            populateTableView(itemOrders);
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
        return "itemorder";
    }

    //UI Handling
    /**
     * Handles the action event for sales invoice.
     */
    @FXML
    private void handleCreateSalesInvoice() {
        if (mainController != null) {
            mainController.openSalesInvoiceAdminPanel();
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
        addHoverEffect(createSalesInvoiceAdminButton);

        createSalesInvoiceAdminButton.setOnAction(event -> handleCreateSalesInvoice());
        salesHistoryAdminModel = new SalesHistoryAdminModel(MainController.registry, MainController.clientCallback);
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (salesHistoryAdminTable != null && createSalesInvoiceAdminButton != null) {
                addHoverEffect(createSalesInvoiceAdminButton);
                createSalesInvoiceAdminButton.setOnAction(event -> handleCreateSalesInvoice());

                try {
                    if (salesHistoryAdminModel != null) {
                        populateTableView(salesHistoryAdminModel.fetchItems());
                    } else {
                        // Handle the case where salesHistoryAdminModel is null
                        System.out.println("Sales History Admin Model is null.");
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
     * Populates the table view with the given list of items.
     * @param itemOrders The list of items to populate the table with.
     */
    private void populateTableView(LinkedList<ItemOrder> itemOrders) {
        ObservableList<ItemOrder> observableItems = FXCollections.observableArrayList(itemOrders);
        salesHistoryAdminTable.setItems(observableItems);

        orderIDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrderID()).asObject());
        dateColumn.setCellValueFactory(cellData -> {
            ItemOrder itemOrder = cellData.getValue();
            if (itemOrder.getOrderDetails().isEmpty()) {
                return new SimpleStringProperty("");
            }
            StringBuilder dateString = new StringBuilder();
            for (OrderDetail orderDetail : itemOrder.getOrderDetails()) {
                dateString.append(itemOrder.getDate()).append("\n");
            }
            return new SimpleStringProperty(dateString.toString().trim());
        });//Lambda for populating dateColumn

        productColumn.setCellValueFactory(cellData -> {
            ItemOrder itemOrder = cellData.getValue();
            StringBuilder itemList = new StringBuilder();
            for (OrderDetail orderDetail : itemOrder.getOrderDetails()) {
                int itemId = orderDetail.getItemId();
                try {
                    Item item = salesHistoryAdminModel.fetchItem(itemId);
                    if (item != null) {
                        itemList.append(item.getItemName()).append("\n");
                    }
                } catch (NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
            }
            return new SimpleStringProperty(itemList.toString().trim());
        });//Lambda to populate product column

        quantityColumn.setCellValueFactory(cellData -> {
            ItemOrder itemOrder = cellData.getValue();
            StringBuilder quantityList = new StringBuilder();
            for (OrderDetail orderDetail : itemOrder.getOrderDetails()) {
                quantityList.append(orderDetail.getQty()).append("\n");
            }
            return new SimpleStringProperty(quantityList.toString().trim());
        });//lambda to populate quantity column

        priceColumn.setCellValueFactory(cellData -> {
            ItemOrder itemOrder = cellData.getValue();
            StringBuilder priceList = new StringBuilder();
            for (OrderDetail orderDetail : itemOrder.getOrderDetails()) {
                priceList.append("₱")
                        .append(String.format("%.2f", orderDetail.getUnitPrice()))
                        .append("\n");
            }
            return new SimpleStringProperty(priceList.toString().trim());
        });//lamda to populate priceColumn

        totalSalesColumn.setCellValueFactory(cellData -> {
            ItemOrder order = cellData.getValue();
            float totalCost = order.getOrderDetails().stream()
                    .map(detail -> {
                        return detail.getQty() * detail.getUnitPrice();
                    })
                    .reduce(0.0f, Float::sum);
            return new SimpleStringProperty("₱" + String.format("%.2f", totalCost));
        });//lambda to populate total sales column
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
}