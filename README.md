# Pong Game — Project Specification

## Overview

A single-player Pong game built in **Java with Swing**, following the **Model-View-Controller (MVC)** architecture. The game is a faithful recreation of the classic 1972 arcade game. The player controls a paddle on the left side of the screen using the `↑` and `↓` arrow keys, attempting to deflect a bouncing ball past the CPU-controlled paddle on the right. The ball moves at a fixed speed and bounces off the top and bottom walls; when it passes a paddle it scores a point for the other side. The first side to reach **5 points** wins. The AI paddle tracks the ball but occasionally introduces random positional error, giving the player a fair chance to win. The game runs in a classic arcade style — white paddles and ball on a black background — at a smooth ~60 fps.

---

## Game Specifics

| Property           | Detail                                                        |
|--------------------|---------------------------------------------------------------|
| Mode               | 1 player vs. AI (CPU)                                         |
| Window size        | 800 × 600 px                                                  |
| Controls           | Player paddle: `↑` / `↓` arrow keys                         |
| Win condition      | First to **5 points** wins                                    |
| Ball speed         | Fixed throughout the match; resets to center after each point |
| Ball serve         | After a point, ball is served toward the player who just scored |
| Paddle size        | Standard — approximately 1/5 of canvas height                 |
| AI behavior        | Tracks ball position but applies random error, capped speed   |
| Visual style       | Classic arcade — black background, white paddles and ball     |
| Screens            | Start screen, gameplay screen, game-over screen               |

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

## Definition of Done

This project is considered complete when **all** of the following criteria are met:

- [ ] All source code compiles and runs with no errors under a standard JDK (Java 11+).
- [ ] The full game loop is playable end-to-end: Start screen → gameplay → Game Over screen → restart.
- [ ] Player paddle responds correctly to `↑`/`↓` input and is clamped within the canvas bounds.
- [ ] AI paddle moves toward the ball each frame with a random error offset, making it beatable.
- [ ] Ball collides correctly with top/bottom walls and both paddles; no tunneling through objects.
- [ ] Scores increment correctly; the correct winner is declared at 5 points.
- [ ] Ball resets to center and serves toward the scoring side after each point.
- [ ] No known gameplay bugs (score glitches, ball getting stuck, paddles leaving bounds, etc.).
- [ ] Code is organized into the defined `model/`, `view/`, and `controller/` packages; no cross-layer violations (View never mutates Model directly).

---

## Flow Summary

1. **Launch** → `Main` creates `GameController`, which wires Model + View and shows `StartScreen`.
2. **Enter** → `GameController` transitions to `GamePanel`, starts `GameLoop`.
3. **Each tick** → `GameLoop` updates `Ball`, `AiPaddle`, checks collisions, updates `GameState` scores, notifies `GameController`, which repaints the View.
4. **Score reaches 5** → `GameState` sets phase to `GAME_OVER`; `GameController` shows `GameOverScreen`.
5. **R key** → `GameController` resets `GameState` and restarts.
