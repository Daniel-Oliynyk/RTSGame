package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.event.MouseEvent;

public class Ship extends Sprite {
    static final int RANGE = 300;
    int turnRange = RANGE;
    boolean moveSelected, shootSelected, moving;
    int turns = 2;
    Group bullets;
    Position lastLocation = Game.getCenter(), moveLocation = Game.getCenter();
    
    public Ship(Position pos) {
        super(loadImage("img/mothership.png"));
        centerOn(pos);
        bullets = new Group();
    }
    
    @Override
    protected void update() {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        int endX = (int) Game.mousePosition().x(), endY = (int) Game.mousePosition().y();
        if (moveSelected) {
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
                moving = true;
                turnRange -= getCenter().dist(moveLocation);
                if (turns > 1 && turnRange < RANGE / 2) turns--;
                turns--;
            }
        }
        else if (shootSelected) {
            Game.painter().setColor(Color.RED);
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            Game.painter().setColor(Color.WHITE);
            if (Game.mouseEngaged()) {
                Sprite bullet = new Sprite(RTSGame.bullet);
                bullet.centerOn(this);
                bullet.face(Game.mousePosition());
                bullet.setRelationalMovement(true);
                bullet.script(new Script(bullet) {
                    @Override
                    public void update() {
                        sprite().move(Direction.EAST);
                    }
                });
                bullets.add(bullet);
                turns--;
                if (turns < 1) shootSelected = false;
            }
        }
        
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this) && turns > 0) {
            moveSelected = true;
            shootSelected = false;
        }
        else if (Game.mouseEngaged(MouseEvent.BUTTON2) && Game.mouseWithin(this) && turns > 0) {
            moveSelected = false;
            shootSelected = true;
        }
        else if (Game.mouseEngaged(MouseEvent.BUTTON3)) {
            moveSelected = false;
            shootSelected = false;
        }
        if (moving) moveTo(moveLocation);
        lastLocation = getCenter();
    }
    
    boolean turnComplete() {
        return turns < 1 && getCenter().x() == lastLocation.x() && getCenter().y() == lastLocation.y();
    }
    
    void resetTurn() {
        turns = 2;
        turnRange = RANGE;
    }
    
    @Override
    public void draw(UpdateType type) {
        if (type.draw()) bullets.drawAll();
        super.draw(type);
        if (type.draw()) Game.painter().drawString(turns + "", (int) (x + getWidth()), (int) (y + getHeight()));
    }
}
