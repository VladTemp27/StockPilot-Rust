package com.example.inventorymanagement.client.admin.controllers;

import com.example.inventorymanagement.client.admin.models.AddItemAdminModel;
import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.client.microservices.UpdateCallback;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.exceptions.OutOfRoleException;
import com.example.inventorymanagement.util.objects.Item;
import com.example.inventorymanagement.util.objects.ItemOrder;
import com.example.inventorymanagement.util.objects.OrderDetail;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AddItemAdminController implements ControllerInterface {

    /**
     * FXML Controller Variables
     */
    @FXML
    private ComboBox<String> itemNameComboBox;
    @FXML
    private ComboBox<String> supplierComboBox;
    @FXML
    private TextField itemPriceField;
    @FXML
    private TextField quantityField;
    @FXML
    private Button okButton;
    private Map<String, Item> itemMap = new HashMap<>();

    /**
     * Controller Variables
     */
    private MainController mainController;
    private AddItemAdminModel addItemAdminModel;
    boolean initialized = false;

    /**
     * Getters
     */
    public AddItemAdminModel getAddItemAdminModel() {
        return addItemAdminModel;
    }
    public ComboBox<String> geItemNameComboBox() { return itemNameComboBox;}
    public ComboBox<String> getSupplierComboBox() { return supplierComboBox; }
    public TextField getItemPriceField() {
        return itemPriceField;
    }
    public Button getOkButton() { return okButton; }
    public TextField getQuantityField() { return quantityField; }


    /**
     * Default constructor.
     */
    public AddItemAdminController() {
        // Default Constructor
    }

    /**
     * Parameterized constructor.
     * @param clientCallback Callback for client communication.
     * @param userService User service interface.
     * @param iOService Item order service interface.
     * @param itemService Item service interface.
     * @param registry RMI registry.
     * @param mainController Main controller reference.
     */
    public AddItemAdminController(ClientCallback clientCallback, UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry, MainController mainController){
        this.addItemAdminModel = new AddItemAdminModel(registry, clientCallback);
    }

    /**
     * Fetches list of suppliers and updates the UI.
     * @throws RemoteException if a remote communication error occurs.
     */
    private void fetchSuppliersAndUpdate() throws RemoteException {
        try {
            // Fetch list of suppliers
            ArrayList<String> suppliers = addItemAdminModel.fetchSuppliers();

            // Update UI with suppliers data
            Platform.runLater(() -> {
                // Clear existing items in the ComboBox
                supplierComboBox.getItems().clear();

                // Add fetched suppliers to the ComboBox
                supplierComboBox.getItems().addAll(suppliers);
            });

        } catch (NotLoggedInException e) {
            showAlert("Error occurred while fetching suppliers: " + e.getMessage());
        }
    }

    /**
     * Adds hover effect to a button.
     * @param button The button to add hover effect to.
     */
    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#EAD7D7, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #EAD7D7;"));
    }

    /**
     * Shows an alert with the given message.
     * @param message The message to display in the alert.
     */
    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows a process success prompt
     * @param message to be shown
     */
    private void showSuccess(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Looks up an item in the item map by its name.
     *
     * @param itemName The name of the item to look up.
     * @return The item associated with the given name, or null if not found.
     */
    private Item lookupItem(String itemName) {
        return itemMap.get(itemName);
    }

    /**
     * Fetches and updates the list of items in the ComboBox.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public void fetchAndUpdate() throws RemoteException {
        try {
            // Fetch list of items and populate the ComboBox
            LinkedList<Item> itemList = addItemAdminModel.fetchListOfItems();
            for (Item item : itemList) {
                itemNameComboBox.getItems().add(item.getItemName());
                itemMap.put(item.getItemName(), item); // Add item to the map
            }
        } catch (NotLoggedInException e) {
            showAlert("Error occurred while fetching items: " + e.getMessage());
        }
    }

    /**
     * Gets the objects used in this controller.
     * @return A string representing the object used.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "itemorder";
    }

    /**
     * Handles the action event when the OK button is clicked.
     * @param actionEvent The action event triggered by clicking the OK button.
     * @throws NotLoggedInException if the user is not logged in.
     * @throws OutOfRoleException if the user does not have the required permission.
     */
    @FXML
    private void handleOkButton(ActionEvent actionEvent) throws NotLoggedInException, OutOfRoleException {
        handleAddItem();
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action event when the Add Item Button is clicked.
     */
    @FXML
    private void handleAddItem() {
        try {
            if (okButton != null && itemNameComboBox != null && supplierComboBox != null) {
                String selectedItemName = itemNameComboBox.getValue();
                if (selectedItemName == null) {
                    showAlert("Please select an item.");
                    return;
                }
                // Retrieve the actual Item object using the selected name (replace with your implementation
                Item selectedItem = lookupItem(selectedItemName);
                if (selectedItem == null) {
                    showAlert("Error: Item not found.");
                    return;
                }

                String selectedSupplier = supplierComboBox.getValue();
                float unitPrice = Float.parseFloat(itemPriceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());

                if (selectedItem != null && selectedSupplier != null && quantity > 0 && unitPrice > 0) {
                    // Generate batchNo
                    String currentDate = java.time.LocalDate.now().toString();
                    String batchNo = selectedSupplier + "_" + currentDate + "_" + unitPrice;

                   OrderDetail orderDetail = new OrderDetail(selectedItem.getItemId(), quantity, unitPrice, batchNo);
                   LinkedList<OrderDetail> orderDetailsList = new LinkedList<>();
                   orderDetailsList.add(orderDetail);
                   ItemOrder itemOrder = new ItemOrder(0, MainController.clientCallback.getUser().getUsername(), currentDate, orderDetailsList);

                    // Call createPurchaseOrder method from the model
                    boolean orderCreated = addItemAdminModel.createPurchaseOrder(itemOrder);
                    if (orderCreated) {
                        showSuccess("Item order created successfully.");
                    } else {
                        showAlert("Failed to create item order.");
                    }
                } else {
                    showAlert("Please select an item, supplier, and provide valid quantity and price.");
                }
            } else {
                System.out.println("Button or Field is null. Cannot continue");
            }
        } catch (NumberFormatException e) {
            showAlert("Please provide valid quantity and price.");
        } catch (NotLoggedInException | OutOfRoleException e) {
            showAlert("Error occurred: " + e.getMessage());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {

        addItemAdminModel = new AddItemAdminModel(MainController.registry, MainController.clientCallback);

        if (!initialized) {
            addHoverEffect(okButton);
            initialized = true; // Set initialized to true after initialization

            if (okButton != null && itemNameComboBox != null && supplierComboBox != null && itemPriceField != null && quantityField != null) {
                addHoverEffect(okButton);
                try {
                    fetchAndUpdate(); // Fetch and update items list on initialization
                    fetchSuppliersAndUpdate(); // Fetch and update suppliers list on initialization
                } catch (RemoteException e) {
                    showAlert("Error occurred while initializing: " + e.getMessage());
                }
            } else {
                System.out.println("Button or Field is null. Cannot continue");
                return; // Exit the method if UI components are not properly initialized
            }

            okButton.setOnAction(actionEvent -> {
                try {
                    if (addItemAdminModel != null) {
                        handleOkButton(actionEvent);
                    } else {
                        System.out.println("CreateSalesInvoiceAdminModel is null");
                    }
                } catch (NotLoggedInException e) {
                    showAlert("User not logged in.");
                } catch (OutOfRoleException e) {
                    showAlert("User does not have required permission.");
                }
            });
        }
//        try {
//            MainController.clientCallback.setCurrentPanel(this);
//            UpdateCallback.process(MainController.clientCallback, MainController.registry);
//        } catch (NotLoggedInException e){
//            showAlert("User is not logged in");
//        } catch (RemoteException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
