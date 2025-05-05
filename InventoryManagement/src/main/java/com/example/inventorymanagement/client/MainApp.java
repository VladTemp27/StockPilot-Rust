package com.example.inventorymanagement.client;

import com.example.inventorymanagement.client.common.controllers.MainController;
import com.example.inventorymanagement.util.requests.ItemOrderRequestInterface;
import com.example.inventorymanagement.util.requests.ItemRequestInterface;
import com.example.inventorymanagement.util.requests.UserRequestInterface;
import javafx.application.Application;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        System.setProperty("java.util.logging.config.file", "logging.properties");
        System.out.println("Connecting...");
        Registry registry = LocateRegistry.getRegistry("serverMachine", 1099);
        System.out.println("Connected");
        UserRequestInterface userService = (UserRequestInterface) registry.lookup("userRequest");
        System.out.println("userStub Retrieved");
        ItemOrderRequestInterface iOService = (ItemOrderRequestInterface) registry.lookup("itemOrder");
        System.out.println("itemOrderStub Retrieved");
        ItemRequestInterface itemService = (ItemRequestInterface) registry.lookup("item");
        System.out.println("itemStub Retrieved");
        MainController mainController = new MainController(userService, iOService, itemService, registry);
        mainController.setStage(primaryStage);
        mainController.loadWelcomeView();
        primaryStage.setTitle("Stock Pilot");
        primaryStage.show();
    }

    public static void main(String[] args) throws NotBoundException, RemoteException {
        launch(args);
    }
}
