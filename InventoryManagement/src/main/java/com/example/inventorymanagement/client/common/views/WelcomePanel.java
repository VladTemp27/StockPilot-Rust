//package com.example.inventorymanagement.client.common.views;
//
//import com.example.inventorymanagement.client.common.controllers.MainController;
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Scene;
//import javafx.scene.image.Image;
//import javafx.scene.text.Font;
//import javafx.stage.Stage;
//
//import java.io.IOException;
//import java.io.InputStream;
//
//public class WelcomePanel extends Application {
//
//    @Override
//    public void start(Stage stage) throws IOException {
//        Font.loadFont(getClass().getResourceAsStream("/fonts/ShareTechMono-Regular.ttf"), 20);
//
//        MainController mainController = new MainController(null); // Get the instance
//
//        FXMLLoader fxmlLoader = new FXMLLoader(WelcomePanel.class.getResource("/com/example/inventorymanagement/client/view/welcome/welcome-view.fxml"));
//        Scene sceneWelcome = new Scene(fxmlLoader.load(), 600, 400);
//
//        InputStream inputStream = getClass().getResourceAsStream("/icons/logo.png");
//        if (inputStream != null) {
//            Image image = new Image(inputStream);
//            stage.getIcons().add(image);
//        } else {
//            System.err.println("Failed to load image: logo.png");
//        }
//
//        stage.setTitle("Stock Pilot");
//        stage.setScene(sceneWelcome);
//        stage.setResizable(false);
//
//        mainController.setStage(stage);
//
//        stage.show();
//    }
//
//    public static void main(String[] args) {
//        launch();
//    }
//}
