package br.com.flipbits.mazegenerator;

import java.awt.*;

public class Cell {
    public int row, col;
    public boolean wallNorth, wallEast, wallSouth, wallWest;
    public boolean visited = false;
    public Cell previousCell;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.wallNorth = true;
        this.wallEast = true;
        this.wallSouth = true;
        this.wallWest = true;
        this.previousCell = null;
    }

    public Point getPoint(){
        return new Point(this.row,this.col);
    }
}
