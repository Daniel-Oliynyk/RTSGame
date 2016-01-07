package rtsgame;

import gametools.*;
import static gametools.Tools.*;
import java.awt.event.MouseEvent;

public class Ship extends Sprite {
    static final int RANGE = 150;
    boolean selected, moving;
    Position moveTo = Game.getCenter();
    
    public Ship(Position pos) {
        super(loadImage("img/mothership.png"));
        centerOn(pos);
    }
    
    @Override
    protected void update() {
        if (selected) {
            int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
            Game.painter().drawOval(centerX - RANGE, centerY - RANGE, RANGE * 2, RANGE * 2);
            int endX = (int) Game.mousePosition().x(), endY = (int) Game.mousePosition().y();
            if (getCenter().dist(Game.mousePosition()) > RANGE) {
                endX = centerX + (int) (Math.cos(getCenter().angleTo(Game.mousePosition())) * RANGE);
                endY = centerY + (int) (Math.sin(getCenter().angleTo(Game.mousePosition())) * RANGE);
            }
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            if (Game.mouseEngaged(MouseEvent.BUTTON1)) {
                moveTo = new Position(endX, endY);
                selected = false;
                moving = true;
            }
        }
        if (Game.mouseEngaged(MouseEvent.BUTTON1) && Game.mouseWithin(this)) selected = true;
        else if (Game.mouseEngaged(MouseEvent.BUTTON3)) selected = false;
        if (moving) moveTo(moveTo);
    }
    
    @Override
    public void draw() {
        super.draw();
        new Area(this).draw();
    }
}
