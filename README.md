# Reversi (Othello) — Java Swing

A desktop implementation of **Reversi (Othello)** written in Java with a clear **MVC** separation.  
Two synchronized player windows (one per side), simple AIs, and a minimal text UI for debugging.



---

## ✨ Features

- ✅ Full Reversi rules (legal moves, flipping, pass turns, game over & scoring)
- 🖥️ **GUI** in Java Swing (two frames: Black / White)
- 🧠 Simple “greedy” AI (choose best immediate flip)
- 🧪 Lightweight test classes for the model/controller
- 🧩 Clean interfaces: `IModel`, `IView`, `IController`
- 🧱 MVC architecture with interchangeable views (GUI or text)

---

## 📦 Requirements

- **Java 11+** (tested on macOS; should run on Windows/Linux as well)

Check your Java:
```bash
java -version
```

## 🚀 How to Run

### 1) Compile from source
```bash
# from project root
javac -d bin src/*.java

# if your main expects an arg to choose the GUI:
java -cp bin reversi.ReversiMain gui

# if you made GUI the default in ReversiMain, this also works:
java -cp bin reversi.ReversiMain
java -cp bin reversi.ReversiMain text

# compile (if you haven't already)
javac -d bin src/*.java

# package with correct entry point (note the package prefix)
jar cfe dist/ReversiGame.jar reversi.ReversiMain -C bin .

# run the jar (GUI)
java -jar dist/ReversiGame.jar gui






