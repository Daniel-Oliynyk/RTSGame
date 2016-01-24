package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Sniper extends Ship {

    public Sniper(double x, double y) {
        super(x, y, 150, 2, loadImage("img/ship/sniper.png"));
        shipInformation("Corvette", "Move", "Shoot", "Laser");
        hp = 16;
    }

    @Override
    protected void actionTwo() {
        shootRange(200);
    }
    
    @Override
    protected void actionThree() {
        if (getTurns() < 2) {
            setMode(2);
            actionTwo();
        }
        else {
            face(mouse());
            painter().setColor(Color.CYAN);
            drawRangePointer(500);
            if (click()) {
                RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(500)));
                RTSGame.bullets.add(new Bullet(mouseConstraint(30), mouseConstraint(500)));
                RTSGame.bullets.add(new Bullet(mouseConstraint(60), mouseConstraint(500)));
                decreaseTurns(2);
                en--;
            }
        }
    }
}
