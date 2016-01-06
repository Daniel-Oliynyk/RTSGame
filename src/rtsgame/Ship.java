package rtsgame;

import gametools.*;
import static gametools.Tools.*;

public class Ship extends Sprite {
    static final int RANGE = 150;
    boolean selected, moving;
    Position moveTo = Game.getCenter();
    
    public Ship() {
        super(loadImage("img/mothership.png"));
        centerOn(randomPosition(Game.getArea()));
    }
    
    @Override
    protected void update() {
        if (Game.mouseEngaged() && Game.mouseWithin(this)) selected = !selected;
        if (selected) {
            int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
            Game.painter().drawOval(centerX - RANGE, centerY - RANGE, RANGE * 2, RANGE * 2);
            int endX = (int) Game.mousePosition().x(), endY = (int) Game.mousePosition().y();
            if (getCenter().dist(Game.mousePosition()) > RANGE) {
                endX = centerX + (int) (Math.cos(getCenter().angleTo(Game.mousePosition())) * RANGE);
                endY = centerY + (int) (Math.sin(getCenter().angleTo(Game.mousePosition())) * RANGE);
            }
            endX = endX - (endX % 20);
            endY = endY - (endY % 20);
            Game.painter().drawLine(centerX, centerY, endX, endY);
            new Position(endX, endY).draw(5);
            if (Game.mouseReleased()) {
                moveTo = new Position(endX, endY);
                selected = false;
                moving = true;
            }
        }
        if (moving) moveTo(moveTo);
    }
    
    @Override
    public void draw() {
        super.draw();
        new Area(this).draw();
    }
}
