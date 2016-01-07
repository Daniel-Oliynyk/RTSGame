package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.event.MouseEvent;

public class Ship extends Sprite {
    static final int RANGE = 300;
    int turnRange = RANGE;
    boolean selected, moving, turnComplete;
    int turns = 2;
    Position moveLocation = Game.getCenter();
    
    public Ship(Position pos) {
        super(loadImage("img/mothership.png"));
        centerOn(pos);
    }
    
    @Override
    protected void update() {
        if (selected) {
            int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
            if (turns > 1) {
                Game.painter().drawOval(centerX - RANGE / 2, centerY - RANGE / 2, RANGE, RANGE);
                Game.painter().drawOval(centerX - RANGE, centerY - RANGE, RANGE * 2, RANGE * 2);
            }
            else Game.painter().drawOval(centerX - turnRange, centerY - turnRange, turnRange * 2, turnRange * 2);
            int endX = (int) Game.mousePosition().x(), endY = (int) Game.mousePosition().y();
            if (getCenter().dist(Game.mousePosition()) > turnRange) {
                endX = centerX + (int) (Math.cos(getCenter().angleTo(Game.mousePosition())) * turnRange);
                endY = centerY + (int) (Math.sin(getCenter().angleTo(Game.mousePosition())) * turnRange);
            }
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            if (Game.mouseEngaged(MouseEvent.BUTTON1)) {
                moveLocation = new Position(endX, endY);
                selected = false;
                moving = true;
                turnRange -= getCenter().dist(moveLocation);
                if (turns > 1 && turnRange < RANGE / 2) turns--;
                turns--;
                if (turns < 1) turnComplete = true;
                if (turns > 0 && turnRange < RANGE) turnRange -= RANGE / 3;
            }
        }
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && !turnComplete) selected = true;
        else if (Game.mouseEngaged(MouseEvent.BUTTON3)) selected = false;
        if (moving) moveTo(moveLocation);
    }
    
    boolean turnComplete() {
        return turnComplete && getCenter().x() == moveLocation.x() && getCenter().y() == moveLocation.y();
    }
    
    void resetTurn() {
        turns = 2;
        turnRange = RANGE;
        turnComplete = false;
    }
    
    @Override
    public void draw(UpdateType type) {
        super.draw(type);
        if (type.draw()) Game.painter().drawString(turns + "", (int) (x + getWidth()), (int) (y + getHeight()));
    }
}
