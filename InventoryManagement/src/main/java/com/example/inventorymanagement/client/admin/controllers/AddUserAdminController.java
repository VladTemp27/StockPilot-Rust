package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.AddUserAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;

import static com.example.inventorymanagement.client.common.controllers.MainController.clientCallback;
import static com.example.inventorymanagement.client.common.controllers.MainController.registry;

public class AddUserAdminController implements ControllerInterface {

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button saveButton;

    private AddUserAdminModel addUserAdminModel;
    private MainController mainController;

    // Getter methods of FXML Components
    public ComboBox<String> getRoleComboBox() {
        return roleComboBox;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public AddUserAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.addUserAdminModel = new AddUserAdminModel(registry, clientCallback);
    }
    public AddUserAdminController(){
        //Default Constructor
    }

    boolean initialized = false;

    public void fetchAndUpdate() throws RemoteException {
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
        String newUserUsername = usernameField.getText(); // Get the new username from the input field
        String newUserPassword = passwordField.getText(); // Get the new password from the input field
        String newUserRole = roleComboBox.getValue(); // Get the selected role from the combo box

        // Check if any of the fields are empty
        if (newUserUsername.isEmpty() || newUserPassword.isEmpty() || newUserRole == null) {
            showErrorDialog("Error", "Please fill in all fields.");
            return;
        }

        // Create a new User object with the provided username, password, and role
        User newUser = new User(newUserUsername, newUserPassword, newUserRole);

        // Call the addUserService method from the model to add the new user
        boolean success = addUserAdminModel.addUserService(newUser);
        if (success) {
            // User addition was successful
            showInformationDialog("Success", "User added successfully.");
        } else {
            // User addition failed
            showErrorDialog("Error", "Failed to add user.");
        }
    }


    // Helper method to show an information dialog
    private void showInformationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Helper method to show an error dialog
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void initialize() {
        addUserAdminModel = new AddUserAdminModel(registry,clientCallback);
        // Create a list of choices
        ObservableList<String> choices = FXCollections.observableArrayList("Sales", "Purchaser", "Admin");

        // Set the choices to the ComboBox
        roleComboBox.setItems(choices);

        // Set font style for ComboBox
        Font font = new Font("Share Tech Mono", 15);
        roleComboBox.setStyle("-fx-font-family: '" + font.getFamily() + "'; -fx-font-size: " + font.getSize() + "px;");

        // Add hover effect to the save button
        addHoverEffect(saveButton);

        // action handlers
        saveButton.setOnAction(event -> handleSave());
        if (!initialized) {
            initialized = true;
            try {
                if (addUserAdminModel != null) {
                    fetchAndUpdate();
                }
                else {
                    System.out.println("Add User Model is null");
                }
            } catch (RemoteException e) {
                System.out.println("User is not logged in");
            }
        }else {
            System.out.println("Error: Save button is null. Cannot Initialize");
        }
    }
}
