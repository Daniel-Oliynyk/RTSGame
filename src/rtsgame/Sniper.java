package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Sniper extends Ship {

    public Sniper(double x, double y) {
        super(x, y, 100, 2, 2, loadImage("img/ship/sniper.png"));
        shipInformation("Corvette", "Move", "Shoot");
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.CYAN);
        drawRangePointer(500);
        if (click()) {
            RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(800)));
            RTSGame.bullets.add(new Bullet(mouseConstraint(30), mouseConstraint(800)));
            RTSGame.bullets.add(new Bullet(mouseConstraint(60), mouseConstraint(800)));
            decreaseTurns(1);
        }
    }
}
