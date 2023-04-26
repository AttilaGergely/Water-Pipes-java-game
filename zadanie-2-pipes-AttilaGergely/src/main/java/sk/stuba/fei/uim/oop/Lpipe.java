package sk.stuba.fei.uim.oop;

import java.awt.*;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

public class Lpipe extends Pipe{
    private Direction direction;
    public boolean visited;

    public Lpipe() {
        super();
        this.visited=false;
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setOpaque(true);
        Random rand=new Random();
        if(rand.nextInt(4)==0){
            direction=Direction.DOWNRIGHT;
        }
        else if(rand.nextInt(4)==1){
            direction=Direction.DOWNLEFT;
        }
        else if(rand.nextInt(4)==2){
            direction=Direction.UPLEFT;
        }
        else{
            direction=Direction.UPRIGHT;

        }
    }

    void rotate() {
        if(direction==Direction.DOWNLEFT){
            direction=Direction.UPLEFT;
        }
        else if(direction==Direction.UPLEFT){
            direction=Direction.UPRIGHT;
        }
        else if(direction==Direction.UPRIGHT){
            direction=Direction.DOWNRIGHT;
        }else{
            direction=Direction.DOWNLEFT;
        }

        // repaint the label with the new rotation
        this.repaint();
    }

    @Override
    public Direction getDirection(){
        return this.direction;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setStroke(new BasicStroke(20));
        if(this.isVisited()){
            System.out.println("EWFEFWF");
            g2d.setColor(Color.green);
        }

        switch (direction) {
            case UPRIGHT:
                g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() / 2);
                g2d.drawLine(getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2);
                break;
            case DOWNRIGHT:
                g2d.drawLine(getWidth() / 2, getHeight(), getWidth() / 2, getHeight() / 2);
                g2d.drawLine(getWidth() / 2, getHeight() / 2, getWidth(), getHeight() / 2);
                break;
            case UPLEFT:
                g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight() / 2);
                g2d.drawLine(getWidth() / 2, getHeight() / 2, 0, getHeight() / 2);
                break;
            case DOWNLEFT:
                g2d.drawLine(0, getHeight() / 2, getWidth() / 2, getHeight() / 2);
                g2d.drawLine(getWidth() / 2, getHeight() / 2, getWidth() / 2, getHeight());
                break;
        }

        g2d.dispose();
    }

}