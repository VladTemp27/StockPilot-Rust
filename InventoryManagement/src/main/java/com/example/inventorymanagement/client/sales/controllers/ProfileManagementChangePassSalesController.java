package com.example.inventorymanagement.client.sales.controllers;

import com.example.inventorymanagement.client.admin.models.ProfileManagementAdminModel;
import com.example.inventorymanagement.client.admin.models.ProfileManagementChangePassAdminModel;
import com.example.inventorymanagement.client.admin.views.ProfileManagementChangePassAdminPanel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.client.purchaser.models.ProfileManagementChangePassPurchaserModel;
import com.example.inventorymanagement.client.sales.models.ProfileManagementChangePassSalesModel;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.exceptions.UserExistenceException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.ResourceBundle;

import static com.example.inventorymanagement.client.common.controllers.MainController.clientCallback;
import static com.example.inventorymanagement.client.common.controllers.MainController.registry;

public class ProfileManagementChangePassSalesController  implements ControllerInterface {
    @FXML
    private BorderPane borderPaneProfileManagementChangePass;
    @FXML
    private ImageView personIconImage;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label changePasswordLabel;
    @FXML
    private TextField oldPasswordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private Button saveButton;
    private ListView<User> userListView;

    // Getter methods of FXMl components

    public BorderPane getBorderPaneProfileManagementChangePass() {
        return borderPaneProfileManagementChangePass;
    }

    public ImageView getPersonIconImage() {
        return personIconImage;
    }

    public Label getChangePasswordLabel() {
        return changePasswordLabel;
    }

    public TextField getOldPasswordTextField() {
        return oldPasswordTextField;
    }

    public TextField getNewPasswordTextField() {
        return newPasswordTextField;
    }

    public Label getUsernameLabel() {
        return usernameLabel;
    }

    public Button getSaveButton() {
        return saveButton;
    }
    private ProfileManagementChangePassSalesModel profileManagementChangePassSalesModel;
    private MainController mainController;

    public ProfileManagementChangePassSalesController(){
        // Default Constructor
    }

    public ProfileManagementChangePassSalesController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController) {
        this.profileManagementChangePassSalesModel = new ProfileManagementChangePassSalesModel(registry, clientCallback);
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

    public String getObjectsUsed() throws RemoteException {
        return "user";
    }

    private void addHoverEffect (Button button){
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }
    @FXML
    private void handleSave() {
        String oldPassword = MainController.getProfileManagementChangePassSalesController().oldPasswordTextField.getText();
        String newPassword = MainController.getProfileManagementChangePassSalesController().newPasswordTextField.getText();

        try{
            User currentUser = MainController.clientCallback.getUser();
            if(!(currentUser.getPassword().equals(oldPassword))) {
                throw new RuntimeException("Incorrect Password");
            }
            else if(newPassword != null) {
                profileManagementChangePassSalesModel.changePassword(currentUser,newPassword);
                showSuccessDialog("Your password has been change.");
            } else {
                showErrorDialog("Error", "Please fill in new password field.");
            }
        }catch(RemoteException  e){

        } catch (UserExistenceException e) {
            throw new RuntimeException(e);
        } catch (OutOfRoleException e) {
            throw new RuntimeException(e);
        } catch (NotLoggedInException e) {
            throw new RuntimeException(e);
        }catch(RuntimeException e){
            if(e.getMessage().equals("Incorrect Password")){
                showAlert("Incorrect Password" + e.getMessage());
            }
        }
    }

    // Helper method to show an error dialog
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessDialog(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void updateUsernameLabel() {
        try {
            usernameLabel.setText(clientCallback.getUser().getUsername());
        } catch (RemoteException e) {
            //Prompt user unable to fetch User object
        }
    }
    @FXML
    public void initialize() {
        System.out.println("initialize sales");
        addHoverEffect(saveButton);
        //add action handlers
        saveButton.setOnAction(event -> handleSave());
        profileManagementChangePassSalesModel = new ProfileManagementChangePassSalesModel(registry, clientCallback);
        if (!initialized) {
            initialized = true;
            try {
                if (profileManagementChangePassSalesModel != null) {
                    fetchAndUpdate();
                }
                else {
                    System.out.println("Profile Management Change Pass Admin Model is null");
                }
            } catch (Exception e) {
                System.out.println("User is not logged in");
            }
        }else {
            System.out.println("Error: Save button is null. Cannot Initialize");
        }
    }
}




