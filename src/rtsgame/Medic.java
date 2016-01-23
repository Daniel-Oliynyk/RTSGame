package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Medic extends Ship {

    public Medic(double x, double y) {
        super(x, y, 150, 2, 3, loadImage("img/ship/medic.png"));
        shipInformation("Repair Ship", "Move", "Repair", "Shoot");
    }

    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.CYAN);
        drawRangePointer(100);
        if (click()) decreaseTurns(1);
    }
    
    @Override
    protected void actionThree() {
        if (getTurns() < 2) {
            setMode(2);
            actionTwo();
        }
        else {
            face(mouse());
            painter().setColor(Color.GREEN);
            drawRangePointer(300);
            if (click()) {
                RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(400)));
                decreaseTurns(getTurns());
            }
        }
    }
}
