package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class RTSGame extends Game {
    Group stars, ships;
    static BufferedImage star, bullet;
    static Group bullets;
    int starCooldown;
    
    public static void main(String[] args) {
        initialize(RTSGame.class);
        new RTSGame();
    }
    
    @Override
    protected void window() {
        setTitle("Strategy Game");
        setBackground(new Color(0x0b1037));
        create();
    }
    
    @Override
    protected void setup() {
        ships = new Group();
        ships.add(new Ship(pt(128 + 32, 128 + 32)));
        ships.add(new Ship(pt(256 + 32, 64 + 32)));
        ships.add(new Ship(pt(512 + 32, 512 + 32)));
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        
        bullets = new Group();
        bullets.removeWhenOffScreen();
        
        stars = new Group();
        for (int i = 0; i < 200; i++) stars.add(new Sprite(randomPosition(getArea()), star));
        starCooldown = 0;
    }
    
    @Override
    protected void run() {
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
        bullets.drawAll();
        stars.drawAll();
        ships.drawAll();
        
        boolean allComplete = true;
        for (Sprite ship : ships.getAll()) if (!((Ship) ship).turnComplete()) allComplete = false;
        if (allComplete) for (Sprite ship : ships.getAll()) ((Ship) ship).resetTurn();
    }
}
