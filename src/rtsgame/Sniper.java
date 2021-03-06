package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Sniper extends Ship {
    final int SNIPER_RANGE = 400;
    
    public Sniper(double x, double y, int team) {
        super(x, y, 150, 2, loadImage("img/ship/sniper.png"), team);
        shipInformation("Destroyer", "Movement", "Shield", "Plasma Beam", "EMP Round");
        health = 16;
    }
    
    @Override
    protected void actionThree() {
        shootRange(200, 8);
    }
    
    @Override
    protected void actionFour() {
        face(mouse());
        painter().setColor(energy > 1 && getTurns() > 1? Color.CYAN : Color.GRAY);
        drawRangePointer(SNIPER_RANGE);
        if (click() && energy > 0 && getTurns() > 1) {
            shootBullet(18, getCenter(), mouseConstraint(SNIPER_RANGE), RTSGame.bolt, TEAM);
            RTSGame.addMessage("-2", Color.CYAN, getCenter());
            energy -= 2;
            decreaseTurns(2);
        }
    }
}
