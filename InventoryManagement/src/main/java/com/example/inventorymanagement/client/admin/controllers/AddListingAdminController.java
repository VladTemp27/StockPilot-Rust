package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.AddListingAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class AddListingAdminController implements ControllerInterface {
    /**
     * Variables
     */
    @FXML
    private TextField itemNameField;
    @FXML
    private Button okButton;

    /**
     * Other Variables
     */
    private AddListingAdminModel addListingAdminModel;
    private MainController mainController;
    boolean initialized = false;

    /**
     * Getters
     */
    public TextField getItemNameField() {
        return itemNameField;
    }
    public Button getOkButton() {
        return okButton;
    }

    /**
     * Default Constructor
     */
    public AddListingAdminController (){
    }

    /**
     * Parameterized Constructor
     * @param clientCallback for client handling
     * @param userService for users
     * @param iOService for item orders
     * @param itemService for items
     * @param registry for rmi server
     * @param mainController to set as main
     */
    public AddListingAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.addListingAdminModel = new AddListingAdminModel(registry, clientCallback);
    }


    //Interface Implementation

    /**
     * Fetches data from the model and updates the UI components accordingly.
     * This method is called to refresh the UI with the latest data.
     *
     * @throws RemoteException If there is a problem with the remote communication.
     */
    @Override
    public void fetchAndUpdate() throws RemoteException {
        okButton.setOnAction(actionEvent -> {
            try {
                handleOkButton(actionEvent);
            } catch (NotLoggedInException e) {
                showAlert("User not logged in.");
            } catch (OutOfRoleException e) {
                showAlert("User does not have required permission.");
            }
        });
    }

    /**
     * Retrieves a string representation of the objects used by this controller.
     * This method is typically used for logging or debugging purposes.
     *
     * @return A string representation of the objects used by this controller.
     * @throws RemoteException If there is a problem with the remote communication.
     */
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "Item";
    }

    //Event Handlers

    /**
     * Method for handling event of mouseclick
     * @param actionEvent
     * @throws NotLoggedInException
     * @throws OutOfRoleException
     */
    @FXML
    private void handleOkButton(ActionEvent actionEvent) throws NotLoggedInException, OutOfRoleException {
        handleAddListing();
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Method to implement model logic
     */
    @FXML
    private void handleAddListing() {
        try{
            String newItemListing = itemNameField.getText();
            Item newItem = new Item();
            newItem.setItemName(newItemListing);
            addListingAdminModel.addListing(newItem);
            showSucess("Item added successfuly!");
        } catch (NotLoggedInException e) {
            showAlert(e.getMessage());
        } catch (OutOfRoleException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * method to initialize ui components
     */
    @FXML
    public void initialize() {
        addHoverEffect(okButton);
        okButton.setOnAction(actionEvent -> {
            try {
                handleOkButton(actionEvent);
            } catch (NotLoggedInException e) {
                showAlert("User not logged in.");
            } catch (OutOfRoleException e) {
                showAlert("User does not have required permission");
            }
        });

        addListingAdminModel = new AddListingAdminModel(MainController.registry, MainController.clientCallback);
        if (!initialized){
            initialized = true;
            if (okButton != null && itemNameField != null){
                addHoverEffect(okButton);
                okButton.setOnAction(actionEvent -> {
                    try {
                        if (addListingAdminModel != null){
                            handleOkButton(actionEvent);
                        }else {
                            System.out.println("AddListingAdminModel is null");
                        }
                    } catch (NotLoggedInException e) {
                        showAlert("User not Logged In");
                    } catch (OutOfRoleException e) {
                        showAlert("User does not have required permission.");
                    }
                });
            }
        }
    }

    //Helper Methods

    /**
     * Adds hover effect to the specified button.
     *
     * @param button The button to which hover effect needs to be added.
     */
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    /**
     * Method for error prompt
     * @param message to be shown
     */
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method for success prompt
     * @param message to be shown
     */
    private void showSucess(String message){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
