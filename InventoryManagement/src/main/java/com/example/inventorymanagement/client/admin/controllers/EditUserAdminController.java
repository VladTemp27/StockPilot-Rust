package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.EditUserAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.User;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.*;

import static com.example.inventorymanagement.client.common.controllers.MainController.*;

public class EditUserAdminController implements ControllerInterface {

    @FXML
    private BorderPane borderPaneEditUser;
    @FXML
    private TextField newPasswordField;
    @FXML
    private Button changePasswordButton;
    @FXML
    private Button deleteUserButton;
    @FXML
    private ComboBox<String> changeRoleComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private ComboBox<String> selectUserAdminComboBox;
    boolean initialized = false;
    private Map<String, User> userMap = new HashMap<>();

    private EditUserAdminModel editUserAdminModel;
    private ProfileManagementChangePassAdminController profileManagementChangePassAdminController = new ProfileManagementChangePassAdminController();
    private User userToEdit;

    private MainController mainController;
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public EditUserAdminController() {

    }

    // getter methods of FXML components
    public User getUserToEdit(){return userToEdit;}
    public BorderPane getBorderPaneEditUser() {
        return borderPaneEditUser;
    }
    public TextField getNewPasswordField() {
        return newPasswordField;
    }
    public Button getChangePasswordButton() {
        return changePasswordButton;
    }
    public Button getDeleteUserButton() {
        return deleteUserButton;
    }
    public ComboBox<String> getChangeRoleComboBox() {
        return changeRoleComboBox;
    }
    public Button getSaveButton() {
        return saveButton;
    }
    public EditUserAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.editUserAdminModel = new EditUserAdminModel(registry, clientCallback);
        this.mainController = mainController;
    }
    public EditUserAdminController(ProfileManagementChangePassAdminController profileManagementChangePassAdminController) {
        this.profileManagementChangePassAdminController = profileManagementChangePassAdminController;
    }
    public void setUserToEdit(User userToEdit) {
        this.userToEdit = userToEdit;
    }

    @Override
    public void fetchAndUpdate() {
        Platform.runLater( () -> {
            selectUserAdminComboBox.getItems().clear();

            try {
                // Fetch the current users from the model
                LinkedList<User> currentUsers = editUserAdminModel.fetchUsers();
                for (User user : currentUsers) {
                    selectUserAdminComboBox.getItems().add(user.getUsername());
                    userMap.put(user.getUsername(), user);
                }
            }
            catch (OutOfRoleException | NotLoggedInException | RemoteException e) {

            }
        });

    }
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "user";
    }

    //UI Handler
    @FXML
    private void handleSave() {
        User selectedUser = getSelectedUser(); // Get the selected user
        String newRole = changeRoleComboBox.getValue();

        try {
            // Call the changeUserRole method from the model
            boolean success = editUserAdminModel.changeUserRole(selectedUser, newRole);
            if (success) {
                // Role change was successful
                showSucess(true);
            } else {
                // Role change failed
                showSucess(false);
            }
        } catch (RemoteException e) {
            // Handle RemoteException
            e.printStackTrace();
            showSucess(false);
        }
    }

