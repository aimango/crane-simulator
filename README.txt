Assignment 2 Readme
Elisa Lou 20372456

Notes:
There is an allowed offset of +/- 5 degrees for blocks to be considered "parallel" to the ground.
Crane arms are allowed to be rotated fully, 360 degrees. 

Makefile:


Enhancements:
1. Blocks can stack on top of each other. If not dropped above another block, the block will drop to the ground.
2. Some physics: showing path that a rotated block would follow when dropped. As the blocks are fragile, they are still returned to the original position.
3. In terms of "undoing" the movement of the block, the block (when dropped at a rotated position) is returned to its last position when placed parallel to the ground. For example, a block is picked up from position A and dropped parallel to position B. If it is then picked up and dropped at an angle, it will "undo" to position B. 