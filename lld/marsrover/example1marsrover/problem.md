/**
 * MARS ROVER PROBLEM STATEMENT:
 * 
 * A squad of robotic rovers are to be landed by NASA on a plateau on Mars.
 * This plateau is rectangular and must be navigated by the rovers so that their 
 * on-board cameras can get a complete view of the surrounding terrain.
 * 
 * A rover's position is represented by x, y coordinates and a letter representing 
 * compass direction (N, S, E, W). The plateau is divided into a grid.
 * 
 * Example position: 0, 0, N (bottom left corner, facing North)
 * 
 * Commands:
 * - L: Turn left 90 degrees
 * - R: Turn right 90 degrees
 * - M: Move forward one grid point
 * 
 * INPUT:
 * First line: Upper-right coordinates of plateau (lower-left is 0,0)
 * Rest: Rover positions and instructions
 * 
 * Example:
 * 5 5
 * 1 2 N
 * LMLMLMLMM
 * 3 3 E
 * MMRMMRMRRM
 * 
 * OUTPUT:
 * 1 3 N
 * 5 1 E
 */
