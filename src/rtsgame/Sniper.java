package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Sniper extends Ship {
    final int SNIPER_RANGE = 400;
    
    public Sniper(double x, double y) {
        super(x, y, 150, 2, loadImage("img/ship/sniper.png"));
        shipInformation("Destroyer", "Move", "Plasma Beam", "EMP Round");
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
            drawRangePointer(SNIPER_RANGE);
            if (click()) {
                RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(SNIPER_RANGE), RTSGame.bolt));
                decreaseTurns(2);
                en -= 2;
            }
        }
    }
}
