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
    
    static void showMenu(Ship owner) {
        menu.clear(false);
        Sprite moveButton = new Sprite(move);
        moveButton.centerOn(getCenter().x() - moveButton.getWidth() - 8, getHeight() - moveButton.getHeight() / 2 - 8);
        moveButton.script(new Script() {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(moveButton)) || keyEngaged(KeyEvent.VK_1)) owner.mode = 0;
            }
        });
        menu.add(moveButton);
        Sprite shootButton = new Sprite(shoot);
        shootButton.centerOn(getCenter().x(), getHeight() - shootButton.getHeight() / 2 - 8);
        shootButton.script(new Script() {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(shootButton)) || keyEngaged(KeyEvent.VK_2)) owner.mode = 1;
            }
        });
        menu.add(shootButton);
        Sprite cancelButton = new Sprite(cancel);
        cancelButton.centerOn(getCenter().x() + cancelButton.getWidth() + 8, getHeight() - cancelButton.getHeight() / 2 - 8);
        cancelButton.script(new Script() {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(cancelButton)) || keyEngaged(KeyEvent.VK_3) || mouseEngaged(MouseEvent.BUTTON3)) {
                    owner.deselect();
                    hideMenu();
                }
            }
        });
        menu.add(cancelButton);
    }
    
    static void hideMenu() {
        menu = new Group();
    }
}
