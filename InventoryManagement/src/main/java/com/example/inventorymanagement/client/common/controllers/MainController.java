package com.example.inventorymanagement.client.common.controllers;

import com.example.inventorymanagement.client.admin.controllers.*;
import com.example.inventorymanagement.client.microservices.LogOutMicroservice;
import com.example.inventorymanagement.client.model.ClientCallbackImpl;
import com.example.inventorymanagement.client.purchaser.controllers.*;
import com.example.inventorymanagement.client.admin.controllers.StockControlAdminController;
import com.example.inventorymanagement.client.sales.controllers.*;
import com.example.inventorymanagement.util.ClientCallback;
import com.example.inventorymanagement.util.ControllerInterface;
import com.example.inventorymanagement.util.exceptions.NotLoggedInException;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.Optional;

/**
 * Purpose:
 * - Loads .fxml and display the view/scene/activity
 * - Main entry point of communication on the server
 * - Temporarily display main menu for each client might modify in such a way...
 */
public class MainController implements ControllerInterface {
    public Stage stage;
    public static ClientCallback clientCallback;
    public static UserRequestInterface userService;
    public static ItemOrderRequestInterface iOService;
    public static ItemRequestInterface itemService;
    public static Registry registry;

    /**
     * Admin Controller and Panel Variables
     */
    NavigationBarAdminController navigationBarAdminController;

    static StockControlAdminController stockControlAdminController;
    static BorderPane stockControlAdminPanel;

    static SalesHistoryAdminController salesHistoryAdminController;
    static BorderPane salesHistoryAdminPanel;

    static ProfileManagementAdminController profileManagementAdminController;
    static BorderPane profileManagementAdminPanel;

    static DashboardAdminController dashboardAdminController;
    static BorderPane dashboardAdminPanel;
    static FinancesAdminController financesAdminController;
    static BorderPane financesAdminPanel;

    static CreateSalesInvoiceAdminController createSalesInvoiceAdminController;
    static DialogPane createSalesInvoiceAdminPanel;

    static AddItemAdminController addItemAdminController;
    static DialogPane addItemAdminPanel;

    static AddListingAdminController addListingAdminController;
    static DialogPane addListingAdminPanel;

    static LowStocksAdminController lowStocksAdminController;
    static BorderPane lowStocksAdminPanel;

    static AddUserAdminController addUserAdminController;
    static BorderPane addUserAdminPanel;

    static ProfileManagementChangePassAdminController profileManagementChangePassAdminController;
    static BorderPane profileManagementChangePassAdminPanel;

    static EditUserAdminController editUserAdminController;
    static BorderPane editUserAdminPanel;

    /**
     * Purchaser Controller and Panel Variables
     */
    NavigationBarPurchaserController navigationBarPurchaserController;

    static StockControlPurchaserController stockControlPurchaserController;
    static BorderPane stockControlPurchaserPanel;

    static ProfileManagementPurchaserController profileManagementPurchaserController;
    static BorderPane profileManagementPurchaserPanel;

    static AddItemPurchaserController addItemPurchaserController;
    static DialogPane addItemPurchaserPanel;

    static LowStocksPurchaserController lowStocksPurchaserController;
    static BorderPane lowStocksPurchaserPanel;
    static ProfileManagementChangePassPurchaserController profileManagementChangePassPurchaserController;
    static BorderPane profileManagementChangePassPurchaserPanel;

    /**
     * Sales Controller and Panel Variables
     */
    NavigationBarSalesController navigationBarSalesController;

    static StockControlSalesController stockControlSalesController;
    static BorderPane stockControlSalesPanel;

    static SalesHistorySalesController salesHistorySalesController;
    static BorderPane salesHistorySalesPanel;

    static ProfileManagementSalesController profileManagementSalesController;
    static BorderPane profileManagementSalesPanel;

    static CreateSalesInvoiceSalesController createSalesInvoiceSalesController;
    static DialogPane createSalesInvoiceSalesPanel;
    static ProfileManagementChangePassSalesController profileManagementChangePassSalesController;
    static BorderPane getProfileManagementChangePassSalesPanel;

