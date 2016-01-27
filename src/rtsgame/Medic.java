package rtsgame;

import gametools.Game;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Medic extends Ship {
    static final int HEAL_RANGE = 100;

    public Medic(double x, double y, int team) {
        super(x, y, 200, 2, loadImage("img/ship/medic.png"), team);
        shipInformation("Service Vessel", "Movement", "Shield", "Repair Beam", "Energy Bolt");
    }

    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(energy > 0? Color.CYAN : Color.GRAY);
        drawRangePointer(HEAL_RANGE);
        if (click() && energy > 0) {
            if (RTSGame.ships[TEAM].isWithin(mouseConstraint(HEAL_RANGE)) && !Game.mouseWithin(this)) {
                ((Ship) RTSGame.ships[TEAM].getAllWithin(mouseConstraint(HEAL_RANGE)).get(0)).health += 12;
                energy--;
                RTSGame.addMessage("+15", Color.GREEN, mouseConstraint(HEAL_RANGE));
                RTSGame.addMessage("-1", Color.CYAN, getCenter());
                decreaseTurns(1);
            }
        }
    }
    
    @Override
    protected void actionFour() {
        face(mouse());
        painter().setColor(getTurns() > 1? Color.RED : Color.GRAY);
        drawRangePointer(200);
        if (click() && getTurns() > 1) {
            shootBullet(8, getCenter(), mouseConstraint(200), TEAM);
            decreaseTurns(2);
        }
    }
}
