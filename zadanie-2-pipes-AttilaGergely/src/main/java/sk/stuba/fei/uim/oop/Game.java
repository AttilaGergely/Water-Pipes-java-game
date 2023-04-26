package sk.stuba.fei.uim.oop;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Game implements ActionListener, MouseListener,ChangeListener,KeyListener,MouseMotionListener {

    private JFrame frame;
    private JPanel panel,levelPanel,levelPanel2;
    private final JButton regenerateButton, checkWinButton;

    private Pipe[][] pipes;
    private final JSlider slider;
    private int sliderBefore;
    private int startRow;
    private int startRowCopy;
    private int startCol;
    private int level=1;
    private boolean isWon=false;

    public Game(){
        frame = new JFrame("Water Pipes - Attila Gergely");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(this);
        frame.setFocusable(true);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        regenerateButton = new JButton("Regenerate a new level");
        buttonPanel.add(regenerateButton);
        regenerateButton.setBackground(Color.magenta);
        regenerateButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        regenerateButton.addActionListener(this);
        regenerateButton.setPreferredSize(new Dimension(100,50));
        checkWinButton = new JButton("Check win");
        buttonPanel.add(checkWinButton);
        checkWinButton.setBackground(Color.magenta);
        checkWinButton.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        checkWinButton.addActionListener(this);
        checkWinButton.setPreferredSize(new Dimension(100,50));
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);

        slider = new JSlider(JSlider.HORIZONTAL, 8, 12, 8);
        slider.addChangeListener(e -> regeneratePipes());
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(0);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setSnapToTicks(true);
        slider.setBackground(new Color(255, 0, 255, 255));
        slider.setBorder(BorderFactory.createLineBorder(Color.BLACK,5));
        slider.setSize(new Dimension(200,60));
        slider.setFont(new Font("Arial", Font.BOLD, 15));
        frame.getContentPane().add(slider, BorderLayout.SOUTH);
        sliderBefore=slider.getValue();

        generatePipes(slider.getValue());


        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.requestFocusInWindow();

        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void generatePipes(int size) {
        if(sliderBefore!= size){
         level=1;
         sliderBefore=size;
        }
        levelPanel = createPanel("Level: " +level, BorderLayout.WEST);
        levelPanel2 = createPanel("Size: " + slider.getValue()+"x"+ slider.getValue(), BorderLayout.EAST);

        startCol=0;
        panel = new JPanel(new GridLayout(size, size));
        pipes = new Pipe[size][size];
        Random random = new Random();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Pipe pipe;
                if (random.nextInt(2) == 0) {
                    pipe = new Lpipe();
                } else {
                    pipe = new Ipipe();
                }
                pipe.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                pipe.setHorizontalAlignment(SwingConstants.CENTER);
                pipe.setVerticalAlignment(SwingConstants.CENTER);
                pipes[row][col] = pipe;
                pipe.setOpaque(true);
            }
        }

        startRow = random.nextInt(size);
        startRowCopy=startRow;
        pipes[startRow][0] = new StartPipe();
        pipes[startRow][0].setOpaque(true);

        int endRow = random.nextInt(size);
        pipes[endRow][size - 1] = new EndPipe();
        pipes[endRow][size - 1].setOpaque(true);

        for (int row = 0; row < slider.getValue(); row++) {
            for (int col = 0; col < slider.getValue(); col++) {
                Pipe pipe = pipes[row][col];
                panel.add(pipe);
            }
        }
    }

    private void regeneratePipes() {
        this.frame.getContentPane().remove(panel);
        this.frame.getContentPane().remove(levelPanel);
        this.frame.getContentPane().remove(levelPanel2);
        levelPanel.removeAll();
        levelPanel2.removeAll();
        panel.removeAll();
        generatePipes(slider.getValue());

        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);
        panel.setBackground(Color.black);
        panel.requestFocusInWindow();
        frame.add(panel);
        frame.pack();
        frame.requestFocusInWindow();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }


    private void resetPipesVisited(){
        for (int row = 0; row < slider.getValue(); row++) {
            for (int col = 0; col < slider.getValue(); col++) {
                pipes[row][col].resetVisited();
            }
        }
    }

    private Pipe getNeighbour(Pipe current){
        if(current instanceof StartPipe){
            if (pipes[startRow][startCol].getDirection() == Direction.RIGHT
                    && pipes[startRow][startCol+1].getDirection() != Direction.UPRIGHT &&
                    pipes[startRow][startCol+1].getDirection() != Direction.DOWNRIGHT
                    &&
                    pipes[startRow][startCol+1].getDirection() != Direction.UPTODOWN) {
                startCol++;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UP  &&
                    pipes[startRow-1][startCol].getDirection() != Direction.UPRIGHT &&
                    pipes[startRow-1][startCol].getDirection() != Direction.UPLEFT
                    && pipes[startRow-1][startCol].getDirection() != Direction.RIGHTTOLEFT) {
                startRow--;
                return pipes[startRow][startCol];
            }
            if (pipes[startRow][startCol].getDirection() == Direction.DOWN  &&
                    pipes[startRow+1][startCol].getDirection() != Direction.DOWNRIGHT &&
                    pipes[startRow+1][startCol].getDirection() != Direction.DOWNLEFT
                    && pipes[startRow+1][startCol].getDirection() != Direction.RIGHTTOLEFT) {
                startRow++;
                return pipes[startRow][startCol];
            }
        }
        else if(current instanceof Ipipe) {
            if (pipes[startRow][startCol].getDirection() == Direction.RIGHTTOLEFT && pipes[startRow][startCol+1].isVisited()) {
                startCol--;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.RIGHTTOLEFT && !pipes[startRow][startCol+1].isVisited()) {
                startCol++;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UPTODOWN && pipes[startRow+1][startCol].isVisited()) {
                startRow--;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UPTODOWN && !pipes[startRow+1][startCol].isVisited()) {
                startRow++;
                return pipes[startRow][startCol];
            }
        }
        else if(current instanceof Lpipe) {
            if (pipes[startRow][startCol].getDirection() == Direction.UPLEFT && pipes[startRow-1][startCol].isVisited()) {
                startCol--;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UPLEFT && !pipes[startRow-1][startCol].isVisited()) {
                startRow--;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UPRIGHT && pipes[startRow-1][startCol].isVisited()) {
                startCol++;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.UPRIGHT && !pipes[startRow-1][startCol].isVisited()) {
                startRow--;
                return pipes[startRow][startCol];
            }

            else if (pipes[startRow][startCol].getDirection() == Direction.DOWNLEFT && pipes[startRow+1][startCol].isVisited()) {
                startCol--;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.DOWNLEFT && !pipes[startRow+1][startCol].isVisited()) {
                startRow++;
                return pipes[startRow][startCol];
            }

            else if (pipes[startRow][startCol].getDirection() == Direction.DOWNRIGHT && pipes[startRow+1][startCol].isVisited()) {
                startCol++;
                return pipes[startRow][startCol];
            }
            else if (pipes[startRow][startCol].getDirection() == Direction.DOWNRIGHT && !pipes[startRow+1][startCol].isVisited()) {
                startRow++;
                return pipes[startRow][startCol];
            }

        }
        return null;
    }


    private void checkWin(Pipe current){
        current.setVisited();
        current.setBackground(new Color(157, 84, 255));

        if(current.getDirection()==Direction.RIGHT) {
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.UPTODOWN &&
                    neighbour.getDirection() != Direction.UPRIGHT &&
                    neighbour.getDirection() != Direction.DOWNRIGHT && !neighbour.isVisited()){
                checkWin(neighbour);
            }
        }
        else if(current.getDirection()==Direction.UP) {
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.RIGHTTOLEFT &&
                    neighbour.getDirection() != Direction.UPRIGHT &&
                    neighbour.getDirection() != Direction.UPLEFT && !neighbour.isVisited()){
                checkWin(neighbour);
            }
        }
        else if(current.getDirection()==Direction.DOWN) {
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.RIGHTTOLEFT &&
                    neighbour.getDirection() != Direction.DOWNLEFT &&
                    neighbour.getDirection() != Direction.DOWNRIGHT && !neighbour.isVisited()){
                checkWin(neighbour);
            }
        }
        else if(current.getDirection()==Direction.RIGHTTOLEFT) {
            if ( (startCol+1<=slider.getValue()-1) && ((pipes[startRow][startCol+1].getDirection() == Direction.RIGHTTOLEFT
                || pipes[startRow][startCol+1].getDirection() == Direction.UPLEFT ||
                pipes[startRow][startCol+1].getDirection() == Direction.DOWNLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.LEFT)&&
                !pipes[startRow][startCol+1].isVisited)
                ||((startCol-1>=0) &&(pipes[startRow][startCol-1].getDirection() == Direction.RIGHTTOLEFT
                || pipes[startRow][startCol-1].getDirection() == Direction.UPRIGHT
                || pipes[startRow][startCol-1].getDirection() == Direction.DOWNRIGHT)
                && !pipes[startRow][startCol-1].isVisited)){
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.UPTODOWN && !neighbour.isVisited()) {
                checkWin(neighbour);
                if (neighbour instanceof EndPipe) {
                    isWon=true;
                }
            }
            }
        }
        else if(current.getDirection()==Direction.UPTODOWN) {
            if ((startRow+1<=slider.getValue()-1 &&(pipes[startRow+1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow + 1][startCol].getDirection() == Direction.UPLEFT ||
                    pipes[startRow + 1][startCol].getDirection() == Direction.UPRIGHT
                    || pipes[startRow + 1][startCol].getDirection() == Direction.UP) &&
                    !pipes[startRow + 1][startCol].isVisited)
                    || ((startRow-1>=0) &&(pipes[startRow-1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNLEFT
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNRIGHT
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWN)
                    && !pipes[startRow-1][startCol].isVisited)) {
                Pipe neighbour = getNeighbour(current);
                if (neighbour != null && neighbour.getDirection() != Direction.RIGHTTOLEFT && !neighbour.isVisited())
                    checkWin(neighbour);
                if (neighbour instanceof EndPipe) {
                    isWon=true;
                }
            }
        }
        else if(current.getDirection()==Direction.UPLEFT) {
            if (((startRow-1>=0) && (pipes[startRow-1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNRIGHT
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNLEFT
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWN) &&
                    !pipes[startRow-1][startCol].isVisited)
                    || ((startCol-1>=0) && (pipes[startRow][startCol-1].getDirection() == Direction.RIGHTTOLEFT
                    || pipes[startRow][startCol-1].getDirection() == Direction.DOWNRIGHT
                    || pipes[startRow][startCol-1].getDirection() == Direction.UPRIGHT)
                    && !pipes[startRow][startCol-1].isVisited)) {
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.UPLEFT && !neighbour.isVisited()) {
                checkWin(neighbour);
                if (neighbour instanceof EndPipe) {
                    isWon=true;
                }
            }
            }
        }
        else if(current.getDirection()==Direction.DOWNLEFT) {
            if (((startRow+1<=slider.getValue()-1) && (pipes[startRow+1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow+1][startCol].getDirection() == Direction.UPRIGHT
                    || pipes[startRow+1][startCol].getDirection() == Direction.UPLEFT
                    || pipes[startRow+1][startCol].getDirection() == Direction.UP) &&
                    !pipes[startRow+1][startCol].isVisited)
                    || ((startCol-1>=0) && (pipes[startRow][startCol-1].getDirection() == Direction.RIGHTTOLEFT
                    || pipes[startRow][startCol-1].getDirection() == Direction.DOWNRIGHT
                    || pipes[startRow][startCol-1].getDirection() == Direction.UPRIGHT)
                    && !pipes[startRow][startCol-1].isVisited)) {
                Pipe neighbour = getNeighbour(current);
                if (neighbour != null && neighbour.getDirection() != Direction.DOWNLEFT && !neighbour.isVisited()) {
                    checkWin(neighbour);
                    if (neighbour instanceof EndPipe) {
                        isWon=true;
                    }
                }
            }
        }
        else if(current.getDirection()==Direction.UPRIGHT) {
            if (((startRow-1>=0) && (pipes[startRow-1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNRIGHT
                    || pipes[startRow-1][startCol].getDirection() == Direction.DOWNLEFT) &&
                    !pipes[startRow-1][startCol].isVisited)
                    || ((startCol+1<=slider.getValue()-1) && (pipes[startRow][startCol+1].getDirection() == Direction.RIGHTTOLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.DOWNLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.UPLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.LEFT)
                    && !pipes[startRow][startCol+1].isVisited)) {
            Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.UPRIGHT && !neighbour.isVisited()) {
                checkWin(neighbour);
                if (neighbour instanceof EndPipe) {
                    isWon=true;
                }
            }
            }
        }
        else if(current.getDirection()==Direction.DOWNRIGHT) {
            if (((startRow+1<=slider.getValue()-1) && (pipes[startRow+1][startCol].getDirection() == Direction.UPTODOWN
                    || pipes[startRow+1][startCol].getDirection() == Direction.UPRIGHT
                    || pipes[startRow+1][startCol].getDirection() == Direction.UPLEFT) &&
                    !pipes[startRow+1][startCol].isVisited)
                    || ((startCol+1<=slider.getValue()-1) && (pipes[startRow][startCol+1].getDirection() == Direction.RIGHTTOLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.DOWNLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.UPLEFT
                    || pipes[startRow][startCol+1].getDirection() == Direction.LEFT)
                    && !pipes[startRow][startCol+1].isVisited)) {
               Pipe neighbour=getNeighbour(current);
            if (neighbour != null && neighbour.getDirection() != Direction.DOWNRIGHT && !neighbour.isVisited()){
                checkWin(neighbour);
                if(neighbour instanceof EndPipe) {
                    isWon=true;
                }
                }
            }
        }

    }


    public JPanel createPanel(String labelText, String position) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(labelText);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentY(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(label);
        panel.add(Box.createVerticalGlue());
        panel.setBackground(new Color(239, 148, 207));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        frame.getContentPane().add(panel, position);
        return panel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == regenerateButton) {
            regeneratePipes();
        }
        if (e.getSource() == checkWinButton) {
            checkWin(pipes[startRow][startCol]);
            if(isWon){
                level++;
                regeneratePipes();
                isWon=false;
            }
            resetPipesVisited();
            startRow=startRowCopy;
            startCol=0;
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            regeneratePipes();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            checkWin(pipes[startRow][startCol]);
            if(isWon){
                level++;
                regeneratePipes();
                isWon=false;
            }
            resetPipesVisited();
            startRow=startRowCopy;
            startCol=0;
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Component clickedObject = panel.getComponentAt(e.getX(), e.getY());
        if (clickedObject instanceof Lpipe) {
            ((Lpipe) clickedObject).rotate();
        } else if (clickedObject instanceof Ipipe) {
            ((Ipipe) clickedObject).rotate();
        }
        else if (clickedObject instanceof StartPipe) {
            ((StartPipe) clickedObject).rotate();
        }
        else if (clickedObject instanceof EndPipe) {
            ((EndPipe) clickedObject).rotate();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        Component clickedObject = panel.getComponentAt(e.getX(), e.getY());
        if (!(clickedObject instanceof Pipe)) {
            return;
        }
        for (Component c : panel.getComponents()) {
            if (c instanceof Pipe) {
                if (c == clickedObject && (c instanceof StartPipe || c instanceof EndPipe)) {
                    c.setBackground(new Color(210, 54, 158));
                }
                else if(c==clickedObject){
                    c.setBackground(new Color(255, 0, 255));
                }
                else if(c instanceof StartPipe || c instanceof EndPipe ) {
                    c.setBackground(new Color(255, 0, 255));
                }
                else{
                    c.setBackground(new Color(239, 148, 207));
                }
            }
        }
    }
}