    /**
     * Getters of all Panels
     */
    public static BorderPane getStockControlPurchaserPanel() {return stockControlPurchaserPanel;}
    public static BorderPane getStockControlAdminPanel() {return stockControlAdminPanel;}
    public static BorderPane getStockControlSalesPanel() {return stockControlSalesPanel;}

    public static BorderPane getSalesHistorySalesPanel() {return salesHistorySalesPanel;}
    public static BorderPane getSalesHistoryAdminPanel() {return salesHistoryAdminPanel;}

    public static BorderPane getProfileManagementSalesPanel() {return profileManagementSalesPanel;}
    public static BorderPane getProfileManagementPurchaserPanel() {return profileManagementPurchaserPanel;}
    public static BorderPane getProfileManagementAdminPanel() { return profileManagementAdminPanel;}
    public static BorderPane getDashboardAdminPanel(){ return dashboardAdminPanel;}
    public static BorderPane getFinancesAdminPanel(){ return financesAdminPanel;}
    public static BorderPane getEditUserAdminPanel() {return editUserAdminPanel;}

    public static DialogPane getCreateSalesInvoiceSalesPanel() { return createSalesInvoiceSalesPanel;}
    public static DialogPane getCreateSalesInvoiceAdminPanel() { return createSalesInvoiceAdminPanel;}

    public static DialogPane getAddItemPurchaserPanel() {return addItemPurchaserPanel;}
    public static DialogPane getAddItemAdminPanel() { return addItemAdminPanel;}

    public static DialogPane getAddListingAdminPanel() { return addListingAdminPanel;}

    public static BorderPane getLowStocksPurchaserPanel() { return lowStocksPurchaserPanel;}
    public static BorderPane getLowStocksAdminPanel() { return lowStocksAdminPanel;}
    public static BorderPane getAddUserAdminPanel() { return addUserAdminPanel;}
    public static BorderPane getProfileManagementChangePassAdminPanel(){ return profileManagementChangePassAdminPanel;}

    /**
     * Getters of all Controllers
     */
    public NavigationBarAdminController getNavigationBarAdminController() { return navigationBarAdminController;}
    public NavigationBarPurchaserController getNavigationBarPurchaserController() { return navigationBarPurchaserController;}
    public NavigationBarSalesController getNavigationBarSalesController() { return navigationBarSalesController;}

    public static StockControlPurchaserController getStockControlPurchaserController() { return stockControlPurchaserController;}
    public static StockControlAdminController getStockControlAdminController() { return stockControlAdminController;}
    public static StockControlSalesController getStockControlSalesController() { return stockControlSalesController;}

    public static SalesHistorySalesController getSalesHistorySalesController() { return salesHistorySalesController;}
    public static SalesHistoryAdminController getSalesHistoryAdminController() { return salesHistoryAdminController;}

    public static ProfileManagementSalesController getProfileManagementSalesController() { return profileManagementSalesController;}
    public static ProfileManagementPurchaserController getProfileManagementPurchaserController() { return profileManagementPurchaserController;}
    public static ProfileManagementAdminController getProfileManagementAdminController() { return profileManagementAdminController;}
    public static DashboardAdminController getDashboardAdminController(){ return dashboardAdminController;}
    public static FinancesAdminController getFinancesAdminController(){ return financesAdminController;}
    public static EditUserAdminController getEditUserAdminController() {return editUserAdminController;}

    public static CreateSalesInvoiceSalesController getCreateSalesInvoiceSalesController() { return createSalesInvoiceSalesController;}
    public static CreateSalesInvoiceAdminController getCreateSalesInvoiceAdminController() { return createSalesInvoiceAdminController;}

    public static AddItemPurchaserController getAddItemPurchaserController() { return addItemPurchaserController;}
    public static AddItemAdminController getAddItemAdminController() { return addItemAdminController;}


