package rtsgame;

import gametools.Game;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Mothership extends Ship {
    final int CHARGE_RANGE = 150;

    public Mothership(double x, double y, int team) {
        super(x, y, 100, 2, loadImage("img/ship/mothership.png"), team);
        setSpeed(3);
        shipInformation("Mothership", "Move", "Twin Plasma Turrets", "Charge Beam");
        health = 72;
        energy = 16;
    }

    @Override
    protected void actionTwo() {
        shootRange(300);
    }
    
    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(Color.CYAN);
        drawRangePointer(CHARGE_RANGE);
        if (click()) {
            if (RTSGame.ships[TEAM].isWithin(mouseConstraint(CHARGE_RANGE)) && !Game.mouseWithin(this)) {
                ((Ship) RTSGame.ships[TEAM].getAllWithin(mouseConstraint(CHARGE_RANGE)).get(0)).energy += 8;
                energy--;
            }
            decreaseTurns(1);
            energy--;
        }
    }
}
