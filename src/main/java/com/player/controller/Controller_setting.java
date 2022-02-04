package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller_setting {
    @FXML
    Button exitButton;

    //无内容，点击确定则关闭窗口
    public void exit() {
        Stage window = (Stage) exitButton.getScene().getWindow();
        window.close();
    }
}
