package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RTSGame extends Game {
    static BufferedImage star, bullet, move, shoot, cancel;
    static Group bullets, menu;
    Group stars, ships;
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
        menu = new Group();
        
        ships = new Group();
        ships.add(new Ship(pt(128 + 32, 128 + 32)));
        ships.add(new Ship(pt(256 + 32, 64 + 32)));
        ships.add(new Ship(pt(512 + 32, 512 + 32)));
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        
        move = loadImage("img/actions/move.png");
        shoot = loadImage("img/actions/shoot.png");
        cancel = loadImage("img/actions/cancel.png");
        
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
        menu.drawAll();
        
        boolean allComplete = true;
        for (Sprite ship : ships.getAll()) if (!((Ship) ship).turnComplete()) allComplete = false;
        if (allComplete) for (Sprite ship : ships.getAll()) ((Ship) ship).resetTurn();
    }
    
    static void showMenu(Ship owner) {
        menu.clear();
        Sprite moveButton = new Sprite(move);
        moveButton.centerOn(pt(getCenter().x() - moveButton.getWidth() - 8, getHeight() - moveButton.getHeight() / 2 - 8));
        moveButton.script(new Script(moveButton) {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(sprite())) || keyEngaged(KeyEvent.VK_1)) owner.type = Ship.Select.MOVE;
            }
        });
        menu.add(moveButton);
        Sprite shootButton = new Sprite(shoot);
        shootButton.centerOn(pt(getCenter().x(), getHeight() - shootButton.getHeight() / 2 - 8));
        shootButton.script(new Script(shootButton) {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(sprite())) || keyEngaged(KeyEvent.VK_2)) owner.type = Ship.Select.SHOOT;
            }
        });
        menu.add(shootButton);
        Sprite cancelButton = new Sprite(cancel);
        cancelButton.centerOn(pt(getCenter().x() + cancelButton.getWidth() + 8, getHeight() - cancelButton.getHeight() / 2 - 8));
        cancelButton.script(new Script(cancelButton) {
            @Override
            public void update() {
                if ((mouseEngaged() && mouseWithin(sprite())) || keyEngaged(KeyEvent.VK_3) || mouseEngaged(MouseEvent.BUTTON3)) {
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
