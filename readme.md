# Akari (Light Up) — Rules & Code Structure

## Game Rules
- **Grid** of cells, each one of:
  - **Corridor** (empty)  
  - **Wall** (black)  
  - **Clue** (black + number 0–4)  
- **Objective**: Place “lamps” in corridors so that:
  1. **Every corridor** is illuminated. A lamp lights its entire row and column, stopping at walls.  
  2. **No two lamps** see each other (i.e. no direct line of sight).  
  3. **Each clue** has exactly that many lamps in the four adjacent cells.  
- **Controls** (in the GUI): click a corridor to toggle a lamp; walls and clues cannot hold lamps.  

---

## Code Organization (MVC)

```
src/
├── model/
│   ├── Puzzle.java               ← interface for grid layout (cell‐type + clue value)
│   ├── PuzzleImpl.java           ← implements Puzzle(int[][] encoding)
│   ├── PuzzleLibrary.java        ← interface for a list of puzzles
│   ├── PuzzleLibraryImpl.java    ← default, list‐backed implementation
│   ├── Model.java                ← interface: current puzzle index, lamp locations, isSolved(), observer support
│   └── ModelImpl.java            ← implements Model(PuzzleLibrary): tracks state, notifies observers
│
├── controller/
│   ├── ClassicMvcController.java ← (optional) button‐handler interface
│   ├── AlternateMvcController.java ← (optional) stateless interface
│   └── ControllerImpl.java       ← glue between Model and View
│
└── view/
    ├── AppLauncher.java          ← JavaFX `start()` entry point
    ├── FXComponent.java          ← interface for composable UI components
    └── [your views].java         ← e.g. PuzzleView, ControlView, MessageView
```
- **Model** holds all puzzle data and lamp state, provides `isSolved()` logic.  
- **Controller** invokes model updates in response to UI events (next/prev puzzle, toggle lamp, reset).  
- **View** builds and updates the JavaFX scene graph, registers as a `ModelObserver`, and calls controller methods on user actions.  

