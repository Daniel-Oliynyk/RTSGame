package rtsgame;

import static gametools.Game.painter;
import gametools.Position;
import static gametools.Tools.*;
import java.awt.Color;

public class Tank extends Ship {

    public Tank(double x, double y) {
        super(x, y, 150, 1, loadImage("img/ship/tank.png"));
        setSpeed(5);
        shipInformation("Battlecruiser", "Move", "Shoot");
        hp = 48;
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(300);
        if (click()) {
            RTSGame.bullets.add(new Bullet(new Position(getCenter().x(), y), mouseConstraint(300)));
            RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(300)));
            RTSGame.bullets.add(new Bullet(new Position(getCenter().x(), y + getHeight()), mouseConstraint(300)));
            decreaseTurns(1);
        }
    }
}
