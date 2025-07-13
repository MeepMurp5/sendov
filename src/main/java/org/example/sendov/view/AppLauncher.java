package org.example.sendov.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.sendov.controller.Controller;
import org.example.sendov.model.Model;

public class AppLauncher extends Application{

    @Override
    public void start(Stage stage) throws Exception{
        Model model = new Model();
        Controller controller = new Controller(model);
        StartView startView = new StartView(model, controller, stage);
        Scene scene = new Scene(startView.render());
        scene.getStylesheets().add("main.css");

        // setup stage
        stage.setTitle("sendov");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}