package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.DashboardAdminModel;
import com.example.inventorymanagement.client.admin.models.FinancesAdminModel;
import com.example.inventorymanagement.client.admin.views.DashboardAdminPanel;
import com.example.inventorymanagement.client.admin.views.FinancesAdminPanel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;


import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;

public class FinancesAdminController implements ControllerInterface {
    @FXML
    private BorderPane borderPaneFinancesAdmin;
    @FXML
    private TextField searchBar;
    @FXML
    private Button grossRevenueBg;
    @FXML
    private Button taxDeductableBg;
    @FXML
    private Button salesWorthBg;
    @FXML
    private Button grossProfitsBg;
    @FXML
    private Label helloLabel;
    @FXML
    private Label grossRevenueLabel;
    @FXML
    private Label taxDeductableLabel;
    @FXML
    private Label salesWorthLabel;
    @FXML
    private Label grossProfitLabel;
    @FXML
    private Label grossRevenueAmount;
    @FXML
    private Label taxDeductableAmount;
    @FXML
    private Label salesWorthAmount;
    @FXML
    private Label grossProfitsAmount;
    @FXML
    private ImageView grossRevenueIcon;
    @FXML
    private ImageView taxDeductableIcon;
    @FXML
    private ImageView salesWorthIcon;
    @FXML
    private ImageView grossProfitsIcon;
    @FXML
    private StackedBarChart revenueCostChart;
    @FXML
    private Label analyticsTrackingLabel;
    @FXML
    private Button dateBg;
    @FXML
    private Button timeBg;
    @FXML
    private Button dateWhiteBg;
    @FXML
    private Label dayLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Circle clockFace;
    @FXML
    private Line hourHand;
    @FXML
    private Line minuteHand;
    @FXML
    private Label dateTodayLabel;
    private Label grossRevenueAmountLabel;
    private Label taxDeductableAmountLabel;
    private Label salesWorthAmountLabel;
    private Label grossProfitsAmountLabel;
    private MainController mainController;

    private FinancesAdminModel financesAdminModel;

