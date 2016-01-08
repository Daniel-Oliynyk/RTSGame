package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class Ship extends Sprite {
    static final int RANGE = 400, ORIGINAL_SIZE = 128;
    int turnRange = RANGE;
    boolean moveSelected, shootSelected, arrived = true;
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
        if (moveSelected) {
            face(Game.mousePosition());
            int tempRange = turns < 2? turnRange / 2 : turnRange;
            if (turns > 1) {
                Game.painter().drawOval(centerX - RANGE / 2, centerY - RANGE / 2, RANGE, RANGE);
                Game.painter().drawOval(centerX - RANGE, centerY - RANGE, RANGE * 2, RANGE * 2);
            }
            else Game.painter().drawOval(centerX - tempRange, centerY - tempRange, tempRange * 2, tempRange * 2);
            if (getCenter().dist(Game.mousePosition()) > tempRange) {
                endX = centerX + (int) (Math.cos(getCenter().angleTo(Game.mousePosition())) * tempRange);
                endY = centerY + (int) (Math.sin(getCenter().angleTo(Game.mousePosition())) * tempRange);
            }
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            if (Game.mouseEngaged(MouseEvent.BUTTON1)) {
                moveLocation = new Position(endX, endY);
                moveSelected = false;
                turnRange -= getCenter().dist(moveLocation);
                if (turns > 1 && turnRange < RANGE / 2) turns--;
                turns--;
            }
        }
        else if (shootSelected) {
            face(Game.mousePosition());
            Game.painter().setColor(Color.RED);
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            Game.painter().setColor(Color.WHITE);
            if (Game.mouseEngaged(MouseEvent.BUTTON1) || Game.mouseEngaged(MouseEvent.BUTTON2)) {
                RTSGame.bullets.add(new Bullet(getCenter(), Game.mousePosition()));
                turns--;
                if (turns < 1) shootSelected = false;
            }
        }
        
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && turns > 0 && arrived) {
            moveSelected = true;
            shootSelected = false;
        }
        else if (Game.mouseEngaged(MouseEvent.BUTTON2) && Game.mouseWithin(this) && turns > 0 && arrived) {
            moveSelected = false;
            shootSelected = true;
        }
        else if (Game.mouseEngaged(MouseEvent.BUTTON3)) {
            moveSelected = false;
            shootSelected = false;
        }
        if (!arrived) moveTo(moveLocation);
        if (moveLocation.x() == getCenter().x() && moveLocation.y() == getCenter().y()) arrived = true;
        else arrived = false;
    }
    
    boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    void resetTurn() {
        turns = 2;
        turnRange = RANGE;
    }
    
    @Override
    public void draw(UpdateType type) {
        super.draw(type);
        if (type.draw()) {
            int newX = (int) (getCenter().x() + ORIGINAL_SIZE / 2);
            int newY = (int) (getCenter().y() + ORIGINAL_SIZE / 2);
            Game.painter().drawString(turns + "", newX, newY);
        }
    }
}
