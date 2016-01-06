package rtsgame;

import gametools.*;
import static gametools.Tools.*;

public class Ship extends Sprite {
    
    public Ship() {
        super(loadImage("img/mothership.png"));
    }
    
    @Override
    protected void update() {
        if (isWithin(Game.mousePosition())) {
            Game.painter().drawOval((int) x, (int) y, 300, 300);
        }
    }
}
