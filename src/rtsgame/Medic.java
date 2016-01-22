package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Medic extends Ship {

    public Medic(double x, double y) {
        super(x, y, 150, 3, 2, loadImage("img/ship/medic.png"));
    }

    @Override
    protected void actionTwo() {
        painter().setColor(Color.CYAN);
        drawBoth(100);
        if (click()) decreaseTurns(1);
    }
    
    @Override
    protected void actionThree() {
        if (getTurns() < 2) {
            setMode(2);
            actionTwo();
        }
        else {
            painter().setColor(Color.GREEN);
            drawBoth(400);
            if (click()) {
                RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(400)));
                decreaseTurns(getTurns());
            }
        }
    }
}
