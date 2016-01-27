package rtsgame;

import gametools.*;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Bullet extends Sprite {
    final int TEAM;
    int damage;
    Position target;
    
    public Bullet(int damage, Position center, Position target, int team) {
        this(damage, center, target, RTSGame.bullet, team);
        setSpeed(10);
    }
    
    public Bullet(int damage, Position center, Position target, BufferedImage image, int team) {
        super(image);
        centerOn(center);
        face(target);
        this.damage = damage;
        this.target = target;
        TEAM = team;
        setSpeed(14);
    }
    
    @Override
    protected void update() {
        if (getAnimation().getAllFrames().length < 2) {
            moveTo(target);
            boolean collide = false;
            if (RTSGame.ships[TEAM].getAllWithin(this).size() > 0) {
                Ship ship = (Ship) RTSGame.ships[TEAM].getAllWithin(this).get(0);
                int dmg = ship.shield? damage / 2 : damage;
                ship.health -= dmg;
                collide = true;
                RTSGame.addMessage("-" + dmg, ship.shield? Color.YELLOW : Color.RED, getCenter());
                if (ship.health < 1) {
                    ship.remove(true);
                    Sprite explosion = new Sprite(ship.getWidth() > 100? RTSGame.explosionLarge : RTSGame.explosionMedium);
                    explosion.centerOn(ship);
                    explosion.script(new Script() {
                        @Override
                        public void update() {
                            if (explosion.getAnimation().isComplete()) explosion.remove(true);
                        }
                    });
                    RTSGame.explosions.add(explosion);
                }
                
            }
            if (target.dist(getCenter()) < 1 || collide) {
                setAnimation(new Animation(RTSGame.explosionSmall));
                centerOn(target);
            }
        }
        else if (getAnimation().isComplete()) remove(true);
    }
}
