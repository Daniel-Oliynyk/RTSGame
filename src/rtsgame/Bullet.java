package rtsgame;

import gametools.*;
import java.awt.image.BufferedImage;

public class Bullet extends Sprite {
    Position target;
    
    public Bullet(Position center, Position target, int team) {
        this(center, target, RTSGame.bullet, team);
        setSpeed(10);
    }
    
    public Bullet(Position center, Position target, BufferedImage image, int team) {
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
            if (target.dist(getCenter()) < 1) {
                setAnimation(new Animation(RTSGame.explosion));
                centerOn(target);
            }
        }
        else if (getAnimation().isComplete()) remove(true);
    }
}
