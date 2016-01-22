package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class RTSGame extends Game {
    static final int PAN_SPEED = 10;
    static BufferedImage star, bullet;
    static Group bullets, ships;
    private Group stars;
    int starCooldown;
    Position prevPainter;
    
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
        prevPainter = getPainterPosition();
        bullets = new Group();
        
        ships = new Group();
        ships.add(new Fighter(100, 100));
        ships.add(new Sniper(100, 300));
        ships.add(new Medic(100, 500));
        ships.add(new Mothership(400, 100));
        ships.add(new Tank(400, 300));
        ships.add(new Miner(400, 500));
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        
        stars = new Group();
        for (int i = 0; i < 200; i++) stars.add(new Sprite(randomPosition(getArea()), star));
        starCooldown = 0;
        
        Font large = new Font("Arial", Font.PLAIN, 16);
        painter().setFont(large);
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
        boolean allComplete = true;
        for (Sprite ship : ships.getAll()) if (!((Ship) ship).turnComplete()) allComplete = false;
        if (allComplete) for (Sprite ship : ships.getAll()) ((Ship) ship).resetTurn();
        
        stars.drawAll();
        setPainterPosition(prevPainter);
        
        if (keyPressed(KeyEvent.VK_W)) translatePainter(0, PAN_SPEED);
        if (keyPressed(KeyEvent.VK_S)) translatePainter(0, -PAN_SPEED);
        if (keyPressed(KeyEvent.VK_A)) translatePainter(PAN_SPEED, 0);
        if (keyPressed(KeyEvent.VK_D)) translatePainter(-PAN_SPEED, 0);
        
        bullets.drawAll();
        ships.drawAll();
        
        prevPainter = getPainterPosition();
        setPainterPosition(getCenter());
    }
}
