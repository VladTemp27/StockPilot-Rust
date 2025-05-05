package com.example.inventorymanagement.client.purchaser.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

public class NavigationBarPurchaserPanel extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"),20);

        FXMLLoader fxmlLoader = new FXMLLoader(NavigationBarPurchaserPanel.class.getResource("/com/example/inventorymanagement/client/view/navigationBar/navigationBarPurchaser-view.fxml"));
        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");

        if (inputStream != null) {
            Image image = new Image(inputStream);
            stage.getIcons().add(image);
        } else {
            System.err.println("Failed to load image: logo.png");
        }

        Scene sceneNavigationBarPurchaserPanel = new Scene(fxmlLoader.load(), 230, 650);
        stage.setTitle("Stock Pilot");
        stage.setScene(sceneNavigationBarPurchaserPanel);
        stage.show();

        stage.setResizable(false);
    }
    public static void main(String[] args) {
        launch();
    }
}