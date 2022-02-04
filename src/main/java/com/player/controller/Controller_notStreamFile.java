package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class Controller_notStreamFile {
    @FXML
    Button okButton;

    //退出窗口
    public void exit() {
        Stage window = (Stage) okButton.getScene().getWindow();
        window.close();
    }
}
