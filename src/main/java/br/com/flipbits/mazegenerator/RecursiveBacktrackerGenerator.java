package br.com.flipbits.mazegenerator;

import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrackerGenerator implements MazeGenerator {

    @Override
    public Cell[][] generateMaze(int rows, int cols) {
        Cell[][] grid = createGrid(rows, cols);
        recursiveBacktracker(grid, 0, 0);
        return grid;
    }

    private Cell[][] createGrid(int rows, int cols) {
        Cell[][] grid = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid[row][col] = new Cell(row, col);
            }
        }

        return grid;
    }

    private void recursiveBacktracker(Cell[][] grid, int startRow, int startCol) {
        Stack<Cell> stack = new Stack<>();
        Cell current = grid[startRow][startCol];
        current.visited = true;
        stack.push(current);

        while (!stack.isEmpty()) {
            current = stack.peek();
            Cell neighbor = getUnvisitedNeighbor(grid, current);

            if (neighbor != null) {
                removeWall(current, neighbor);
                neighbor.visited = true;
                neighbor.previousCell = current;
                stack.push(neighbor);
            } else {
                stack.pop();
            }
        }
    }

    private Cell getUnvisitedNeighbor(Cell[][] grid, Cell cell) {
        int row = cell.row;
        int col = cell.col;
        int[][] neighborsPosition = {{row - 1,col},{row, col+1},{row+1,col},{row,col-1}};

        java.util.List<Cell> neighbors = new java.util.ArrayList<Cell>();

        for (int[] pos: neighborsPosition){
            int newRow = pos[0];
            int newCol = pos[1];
            if (isValid(grid, newRow,newCol)){
                Cell n = grid[newRow][newCol];
                if (!n.visited) {
                    neighbors.add(n);
                }
            }
        }

        if (neighbors.size() == 0)
            return null;

        Random random = new Random();
        return  neighbors.get(random.nextInt(neighbors.size()));
    }


    private void removeWall(Cell current, Cell neighbor) {
        int row = current.row - neighbor.row;
        int col = current.col - neighbor.col;

        if (row == 1) {
            current.wallNorth = false;
            neighbor.wallSouth = false;
        } else if (row == -1) {
            current.wallSouth = false;
            neighbor.wallNorth = false;
        }
        if (col == 1) {
            current.wallWest = false;
            neighbor.wallEast = false;
        } else if (col == -1) {
            current.wallEast = false;
            neighbor.wallWest = false;
        }
    }

    private boolean isValid(Cell[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }
}
