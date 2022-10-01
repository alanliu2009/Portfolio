# JARVIS
### Creators
- Alan Liu 
- Steven Ying 
- Steven Kuo
- Joshua Lin

### About the Game
JARVIS (JustAnotherRandomVideogameSandbox) is a game replicating the core mechanics of the popular 2D videogame Terraria.


### Playing the Game
Upon entering the game, you'll be presented with a start screen.
After clicking the "Start" button, you'll then be navigated to the World Select screen. 

To enter a new world, you may press "Q" or the "Start" button. You may also press 1,2,3 or use the bottom two buttons 
(with placeholder images for now) to change the world number.
As of now, clicking the start button will generate a new world from scratch, to test the world generation algorithm.

After entering the game, you as the player will spawn and drop to the ground.
There are a few basic controls you should know:
- WASD: Standard movement controls
- E: EXPLOSION - Destroy a grid of blocks at the cursor location
- R: ROCKET - Shoot a rocket that blocks up blocks upon impact with terrain
- Left Click: Destroy the block (if possible) at the mouse cursor
- Right Click: Place a block from the player's inventory (if possible) at the mouse cursor
- 0-9: Select a block from the player's inventory (shown on the top left)  
- Escape: Exit the game entirely
- P: Pause the game (press P again to unpause)
- \ : Debug mode (press \ again to exit debug mode)

To obtain blocks, you need to pick them up by running into them. When destroying a block, it will drop an item that you may pick up and place into your inventory.
Please keep in mind that in block placement/destruction, the player has a maximum reach distance that they cannot place/destory beyond.

The game has automatic methods for rendering and unrendering world chunks, as well as spawning and killing entities. In this way, the memory the game uses remains relatively constant as it runs.

Goodluck!
