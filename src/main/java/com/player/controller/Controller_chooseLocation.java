package com.player.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Controller_chooseLocation {
    @FXML
    Label warnLabel2;
    @FXML
    Button submitButton;
    @FXML
    Label showFolderLabel;
    @FXML
    Label warnLabel;
    @FXML

    TextField filename;
    Stage stage;
    File folder;
    File duplicate = null;

    public void setCtr(Controller_getStreamURL ctr) {
        this.ctr = ctr;
    }

    Controller_getStreamURL ctr;

    //关闭窗口
    public void exit() {
        stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }

    //保存文件
    public void submit() throws IOException {
        File fileToSub = new File(folder.toString() + File.separatorChar + filename.getText() + ".sf");

        //判断文件是否二次重复
        if (!Objects.equals(duplicate, fileToSub)) { //duplicate != fileToSub
            if (fileToSub.exists()) {
                warnLabel.setVisible(true); //弹出是否覆盖提示
                duplicate = fileToSub;
            }
            //不重复，可以保存
            else {
                fileToSub.createNewFile();
                ctr.writeIntoFile(fileToSub);
                stage = (Stage) submitButton.getScene().getWindow();
                stage.close();
            }
        }
        //文件二次重复
        else { //duplicate == fileToSub
            //删除文件后保存
            warnLabel.setVisible(false);
            fileToSub.delete();
            fileToSub.createNewFile();
            ctr.writeIntoFile(fileToSub);
            stage = (Stage) submitButton.getScene().getWindow();
            stage.close();
        }
    }

    //选择文件夹
    public void chooseFolder() {
        DirectoryChooser file = new DirectoryChooser();
        file.setTitle("请选择一个文件夹");
        Stage stage = (Stage) submitButton.getScene().getWindow();
        folder = file.showDialog(stage);
        showFolderLabel.setText(folder.getPath()); //将文件路径显示
    }
}
