package rtsgame;

import gametools.*;
import static gametools.Tools.*;

public class RTSGame extends Game {
    
    public static void main(String[] args) {
        initialize(RTSGame.class);
        new RTSGame();
    }
    
    @Override
    protected void window() {
        setTitle("RTS Game");
        create();
    }
    
    @Override
    protected void setup() {
        
    }
    
    @Override
    protected void run() {
        
    }
}
