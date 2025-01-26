package br.com.flipbits.mazegenerator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Main {
    private static int rows = 20;
    private static int cols = 30;
    private static JFrame frame;
    private static MazePanel mazePanel;
    private static MazeGenerator generator;
    private static  JTextField rowsField;
    private static  JTextField colsField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createGUI();
        });
    }

    private static void createGUI() {
        frame = new JFrame("Gerador de Labirintos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new FlowLayout());

        rowsField = new JTextField(String.valueOf(rows),5);
        colsField = new JTextField(String.valueOf(cols),5);

        panel.add(new JLabel("Linhas:"));
        panel.add(rowsField);
        panel.add(new JLabel("Colunas:"));
        panel.add(colsField);
        generator = new RecursiveBacktrackerGenerator();
        JButton generateButton = new JButton("Gerar Labirinto");
        JButton findPathButton = new JButton("Encontrar Caminho");

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    rows = Integer.parseInt(rowsField.getText());
                    cols = Integer.parseInt(colsField.getText());
                    if(rows <= 0 || cols <= 0){
                        JOptionPane.showMessageDialog(null, "Por favor, insira valores positivos maiores que 0");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Entrada inválida. Use apenas números inteiros.");
                    return;
                }

                Cell[][] maze = generator.generateMaze(rows, cols);

                if(mazePanel != null){
                    frame.remove(mazePanel);
                }

                mazePanel = new MazePanel(maze);
                frame.add(mazePanel,BorderLayout.CENTER);
                frame.pack();
                frame.setLocationRelativeTo(null); // Centraliza a janela
                findPathButton.setEnabled(true);
            }
        });

        findPathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mazePanel!= null){
                    findPathButton.setEnabled(false);
                    new Thread(() -> {
                        findAndAnimatePath();
                        findPathButton.setEnabled(true);

                    }).start();
                }

            }
        });

        findPathButton.setEnabled(false);

        panel.add(generateButton);
        panel.add(findPathButton);

        frame.add(panel, BorderLayout.NORTH);
        frame.pack();
        frame.setLocationRelativeTo(null); // Centraliza a janela
        frame.setVisible(true);
    }

    private static void findAndAnimatePath() {
        AStarSearch aStar = new AStarSearch();

        List<Cell> path =  aStar.search(
            mazePanel.getMaze(),
            mazePanel.getInitialCellRow(),
            mazePanel.getInitialCellCol(),
            mazePanel.getFinalCellRow(),
            mazePanel.getFinalCellCol()
        );

        if(path != null){
            for (Cell cell : path){
                mazePanel.addToPath(cell.row, cell.col);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mazePanel.repaint();

            }
        } else{
            JOptionPane.showMessageDialog(null,"Caminho não encontrado");
        }
    }
}
