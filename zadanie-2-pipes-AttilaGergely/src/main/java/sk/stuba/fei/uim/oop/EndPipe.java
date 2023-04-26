package sk.stuba.fei.uim.oop;

import java.awt.*;

public class EndPipe extends Pipe {
    private Direction direction;

    public EndPipe() {
        super();
        this.setBackground(new Color(255, 0, 255));
        direction=Direction.LEFT;
    }

    void rotate() {
        if(direction==Direction.RIGHT){
            direction=Direction.DOWN;
        }
        else if(direction==Direction.DOWN){
            direction=Direction.LEFT;
        }
        else if(direction==Direction.LEFT){
            direction=Direction.UP;
        }
        else{
            direction=Direction.RIGHT;
        }
        this.repaint();
    }

    @Override
    public Direction getDirection(){
        return this.direction;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(20));

        int width = getWidth();
        int height = getHeight();
        switch(direction){
            case RIGHT:
                g2d.drawLine(width/2, height/2, width, height/2);
                break;
            case UP:
                g2d.drawLine(width/2, height/2, width/2, 0);
                break;
            case LEFT:
                g2d.drawLine(width/2, height/2, 0, height/2);
                break;
            case DOWN:
                g2d.drawLine(width/2, height/2, width/2, height);
                break;
        }
    }

}


