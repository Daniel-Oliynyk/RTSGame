package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class RTSGame extends Game {
    static final List<String> GLOBAL_ACTIONS = Arrays.asList("Cancel", "Next", "End Turn"),
            ACTION_KEYS = Arrays.asList("Esc", "Tab", "Enter");
    static final int PAN_SPEED = 10;
    static BufferedImage star, bullet, sniperBullet, tankBullet;
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
        setDimensions(1080, 720);
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
        sniperBullet = loadImage("img/laser.png");
        tankBullet = loadImage("img/shell.png");
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
        
        if (keyEngaged(KeyEvent.VK_TAB)) focusOnNextShip();
        if (keyPressed(KeyEvent.VK_W)) translatePainter(0, PAN_SPEED);
        if (keyPressed(KeyEvent.VK_S)) translatePainter(0, -PAN_SPEED);
        if (keyPressed(KeyEvent.VK_A)) translatePainter(PAN_SPEED, 0);
        if (keyPressed(KeyEvent.VK_D)) translatePainter(-PAN_SPEED, 0);
        
        bullets.drawAll();
        ships.drawAll();
        
        for (Sprite ship : ships.getAll()) ((Ship) ship).drawStats();
        for (Sprite ship : ships.getAll()) if (((Ship) ship).selected) ((Ship) ship).drawMenu();
        
        prevPainter = getPainterCenter();
        centerPainterOn(getCenter());
        drawActions();
    }
    
    static void focusOnNextShip() {
        boolean found = false, done = false;
        for (Sprite sprite : RTSGame.ships.getAll()) {
            Ship ship = (Ship) sprite;
            if (!found && !done && ship.selected) {
                ship.selected = false;
                found = true;
            }
            else if (found && !done && ship.getTurns() < 1) ship.selected = false;
            else if (found && !done && ship.getTurns() > 0) {
                ship.selected = true;
                centerPainterOn(ship);
                done = true;
            }
            else if (done) ship.selected = false;
        }
        
        if (!found || !done) {
            for (Sprite sprite : RTSGame.ships.getAll()) {
                Ship ship = (Ship) sprite;
                if (ship.getTurns() > 0) {
                    ship.selected = true;
                    centerPainterOn(ship);
                    break;
                }
            }
        }
    }
    
    private void drawActions() {
        int totalWidth = 0;
        for (int i = 0; i < ACTION_KEYS.size(); i++) totalWidth += stringWidth("[" + ACTION_KEYS.get(i) + "]  " + GLOBAL_ACTIONS.get(i));
        for (int i = 0; i < ACTION_KEYS.size() - 1; i++) totalWidth += stringWidth("   ");
        int offset = (Game.getWidth() - totalWidth) / 2;
        for (int i = 0; i < ACTION_KEYS.size(); i++) {
            painter().setColor(Color.GRAY);
            painter().drawString("[" + ACTION_KEYS.get(i) + "]  ", offset, Game.getHeight() - 30);
            offset += stringWidth("[" + ACTION_KEYS.get(i) + "]  ");
            painter().setColor(Color.WHITE);
            painter().drawString(GLOBAL_ACTIONS.get(i), offset, Game.getHeight() - 30);
            offset += stringWidth(GLOBAL_ACTIONS.get(i) + "   ");
        }
    }
    
    static final int stringWidth(String string) {
        FontMetrics font = painter().getFontMetrics();
        Rectangle2D rect = font.getStringBounds(string, painter());
        return (int) rect.getWidth();
    }
    
}
