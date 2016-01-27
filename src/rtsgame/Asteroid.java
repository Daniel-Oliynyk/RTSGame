package rtsgame;

import gametools.Game;
import gametools.Sprite;
import static gametools.Tools.*;
import java.awt.Color;

public class Asteroid extends Sprite {
    final int RADIUS = 32;
    int resources, health;
    
    public Asteroid(double x, double y, int type, int resources) {
        super(loadImage("img/terrain/asteroid" + type + ".png"));
        centerOn(x, y);
        this.resources = resources;
        health = RTSGame.ran.nextInt(18) + 6;
    }

    @Override
    public void draw(UpdateType type) {
        super.draw(type);
        Game.painter().setColor(resources > 0? Color.WHITE : Color.GRAY);
        Game.painter().drawString(resources + "", (int) getCenter().x() + RADIUS, (int) getCenter().y() + RADIUS);
    }
}
