package com.example.inventorymanagement.client.common.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class WelcomeController {
    public Button nextButton;
    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        addHoverEffect(nextButton);
    }

    private void addHoverEffect(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: derive(#FFFFFF, -10%);"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #FFFFFF;"));
    }

    @FXML
    private void nextButton(ActionEvent event) {
        mainController.loadLoginView();
    }

}
