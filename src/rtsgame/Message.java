package rtsgame;

import gametools.Game;
import gametools.Position;
import java.awt.Color;

public class Message {
    final String message;
    final Color color;
    final Position location;
    int countDown = 60;
    boolean remove = false;
    
    public Message(String message, Color color, Position location) {
        this.message = message;
        this.color = color;
        this.location = location;
    }
    
    public void draw() {
        Game.painter().setColor(color);
        Game.painter().drawString(message, (int) location.x() - RTSGame.stringWidth(message) / 2, (int) location.y());
        countDown--;
        if (countDown < 0) remove = true;
    }
}
