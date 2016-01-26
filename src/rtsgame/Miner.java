package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Miner extends Ship {

    public Miner(double x, double y, int team) {
        super(x, y, 100, 3, loadImage("img/ship/miner.png"), team);
        shipInformation("Mining Drone", "Movement", "Shield", "Harvest Beam");
        health = 8;
        energy = 4;
    }
    
    @Override
    protected void actionThree() {
        face(mouse());
        painter().setColor(Color.GREEN);
        drawRangePointer(50);
        if (click()) {
            decreaseTurns(1);
        }
    }
}
