package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Miner extends Ship {
    static final int MINE_RANGE = 50;

    public Miner(double x, double y, int team) {
        super(x, y, 100, 3, loadImage("img/ship/miner.png"), team);
        shipInformation("Mining Drone", "Movement", "Shield", "Harvest Beam");
        health = 8;
        energy = 4;
    }
    
    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(Color.YELLOW);
        drawRangePointer(MINE_RANGE);
        if (click() && RTSGame.asteroids.getAllWithin(mouseConstraint(MINE_RANGE)).size() > 0) {
            Asteroid asteroid = (Asteroid) RTSGame.asteroids.getAllWithin(mouseConstraint(MINE_RANGE)).get(0);
            if (asteroid.resources > 0) {
                RTSGame.addMessage("-1", Color.CYAN, mouseConstraint(MINE_RANGE));
                RTSGame.addMessage("+1", Color.CYAN, RTSGame.mothership[TEAM].getCenter());
                asteroid.resources -= 1;
                RTSGame.mothership[TEAM].energy += 1;
            }
            else RTSGame.addMessage("-0", Color.GRAY, mouseConstraint(MINE_RANGE));
            decreaseTurns(1);
        }
    }
}
