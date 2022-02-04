package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Controller_reallyClear {
    @FXML
    Button cancelButton;

    public void setCtr(Controller_getStreamURL ctr) {
        this.ctr = ctr;
    }

    Controller_getStreamURL ctr;

    //返回，则直接关闭窗口
    public void exit() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    //确认删除列表，则删除列表后再关闭窗口
    public void clear() {
        ctr.doClear(); //调用Controller_getStreamURL中的doClear()函数进行清除
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
