package org.example.sendov.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sendov.controller.Controller;
import org.example.sendov.model.Model;

public class StartView implements FXComponent{
    private final int BAR_HEIGHT = 50;
    private final int LABEL_HEIGHT = 50;
    private final int CANVAS_SIZE = 500;
    private final int BUTTON_WIDTH = 80;

    private TextArea textArea;

    private final Model model;
    private final Controller controller;
    private final Stage stage;

    public StartView(Model model, Controller controller, Stage stage) {
        this.model = model;
        this.controller = controller;
        this.stage = stage;
    }

    @Override
    public Parent render() {
        // poly textbox
        textArea = new TextArea();
        textArea.setPrefHeight(BAR_HEIGHT);
        Button button = new Button("Enter");
        button.setPrefHeight(BAR_HEIGHT);
        button.setPrefWidth(BUTTON_WIDTH);
        HBox inputBar = new HBox(textArea, button);

        // poly label
        Label polyLabel = new Label("()");
        polyLabel.setPrefHeight(LABEL_HEIGHT);

        // display for poly roots
        Canvas canvas = new Canvas(CANVAS_SIZE, CANVAS_SIZE);

        // link controls
        button.setOnAction((event) -> {
            controller.updatePoly(textArea.getText());
            polyLabel.setText(textArea.getText());
            textArea.setText("");
        });

        VBox vBox = new VBox(inputBar, polyLabel, canvas);
        return vBox;
    }
}
