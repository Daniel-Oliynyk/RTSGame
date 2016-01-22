package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Ship extends Sprite {
    final int RANGE, ORIGINAL_SIZE, ACTION_AMOUNT;
    int totalRange, mode = 1, turns = 2;
    boolean selected, arrived = true;
    Position moveLocation;
    
    public Ship(double x, double y, int range, int actions, BufferedImage image) {
        super(image);
        centerOn(x, y);
        moveLocation = getCenter();
        RANGE = totalRange = range;
        ORIGINAL_SIZE = image.getWidth();
        ACTION_AMOUNT = actions < 2? 2 : actions > 6? 6 : actions;
    }
    
    @Override
    protected final void update() {
        if (selected) {
            if (Game.keyEngaged(KeyEvent.VK_ESCAPE)) deselect();
            if (Game.keyEngaged(KeyEvent.VK_1)) mode = 1;
            if (Game.keyEngaged(KeyEvent.VK_2)) mode = 2;
            if (Game.keyEngaged(KeyEvent.VK_3) && ACTION_AMOUNT > 2) mode = 3;
            if (Game.keyEngaged(KeyEvent.VK_4) && ACTION_AMOUNT > 3) mode = 4;
            if (Game.keyEngaged(KeyEvent.VK_5) && ACTION_AMOUNT > 4) mode = 5;
            if (Game.keyEngaged(KeyEvent.VK_6) && ACTION_AMOUNT > 5) mode = 6;
            if (arrived) selected();
        }
        if (!arrived) moveTo(moveLocation);
        arrived = moveLocation.x() == getCenter().x() && moveLocation.y() == getCenter().y();
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && arrived && turns > 0) {
            boolean otherShipSelected = false;
            for (Sprite ship : RTSGame.ships.getAll()) if (((Ship) ship).selected) otherShipSelected = true;
            selected = !otherShipSelected;
        }
        painter().setColor(Color.WHITE);
        painter().drawString(turns + "", (int) (getCenter().x() + ORIGINAL_SIZE / 2), (int) (getCenter().y() + ORIGINAL_SIZE / 2));
    }
    
    private void selected() {
        face(Game.mousePosition());
        if (mode == 1) actionOne();
        else if (mode == 2) actionTwo();
        else if (mode == 3) actionThree();
        else if (mode == 4) actionFour();
        else if (mode == 5) actionFive();
        else if (mode == 6) actionSix();
    }
    
    protected void actionOne() {
        painter().setColor(Color.GREEN);
        if (turns > 1) {
            drawRange(RANGE / 2);
            drawRange(RANGE);
        }
        else drawRange(totalRange);
        drawPointer(mouseConstraint(totalRange));
        if (Game.mouseEngaged()) {
            moveLocation = mouseConstraint(totalRange);
            totalRange -= getCenter().dist(moveLocation);
            if (turns > 1 && totalRange < RANGE / 2) turns -= 2;
            else turns--;
            if (turns < 1) deselect();
            totalRange /= 2;
        }
    }
    
    protected void actionTwo() {
        painter().setColor(Color.RED);
        drawPointer(Game.mousePosition());
        painter().setColor(Color.WHITE);
        if (Game.mouseEngaged()) {
            RTSGame.bullets.add(new Bullet(getCenter(), Game.mousePosition()));
            turns--;
            if (turns < 1) deselect();
        }
    }
    
    protected void actionThree() {}
    
    protected void actionFour() {}
    
    protected void actionFive() {}
    
    protected void actionSix() {}
    
    protected Position mouseConstraint(double length) {
        if (getCenter().dist(Game.mousePosition()) < length) return Game.mousePosition();
        double mX = getCenter().x() + Math.cos(getCenter().angleTo(Game.mousePosition())) * length;
        double mY = getCenter().y() + Math.sin(getCenter().angleTo(Game.mousePosition())) * length;
        return new Position(mX, mY);
    }
    
    protected void drawRange(int range) {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        painter().drawOval(centerX - range, centerY - range, range * 2, range * 2);
    }
    
    protected void drawPointer(Position pointer) {
        painter().drawLine((int) getCenter().x(), (int) getCenter().y(), (int) pointer.x(), (int) pointer.y());
        pointer.draw(5);
    }
    
    protected void deselect() {
        selected = false;
        mode = 1;
    }
    
    boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    void resetTurn() {
        turns = 2;
        totalRange = RANGE;
    }
}
