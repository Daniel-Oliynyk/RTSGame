package rtsgame;

import gametools.Game;
import gametools.Position;
import java.awt.Color;

public class Message {
    static final int DURATION = 60;
    final String message;
    final Color color;
    Position location;
    int countDown = DURATION;
    boolean remove = false;
    
    public Message(String message, Color color, Position location) {
        this.message = message;
        this.color = color;
        this.location = location;
    }
    
    public void draw() {
        Game.painter().setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), (255 / DURATION) * countDown));
        Game.painter().drawString(message, (int) location.x() - RTSGame.stringWidth(message) / 2, (int) location.y());
        countDown--;
        location.iy(-2);
        if (countDown < 0) remove = true;
    }
}