    public static AddListingAdminController getAddListingAdminController() { return addListingAdminController;}

    public static LowStocksPurchaserController getLowStocksPurchaserController() {return lowStocksPurchaserController;}
    public static LowStocksAdminController getLowStocksAdminController() {return lowStocksAdminController;}
    public static AddUserAdminController getAddUserAdminController() {return addUserAdminController;}
    public static ProfileManagementChangePassAdminController getProfileManagementChangePassAdminController(){return profileManagementChangePassAdminController;}
    public static ProfileManagementChangePassPurchaserController getProfileManagementChangePassPurchaserController(){return profileManagementChangePassPurchaserController;}
    public static ProfileManagementChangePassSalesController getProfileManagementChangePassSalesController(){return profileManagementChangePassSalesController;}

    public ClientCallback getClientCallback() { return clientCallback;}

    public void setClientCallback(ClientCallback clientCallback) { this.clientCallback = clientCallback;}

    public UserRequestInterface getUserService() { return userService;}

    public void setStage(Stage stage) { this.stage = stage;}

    public Registry getRegistry() { return registry;}

    /**
     * Constructor for MainController.
     * Initializes the MainController with the necessary services and sets up the client callback for server communication.
     *
     * @param userService The user service interface.
     * @param iOService The item order service interface.
     * @param itemService The item service interface.
     * @param registry The RMI registry.
     * @throws RemoteException If a remote communication error occurs.
     */
    public MainController(UserRequestInterface userService, ItemOrderRequestInterface iOService, ItemRequestInterface itemService, Registry registry) throws RemoteException {
        MainController.userService = userService;
        MainController.iOService = iOService;
        MainController.itemService = itemService;
        MainController.registry = registry;
        clientCallback = new ClientCallbackImpl(null);
    }

    /** For Future References:
    private void initControllers() {
        stockControlAdminController = new StockControlAdminController(clientCallback, userService, iOService, itemService, registry, this);
        stockControlAdminController.setMainController(this);
        addItemAdminController = new AddItemAdminController(clientCallback, userService, iOService, itemService, registry, this);
        addItemAdminController.setMainController(this);
        stockControlPurchaserController = new StockControlPurchaserController(clientCallback, userService, iOService, itemService, registry, this);
        navigationBarPurchaserController = new NavigationBarPurchaserController(clientCallback, userService, iOService, itemService, registry, this);
    }
     */

