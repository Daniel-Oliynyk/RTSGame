package rtsgame;

import static gametools.Tools.*;

public class Fighter extends Ship {

    public Fighter(double x, double y) {
        super(x, y, 500, 2, loadImage("img/ship/fighter.png"));
    }
}
