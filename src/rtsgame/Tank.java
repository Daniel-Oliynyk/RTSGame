package rtsgame;

import static gametools.Tools.*;

public class Tank extends Ship {

    public Tank(double x, double y) {
        super(x, y, 150, 2, 1, loadImage("img/ship/tank.png"));
    }
}
