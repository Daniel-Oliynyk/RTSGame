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
        setSpeed(12);
    }
    
    @Override
    protected void update() {
        if (getAnimation().getAllFrames().length < 2) {
            moveTo(target);
            if (getCenter().x() == target.x() && getCenter().y() == target.y()) {
                setAnimation(new Animation(RTSGame.explosion));
                centerOn(target);
            }
        }
        else if (getAnimation().isComplete()) remove(true);
    }
}
