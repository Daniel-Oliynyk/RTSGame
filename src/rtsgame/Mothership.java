package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Mothership extends Ship {

    public Mothership(double x, double y) {
        super(x, y, 100, 2, 3, loadImage("img/ship/mothership.png"));
        shipInformation("Mothership", "Move", "Shoot", "Shield");
    }

    @Override
    protected void actionThree() {
        painter().setColor(Color.CYAN);
        for (int i = 50; i <= 150; i += 10) drawRange(i);
        if (click()) decreaseTurns(1);
    }
}
