package com.example.inventorymanagement.client.admin.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;


import java.io.IOException;
import java.io.InputStream;

public class NavigationBarAdminPanel extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"),20);

        FXMLLoader fxmlLoader = new FXMLLoader(NavigationBarAdminPanel.class.getResource("/com/example/inventorymanagement/client/view/navigationBar/navigationBarAdmin-view.fxml"));
        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

        if (inputStream != null) {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } else {
            System.err.println("Failed to load image: logo.png");
        }

        Scene sceneStockControlAdminPanel = new Scene(fxmlLoader.load(), 230, 650);
        stage.setTitle("Stock Pilot");
        stage.setScene(sceneStockControlAdminPanel);
        stage.show();

        stage.setResizable(false);
    }
    public static void main(String[] args) {
        launch();
    }
}