package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Miner extends Ship {

    public Miner(double x, double y) {
        super(x, y, 75, 2, 3, loadImage("img/ship/miner.png"));
    }
    
    @Override
    protected void actionTwo() {
        painter().setColor(Color.CYAN);
        drawBoth(50);
        if (click()) decreaseTurns(1);
    }
}
