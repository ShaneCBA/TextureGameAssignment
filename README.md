# Grid Game
CS460 Texture Assignment - 

## GOALS
 - Create an engine to display sprites and tiles
 - Add collision to characters
 - Figure what the game will actually be?

### Collision Implementation
The plan is to use hitbox collision based on a grid.
This involved taking the player sprites coordinates and using them to calculate the tiles that the sprite is touching
The equation should look something like
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
