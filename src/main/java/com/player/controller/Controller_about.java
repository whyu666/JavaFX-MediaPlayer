package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller_about {
    @FXML
    Button okButton;

    //about窗口的"确定"键（关闭窗口）
    public void exit() {
        Stage window = (Stage) okButton.getScene().getWindow();
        window.close(); //将该窗口关闭
    }
}
