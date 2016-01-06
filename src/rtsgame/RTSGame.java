package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class RTSGame extends Game {
    Ship mothership;
    Group stars;
    BufferedImage star;
    int starCooldown;
    
    public static void main(String[] args) {
        initialize(RTSGame.class);
        new RTSGame();
    }
    
    @Override
    protected void window() {
        setTitle("RTS Game");
        setBackground(new Color(0x0b1037));
        create();
    }
    
    @Override
    protected void setup() {
        mothership = new Ship();
        star = loadImage("img/star.png");
        stars = new Group();
        for (int i = 0; i < 200; i++) stars.add(new Sprite(randomPosition(getArea()), star));
        starCooldown = 0;
    }
    
    @Override
    protected void run() {
        mothership.draw();
        starCooldown--;
        if (starCooldown < 0) {
            stars.remove(stars.get(0));
            stars.add(new Sprite(randomPosition(getArea()), star));
            starCooldown = 3;
        }
        
        for (Sprite dot : stars.getAll()) {
            if (dot.getX() < -dot.getWidth()) dot.setX(dot.getX() + getWidth());
            else if (dot.getX() > getWidth()) dot.setX(dot.getX() - getWidth());
            if (dot.getY() < -dot.getHeight()) dot.setY(dot.getY() + getHeight());
            else if (dot.getY() > getHeight()) dot.setY(dot.getY() - getHeight());
        }
        stars.drawAll();
    }
}
