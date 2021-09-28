# space-wars
Java casual 2D-game with shooter elements, using Swing ([bubble shooter remake](https://github.com/Sergio1308/bubble-shooter))

The game was created for educational and entertainment purposes to get practice.

Mastered and done:

* Swing library, using basic methods to create a game;
* Creation of GUI elements using Swing
* Creation of mathematical regularities of the trajectory of movement of objects using Java
* OOP principles

![screen](https://i.imgur.com/lEeeMTL.png)

# Updates: What has changed in this project compared to the [previous one](https://github.com/Sergio1308/bubble-shooter)?
* Bitmaps added for each object. Now, each object is represented as a sprite, and has a collider that matches the image size (not as before, when every object was created and rendered programmatically)
* A certain theme has been added to the game. Therefore, background images and some effects in the gameplay have changed.
* Ranks of enemies have been removed. Therefore, the effect of dividing the enemy (when killed by the player) into smaller enemies is no longer used.

# Essence and rules:

### Goal 
score as many points as possible. Points are awarded for killing enemies. 

### Game mechanics
the game screen contains the player and opponents who move from top to bottom towards the player, pushing off the walls. Spawn of opponents on the playing field is presented in the form of waves. All opponents are classified by ranks. With each new wave, the number of opponents increases.

### Conditions to end the game
the player has only three lives. When a player collides with an enemy object, he will lose one or more lives (depends on the rank of the enemy). With the loss of all lives, the game ends.

### Control mechanics 
using the keyboard, the player can control an object on the playing field. Using the mouse, you can shoot at enemies. Use the crosshair to target a specific enemy and shoot at him!

### Shooting mechanics 
there is powerups that can drop from destroyed opponents with a small chance. When the player picks up a certain number of powerups, it changes the shooting mechanics and thereby helps the player to kill more enemies. As soon as the player collects the required amount of experience, a new type of shooting will open to him. With each new wave passed, there will be more and more opponents, so the player needs such a mechanic.

# Project structure
"src" folder contains all java classes for game functionality.
The launch of the game is implemented in the "GameStart.java" class.
The main mechanics and properties of the game are implemented in the "GamePanel.java" class.

"image" folder contains bitmaps for the background of the game windows.
