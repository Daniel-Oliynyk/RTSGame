package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import static gametools.Game.painter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Ship extends Sprite {
    static final int RANGE = 400, ORIGINAL_SIZE = 128;
    static enum Select {
        MOVE, SHOOT, NONE;
    }
    int turnRange = RANGE;
    boolean selected, arrived = true;
    Select type = Select.NONE;
    int turns = 2;
    Position moveLocation;
    
    public Ship(Position pos) {
        super(loadImage("img/mothership.png"));
        centerOn(pos);
        setRotationSpeed(20);
        moveLocation = getCenter();
    }
    
    @Override
    protected void update() {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        int endX = (int) Game.mousePosition().x(), endY = (int) Game.mousePosition().y();
        if (selected) {
            face(Game.mousePosition());
            int bottom = (int) (8 + getCenter().y() + ORIGINAL_SIZE / 2);
            int left = (int) (getCenter().x() - ORIGINAL_SIZE / 2);
            painter().drawImage(RTSGame.move, left, bottom + 4, null);
            painter().drawImage(RTSGame.shoot, left + 36, bottom + 4, null);
            painter().drawImage(RTSGame.cancel, left + 72, bottom + 4, null);
            if (type == Select.MOVE) {
                painter().setColor(Color.GREEN);
                int tempRange = turns < 2? turnRange / 2 : turnRange;
                if (turns > 1) {
                    painter().drawOval(centerX - RANGE / 2, centerY - RANGE / 2, RANGE, RANGE);
                    painter().drawOval(centerX - RANGE, centerY - RANGE, RANGE * 2, RANGE * 2);
                }
                else painter().drawOval(centerX - tempRange, centerY - tempRange, tempRange * 2, tempRange * 2);
                if (getCenter().dist(Game.mousePosition()) > tempRange) {
                    endX = centerX + (int) (Math.cos(getCenter().angleTo(Game.mousePosition())) * tempRange);
                    endY = centerY + (int) (Math.sin(getCenter().angleTo(Game.mousePosition())) * tempRange);
                }
                painter().drawLine(centerX, centerY, endX, endY);
                new Position(endX, endY).draw(5);
                if (Game.mouseEngaged(MouseEvent.BUTTON1)) {
                    moveLocation = new Position(endX, endY);
                    type = Select.NONE;
                    selected = false;
                    turnRange -= getCenter().dist(moveLocation);
                    if (turns > 1 && turnRange < RANGE / 2) turns--;
                    turns--;
                }
            }
            else if (type == Select.SHOOT) {
                painter().setColor(Color.RED);
                painter().drawLine(centerX, centerY, endX, endY);
                new Position(endX, endY).draw(5);
                painter().setColor(Color.WHITE);
                if (type == Select.SHOOT && Game.mouseEngaged(MouseEvent.BUTTON1)) {
                    RTSGame.bullets.add(new Bullet(getCenter(), Game.mousePosition()));
                    turns--;
                    if (turns < 1) {
                        type = Select.NONE;
                        selected = false;
                    }
                }
            }
        }
        painter().setColor(Color.WHITE);
        
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && arrived) selected = true;
        if (selected && Game.keyEngaged(KeyEvent.VK_1)) type = Select.MOVE;
        if (selected && Game.keyEngaged(KeyEvent.VK_2)) type = Select.SHOOT;
        if (selected && Game.keyEngaged(KeyEvent.VK_3) || Game.mouseEngaged(MouseEvent.BUTTON3)) {
            type = Select.NONE;
            selected = false;
        }
        
        if (!arrived) moveTo(moveLocation);
        if (moveLocation.x() == getCenter().x() && moveLocation.y() == getCenter().y()) arrived = true;
        else arrived = false;
        
        int bottom = (int) (getCenter().y() + ORIGINAL_SIZE / 2);
        int right = (int) (getCenter().x() + ORIGINAL_SIZE / 2);
        painter().drawString(turns + "", right, bottom);
    }
    
    boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    void resetTurn() {
        turns = 2;
        turnRange = RANGE;
    }
}
