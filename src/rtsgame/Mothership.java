package rtsgame;

import gametools.Game;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Mothership extends Ship {
    final int CHARGE_RANGE = 150, SHOOT_RANGE = 300;

    public Mothership(double x, double y, int team) {
        super(x, y, 100, 2, loadImage("img/ship/mothership.png"), team);
        setSpeed(3);
        shipInformation("Mothership", "Movement", "Shield", "Plasma Turrets", "Charge Beam");
        health = 72;
        energy = 6;
    }

    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(SHOOT_RANGE);
        if (click()) {
            shootBullet(8, getCenter(), mouseConstraint(SHOOT_RANGE), TEAM);
            shootBullet(8, mouseConstraint(60), mouseConstraint(SHOOT_RANGE), TEAM);
            decreaseTurns(1);
        }
    }
    
    @Override
    protected void actionFour() {
        face(mouse());
        painter().setColor(energy > 0? Color.CYAN : Color.GRAY);
        drawRangePointer(CHARGE_RANGE);
        if (click() && energy > 3) {
            if (RTSGame.ships[TEAM].isWithin(mouseConstraint(CHARGE_RANGE)) && !Game.mouseWithin(this)) {
                ((Ship) RTSGame.ships[TEAM].getAllWithin(mouseConstraint(CHARGE_RANGE)).get(0)).energy += 4;
                RTSGame.addMessage("-4", Color.CYAN, getCenter());
                RTSGame.addMessage("+4", Color.CYAN, mouseConstraint(CHARGE_RANGE));
                energy -= 4;
                decreaseTurns(1);
            }
        }
    }
}
