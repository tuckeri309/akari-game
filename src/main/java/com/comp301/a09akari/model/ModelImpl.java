package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private PuzzleLibrary library;
  private int activePuzzleIndex;
  private List<Lamp> lamps;
  private List<ModelObserver> observers;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) {
      throw new IllegalArgumentException();
    }
    this.library = library;
    activePuzzleIndex = 0;
    lamps = new ArrayList<>();
    observers = new ArrayList<>();
  }

  @Override
  public void addLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    } else if (!isInBounds(r, c)) {
      throw new IndexOutOfBoundsException();
    }
    lamps.add(new Lamp(r, c));
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    } else if (!isInBounds(r, c)) {
      throw new IndexOutOfBoundsException();
    }
    for (int i = 0; i < lamps.size(); i++) {
      Lamp lamp = lamps.get(i);
      if (lamp.getRow() == r && lamp.getCol() == c) {
        lamps.remove(i);
        break;
      }
    }
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    } else if (!isInBounds(r, c)) {
      throw new IndexOutOfBoundsException();
    }
    boolean lit = false;
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r && lamp.getCol() == c) {
        lit = true;
        break;
      } else if (lamp.getRow() == r) {
        int i;
        int stopIndex;
        boolean blocked = false;
        if (lamp.getCol() > c) {
          i = c;
          stopIndex = lamp.getCol();
        } else {
          i = lamp.getCol();
          stopIndex = c;
        }
        for (; i < stopIndex; i++) {
          if (activePuzzle.getCellType(r, i) == CellType.WALL
              || activePuzzle.getCellType(r, i) == CellType.CLUE) {
            blocked = true;
            break;
          }
        }
        if (!blocked) {
          lit = true;
          break;
        }
      } else if (lamp.getCol() == c) {
        int i;
        int stopIndex;
        boolean blocked = false;
        if (lamp.getRow() > r) {
          i = r;
          stopIndex = lamp.getRow();
        } else {
          i = lamp.getRow();
          stopIndex = r;
        }
        for (; i < stopIndex; i++) {
          if (activePuzzle.getCellType(i, c) == CellType.WALL
              || activePuzzle.getCellType(i, c) == CellType.CLUE) {
            blocked = true;
            break;
          }
        }
        if (!blocked) {
          lit = true;
          break;
        }
      }
    }

    return lit;
  }

  @Override
  public boolean isLamp(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    } else if (!isInBounds(r, c)) {
      throw new IndexOutOfBoundsException();
    }
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r && lamp.getCol() == c) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (r < 0 || c < 0 || r >= activePuzzle.getHeight() || c >= activePuzzle.getWidth()) {
      throw new IndexOutOfBoundsException();
    }
    else if (!isLamp(r, c)) {
      throw new IllegalArgumentException();
    }
    boolean lit = false;
    for (Lamp lamp : lamps) {
      if (!(lamp.getRow() == r && lamp.getCol() == c)) {
        if (lamp.getRow() == r) {
          int i;
          int stopIndex;
          boolean blocked = false;
          if (lamp.getCol() > c) {
            i = c;
            stopIndex = lamp.getCol();
          } else {
            i = lamp.getCol();
            stopIndex = c;
          }
          for (; i < stopIndex; i++) {
            if (activePuzzle.getCellType(r, i) == CellType.WALL
                || activePuzzle.getCellType(r, i) == CellType.CLUE) {
              blocked = true;
              break;
            }
          }
          if (!blocked) {
            lit = true;
            break;
          }
        } else if (lamp.getCol() == c) {
          int i;
          int stopIndex;
          boolean blocked = false;
          if (lamp.getRow() > r) {
            i = r;
            stopIndex = lamp.getRow();
          } else {
            i = lamp.getRow();
            stopIndex = r;
          }
          for (; i < stopIndex; i++) {
            if (activePuzzle.getCellType(i, c) == CellType.WALL
                || activePuzzle.getCellType(i, c) == CellType.CLUE) {
              blocked = true;
              break;
            }
          }
          if (!blocked) {
            lit = true;
            break;
          }
        }
      }
    }
    return lit;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return library.getPuzzle(activePuzzleIndex);
  }

  @Override
  public int getActivePuzzleIndex() {
    return activePuzzleIndex;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= library.size()) {
      throw new IndexOutOfBoundsException();
    }
    activePuzzleIndex = index;
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    lamps.clear();
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    Puzzle activePuzzle = getActivePuzzle();
    for (int r = 0; r < activePuzzle.getHeight(); r++) {
      for (int c = 0; c < activePuzzle.getWidth(); c++) {
        if (activePuzzle.getCellType(r, c) == CellType.CLUE) {
          if (!isClueSatisfied(r, c)) {
            return false;
          }
        }
        if (activePuzzle.getCellType(r, c) == CellType.CORRIDOR) {
          if (!isLit(r, c)) {
            return false;
          }
        }
      }
    }
    for(Lamp lamp : lamps){
      if(isLampIllegal(lamp.getRow(), lamp.getCol())){
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    Puzzle activePuzzle = getActivePuzzle();
    if (activePuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    } else if (!isInBounds(r, c)) {
      throw new IndexOutOfBoundsException();
    }

    int adjacentLamps = 0;
    int[][] adjacentGetters = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    for (int[] adjacentGetter : adjacentGetters) {
      int col = adjacentGetter[1] + c;
      int row = adjacentGetter[0] + r;
      if (isInBounds(row, col) && isLamp(row, col)) {
        adjacentLamps++;
      }
    }
    if(adjacentLamps == getActivePuzzle().getClue(r, c)){
      return true;
    }
    return false;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    observers.remove(observer);
  }

  public void notifyObservers() {
    for (ModelObserver o : observers) {
      o.update(this);
    }
  }

  private boolean isInBounds(int r, int c){
    return r >= 0
            && r < getActivePuzzle().getHeight()
            && c >= 0
            && c < getActivePuzzle().getWidth();
  }
}
