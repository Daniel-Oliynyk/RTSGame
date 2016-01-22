package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RTSGame extends Game {
    static final int PAN_SPEED = 10;
    static BufferedImage star, bullet, move, shoot, cancel;
    static Group bullets, menu;
    Group stars, ships;
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
        menu = new Group();
        bullets = new Group();
        
        ships = new Group();
        ships.add(new Fighter(100, 100));
        ships.add(new Fighter(100, 300));
        ships.add(new Mothership(400, 100));
        ships.add(new Mothership(400, 300));
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        
        move = loadImage("img/actions/move.png");
        shoot = loadImage("img/actions/shoot.png");
        cancel = loadImage("img/actions/cancel.png");
        
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
        menu.drawAll();
    }
}
