# Pong Game — Project Specification

## Overview

A single-player Pong game built in **Java with Swing**, following the **Model-View-Controller (MVC)** architecture. The player competes against a CPU opponent with the goal of reaching 5 points first.

---

## Game Specifics

| Property       | Detail                                      |
|----------------|---------------------------------------------|
| Mode           | 1 player vs. AI (CPU)                       |
| Controls       | Player paddle: `↑` / `↓` arrow keys        |
| Win condition  | First to **5 points** wins                  |
| AI difficulty  | Single fixed difficulty                     |
| Visual style   | Classic arcade — black background, white paddles and ball |
| Screens        | Start screen, gameplay screen, game-over screen |

---

## MVC Architecture

### Model (`model/`)

Owns all game state and logic — no UI dependencies.

- **`Ball`** — position (`x`, `y`), velocity (`dx`, `dy`), radius; handles movement and wall/paddle collision detection; resets to center after each point.
- **`Paddle`** — position, height, width, speed; exposes `moveUp()` / `moveDown()` clamped to the canvas bounds.
- **`AiPaddle`** (extends `Paddle`) — CPU-controlled paddle; `update(Ball)` tracks the ball at a fixed speed to simulate a single difficulty level.
- **`GameState`** — authoritative scores (player and CPU), current phase (`START`, `PLAYING`, `GAME_OVER`), winner identifier; fires property-change events when state changes.
- **`GameLoop`** — fixed-timestep ticker (e.g., ~60 fps via `javax.swing.Timer`) that calls `tick()` on the model each frame; notifies the Controller of each update.

### View (`view/`)

Renders game state; never mutates it.

- **`GamePanel`** (extends `JPanel`) — main canvas; `paintComponent` draws background, paddles, ball, and center dashed line using `Graphics2D`.
- **`ScorePanel`** — displays current score (player vs. CPU) and remaining points needed.
- **`StartScreen`** — splash overlay shown before the game begins; contains a "Press Enter to Start" prompt.
- **`GameOverScreen`** — overlay displayed when a player reaches 5 points; shows the winner and a "Press R to Restart" prompt.
- **`MainFrame`** (extends `JFrame`) — top-level window; composes all panels via a `CardLayout` to switch between screens.

### Controller (`controller/`)

Bridges input events and the Model; drives transitions between screens.

- **`InputHandler`** (implements `KeyListener`) — listens for `↑`/`↓` key presses/releases; translates them into `paddle.moveUp()` / `paddle.moveDown()` calls on the model each frame.
- **`GameController`** — initialises the Model and View; subscribes to `GameState` change events; advances the `CardLayout` (Start → Playing → Game Over); handles `Enter` (start/restart) and `R` (restart) hotkeys; receives `tick()` callbacks from `GameLoop` and requests a View repaint.

---

## Package Structure

```
pong-game/
├── src/
│   └── pong/
│       ├── model/
│       │   ├── Ball.java
│       │   ├── Paddle.java
│       │   ├── AiPaddle.java
│       │   ├── GameState.java
│       │   └── GameLoop.java
│       ├── view/
│       │   ├── GamePanel.java
│       │   ├── ScorePanel.java
│       │   ├── StartScreen.java
│       │   ├── GameOverScreen.java
│       │   └── MainFrame.java
│       ├── controller/
│       │   ├── InputHandler.java
│       │   └── GameController.java
│       └── Main.java
└── README.md
```

---

## Flow Summary

1. **Launch** → `Main` creates `GameController`, which wires Model + View and shows `StartScreen`.
2. **Enter** → `GameController` transitions to `GamePanel`, starts `GameLoop`.
3. **Each tick** → `GameLoop` updates `Ball`, `AiPaddle`, checks collisions, updates `GameState` scores, notifies `GameController`, which repaints the View.
4. **Score reaches 5** → `GameState` sets phase to `GAME_OVER`; `GameController` shows `GameOverScreen`.
5. **R key** → `GameController` resets `GameState` and restarts.