    /**
     * Loads and displays the welcome view.
     */
    public void loadWelcomeView() { // load welcome/index view
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/welcome/welcome-view.fxml"));
            BorderPane welcomePane = fxmlLoader.load();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Scene scene = new Scene(welcomePane);
            stage.setScene(scene);
            stage.setResizable(false);
            WelcomeController welcomeController = fxmlLoader.getController();
            welcomeController.setMainController(this); // Pass MainController instance to WelcomeController
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads and displays the login view.
     */
    public void loadLoginView() { // load login view
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/login/login-view.fxml"));
            BorderPane loginPane = fxmlLoader.load();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Scene scene = new Scene(loginPane);

            // Retrieve the current stage
            if (stage == null) {
                throw new IllegalStateException("Stage is not set. Please set the stage before calling showLoginPanel.");
            }
            stage.setScene(scene);
            stage.setResizable(false);
            LoginController loginController = fxmlLoader.getController();
            loginController.setMainController(this);
            loginController.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Displays the main menu for the admin user.
     * This method sets up the UI components for the admin user.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void displayAdminMainMenu() throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

        FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/navigationBar/navigationBarAdmin-view.fxml"));
        BorderPane navigationBar = navLoader.load();
        navigationBarAdminController = navLoader.getController();

        FXMLLoader stockAdminLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/stockControlAdmin-view.fxml"));
        stockControlAdminPanel = stockAdminLoader.load();
        stockControlAdminController = stockAdminLoader.getController();
        stockControlAdminController.setMainController(this);

        FXMLLoader salesHistoryLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/salesHistory/salesHistoryAdmin-view.fxml"));
        salesHistoryAdminPanel = salesHistoryLoader.load();
        salesHistoryAdminController = salesHistoryLoader.getController();
        salesHistoryAdminController.setMainController(this);

        FXMLLoader profileManagementLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementAdmin-view.fxml"));
        profileManagementAdminPanel = profileManagementLoader.load();
        profileManagementAdminController = profileManagementLoader.getController();
        profileManagementAdminController.setMainController(this);

        FXMLLoader financesLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/finances/financesAdmin-view.fxml"));
        financesAdminPanel = financesLoader.load();
        financesAdminController = financesLoader.getController();

        FXMLLoader editUserLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/userManagement/editUserAdmin-view.fxml"));
        editUserAdminPanel = editUserLoader.load();
        editUserAdminController = editUserLoader.getController();
        editUserAdminController.setMainController(this);

        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/dashboard/dashboardAdmin-view.fxml"));
        dashboardAdminPanel = dashboardLoader.load();
        dashboardAdminController = dashboardLoader.getController();
        dashboardAdminController.setMainController(this);

        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

        if (inputStream != null) {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } else {
            System.err.println("Failed to load image: logo.png");
        }

        VBox rightComponents = new VBox();
        rightComponents.getChildren().addAll(dashboardAdminPanel, stockControlAdminPanel, salesHistoryAdminPanel, profileManagementAdminPanel, financesAdminPanel, editUserAdminPanel);

        BorderPane root = new BorderPane();
        root.setLeft(navigationBar);
        root.setRight(rightComponents);

        Scene scene = new Scene(root, 1080, 650);
        stage.setScene(scene);
        stage.setTitle("Stock Pilot - " + clientCallback.getUser().getRole().toUpperCase());
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    LogOutMicroservice.process(MainController.clientCallback, MainController.registry);
                } catch (NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }
        });
        stage.show();

        navigationBarAdminController.setMainPane(root);
    }

