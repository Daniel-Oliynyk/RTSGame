package rtsgame;

import gametools.*;
import java.awt.image.BufferedImage;

public class Bullet extends Sprite {
    Position target;
    
    public Bullet(Position center, Position target) {
        this(center, target, RTSGame.bullet);
        setSpeed(10);
    }
    
    public Bullet(Position center, Position target, BufferedImage image) {
        super(image);
        centerOn(center);
        face(target);
        this.target = target;
        setSpeed(14);
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
