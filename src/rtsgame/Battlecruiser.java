package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Battlecruiser extends Ship {

    public Battlecruiser(double x, double y) {
        super(x, y, 150, 1, loadImage("img/ship/battlecruiser.png"));
        setSpeed(3);
        shipInformation("Battlecruiser", "Move", "Rail Gun");
        hp = 48;
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(300);
        if (click()) {
            RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(300), RTSGame.bolt));
            decreaseTurns(1);
            en--;
        }
    }
}
