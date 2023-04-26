package sk.stuba.fei.uim.oop;

import javax.swing.JLabel;
import java.awt.*;

public class Pipe extends JLabel {
    private Direction direction;
    public boolean isVisited=false;
    private static final int PIPE_WIDTH = 58;
    private static final int PIPE_HEIGHT = 58;

    public Pipe() {
        setFocusable(true);
        setPreferredSize(new Dimension(PIPE_WIDTH, PIPE_HEIGHT));
        setBackground(new Color(239, 148, 207));
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isVisited(){
        return isVisited;
    }

    public void setVisited(){
        isVisited=true;
    }
    public void resetVisited(){
        isVisited=false;
    }
}







