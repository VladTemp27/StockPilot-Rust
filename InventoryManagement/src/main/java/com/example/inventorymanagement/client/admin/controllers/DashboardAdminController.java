package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.DashboardAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;


public class DashboardAdminController implements ControllerInterface {
    @FXML
    private BorderPane borderPaneAdminDashboard;
    @FXML
    private Button addUserButton;
    @FXML
    private TextField searchButton;
    @FXML
    private Label usersActiveLabel;
    @FXML
    private StackedBarChart<String, Number> monthRevChart;
    @FXML
    private Button dateButton;
    @FXML
    private Label dateLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label lowStockProductsLabel;
    @FXML
    private Label todayTransactionsLabel;
    @FXML
    private Label topProductsLabel;
    @FXML
    private TableView<User> usersActiveTableView;
    @FXML
    private TableColumn<User, String> usernameTableColumn;
    @FXML
    private TableColumn<User, String> roleTableColumn;
    @FXML
    private TableView<Item> lowStockProductsTableView;
    @FXML
    private TableView<Item> topProductsTableView;
    @FXML
    private TableView<ItemOrder> transTodayTableView;
    @FXML
    private TableColumn<ItemOrder, String> transactionIDTableColumn;
    @FXML
    private TableColumn<ItemOrder, String> dateTableColumn;
    @FXML
    private TableColumn<ItemOrder, Double> amountTableColumn;
    @FXML
    private TableColumn<Item, String> lowStockProductTableColumn;

    private DashboardAdminModel dashboardAdminModel;
    private MainController mainController;
    private AddUserAdminController addUserAdminController;

    public BorderPane getBorderPaneAdminDashboard() {
        return borderPaneAdminDashboard;
    }

    public TextField getSearchButton() {
        return searchButton;
    }

    public Button getAddUserButton() {
        return addUserButton;
    }

    public Label getUsersActiveLabel() {
        return usersActiveLabel;
    }

    public Label getTodayTransactionsLabel() {
        return todayTransactionsLabel;
    }

    public Label getTopProductsLabel() {
        return topProductsLabel;
    }

    public Label getLowStockProductsLabel() {
        return lowStockProductsLabel;
    }

    public Button getDateButton() {
        return dateButton;
    }

