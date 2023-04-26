package sk.stuba.fei.uim.oop;

import java.awt.*;
import java.util.Random;

public class Ipipe extends Pipe {
    private Direction direction;
    public Ipipe() {
        super();
        Random rand=new Random();
        if(rand.nextInt(2)==0){
            direction=Direction.UPTODOWN;
        }
        else{
            direction=Direction.RIGHTTOLEFT;

        }
    }

    void rotate() {
        if(direction==Direction.RIGHTTOLEFT){
            direction=Direction.UPTODOWN;
        }
        else{
            direction=Direction.RIGHTTOLEFT;
        }

        // repaint the label with the new rotation
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(20));
        /*if(checking){
            g2d.setColor(Color.green);
        }*/
        int width = getWidth();
        int height = getHeight();

        switch(direction){
            case RIGHTTOLEFT:
                g2d.drawLine(0, height/2, width, height/2);
                break;
            case UPTODOWN:
                g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
                break;

        }

    }
}
