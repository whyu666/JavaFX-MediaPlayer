package com.player;

import com.player.controller.Controller_mainPanel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("mediaPlayer.fxml"));
        Parent root = fxmlLoader.load();
        Controller_mainPanel controller = fxmlLoader.getController();
        primaryStage.setTitle("播放器");
        primaryStage.setScene(new Scene(root, 1366, 768));
        primaryStage.show();
        controller.init();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
