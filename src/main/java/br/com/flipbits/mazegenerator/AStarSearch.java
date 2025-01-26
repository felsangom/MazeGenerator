package br.com.flipbits.mazegenerator;

import java.util.*;

public class AStarSearch {

    public List<Cell> search(Cell[][] grid, int startRow, int startCol, int endRow, int endCol) {
        Cell start = grid[startRow][startCol];
        Cell end = grid[endRow][endCol];

        PriorityQueue<Cell> openSet = new PriorityQueue<>(Comparator.comparingInt(this::fScore));
        Map<Cell, Integer> gScore = new HashMap<>();
        Map<Cell, Integer> fScore = new HashMap<>();

        gScore.put(start, 0);
        fScore.put(start, heuristic(start,end));
        openSet.add(start);

        while(!openSet.isEmpty()){
            Cell current = openSet.poll();
            if (current == end) {
                return reconstructPath(current);
            }

            List<Cell> neighbors = getNeighbors(grid,current);
            for(Cell neighbor: neighbors){
                int tentantiveGScore = gScore.get(current) + 1;
                if(tentantiveGScore < gScore.getOrDefault(neighbor, Integer.MAX_VALUE)){
                    neighbor.previousCell = current;
                    gScore.put(neighbor, tentantiveGScore);
                    fScore.put(neighbor, tentantiveGScore + heuristic(neighbor,end));

                    if(!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null;
    }

    private List<Cell> getNeighbors(Cell[][] grid, Cell cell) {
        int row = cell.row;
        int col = cell.col;
        int[][] neighborsPosition = {{row - 1,col},{row, col+1},{row+1,col},{row,col-1}};

        java.util.List<Cell> neighbors = new java.util.ArrayList<Cell>();

        for (int[] pos: neighborsPosition){
            int newRow = pos[0];
            int newCol = pos[1];

            if (isValid(grid, newRow,newCol)){
                Cell n = grid[newRow][newCol];
                boolean wall = hasWall(cell,n);

                if(!wall) {
                    neighbors.add(n);
                }
            }
        }

        return neighbors;
    }

    private List<Cell> reconstructPath(Cell cell){
        List<Cell> path = new ArrayList<>();
        Cell current = cell;

        while (current!=null){
            path.add(current);
            current = current.previousCell;
        }

        Collections.reverse(path);
        return  path;
    }

    private boolean hasWall(Cell current, Cell neighbor) {
        int row = current.row - neighbor.row;
        int col = current.col - neighbor.col;

        if (row == 1 && current.wallNorth ) return true;
        if (row == -1 && current.wallSouth ) return true;
        if (col == 1 && current.wallWest) return true;
        if (col == -1 && current.wallEast) return true;

        return false;
    }

    private int heuristic(Cell a, Cell b){
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    private boolean isValid(Cell[][] grid, int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private int fScore(Cell cell){
        return cell.row + cell.col;
    }
}
