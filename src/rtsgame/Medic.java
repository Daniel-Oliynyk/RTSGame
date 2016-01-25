package rtsgame;

import gametools.Game;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Medic extends Ship {
    final int HEAL_RANGE = 100;

    public Medic(double x, double y) {
        super(x, y, 200, 2, loadImage("img/ship/medic.png"));
        shipInformation("Repair Ship", "Move", "Repair Beam", "Energy Bolt");
    }

    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.CYAN);
        drawRangePointer(HEAL_RANGE);
        if (click()) {
            if (RTSGame.ships.isWithin(mouseConstraint(HEAL_RANGE)) && !Game.mouseWithin(this)) {
                ((Ship) RTSGame.ships.getAllWithin(mouseConstraint(HEAL_RANGE)).get(0)).hp += 4;
                en--;
            }
            decreaseTurns(1);
        }
    }
    
    @Override
    protected void actionThree() {
        shootRange(200, Color.RED, 2, 2);
    }
}
