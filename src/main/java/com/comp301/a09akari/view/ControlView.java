package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

import java.awt.*;

public class ControlView implements FXComponent{
    ClassicMvcController controller;

    public ControlView(ClassicMvcController controller){
        this.controller = controller;
    }
    @Override
    public Parent render() {
        HBox puzzlesControl = new HBox(10);
        Button random = new Button ("Random Puzzle");
        Button previous = new Button ("Previous Puzzle");
        Button next = new Button ("Next Puzzle");
        Button reset = new Button ("Reset Puzzle");
        puzzlesControl.setAlignment(Pos.CENTER);
        puzzlesControl.getChildren().addAll(random, previous, next, reset);

        random.setOnAction(
                (ActionEvent event) -> {
                    controller.clickRandPuzzle();
                });
        previous.setOnAction(
                (ActionEvent event) -> {
                    controller.clickPrevPuzzle();
                });
        next.setOnAction(
                (ActionEvent event) -> {
                    controller.clickNextPuzzle();
                });
        reset.setOnAction(
                (ActionEvent event) -> {
                    controller.clickResetPuzzle();
                });
        return puzzlesControl;
    }
}
