package rtsgame;

import gametools.*;
import static gametools.Tools.*;

public class Bullet extends Sprite {
    Position target;
    
    public Bullet(Position center, Position target) {
        super(loadImage("img/bullet.png"));
        centerOn(center);
        face(target);
        this.target = target;
        setSpeed(8);
    }
    
    @Override
    protected void update() {
        moveTo(target);
        if (getCenter().x() == target.x() && getCenter().y() == target.y()) remove(true);
    }
}