    public Label getDayLabel() {
        return dayLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public StackedBarChart<String, Number> getMonthRevChart() {
        return monthRevChart;
    }

    public TableView<User> getUsersActiveTableView() {
        return usersActiveTableView;
    }

    public TableColumn<User, String> getUsernameTableColumn() {
        return usernameTableColumn;
    }

    public TableColumn<User, String> getRoleTableColumn() {
        return roleTableColumn;
    }

    public TableView<Item> getLowStockProductsTableView() {
        return lowStockProductsTableView;
    }

    public TableView<Item> getTopProductsTableView() {
        return topProductsTableView;
    }

    public TableView<ItemOrder> getTransTodayTableView() {
        return transTodayTableView;
    }

    public TableColumn<ItemOrder, String> getTransactionIDTableColumn() {
        return transactionIDTableColumn;
    }

    public TableColumn<ItemOrder, String> getDateTableColumn() {
        return dateTableColumn;
    }

    public TableColumn<ItemOrder, Double> getAmountTableColumn() {
        return amountTableColumn;
    }
    public TableColumn<Item, String> getLowStockProductTableColumn(){
        return lowStockProductTableColumn;
    }

    public DashboardAdminController(){
        // Default Constructor
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    boolean initialized = false;

    public DashboardAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.dashboardAdminModel = new DashboardAdminModel(registry, clientCallback);
    }
    public void updateMonthlyRevenueChart(LinkedHashMap<Integer, Float> monthlyRevenueData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");
        int maxMonth = getMaxMonth(monthlyRevenueData.keySet());

        // Add data to the series
        for (int month = 1; month <= maxMonth; month++) {
            Float revenue = monthlyRevenueData.get(month);
            series.getData().add(new XYChart.Data<>(String.valueOf(month), revenue != null ? revenue : 0));
        }

        monthRevChart.getData().clear();
        monthRevChart.getData().add(series);

        // Get the x-axis of the chart
        CategoryAxis xAxis = (CategoryAxis) monthRevChart.getXAxis();

        // Clear existing categories
        xAxis.getCategories().clear();

        // Add categories with increments of 5
        for (int i = 1; i <= maxMonth; i += 5) {
            xAxis.getCategories().add("Month " + i);
        }
    }

    // Helper method to get the maximum month from the data
    private int getMaxMonth(Set<Integer> months) {
        int maxMonth = 0;
        for (Integer month : months) {
            maxMonth = Math.max(maxMonth, month);
        }
        return maxMonth;
    }

    public void updateActiveUsersLabel(int activeUsersCount) {
        Platform.runLater(() -> {
            usersActiveLabel.setText("Active Users: " + activeUsersCount);
        });
    }

    public void updateLowestStock(LinkedList<Item> lowestStockData) {
        ObservableList<Item> items = FXCollections.observableArrayList(lowestStockData);
        lowStockProductsTableView.setItems(items);
    }

    public void updateTransactionsToday(LinkedList<ItemOrder> transactionsTodayData) {
        ObservableList<ItemOrder> itemOrderList = FXCollections.observableArrayList(transactionsTodayData);
        transTodayTableView.setItems(itemOrderList);
    }

    @Override
    public void fetchAndUpdate() throws RemoteException {
        Platform.runLater(() -> {
            try {
                // Fetch data from the model
                LinkedHashMap<Integer, Float> monthlyRevenueData = dashboardAdminModel.fetchMonthlyRevenue();
                LinkedList<User> activeUsersData = dashboardAdminModel.fetchActiveUsers();
                LinkedList<ItemOrder> transactionsTodayData = dashboardAdminModel.fetchTransactionsToday();
                LinkedList<Item> lowestStockData = dashboardAdminModel.fetchLowestStock();


                // Update the stacked bar chart for monthly revenue
                updateMonthlyRevenueChart(monthlyRevenueData);

                // Update the label for active users count
                updateActiveUsersLabel(activeUsersData.size());

                // Update the lowest stock of products
                updateLowestStock(lowestStockData);

                // update the transactions today data
                updateTransactionsToday(transactionsTodayData);

                // Populate the active users table
                populateUsersActiveTableView(activeUsersData);

                // Populate the today's transaction table
                populateTransTodayTableView(transactionsTodayData);

            } catch (NotLoggedInException | OutOfRoleException e) {
                e.printStackTrace(); // Handle exceptions appropriately
            }
        });
        System.out.println("updated");
    }
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "user,item,itemorder";
    }
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateUsersActiveTableView(LinkedList<User> activeUsers) {
        // Check if the TableView and TableColumn are not null
        if (usersActiveTableView != null && usernameTableColumn != null && roleTableColumn != null) {
            ObservableList<User> observableUsers = FXCollections.observableArrayList(activeUsers);
            usersActiveTableView.setItems(observableUsers);

            // Make sure the cell value factories are set for the table columns
            usernameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsername()));
            roleTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRole()));
        } else {
            System.out.println("Error: Table or columns are null. Cannot populate active users table.");
        }
    }

    private void populateLowStockProductsTableView( LinkedList<Item> lowStockItems){
        // for lowest stock product table
        // Check if the TableView and TableColumn are not null
        if (lowStockProductsTableView !=null && lowStockProductTableColumn !=null){
            ObservableList<Item> observableItems = FXCollections.observableArrayList(lowStockItems);
            lowStockProductsTableView.setItems(observableItems);

            //Make sure the cell value factories are set for the table
            lowStockProductTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItemName()));
        } else {
            System.out.println("Error: Table or column is null. Cannot populate table.");
        }
    }
    private void populateTransTodayTableView(LinkedList<ItemOrder> transactionsTodayData) {
        // Check if the TableView and TableColumn are not null
        if (transTodayTableView != null && transactionIDTableColumn != null && dateTableColumn != null && amountTableColumn != null) {
            // Create an ObservableList of transaction data
            ObservableList<ItemOrder> observableTransactions = FXCollections.observableArrayList(transactionsTodayData);
            // Set the items to the TableView
            transTodayTableView.setItems(observableTransactions);

            // Make sure the cell value factories are set for the table columns
            transactionIDTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getOrderID())));
            dateTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
            amountTableColumn.setCellValueFactory(cellData -> {
                ItemOrder cOrder = cellData.getValue();
                double totalValue = cOrder.getOrderDetails().stream().mapToDouble(orderDetail -> {
                    return orderDetail.getQty() * orderDetail.getUnitPrice();
                }).sum();
                return new SimpleDoubleProperty(totalValue).asObject();
            });
        } else {
            System.out.println("Error: Table or columns are null. Cannot populate table.");
        }
    }

    @FXML
    private void handleSave(){
        if (mainController != null) {
            mainController.openAddUserAdminPanel();
        } else {
            System.out.println("MainController is not set.");
        }
    }

    public void setAddUserAdminController(AddUserAdminController addUserAdminController) {
        this.addUserAdminController = addUserAdminController;
    }

    public void initialize() {
        // Set the current date
        LocalDate currentDate = LocalDate.now();
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        dateLabel.setText(formattedDate);

        // Set the current day
        String currentDay = currentDate.getDayOfWeek().toString();
        dayLabel.setText(currentDay);

        // Update time label every second
        updateTimeLabel();

        // Set up table columns
        transactionIDTableColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountTableColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));

        addHoverEffect(addUserButton);
        addUserButton.setOnAction(event -> handleSave());
        dashboardAdminModel = new DashboardAdminModel(MainController.registry,MainController.clientCallback );
        if (!initialized){
            initialized = true;

            // Check if UI components are not null
            if (usersActiveTableView !=null && usernameTableColumn !=null && roleTableColumn !=null){
                addHoverEffect(addUserButton);
                addUserButton.setOnAction(event -> handleSave());

                try{
                    // Fetch and populate active users count
                    int activeUsersCount = dashboardAdminModel.fetchActiveUsers().size();
                    updateActiveUsersLabel(activeUsersCount);

                    // Populate active users table
                    populateUsersActiveTableView(dashboardAdminModel.fetchActiveUsers());

                    // Populate other tables
                    if (dashboardAdminModel !=null){
                        populateLowStockProductsTableView(dashboardAdminModel.fetchLowestStock());
                        populateTransTodayTableView(dashboardAdminModel.fetchTransactionsToday());
                    }

                    // Fetch and update monthly revenue chart
                    LinkedHashMap<Integer, Float> monthlyRevenueData = dashboardAdminModel.fetchMonthlyRevenue();
                    updateMonthlyRevenueChart(monthlyRevenueData);

                } catch (NotLoggedInException e) {
                    System.out.println("User not logged in.");
                } catch (OutOfRoleException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Error: Table or button is null. Cannot initialize table.");
            }

            try {
                MainController.clientCallback.setCurrentPanel(this);
                boolean success = UpdateCallback.process(MainController.clientCallback, MainController.registry);
                System.out.println(success);
            } catch (NotLoggedInException e){
                showAlert("User is not logged in");
            } catch (RemoteException e) {
                System.out.println(e.getMessage());
            }
        }
    }




    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    // Method to update the time label
    private void updateTimeLabel() {
        Thread updateTimeThread = new Thread(() -> {
            while (true) {
                LocalDateTime currentTime = LocalDateTime.now();
                String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.forLanguageTag("fil-PH")));

                Platform.runLater(() -> {
                    timeLabel.setText(formattedTime);
                });

                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
    }
}

