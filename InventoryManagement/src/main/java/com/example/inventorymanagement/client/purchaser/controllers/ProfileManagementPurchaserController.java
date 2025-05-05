package com.example.inventorymanagement.client.purchaser.controllers;

import com.example.inventorymanagement.client.admin.controllers.ProfileManagementAdminController;
import com.example.inventorymanagement.client.admin.models.ProfileManagementChangePassAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.LogOutMicroservice;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.client.purchaser.models.ProfileManagementChangePassPurchaserModel;
import com.example.inventorymanagement.client.purchaser.models.ProfileManagementPurchaserModel;
import com.example.inventorymanagement.client.purchaser.views.ProfileManagementChangePassPurchaserPanel;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.LinkedList;

import static com.example.inventorymanagement.client.common.controllers.MainController.clientCallback;
import static com.example.inventorymanagement.client.common.controllers.MainController.registry;
public class ProfileManagementPurchaserController  implements ControllerInterface {
    @FXML
    private BorderPane borderPaneProfileManagement;
    @FXML
    private Label profileManagementLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button logoutButton;
    private ListView<User> userListView;

    // Getters for FXML components

    @FXML
    public BorderPane getBorderPaneProfileManagement() {
        return borderPaneProfileManagement;
    }

    @FXML
    public Label getProfileManagementLabel() {
        return profileManagementLabel;
    }

    @FXML
    public Label getUsernameLabel() {
        return usernameLabel;
    }

    @FXML
    public Button getChangePasswordButton() {
        return changePasswordButton;
    }

    @FXML
    public Button getLogoutButton() {
        return logoutButton;
    }

    private ProfileManagementPurchaserModel profileManagementPurchaserModel;

    private MainController mainController;

    public ProfileManagementPurchaserController() {
        //Default constructor
    }

    public ProfileManagementPurchaserController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.profileManagementPurchaserModel = new ProfileManagementPurchaserModel(registry, clientCallback);
    }
    public void setMainController(MainController mainController){
        this.mainController = mainController;
    }


    boolean initialized = false;

    public void fetchAndUpdate() throws RemoteException {
        try {
            // Update UI components with current user's username
            updateUsernameLabel();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error fetching current user information.");
        }
    }
    // Helper method to display alerts
    private void showAlert(String message){ // for main controller alerts
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getObjectsUsed() throws RemoteException {
        return "user";
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    // Action handlers
//    @FXML
//    private void handleChangePassword() {
//        ProfileManagementChangePassPurchaserModel pManagementCPPC = new ProfileManagementChangePassPurchaserModel(MainController.registry, clientCallback);
//        try {
//            new ProfileManagementChangePassPurchaserController(new Stage());
//        } catch (Exception e) {
//            // Show an error dialog to the user
//            showErrorDialog("Error", "Failed to open change password window.");
//            e.printStackTrace();
//        }
//    }
    // Helper method to display an error dialog
//    private void showErrorDialog(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }

    @FXML
    private void handleLogout() {
        try {
            LogOutMicroservice.process(MainController.clientCallback, MainController.registry);
        } catch (NotLoggedInException e) {
            showAlert("User is already Logged Out");
        }
        Platform.exit();
        System.exit(0);
    }
    @FXML
    private void handleSave(){
        if (mainController !=null){
            mainController.openProfileManagementCPPurchaserPanel();
        } else {
            System.out.println("Main controller is not set.");
        }
    }

    @FXML
    public void updateUsernameLabel() {
        try {
            usernameLabel.setText(clientCallback.getUser().getUsername());
        } catch (RemoteException e) {
            showAlert("Error:" + e.getMessage());
        }
    }
    // Initialization method

    @FXML
    public void initialize() {
        profileManagementPurchaserModel = new ProfileManagementPurchaserModel(registry, clientCallback);

        addHoverEffect(changePasswordButton);
        addHoverEffect(logoutButton);

        // Add action handlers
        changePasswordButton.setOnAction(event -> handleSave());
        logoutButton.setOnAction(event -> handleLogout());
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (changePasswordButton != null && logoutButton != null) {
                addHoverEffect(changePasswordButton);
                addHoverEffect(logoutButton);
                changePasswordButton.setOnAction(event -> handleSave());
                logoutButton.setOnAction(event -> handleLogout());

                try {
                    if (profileManagementPurchaserModel != null) {
                        fetchAndUpdate();
                    } else {
                        // Handle the case where profileManagementPurchaserModel is null
                        System.out.println("Profile Management Purchaser Model is null.");
                    }
                } catch (Exception e) {
                    // Show prompt to user not logged in
                    System.out.println("User is not logged in.");
                }
            } else {
                // Handle the case where UI components are null
                System.out.println("Error: ComboBox or Button is null. Cannot initialize.");
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
