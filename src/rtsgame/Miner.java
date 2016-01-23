package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Miner extends Ship {

    public Miner(double x, double y) {
        super(x, y, 75, 3, 2, loadImage("img/ship/miner.png"));
        shipInformation("Mining Drone", "Move", "Harvest");
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.CYAN);
        drawRangePointer(50);
        if (click()) decreaseTurns(1);
    }
}
