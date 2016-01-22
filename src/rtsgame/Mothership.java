package rtsgame;

import static gametools.Tools.*;

public class Mothership extends Ship {

    public Mothership(double x, double y) {
        super(x, y, 100, 2, 2, loadImage("img/ship/mothership.png"));
    }
}
