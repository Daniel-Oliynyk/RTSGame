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
    static Animation explosion;
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
        prevPainter = getPainterCenter();
        bullets = new Group();
        
        ships = new Group();
        ships.add(new Miner(100, 350));
        ships.add(new Miner(100, 450));
        
        ships.add(new Tank(250, 250));
        ships.add(new Mothership(250, 400));
        ships.add(new Tank(250, 550));
        
        ships.add(new Medic(400, 250));
        ships.add(new Sniper(400, 400));
        ships.add(new Medic(400, 550));
        
        ships.add(new Fighter(550, 250));
        ships.add(new Fighter(550, 400));
        ships.add(new Fighter(550, 550));
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        explosion = new Animation(loadSpriteSheet("img/explosion.png", 32, 32));
        explosion.setRepeatAmount(1);
        explosion.setSpeed(2);
        
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
        if (allComplete || keyEngaged(KeyEvent.VK_ENTER)) for (Sprite ship : ships.getAll()) ((Ship) ship).resetTurn();
        
        stars.drawAll();
        centerPainterOn(prevPainter);
        
        if (keyEngaged(KeyEvent.VK_TAB)) {
            boolean found = false, done = false;
            for (Sprite sprite : RTSGame.ships.getAll()) {
                Ship ship = (Ship) sprite;
                if (!found && !done && ship.selected) {
                    ship.selected = false;
                    found = true;
                    if (RTSGame.ships.getAll().indexOf(ship) > RTSGame.ships.size() - 2) {
                        ((Ship)ships.get(0)).selected = true;
                        centerPainterOn(((Ship)ships.get(0)));
                        break;
                    }
                }
                else if (found && !done && ship.turnComplete()) ship.selected = false;
                else if (found && !done && !ship.turnComplete()) {
                    ship.selected = true;
                    centerPainterOn(ship);
                    done = true;
                }
                else if (done) ship.selected = false;
            }
            if (!found) {
                for (Sprite sprite : RTSGame.ships.getAll()) {
                    Ship ship = (Ship) sprite;
                    if (!ship.turnComplete()) {
                        ship.selected = true;
                        centerPainterOn(ship);
                        break;
                    }
                }
            }
        }
        
        if (keyPressed(KeyEvent.VK_W)) translatePainter(0, PAN_SPEED);
        if (keyPressed(KeyEvent.VK_S)) translatePainter(0, -PAN_SPEED);
        if (keyPressed(KeyEvent.VK_A)) translatePainter(PAN_SPEED, 0);
        if (keyPressed(KeyEvent.VK_D)) translatePainter(-PAN_SPEED, 0);
        
        bullets.drawAll();
        ships.drawAll();
        
        for (Sprite ship : ships.getAll()) ((Ship) ship).drawTurnCount();
        for (Sprite ship : ships.getAll()) if (((Ship) ship).selected) ((Ship) ship).drawInformation();
        
        prevPainter = getPainterCenter();
        centerPainterOn(getCenter());
    }
}