    public BorderPane getBorderPaneFinancesAdmin() {
        return borderPaneFinancesAdmin;
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public Button getGrossRevenueBg() {
        return grossRevenueBg;
    }

    public Button getTaxDeductableBg() {
        return taxDeductableBg;
    }

    public Button getSalesWorthBg() {
        return salesWorthBg;
    }

    public Button getGrossProfitsBg() {
        return grossProfitsBg;
    }

    public Label getHelloLabel() {
        return helloLabel;
    }

    public Label getGrossRevenueLabel() {
        return grossRevenueLabel;
    }

    public Label getTaxDeductableLabel() {
        return taxDeductableLabel;
    }

    public Label getSalesWorthLabel() {
        return salesWorthLabel;
    }

    public Label getGrossProfitLabel() {
        return grossProfitLabel;
    }

    public Label getGrossRevenueAmount() {
        return grossRevenueAmount;
    }

    public Label getTaxDeductableAmount() {
        return taxDeductableAmount;
    }

    public Label getSalesWorthAmount() {
        return salesWorthAmount;
    }

    public Label getGrossProfitsAmount() {
        return grossProfitsAmount;
    }

    public ImageView getGrossRevenueIcon() {
        return grossRevenueIcon;
    }

    public ImageView getTaxDeductableIcon() {
        return taxDeductableIcon;
    }

    public ImageView getSalesWorthIcon() {
        return salesWorthIcon;
    }

    public ImageView getGrossProfitsIcon() {
        return grossProfitsIcon;
    }

    public StackedBarChart getRevenueCostChart() {
        return revenueCostChart;
    }

    public Label getAnalyticsTrackingLabel() {
        return analyticsTrackingLabel;
    }

    public Button getDateBg() {
        return dateBg;
    }

    public Button getTimeBg() {
        return timeBg;
    }

    public Button getDateWhiteBg() {
        return dateWhiteBg;
    }

    public Label getDayLabel() {
        return dayLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Circle getClockFace() {
        return clockFace;
    }

    public Line getHourHand() {
        return hourHand;
    }

    public Line getMinuteHand() {
        return minuteHand;
    }

    public Label getDateTodayLabel() {
        return dateTodayLabel;
    }
    public FinancesAdminController(){
        // Default constructor
    }

    boolean initialized = false;

    public FinancesAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.financesAdminModel = new FinancesAdminModel(registry, clientCallback);
    }

    public void updateRevenueCostChart(LinkedHashMap<Integer, Float> monthlyRevenueData, LinkedHashMap<Integer, Float> monthlyCostData){
        MainController.getFinancesAdminController().revenueCostChart.getData().clear();

        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("this month's Revenue");

        XYChart.Series<String, Number> costSeries = new XYChart.Series<>();
        costSeries.setName("this month's Cost");

        // Add data for monthly revenue
        for (Integer month : monthlyRevenueData.keySet()) {
            revenueSeries.getData().add(new XYChart.Data<>(getMonthName(month), monthlyRevenueData.get(month)));
        }

        // Add data for monthly cost
        for (Integer month : monthlyCostData.keySet()) {
            costSeries.getData().add(new XYChart.Data<>(getMonthName(month), monthlyCostData.get(month)));
        }
        MainController.getFinancesAdminController().revenueCostChart.getData().addAll(revenueSeries, costSeries);
    }

    // Method to get the name of the month from its number (1-based)
    private String getMonthName(int monthNumber) {
        return Month.of(monthNumber).name();
    }


    @Override
    public void fetchAndUpdate() throws RemoteException {
        Platform.runLater(() -> {
            try {
                // Fetch data from the model
                LinkedHashMap<Integer, Float> monthlyRevenueData = financesAdminModel.fetchMonthlyRevenue();
                LinkedHashMap<Integer, Float> monthlyCostData = financesAdminModel.fetchMonthlyCost();
//            float revenueTodayData = financesAdminModel.fetchRevenueToday();
//            float costTodayData = financesAdminModel.fetchCostToday();

                // Compute gross revenue
                float grossRevenue = financesAdminModel.computeGrossRevenue(monthlyRevenueData);

                // Compute tax deductible
                float taxDeductable = financesAdminModel.computeTaxDeductible(monthlyRevenueData);

                // Compute stock worth
                float stockWorth = financesAdminModel.computeStockWorth(monthlyCostData);

                // Compute gross profit
                float grossProfit = financesAdminModel.computeGrossProfit(grossRevenue, stockWorth);

                DecimalFormat df = new DecimalFormat("###,###,###,##0.00");

                MainController.getFinancesAdminController().grossRevenueAmount.setText("₱" + df.format(grossRevenue));
                System.out.println("₱" + df.format(grossRevenue));
                MainController.getFinancesAdminController().taxDeductableAmount.setText("₱" + df.format(taxDeductable));
                System.out.println("₱" + df.format(taxDeductable));
                MainController.getFinancesAdminController().salesWorthAmount.setText("₱" + df.format(stockWorth));
                System.out.println("₱" + df.format(stockWorth));
                MainController.getFinancesAdminController().grossProfitsAmount.setText("₱" + df.format(grossProfit));
                System.out.println("₱" + df.format(grossProfit));

                // Update the stacked bar chart for monthly revenue vs. cost
                updateRevenueCostChart(monthlyRevenueData, monthlyCostData);


            } catch (NotLoggedInException | OutOfRoleException e) {
                e.printStackTrace();
            }
        });

    }
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "itemorder";
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        financesAdminModel = new FinancesAdminModel(MainController.registry, MainController.clientCallback);


        // Set the current date
        LocalDate currentDate = LocalDate.now();
        dateTodayLabel.setText(currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));

        // Set the current day
        String currentDay = currentDate.getDayOfWeek().toString();
        dayLabel.setText(currentDay);

        // Update time label every second
        updateTimeLabel();
        // Fetch revenue data from the model and update the labels
        try {
            LinkedHashMap<Integer, Float> monthlyRevenueData = financesAdminModel.fetchMonthlyRevenue();
            float grossRevenue = financesAdminModel.computeGrossRevenue(monthlyRevenueData);
            grossRevenueAmount.setText("Amount: P " + grossRevenue);

            LinkedHashMap<Integer, Float> grossCost = financesAdminModel.fetchMonthlyCost();
            float taxDeductable = financesAdminModel.computeTaxDeductible(grossRevenue, grossCost);
            taxDeductableAmount.setText("Amount: P " + taxDeductable);

            float salesWorth = financesAdminModel.computeStockWorth(financesAdminModel.fetchMonthlyCost());
            salesWorthAmount.setText("Amount: P " + salesWorth);

            float grossProfit = financesAdminModel.computeGrossProfit(grossRevenue, grossCost);
            grossProfitsAmount.setText("Amount: P " + grossProfit);

        } catch (NotLoggedInException | OutOfRoleException e) {
            e.printStackTrace();
            // Handle exceptions
        }

        if (!initialized){
            initialized = true;

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

    // Method to update the time label
    private void updateTimeLabel() {
        Thread updateTimeThread = new Thread(() -> {
            while (true) {
                LocalDateTime currentTime = LocalDateTime.now();
                String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.forLanguageTag("fil-PH")));
                Platform.runLater(() -> timeLabel.setText(formattedTime));

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


