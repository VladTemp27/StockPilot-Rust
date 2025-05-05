package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.ProfileManagementAdminModel;
import com.example.inventorymanagement.client.admin.models.ProfileManagementChangePassAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.LogOutMicroservice;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
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

public class ProfileManagementAdminController implements ControllerInterface {
    @FXML
    private BorderPane borderPaneProfileManagement;
    @FXML
    private Label profileManagementLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private ComboBox<String> changeUserAccountComboBox;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button logoutButton;
    private ListView<User> userListView;

    // Getters for FXML components


    public BorderPane getBorderPaneProfileManagement() {
        return borderPaneProfileManagement;
    }

    public Label getProfileManagementLabel() {
        return profileManagementLabel;
    }

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public ComboBox<String> getChangeUserAccountComboBox() {
        return changeUserAccountComboBox;
    }

    public Button getChangePasswordButton() {
        return changePasswordButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }


    private ProfileManagementAdminModel profileManagementAdminModel;

    private MainController mainController;

    public ProfileManagementAdminController() {
        //Default constructor
    }

    public ProfileManagementAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.profileManagementAdminModel = new ProfileManagementAdminModel(registry, clientCallback);
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public String getObjectsUsed() throws RemoteException {
        return "user";
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }
    private void addHoverEffect(ComboBox<String> comboBox){
        comboBox.setOnMouseEntered(e -> comboBox.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        comboBox.setOnMouseExited(e -> comboBox.setStyle("-fx-background-color: #EAD7D7;"));
    }

    // Action handlers
    @FXML
    private void handleChangePassword() {
        ProfileManagementChangePassAdminModel pManagementCPAC = new ProfileManagementChangePassAdminModel(MainController.registry, clientCallback);
        try {
            new ProfileManagementChangePassAdminController(new Stage());
        } catch (Exception e) {
            // Show an error dialog to the user
            showErrorDialog("Error", "Failed to open change password window.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangeUserRole() {
        String newRole = changeUserAccountComboBox.getValue();
        User currentUser;
        try {
            currentUser = MainController.clientCallback.getUser();
        } catch (RemoteException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch current user information.");
            return;
        }

        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch current user information.");
            return;
        }

        try {
            boolean success = profileManagementAdminModel.changeUserRole(currentUser, newRole);
            if (success) {
                // Handle successful role change
                showAlert(Alert.AlertType.INFORMATION, "Role Change", "Role changed successfully.");
                // Optionally, update the UI to reflect the role change
            } else {
                // Handle unsuccessful role change
                showAlert(Alert.AlertType.ERROR, "Role Change Error", "Failed to change role.");
            }
        } catch (UserExistenceException | OutOfRoleException | NotLoggedInException e) {
            // Handle specific exceptions
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }


    // Helper method to display an error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        try {
            LogOutMicroservice.process(MainController.clientCallback, MainController.registry);
            Platform.exit();
            System.exit(0);
        }catch (NotLoggedInException e){
            showAlert("User is already Logged Out");
        }
    }
    @FXML
    private void handleSave(){
        if (mainController != null) {
            mainController.openProfileManagementCPAdminPanel();
        } else {
            System.out.println("MainController is not set.");
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
        // Combo box choices
        changeUserAccountComboBox.setPromptText("Change User Role");
        changeUserAccountComboBox.getItems().addAll("Sales", "Purchaser", "Admin");

        addHoverEffect(changePasswordButton);
        addHoverEffect(logoutButton);
        addHoverEffect(changeUserAccountComboBox);

        // Add action handlers
        changePasswordButton.setOnAction(event -> handleSave());
        logoutButton.setOnAction(event -> handleLogout());
        changeUserAccountComboBox.setOnAction(event -> handleChangeUserRole());
        profileManagementAdminModel = new ProfileManagementAdminModel(registry, clientCallback);
        if (!initialized) { // Check if already initialized
            initialized = true; // Set the flag to true

            // Check if UI components are not null
            if (changeUserAccountComboBox != null && changePasswordButton != null && logoutButton != null) {
                addHoverEffect(changePasswordButton);
                addHoverEffect(logoutButton);
                addHoverEffect(changeUserAccountComboBox);
                changePasswordButton.setOnAction(event -> handleSave());
                logoutButton.setOnAction(event -> handleLogout());
                changeUserAccountComboBox.setOnAction(event -> handleChangeUserRole());

                try {
                    if (profileManagementAdminModel != null) {
                        fetchAndUpdate();
                    } else {
                        // Handle the case where profileManagementAdminModel is null
                        System.out.println("Profile Management Admin Model is null.");
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

    private class ProfileManagementChangePassAdminController {
        public ProfileManagementChangePassAdminController(Stage stage) {
        }
    }
}


