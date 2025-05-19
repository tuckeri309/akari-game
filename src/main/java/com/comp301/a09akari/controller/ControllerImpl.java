package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;

public class ControllerImpl implements ClassicMvcController {
  private Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
  }

  @Override
  public void clickPrevPuzzle() {
    model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
  }

  @Override
  public void clickRandPuzzle() {
    int numPuzzles = model.getPuzzleLibrarySize();
    int index = (int) (Math.random() * numPuzzles);
    model.setActivePuzzleIndex(index);
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    }
    else{
      model.addLamp(r, c);
    }
  }
}
