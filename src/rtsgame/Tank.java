package rtsgame;

import static gametools.Game.painter;
import gametools.Position;
import static gametools.Tools.*;
import java.awt.Color;

public class Tank extends Ship {

    public Tank(double x, double y) {
        super(x, y, 150, 1, 2, loadImage("img/ship/tank.png"));
        shipInformation("Battlecruiser", "Move", "Shoot");
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(500);
        if (click()) {
            RTSGame.bullets.add(new Bullet(new Position(getCenter().x(), y), mouseConstraint(500)));
            RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(500)));
            RTSGame.bullets.add(new Bullet(new Position(getCenter().x(), y + getHeight()), mouseConstraint(500)));
            decreaseTurns(1);
        }
    }
}
