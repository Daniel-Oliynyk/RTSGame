package rtsgame;

import static gametools.Game.painter;
import gametools.Position;
import static gametools.Tools.*;
import java.awt.Color;

public class Battlecruiser extends Ship {

    public Battlecruiser(double x, double y, int team) {
        super(x, y, 200, 1, loadImage("img/ship/battlecruiser.png"), team);
        setSpeed(3);
        shipInformation("Battlecruiser", "Movement", "Shield", "Rail Gun");
        health = 48;
    }
    
    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(energy > 0? Color.CYAN : Color.GRAY);
        drawRangePointer(300);
        if (click() && energy > 0) {
            shootBullet(getCenter(), mouseConstraint(300), RTSGame.bolt, TEAM);
            RTSGame.addMessage("-1", Color.CYAN, new Position(x, y + 50));
            energy--;
            decreaseTurns(1);
        }
    }
}