//    @FXML
//    private void handleUserSelectionChange() {
//        // Call fetchAndUpdate to update the list of users in the ComboBox
//        fetchAndUpdate();
//
//        // Get the selected username from the ComboBox
//        String selectedUsername = selectUserAdminComboBox.getValue();
//
//        // Fetch the selected user separately
////        LinkedList<User> selectedUserList = selectUserAdminComboBox.getSelectionModel().getSelectedItem();
////
////        // Check if the selectedUserList is not null and contains at least one user
////        if (selectedUserList != null && !selectedUserList.isEmpty()) {
////            // Since you're fetching by username, assuming there's only one user with the given username
////            User selectedUser = selectedUserList.getFirst(); // Get the first user from the list
////
////            // Call a method to update the UI with the selected user's data
////            editUser(selectedUser);
////        } else {
////            // Handle the case where no user is found with the given username
////            showAlert("User not found.");
////        }
//    }

    @FXML
    public void initialize() throws RemoteException, OutOfRoleException, NotLoggedInException {
        changeRoleComboBox.getItems().addAll("Sales Person", "Purchaser", "Admin");
        changeRoleComboBox.setPromptText("Change Role...");
        Font font = new Font("Share Tech Mono", 15);
        changeRoleComboBox.setStyle("-fx-font-family: '" + font.getFamily() + "'; -fx-font-size: " + font.getSize() + "px;");
        addHoverEffect(saveButton);
        addHoverEffect(changePasswordButton);
        addHoverEffect(deleteUserButton);

        // action handlers
        saveButton.setOnAction(event -> handleSave());
        changePasswordButton.setOnAction(event -> handleChangePasswordButton());
        deleteUserButton.setOnAction(event -> handleDeleteUserButton());
        editUserAdminModel = new EditUserAdminModel(registry, clientCallback);
        if (!initialized){
            initialized = true;
            if (editUserAdminModel !=null){
            }
        } else {
            System.out.println("Error: Save button is null. cannot initialize");
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

    //Helper Methods

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

    private void showSucess(boolean success) {
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? "Success" : "Error");
        alert.setHeaderText(success ? "User details updated successfully!" : "Failed to update user details");
        alert.showAndWait();
    }
    /**
     * Displays a confirmation dialog with the specified message.
     *
     * @param message The message to be displayed in the confirmation dialog.
     * @return {@code true} if the user confirms the action, {@code false} otherwise.
     */
    private boolean confirmAction(String message) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Confirmation Required");
        confirmation.setContentText(message);
        ButtonType okButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmation.getButtonTypes().setAll(okButton, cancelButton);
        Optional<ButtonType> result = confirmation.showAndWait();
        return result.orElse(ButtonType.CANCEL) == okButton;
    }

    public List<User> getCurrentUsers() throws RemoteException, OutOfRoleException, NotLoggedInException {
        try {
            return editUserAdminModel.fetchUsers();
        } catch (RemoteException | OutOfRoleException | NotLoggedInException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void handleChangePasswordButton() {
        String newPassword = newPasswordField.getText();
        try {
            editUserAdminModel.changePassword(getSelectedUser() ,newPassword);
            showSucess(true);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }
    private void handleDeleteUserButton(){
        try {
            User selectedUser = getSelectedUser();

            if (selectedUser != null) {
                // Show the confirmation dialog
                if (confirmAction("Are you sure you want to delete this user?")) {
                    // If the user clicks "Yes" in the confirmation dialog
                    boolean success = editUserAdminModel.removeUser(selectedUser);
                    if (success) {
                        showSucess(true);
                    } else {
                        showSucess(false);
                    }
                } else {
                    // If the user clicks "No" in the confirmation dialog, do nothing
                    // You may choose to show a message or perform any other action here
                }
            } else {
                showAlert("Please select a user before deleting.");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            showSucess(false);
        }
    }
    private User getSelectedUser(){
        String selectedUserName = selectUserAdminComboBox.getValue();
        if (selectedUserName == null){
            showAlert("Please Select a User a User to Edit");
            return null;
        }

        User selectedUser = userMap.get(selectedUserName);
        if (selectedUser == null){
            showAlert("Please select a User to Edit");
        } return selectedUser;
    }

//    public void editUser(User selectedUser) {
//        this.userToEdit = selectedUser;
//        System.out.println(userToEdit);
//
//        // Set the selected user's data as user data of the usernameLabel
//        usernameLabel.setUserData(selectedUser);
//
//        // Set the selected user's current role as the default value of the changeRoleComboBox
//        changeRoleComboBox.setValue(selectedUser.getRole()); // Assuming you have a method to get the user's role
//    }

}