//    // Method to load FXML file dynamically
//    private void loadFXML(String fxmlFileName) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
//            Parent newContent = loader.load();
//            root.getChildren().setAll(newContent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to reload FXML file
//    private void reloadFXML(String fxmlFileName) {
//        // Clear existing content
//        root.getChildren().clear();
//
//        // Load new FXML content
//        loadFXML(fxmlFileName);
//    }

    /**
     * Displays the main menu for the purchaser user.
     * This method sets up the UI components for the purchaser user.
     *
     * @throws Exception If an error occurs.
     */
    public void displayPurchaserMainMenu() throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

        FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/navigationBar/navigationBarPurchaser-view.fxml"));
        BorderPane navigationBar = navLoader.load();
        navigationBarPurchaserController = navLoader.getController();


        FXMLLoader profileManagementLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementPurchaser-view.fxml"));
        profileManagementPurchaserPanel = profileManagementLoader.load();
        profileManagementPurchaserController = profileManagementLoader.getController();
        profileManagementPurchaserController.setMainController(this);

        FXMLLoader stockPurchaserLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/stockControlPurchaser-view.fxml"));
        stockControlPurchaserPanel = stockPurchaserLoader.load();
        stockControlPurchaserController = stockPurchaserLoader.getController();
        stockControlPurchaserController.setMainController(this);

        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

        if (inputStream != null) {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } else {
            System.err.println("Failed to load image: logo.png");
        }

        VBox rightComponents = new VBox();
        rightComponents.getChildren().addAll(stockControlPurchaserPanel, profileManagementPurchaserPanel);

        BorderPane root = new BorderPane();
        root.setLeft(navigationBar);
        root.setRight(rightComponents);

        Scene scene = new Scene(root, 1080, 650);
        stage.setScene(scene);
        stage.setTitle("Stock Pilot - " + clientCallback.getUser().getRole().toUpperCase());
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    LogOutMicroservice.process(MainController.clientCallback, MainController.registry);
                } catch (NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }
        });
        stage.show();

        navigationBarPurchaserController.setMainPane(root);
    }

    /**
     * Displays the main menu for the sales user.
     * This method sets up the UI components for the sales user.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void displaySalesMainMenu() throws IOException {
        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

        FXMLLoader navLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/navigationBar/navigationBarSales-view.fxml"));
        BorderPane navigationBar = navLoader.load();
        navigationBarSalesController = navLoader.getController();

        FXMLLoader salesHistoryLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/salesHistory/salesHistorySales-view.fxml"));
        salesHistorySalesPanel = salesHistoryLoader.load();
        salesHistorySalesController = salesHistoryLoader.getController();
        salesHistorySalesController.setMainController(this);

        FXMLLoader profileManagementLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementSales-view.fxml"));
        profileManagementSalesPanel = profileManagementLoader.load();
        profileManagementSalesController = profileManagementLoader.getController();
        profileManagementSalesController.setMainController(this);

        FXMLLoader stockSalesLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/stockControlSales-view.fxml"));
        stockControlSalesPanel = stockSalesLoader.load();
        stockControlSalesController = stockSalesLoader.getController();
        stockControlSalesController.setMainController(this);

        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
        if (inputStream != null) {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } else {
            System.err.println("Failed to load image: logo.png");
        }

        VBox rightComponents = new VBox();
        rightComponents.getChildren().addAll(stockControlSalesPanel, salesHistorySalesPanel, profileManagementSalesPanel);

        BorderPane root = new BorderPane();
        root.setLeft(navigationBar);
        root.setRight(rightComponents);

        Scene scene = new Scene(root, 1080, 650);
        stage.setScene(scene);
        stage.setTitle("Stock Pilot - " + clientCallback.getUser().getRole().toUpperCase());
        stage.setResizable(false);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    LogOutMicroservice.process(MainController.clientCallback, MainController.registry);
                } catch (NotLoggedInException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }
        });
        stage.show();

        navigationBarSalesController.setMainPane(root);
    }

    /**
     * Opens the sales invoice panel for the sales user.
     * This method displays a dialog for creating a sales invoice.
     */
    public void openSalesInvoiceSalesPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader createSalesInvoiceLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/createSalesInvoiceSales-view.fxml"));
            createSalesInvoiceSalesPanel = createSalesInvoiceLoader.load();
            createSalesInvoiceSalesController = createSalesInvoiceLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Sales Invoice");
            Scene scene = new Scene(createSalesInvoiceSalesPanel);
            dialogStage.setScene(scene);
            dialogStage.setOnCloseRequest(event -> {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation");
                confirmation.setHeaderText("Confirm Close");
                confirmation.setContentText("Are you sure you want to close the window? \nYour current selection will be removed");

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                confirmation.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                   dialogStage.close();
                } else {
                    event.consume();
                }
            });

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the sales invoice panel for the admin user.
     * This method displays a dialog for creating a sales invoice.
     */
    public void openSalesInvoiceAdminPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader createSalesInvoiceLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/createSalesInvoiceAdmin-view.fxml"));
            createSalesInvoiceAdminPanel = createSalesInvoiceLoader.load();
            createSalesInvoiceAdminController = createSalesInvoiceLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Sales Invoice");
            Scene scene = new Scene(createSalesInvoiceAdminPanel);
            dialogStage.setScene(scene);
            dialogStage.setOnCloseRequest(event -> {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation");
                confirmation.setHeaderText("Confirm Close");
                confirmation.setContentText("Are you sure you want to close the window? \nYour current selection will be removed.");

                ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);

                confirmation.getButtonTypes().setAll(yesButton, noButton);

                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    dialogStage.close();
                } else {
                    event.consume();
                }
            });

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the add item panel for the purchaser user.
     * This method displays a dialog for adding an item to the inventory.
     */
    public void openAddItemPurchaserPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader addItemLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/addItemPurchaser-view.fxml"));
            addItemPurchaserPanel = addItemLoader.load();
            addItemPurchaserController = addItemLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Item");
            Scene scene = new Scene(addItemPurchaserPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the add item panel for the admin user.
     * This method displays a dialog for adding an item to the inventory.
     */
    public void openAddItemAdminPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader addItemLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/addItemAdmin-view.fxml"));
            addItemAdminPanel = addItemLoader.load();
            addItemAdminController = addItemLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Item");
            Scene scene = new Scene(addItemAdminPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the low stocks panel for the purchaser user.
     * This method displays a dialog showing items with low stock levels.
     */
    public void openLowStocksPurchaserPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader lowStocksLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/lowStocksPurchaser-view.fxml"));
            lowStocksPurchaserPanel = lowStocksLoader.load();
            lowStocksPurchaserController = lowStocksLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Item");
            Scene scene = new Scene(lowStocksPurchaserPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the low stocks panel for the admin user.
     * This method displays a dialog showing items with low stock levels.
     */
    public void openLowStocksAdminPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader lowStocksLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/lowStocksAdmin-view.fxml"));
            lowStocksAdminPanel = lowStocksLoader.load();
            lowStocksAdminController = lowStocksLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Item");
            Scene scene = new Scene(lowStocksAdminPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the add liting panel for the admin user.
     * This method displays a pane that adds a listing.
     */
    public void openAddListingAdminPanel() {
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader addListingPanelLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/stockControl/addListingAdmin-view.fxml"));
            addListingAdminPanel = addListingPanelLoader.load();
            addListingAdminController = addListingPanelLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add Item");
            Scene scene = new Scene(addListingAdminPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openAddUserAdminPanel(){
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader addUserAdminPanelLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/userManagement/addUserAdmin-view.fxml"));
            addUserAdminPanel = addUserAdminPanelLoader.load();
            addUserAdminController = addUserAdminPanelLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Add User");
            Scene scene = new Scene(addUserAdminPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openProfileManagementCPAdminPanel(){
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader profileManagementCPAdminLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementChangePassAdmin-view.fxml"));
            profileManagementChangePassAdminPanel = profileManagementCPAdminLoader.load();
            profileManagementChangePassAdminController = profileManagementCPAdminLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Change Password");
            Scene scene = new Scene(profileManagementChangePassAdminPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openProfileManagementCPPurchaserPanel(){
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader profileManagementCPPurchaserLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementChangePassPurchaser-view.fxml"));
            profileManagementChangePassPurchaserPanel = profileManagementCPPurchaserLoader.load();
            profileManagementChangePassPurchaserController = profileManagementCPPurchaserLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Change Password");
            Scene scene = new Scene(profileManagementChangePassPurchaserPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openProfileManagementCPSalesPanel(){
        try {
            Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);

            FXMLLoader profileManagementCPSalesLoader = new FXMLLoader(getClass().getResource("/com/example/inventorymanagement/client/view/profileManagement/profileManagementChangePassSales-view.fxml"));
            profileManagementSalesPanel = profileManagementCPSalesLoader.load();
            profileManagementChangePassSalesController = profileManagementCPSalesLoader.getController();

            InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
            if (inputStream != null) {
                Image image = new Image(inputStream);
                stage.getIcons().add(image);
            } else {
                System.err.println("Failed to load image: logo.png");
            }

            Stage dialogStage = new Stage();
            dialogStage.initOwner(stage);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Change Password");
            Scene scene = new Scene(profileManagementSalesPanel);
            dialogStage.setScene(scene);

            // Set the stage not resizable
            dialogStage.setResizable(false);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches and updates data remotely.
     * This method is called to update the data displayed in the UI.
     *
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public void fetchAndUpdate() throws RemoteException {

    }

    /**
     * Gets the objects used.
     * This method returns a string indicating the type of objects used by the controller.
     *
     * @return A string representing the objects used.
     * @throws RemoteException If a remote communication error occurs.
     */
    @Override
    public String getObjectsUsed() throws RemoteException {
        return "user";
    }
}
