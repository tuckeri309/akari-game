package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import com.comp301.a09akari.model.CellType;
import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.model.Puzzle;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class PuzzleView implements FXComponent {
  private ClassicMvcController controller;
  private Model model;

  public PuzzleView(ClassicMvcController controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    GridPane grid = new GridPane();
    int height = model.getActivePuzzle().getHeight();
    int width = model.getActivePuzzle().getWidth();
    Puzzle puzzle = model.getActivePuzzle();
    for(int r = 0; r < height; r++){
      for (int c = 0; c < width; c++) {
        StackPane cell = new StackPane();
        int cellSize = 50;
        cell.setMinSize(cellSize, cellSize);
        cell.setMaxSize(cellSize, cellSize);
        cell.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-border-radius: 0px;");
        if(puzzle.getCellType(r, c) == CellType.WALL){
          cell.setStyle("-fx-background-color:BLACK");
        }
        else if(puzzle.getCellType(r, c) == CellType.CLUE){
          String clue = String.valueOf(puzzle.getClue(r, c));
          cell.setStyle("-fx-background-color:BLACK");
          Label label = new Label(clue);
          label.setTextFill(Color.WHITE);
          cell.getChildren().add(label);
        }
        else if(puzzle.getCellType(r, c) == CellType.CORRIDOR){
          final int row = r;
          final int col = c;
          cell.setOnMouseClicked(event -> handleClick(row, col));
          if(model.isLamp(r, c)){
            cell.setStyle("-fx-background-color:ORANGE");
          }
          else{
            if(model.isLit(r, c)){
              cell.setStyle("-fx-background-color:YELLOW");
            }
          }
        }
        grid.add(cell, r, c);
      }
    }
    return grid;
  }

  private void handleClick(int r, int c) {
    controller.clickCell(r, c);
  }
}
