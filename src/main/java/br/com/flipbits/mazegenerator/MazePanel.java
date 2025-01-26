package br.com.flipbits.mazegenerator;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class MazePanel extends JPanel {
    private Cell[][] maze;
    private int cellSize = 20;
    private final int borderWidth = 8;
    private int initialCellRow;
    private int initialCellCol;
    private int finalCellRow;
    private int finalCellCol;
    private List<Point> path = new ArrayList<>();

    public MazePanel(Cell[][] maze) {
        this.maze = maze;
        setPreferredSize(new Dimension(maze[0].length * cellSize + 2 * borderWidth, maze.length * cellSize + 2 * borderWidth));

        this.initialCellRow = 0;
        this.initialCellCol = 0;

        this.finalCellRow = maze.length - 1;
        this.finalCellCol = maze[0].length - 1;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Desenha as células e as paredes
        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[0].length; col++) {
                drawCell(g, maze[row][col], col, row);
            }
        }

        // Desenha o caminho sobre o labirinto, depois de desenhar as paredes.
        drawPath(g);
        drawStartingAndFinalPositions(g);
    }

    private void drawPath(Graphics g) {
        g.setColor(Color.BLUE);

        for (Point p : this.path) {
            int cellX = borderWidth + p.y * cellSize;
            int cellY = borderWidth + p.x * cellSize;
            g.fillOval(cellX + cellSize / 4, cellY + cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    private void drawStartingAndFinalPositions(Graphics g) {
        g.setColor(Color.GREEN);
        int startX = borderWidth + initialCellCol * cellSize;
        int startY = borderWidth + initialCellRow * cellSize;
        g.fillRect(startX, startY, cellSize, cellSize);

        g.setColor(Color.RED);
        int endX = borderWidth + finalCellCol * cellSize;
        int endY = borderWidth + finalCellRow * cellSize;
        g.fillRect(endX, endY, cellSize, cellSize);
    }

    public Cell[][] getMaze() {
        return this.maze;
    }

    private void drawCell(Graphics g, Cell cell, int x, int y) {
        int cellX = borderWidth + x * cellSize;
        int cellY = borderWidth + y * cellSize;

        Graphics2D g2d = (Graphics2D) g;

        g2d.setStroke(new BasicStroke(borderWidth));
        g2d.setColor(Color.BLACK);

        if (cell.wallNorth) {
           g2d.draw(new Line2D.Float(cellX, cellY, cellX + cellSize, cellY));
        }

        if (cell.wallEast) {
            g2d.draw(new Line2D.Float(cellX + cellSize, cellY, cellX + cellSize, cellY + cellSize));
        }

        if (cell.wallSouth) {
            g2d.draw(new Line2D.Float(cellX, cellY + cellSize, cellX + cellSize, cellY + cellSize));
        }

        if (cell.wallWest) {
            g2d.draw(new Line2D.Float(cellX, cellY, cellX, cellY + cellSize));
        }
    }

    public int getInitialCellCol() {
        return initialCellCol;
    }

    public int getInitialCellRow() {
        return initialCellRow;
    }

    public int getFinalCellCol() {
        return finalCellCol;
    }

    public int getFinalCellRow() {
        return finalCellRow;
    }

    public void addToPath(int row, int col) {
        this.path.add(new Point(row, col));
    }

    public void clearPath() {
        this.path.clear();
    }
}
