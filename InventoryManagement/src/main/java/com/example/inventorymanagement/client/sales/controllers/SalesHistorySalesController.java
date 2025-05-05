package com.example.inventorymanagement.client.sales.controllers;

import com.example.inventorymanagement.client.sales.models.SalesHistorySalesModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

public class SalesHistorySalesController implements ControllerInterface {
    //Variables
    @FXML
    private BorderPane borderPaneSalesHistorySales;
    @FXML
    private Button createSalesInvoiceSalesButton;
    @FXML
    private TextField searchFieldSales;
    @FXML
    private TableView salesHistorySalesTable;
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
    boolean initialized = false;
    private MainController mainController;
    private SalesHistorySalesModel salesHistorySalesModel;

    //Constructors
    public SalesHistorySalesController() {

    }
    public SalesHistorySalesController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.salesHistorySalesModel = new SalesHistorySalesModel(registry, clientCallback);
    }

    //MainController Setter
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    //Getters
    public BorderPane getBorderPaneSalesHistorySales() {
        return borderPaneSalesHistorySales;
    }
    public Button getcreateSalesInvoiceSalesSalesButton() {
        return createSalesInvoiceSalesButton;
    }
    public TextField getSearchFieldSales() {
        return searchFieldSales;
    }
    public TableView getsalesHistorySalesTable() {
        return salesHistorySalesTable;
    }
    public TableColumn getOrderIDColumn() {
        return orderIDColumn;
    }
    public TableColumn getDateColumn() {
        return dateColumn;
    }
    public TableColumn getProductColumn() {
        return productColumn;
    }
    public TableColumn getPriceColumn() {
        return priceColumn;
    }
    public TableColumn getQuantityColumn() {
        return quantityColumn;
    }
    public TableColumn getTotalSalesColumn() {
        return totalSalesColumn;
    }

    @Override
    public void fetchAndUpdate() throws RemoteException {
        try {
            LinkedList<ItemOrder> itemOrders = salesHistorySalesModel.fetchItems();
            populateTableView(itemOrders);
        } catch (NotLoggedInException e) {
            showAlert("Error occurred while fetching items: " + e.getMessage());
        }
    }

    /**
     * For populating table view in fxml
     * @param itemOrders objects to populate table with
     */
    private void populateTableView(LinkedList<ItemOrder> itemOrders) {
        ObservableList<ItemOrder> observableItems = FXCollections.observableArrayList(itemOrders);
        salesHistorySalesTable.setItems(observableItems);

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
                    Item item = salesHistorySalesModel.fetchItem(itemId);
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

    @Override
    public String getObjectsUsed() throws RemoteException {
        return "itemorder";
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSalesInvoice() {
        if (mainController != null) {
            mainController.openSalesInvoiceSalesPanel();
        } else {
            System.out.println("MainController is not set.");
        }
    }

    @FXML
    public void initialize() { // initialize components -> better approach is to initialize just the components and let nav___bar buttons handle the population of data/realtime
        addHoverEffect(createSalesInvoiceSalesButton);

        createSalesInvoiceSalesButton.setOnAction(event -> handleSalesInvoice());
        salesHistorySalesModel = new SalesHistorySalesModel(MainController.registry, MainController.clientCallback);
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (salesHistorySalesTable != null && createSalesInvoiceSalesButton != null) {
                addHoverEffect(createSalesInvoiceSalesButton);
                createSalesInvoiceSalesButton.setOnAction(event -> handleSalesInvoice());

                try {
                    if (salesHistorySalesModel != null) {
                        populateTableView(salesHistorySalesModel.fetchItems());
                    } else {
                        // Handle the case where salesHistorySalesModel is null
                        System.out.println("Sales History Sales Model is null.");
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
}
