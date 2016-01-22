package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Ship extends Sprite {
    final int MOVE_RANGE, ORIGINAL_SIZE, ACTION_AMOUNT, MAX_TURNS;
    private int mode = 1, turns;
    boolean selected, arrived = true;
    Position moveLocation;
    
    public Ship(double x, double y, int range, int actions, int turns, BufferedImage image) {
        super(image);
        centerOn(x, y);
        moveLocation = getCenter();
        MOVE_RANGE = range;
        ORIGINAL_SIZE = image.getWidth();
        ACTION_AMOUNT = actions < 2? 2 : actions > 6? 6 : actions;
        MAX_TURNS = turns < 1? 1 : turns;
        resetTurn();
    }
    
    @Override
    protected final void update() {
        if (selected) {
            if (Game.keyEngaged(KeyEvent.VK_ESCAPE) || Game.mouseEngaged(MouseEvent.BUTTON3)) deselect();
            if (Game.keyEngaged(KeyEvent.VK_1)) mode = 1;
            if (Game.keyEngaged(KeyEvent.VK_2)) mode = 2;
            if (Game.keyEngaged(KeyEvent.VK_3) && ACTION_AMOUNT > 2) mode = 3;
            if (Game.keyEngaged(KeyEvent.VK_4) && ACTION_AMOUNT > 3) mode = 4;
            if (Game.keyEngaged(KeyEvent.VK_5) && ACTION_AMOUNT > 4) mode = 5;
            if (Game.keyEngaged(KeyEvent.VK_6) && ACTION_AMOUNT > 5) mode = 6;
            if (arrived) selected();
        }
        if (!arrived) moveTo(moveLocation);
        if (moveLocation.dist(getCenter()) < 2) arrived = true;
        if (click() && Game.mouseWithin(this) && arrived && turns > 0) {
            boolean otherShipSelected = false;
            for (Sprite ship : RTSGame.ships.getAll()) if (((Ship) ship).selected) otherShipSelected = true;
            selected = !otherShipSelected;
        }
        painter().setColor(turns > 0? Color.YELLOW : Color.GRAY);
        painter().drawString(turns + "", (int) (getCenter().x() + ORIGINAL_SIZE / 2), (int) (getCenter().y() + ORIGINAL_SIZE / 2));
    }
    
    private void selected() {
        face(mouse());
        if (mode == 1) actionOne();
        else if (mode == 2) actionTwo();
        else if (mode == 3) actionThree();
        else if (mode == 4) actionFour();
        else if (mode == 5) actionFive();
        else if (mode == 6) actionSix();
    }
    
    protected void actionOne() {
        painter().setColor(Color.WHITE);
        for (int i = 1; i <= turns; i++) drawRange(MOVE_RANGE * i);
        drawPointer(mouseConstraint(MOVE_RANGE * turns));
        if (click()) {
            moveLocation = mouseConstraint(MOVE_RANGE * turns);
            arrived = false;
            decreaseTurns((int) Math.ceil(getCenter().dist(moveLocation) / MOVE_RANGE));
        }
    }
    
    protected void actionTwo() {
        painter().setColor(Color.RED);
        drawPointer(mouse());
        if (click()) {
            RTSGame.bullets.add(new Bullet(getCenter(), mouse()));
            decreaseTurns(1);
        }
    }
    
    protected void actionThree() {}
    
    protected void actionFour() {}
    
    protected void actionFive() {}
    
    protected void actionSix() {}
    
    protected final boolean click() {
        return Game.mouseEngaged(MouseEvent.BUTTON1);
    }
    
    protected final Position mouse() {
        return Game.mousePosition();
    }
    
    protected final Position mouseConstraint(double length) {
        if (getCenter().dist(mouse()) < length) return mouse();
        double mX = getCenter().x() + Math.cos(getCenter().angleTo(mouse())) * length;
        double mY = getCenter().y() + Math.sin(getCenter().angleTo(mouse())) * length;
        return new Position(mX, mY);
    }
    
    protected final void drawRange(int range) {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        painter().drawOval(centerX - range, centerY - range, range * 2, range * 2);
    }
    
    protected final void drawPointer(Position pointer) {
        painter().drawLine((int) getCenter().x(), (int) getCenter().y(), (int) pointer.x(), (int) pointer.y());
        pointer.draw(5);
    }
    
    protected final void drawBoth(int range) {
        drawRange(range);
        drawPointer(mouseConstraint(range));
    }
    
    protected final void deselect() {
        selected = false;
        mode = 1;
    }
    
    final boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    final void resetTurn() {
        turns = MAX_TURNS;
    }
    
    final int getMode() {
        return mode;
    }
    
    final int getTurns() {
        return turns;
    }
    
    final void setMode(int mode) {
        this.mode = mode > ACTION_AMOUNT? ACTION_AMOUNT : mode < 2? 2 : mode;
    }
    
    final void decreaseTurns(int amount) {
        turns -= amount;
        if (turns < 1) {
            turns = 0;
            deselect();
        }
    }
}
