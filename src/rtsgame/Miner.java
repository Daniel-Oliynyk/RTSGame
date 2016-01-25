package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Miner extends Ship {

    public Miner(double x, double y) {
        super(x, y, 100, 3, loadImage("img/ship/miner.png"));
        shipInformation("Mining Drone", "Move", "Harvest");
        hp = 8;
        en = 4;
    }
    
    @Override
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.GREEN);
        drawRangePointer(50);
        if (click()) {
            decreaseTurns(1);
        }
    }
}
