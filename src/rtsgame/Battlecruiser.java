package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Battlecruiser extends Ship {

    public Battlecruiser(double x, double y, int team) {
        super(x, y, 150, 1, loadImage("img/ship/battlecruiser.png"), team);
        setSpeed(3);
        shipInformation("Battlecruiser", "Move", "Rail Gun");
        health = 48;
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(300);
        if (click()) {
            shootBullet(getCenter(), mouseConstraint(300), RTSGame.bolt, TEAM);
            decreaseTurns(1);
            energy--;
        }
    }
}
