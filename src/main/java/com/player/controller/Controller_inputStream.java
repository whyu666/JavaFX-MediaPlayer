package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller_inputStream {
    @FXML
    TextField context;

    public void setCtr(Controller_getStreamURL ctr) {
        this.ctr = ctr;
    }

    Controller_getStreamURL ctr;

    //点击确定键
    public void submit() {
        String[] split = context.getText().split(","); //经context按照逗号分开，放到split中
        ctr.addNewUrl(split[0],split[1]); //将split存放在表中
        Stage stage = (Stage)context.getScene().getWindow();
        stage.close();
    }
}
