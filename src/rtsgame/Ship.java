package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Ship extends Sprite {
    final int RANGE, ORIGINAL_SIZE;
    int totalRange, mode = 1, turns = 2;
    boolean selected, arrived = true;
    Position moveLocation;
    
    public Ship(double x, double y, int range, BufferedImage image) {
        super(image);
        centerOn(x, y);
        moveLocation = getCenter();
        RANGE = totalRange = range;
        ORIGINAL_SIZE = image.getWidth();
    }
    
    @Override
    protected final void update() {
        if (selected) {
            if (Game.keyEngaged(KeyEvent.VK_1)) mode = 1;
            if (Game.keyEngaged(KeyEvent.VK_2)) mode = 2;
            if (Game.keyEngaged(KeyEvent.VK_3)) mode = 3;
            if (Game.keyEngaged(KeyEvent.VK_4)) mode = 4;
            if (arrived) selected(mode);
        }
        if (!arrived) moveTo(moveLocation);
        arrived = moveLocation.x() == getCenter().x() && moveLocation.y() == getCenter().y();
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && arrived && turns > 0) selected = true;
        painter().setColor(Color.WHITE);
        painter().drawString(turns + "", (int) (getCenter().x() + ORIGINAL_SIZE / 2), (int) (getCenter().y() + ORIGINAL_SIZE / 2));
    }
    
    protected void selected(int mode) {
        face(Game.mousePosition());
        if (mode == 1) {
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
        else if (mode == 2) {
            painter().setColor(Color.RED);
            drawPointer(Game.mousePosition());
            painter().setColor(Color.WHITE);
            if (Game.mouseEngaged()) {
                RTSGame.bullets.add(new Bullet(getCenter(), Game.mousePosition()));
                turns--;
                if (turns < 1) deselect();
            }
        }
    }
    
    Position mouseConstraint(double length) {
        if (getCenter().dist(Game.mousePosition()) < length) return Game.mousePosition();
        double mX = getCenter().x() + Math.cos(getCenter().angleTo(Game.mousePosition())) * length;
        double mY = getCenter().y() + Math.sin(getCenter().angleTo(Game.mousePosition())) * length;
        return new Position(mX, mY);
    }
    
    void drawRange(int range) {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        painter().drawOval(centerX - range, centerY - range, range * 2, range * 2);
    }
    
    void drawPointer(Position pointer) {
        painter().drawLine((int) getCenter().x(), (int) getCenter().y(), (int) pointer.x(), (int) pointer.y());
        pointer.draw(5);
    }
    
    boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    void resetTurn() {
        turns = 2;
        totalRange = RANGE;
    }
    
    void deselect() {
        selected = false;
        mode = 0;
    }
}
