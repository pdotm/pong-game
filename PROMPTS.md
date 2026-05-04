## Prompt 1:
Prompt used: I am building a Pong game in Java with Swing using MVC architecture. Before writing any code, interview me about game specifics. After a few questions, summarize my answers as a one-page spec in README.md. In the spec, outline responsibilities for each part of the MVC architecture.

Result: This prompt made the AI ask me questinos about game specifics, such as UI and game end criteria. Afterwards, AI produced the initial README.md file with the spec.

Fixes: AI did not add a section to the spec defining the "done" state for this project. It also did not add enough to the game description. I fixes these problems with prompt 2, which instructed AI to revise the spec.

Observation: AI broke up each section of the MVC architecture into several files. I like this approach because it allows each file to only have one class.

## Prompt 2:
Prompt used: Ask me more questions to define the scope of the game. Afterwards, add a section to the spec with the definition of done for this project. Additionally, add to the overview so it better describes the game and how it is played.

Result: This prompt added a definition of done to the spec. It also added to the description of the game to better describe what it is and how to play it.

Fixes: No fixes needed for these code changes. I was positively surprised with the result of this prompt because I did not expect AI to align the spec with what I wanted as accurately as it did.

Observation: AI added checkboxes by each of the items in the definition of done section. These will be useful later when we are reviewing whether or not the game meets the done criteria we defined.

## Prompt 3: 
Prompt used: Read the final spec document. After you are done reviewing, create the folders and class shells for each file we planned to add with a short description in each file stating its purpose.

Result: This prompt created the 13 files that were planned in the spec. It also added comments to each that described what they were going to do. 

Fixes: This prompt initially did not work as I expected. Even though I only specified to add a comment about the purpose of the file, AI also added a responsibilities comment section to each file. To fix this, I ran the following prompt: "Remove the responsibilities comment section from each file". 

Observation: AI made an inference regarding what I wanted in the comment section of each file. In the future, I should add something like "do not add any other comment besides those that were specified" to the end of my prompts to prevent this.

## Prompt 4: 
Prompt used: Fill in the model files. Add the game state and add methods for the actions the game will need to do, as well as the state and methods for for the component classes.

Result: This prompt added fields and methods to the game components, game loop, and game state classes. 

Fixes: I removed a couple of unnecessary comments that AI added. Aside from that, no other fixes were needed.

Observation: AI introduced constants such as ERROR_RANGE. After the initial game is developed, I will likely tinker with these constants to find the optimal values for improving the game experience.

## Prompt 5: 
Prompt used: Fill in the game view classes. These files should draw everything the player sees, including the paddles, ball, and game board. They should also draw the start screen and game over screen.

Result: AI filled in the view classes, adding methods for painting the screen.

Fixes: No fixes needed for these code changes.

Observation: In MainFrame.java, AI added constants for the "START", "GAME", an "GAMEOVER" strings. Worth asking why it chose this approach rather than writing the strings in the one place they are used.

## Prompt 6: 
Prompt used: Fill in the game controller classes and the Main class. After these edits, the game window should open when the program is run and the game behavior should match the spec in README.md. The player should be able to start the game and move their paddle until the game reaches a terminal state, at which point the game over screen should appear.

Result: AI filled in the controller classes, making the game window open when the program is run and allowing me to play the game.

Fixes: This prompt did not work quite how I expected it to. The game mechanics all worked and I was able to play through a full game, but pressing R on the game over screen did not restart the game. It instead took me back to the start screen. To fix this, I ran the following small follow-up prompt: "When R is pressed while the game is on the game over screen, restart the game rather than taking the player back to the start screen".

Observation: AI added a line of code to reset the scores to the restart method even though the other code to reset components of the game is in the start method. Worth asking why it chose this location for the score reset code.

## Prompt 7: 
Prompt used: The game feels slow paced right now. Increase the ball speed to fix this. Additionally, it is not possible for the player to score on the computer opponent. Find out why the AI paddle never misses and fix.

Result: AI increased the base speed of the ball from 4 to 7 and the error range of the paddle from 60 to 110.

Fixes: This prompt did not work as I expected. Even though the AI paddle began missing, it stopped missing again when I increased the paddle speed, meaning that the error system was dependent on the ball moving faster than the paddles. To fix this, I undid the changes and reran the prompt, replacing the last sentence with the following: "The current error system is dependent on the ball speed being faster than the paddle speed, otherwise it does not work. Revise the error system so that it works even when the paddle speed if much higher than the ball speed."

After that prompt, there were still bugs in the AI paddle movement. The AI paddle always went to either the top or bottom of the screen, never in the middle, and it did not move when the ball was served its way. These were fixed with the next prompt. 

Observation: AI initially did not look further into the AI paddle bug after adjusting the paddle error range. The remaining problems became apparent after testing, making testing and reviewing the code AI generates critical for finding and fixing bugs. 

## Prompt 8: 
Prompt used: The AI paddle always goes to one side of the screen (top or bottom), never somewhere in the middle. Additionally, it is unreponsive during serves. Correct the movement logic to fix both bugs.

Result: AI fixed paddle movement by making the destination calculation for the ball take into account wall bounces. 

Fixes: No fixes needed for these code changes. The AI paddle did not have any apparent bugs after this round of changes. Unrelated to these changes, I increased the player paddle speed from 5 to 8.

Observation: AI edited and added comments explaining its code changes. This is super helpful as it lets me analyze its thinking process more quickly and effectively.

## Prompt 9: 
Prompt used: Improve the visual style of the game. Make the player paddle red and the AI paddle blue. Add text on each side of the screen stating whose side it is. Make the score pop out when it increments. Improve the background, start page, and game over page by adding color or shapes.

Result: AI restyled the start page, game page, and game over page. It added color and some shapes to pages and their backgrounds, as well as added a score increment animation.

Fixes: This prompt did not work as I expected. The styling changes were made correctly, but they caused the AI paddle to miss more often than it did before. This is because the change in height of the playing field threw off the error calculations. To fix this bug, I ran this follow up prompt: "The change in playing screen height is affecting the error rate of the AI paddle and causing it to miss more than it should. Correct the calculations that predict where the ball destination to account for the new playing area height."

Observation: AI defined color constants in some areas but not others. Worth asking why it did this and how it determined where color constants should be defined.

## Prompt 10: 
Prompt used: Add a couple more game mechanics. Before the ball is served each round, reset the paddles to the middle and have add a three second countdown to the middle of the screen. Also, add a 10% chance for the ball to catch on fire when touches any paddle (the player's paddle or the AI paddle). When it lights on fire, it should have a special UI that makes it look like a fireball and it should stay at 150% speed for the rest of the round. When the round ends, it should return to normal for the next round.

Result: This prompt added a countdown before each round and added the described "fireball" functionality.

Fixes: I decreased countdown time by 50% and increased fireball speed to 200% of normal speed after playing the game for a bit to improve the experience.

Observation: AI added the "GET READY" text below the countdown without me specifying to do so. Really good inference from AI that I believe improved the UI.