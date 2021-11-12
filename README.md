# Grid Game
CS460 Texture Assignment - 

## GOALS
 - [ ] Create an engine to display sprites and tiles
 - [ ] Figure out what should and shouldn't be loaded from the file
 - [ ] Figure what the game will actually be?
 - [ ] Add collision to character(s)
 

### Collision Implementation
The plan is to use hitbox collision based on a grid.
This involved taking the player sprites coordinates and using them to calculate the tiles that the sprite is touching
The equation might look something like:
```
leftBottomCorner //Left Bottom Corner Position of the sprite
rightBottomCorner //Right Bottom Corner Position of the sprite

for (int x = leftBottomCorner.x; x < rightBottomCorner.x; i++)
{
  int [] tilePVector = World.getTilePosition(x, leftBottomCorner.y);
  if (World.getTile(tilePVector.x, tilePVector.y).collidable())
  {
    return true;
  }
}
return false;
```
This doesn't take into account higher velocities, so interpolation equations will be needed in addition to this.  
(Collision with other sprites will need to be a whole other equation)

### File Loading
**World(s)**  
  
*Tiles*  
Byte code (In hex form, every two "digits" is one tile code)  
First 4 bytes represent the `width` and `height` of the level (2 bytes each)  
Following that would be bytes that represent the tiles, with a total count equal to the `width * height`  
  
*Entities (Sprites)*  
At the beginning of the sprite code, we have 2 bytes representing the number of sprites. This will help in the future if more information needs to be added to the level files (such as backgrounds, different texture sets, etc. Though this may only be a thing if we continue to use this project through the semester)  
Sprites such as spawn points, check points, and goals are stored as bytes in the following order  
`id[1B]x[2B]y[2B]`  
This way, every 5 bytes is a new character