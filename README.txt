This was a Direct Manipulation project built using Java, built by myself (Elisa Lou). Written for a CS 349 Assignment taught at uWaterloo during the Winter 2013 term.

Disclaimer: Policy 73 at Waterloo defines IP ownership. Students own everything that they produce as a member of the Waterloo community, and the university has no implied ownership in anything created by its students.

How to Run:
-"make run" or "make" will compile and run the program.
-"make clean" will remove all .class files in the cranepackage folder.

Notes:
-There is an allowed offset of +/- 5 degrees for blocks to be considered "parallel" to the ground.
-Crane arms are allowed to be rotated fully, 360 degrees. 
-Assume that the user is able to drag the tractor horizontally via the tracks or the body of the crane.
-When magnets detect blocks and are in the right range to be picked up, they will "snap" to centre on the magnet.
-I meant to implement some gravity but didn't have much time. So if a block from the bottom of the stack is picked up, the ones above it will remain stationary. Thus, it is possible to have blocks that float in mid-air.

Enhancements:
-Blocks can stack on top of each other. If not dropped above another block, the block will drop to the ground.
-Some physics: showing vertical path that a rotated block would follow when dropped. As the blocks are fragile, they are still returned to the original position.
-In terms of "undoing" the movement of the block, the block (when dropped at a rotated position) is returned to its last position when placed parallel to the ground. For example, a block is picked up from position A and dropped parallel to position B. If it is then picked up and dropped at an angle, it will "undo" to position B. 

