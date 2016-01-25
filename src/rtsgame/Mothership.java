package rtsgame;

import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;

public class Mothership extends Ship {

    public Mothership(double x, double y) {
        super(x, y, 100, 2, loadImage("img/ship/mothership.png"));
        setSpeed(3);
        shipInformation("Mothership", "Move", "Shoot", "Shield");
        hp = 72;
        en = 16;
    }

    @Override
    protected void actionThree() {
        painter().setColor(Color.CYAN);
        for (int i = 50; i <= 150; i += 10) drawRange(i);
        if (click()) {
            decreaseTurns(1);
            en--;
        }
    }
}
