package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
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

public class NavigationBarAdminController implements ControllerInterface {
    @FXML
    private BorderPane borderPaneNavigationBarAdmin;
    @FXML
    private Button dashboardButtonAdmin;
    @FXML
    private Button financesButtonAdmin;
    @FXML
    private Button salesHistoryButtonAdmin;
    @FXML
    private Button stockControlButtonAdmin;
    @FXML
    private Button profileButtonAdmin;
    @FXML
    private Button userManagementButtonAdmin;

    // Reference to the main BorderPane
    private BorderPane mainPane;
    private MainController mainController;
    public NavigationBarAdminController() {

    }
    public NavigationBarAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
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
        dashboardButtonAdmin.setOnAction(event -> loadDashboardPanel());
        financesButtonAdmin.setOnAction(event -> loadFinancesPanel());
        salesHistoryButtonAdmin.setOnAction(event -> loadSalesHistoryPanel());
        stockControlButtonAdmin.setOnAction(event -> loadStockControlPanel());
        profileButtonAdmin.setOnAction(event -> loadProfileManagementPanel());
        userManagementButtonAdmin.setOnAction(event -> loadEditUserManagementPanel());

        addHoverEffect(userManagementButtonAdmin);
        addHoverEffect(dashboardButtonAdmin);
        addHoverEffect(financesButtonAdmin);
        addHoverEffect(salesHistoryButtonAdmin);
        addHoverEffect(stockControlButtonAdmin);
        addHoverEffect(profileButtonAdmin);
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #967373;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    private void loadDashboardPanel() {
        try {
            MainController.getDashboardAdminController().fetchAndUpdate();
            mainPane.setRight(MainController.getDashboardAdminPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getDashboardAdminController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e){
            throw new RuntimeException();
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFinancesPanel() {
      try{
          MainController.getFinancesAdminController().fetchAndUpdate();
          mainPane.setRight(MainController.getFinancesAdminPanel());
          MainController.clientCallback.setCurrentPanel(MainController.getFinancesAdminController());
          UpdateCallback.process(MainController.clientCallback, MainController.registry);
      } catch (IOException e){
          throw new RuntimeException();
      } catch (NotLoggedInException e) {
          throw new RuntimeException(e);
      }
    }


    private void loadStockControlPanel() {
        try {
            MainController.getStockControlAdminController().fetchAndUpdate(); // triggered when btn stock control is click
            mainPane.setRight(MainController.getStockControlAdminPanel()); // get the refresh components of stock control of admin
            MainController.clientCallback.setCurrentPanel(MainController.getStockControlAdminController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSalesHistoryPanel() {
        try {
            MainController.getSalesHistoryAdminController().fetchAndUpdate();
            mainPane.setRight(MainController.getSalesHistoryAdminPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getSalesHistoryAdminController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadProfileManagementPanel() {
        try {
            MainController.getProfileManagementAdminController().fetchAndUpdate();
            mainPane.setRight(MainController.getProfileManagementAdminPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getProfileManagementAdminController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEditUserManagementPanel() {
        try {
            MainController.getEditUserAdminController().fetchAndUpdate();
            mainPane.setRight(MainController.getEditUserAdminPanel());
            MainController.clientCallback.setCurrentPanel(MainController.getEditUserAdminController());
            UpdateCallback.process(MainController.clientCallback, MainController.registry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public BorderPane getBorderPaneNavigationBarAdmin() { return borderPaneNavigationBarAdmin;}

    @FXML
    public Button getDashboardButtonAdmin() { return dashboardButtonAdmin;}

    @FXML
    public Button getFinancesButtonAdmin() { return financesButtonAdmin;}

    @FXML
    public Button getStockControlButtonAdmin() { return stockControlButtonAdmin; }

    @FXML
    public Button getSalesHistoryButtonAdmin() { return salesHistoryButtonAdmin;}

    @FXML
    public Button getProfileButtonAdmin() { return profileButtonAdmin; }
}
