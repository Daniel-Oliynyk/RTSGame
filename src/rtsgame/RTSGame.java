package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import static gametools.Tools.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.List;

public class RTSGame extends Game {
    static final List<String> GLOBAL_ACTIONS = Arrays.asList("Cancel", "Pan Camera", "Next", "End Turn"),
            ACTION_KEYS = Arrays.asList("Esc", "W] [A] [S] [D", "Tab", "Enter");
    static final int PAN_SPEED = 10;
    static int player = 0;
    static BufferedImage star, bullet, bolt;
    static Group[] ships, bullets;
    static Animation explosion;
    private Group stars;
    int starCooldown;
    Position prevPainter;
    
    public static void main(String[] args) {
        initialize(RTSGame.class);
        new RTSGame();
    }
    
    @Override
    protected void window() {
        setDimensions(1080, 720);
        setTitle("Strategy Game");
        setBackground(new Color(0x0b1037));
        create();
    }
    
    @Override
    protected void setup() {
        prevPainter = getPainterCenter();
        
        bullets = new Group[2];
        bullets[0] = new Group();
        bullets[1] = new Group();
        
        //<editor-fold defaultstate="collapsed" desc="Spawn Positions">
        ships = new Group[2];
        ships[0] = new Group();
        ships[1] = new Group();
        
        ships[0].add(new Miner(100, 350, 0));
        ships[0].add(new Miner(100, 450, 0));
        
        ships[0].add(new Battlecruiser(250, 250, 0));
        ships[0].add(new Mothership(250, 400, 0));
        ships[0].add(new Battlecruiser(250, 550, 0));
        
        ships[0].add(new Medic(400, 250, 0));
        ships[0].add(new Sniper(400, 400, 0));
        ships[0].add(new Medic(400, 550, 0));
        
        ships[0].add(new Fighter(550, 250, 0));
        ships[0].add(new Fighter(550, 400, 0));
        ships[0].add(new Fighter(550, 550, 0));
        
        ships[1].add(new Miner(1900, 350, 1));
        ships[1].add(new Miner(1900, 450, 1));
        
        ships[1].add(new Battlecruiser(1750, 250, 1));
        ships[1].add(new Mothership(1750, 400, 1));
        ships[1].add(new Battlecruiser(1750, 550, 1));
        
        ships[1].add(new Medic(1600, 250, 1));
        ships[1].add(new Sniper(1600, 400, 1));
        ships[1].add(new Medic(1600, 550, 1));
        
        ships[1].add(new Fighter(1450, 250, 1));
        ships[1].add(new Fighter(1450, 400, 1));
        ships[1].add(new Fighter(1450, 550, 1));
        
        for (Sprite ship : ships[1].getAll()) ship.setAngle(Math.PI);
//</editor-fold>
        
        star = loadImage("img/star.png");
        bullet = loadImage("img/bullet.png");
        bolt = loadImage("img/bolt.png");
        explosion = new Animation(loadSpriteSheet("img/explosion.png", 32, 32));
        explosion.setRepeatAmount(1);
        explosion.setSpeed(2);
        
        stars = new Group();
        for (int i = 0; i < 200; i++) stars.add(new Sprite(randomPosition(getArea()), star));
        starCooldown = 0;
        
        Font large = new Font("Arial", Font.PLAIN, 16);
        painter().setFont(large);
    }
    
    @Override
    protected void run() {
        starCooldown--;
        if (starCooldown < 0) {
            stars.remove(stars.get(0));
            stars.add(new Sprite(randomPosition(getArea()), star));
            starCooldown = 3;
        }
        for (Sprite dot : stars.getAll()) {
            if (dot.getX() < -dot.getWidth()) dot.setX(dot.getX() + getWidth());
            else if (dot.getX() > getWidth()) dot.setX(dot.getX() - getWidth());
            if (dot.getY() < -dot.getHeight()) dot.setY(dot.getY() + getHeight());
            else if (dot.getY() > getHeight()) dot.setY(dot.getY() - getHeight());
        }
        
        stars.drawAll();
        centerPainterOn(prevPainter);
        
        boolean allComplete = true;
        for (Sprite ship : ships[player].getAll()) if (!((Ship) ship).turnComplete()) allComplete = false;
        if (allComplete || keyEngaged(KeyEvent.VK_ENTER)) {
            for (Sprite ship : ships[0].getAll()) ((Ship) ship).resetTurn();
            for (Sprite ship : ships[1].getAll()) ((Ship) ship).resetTurn();
            player = player == 0? 1 : 0;
            focusOnNextShip();
        }
        
        if (keyEngaged(KeyEvent.VK_TAB)) focusOnNextShip();
        if (keyPressed(KeyEvent.VK_W)) translatePainter(0, PAN_SPEED);
        if (keyPressed(KeyEvent.VK_S)) translatePainter(0, -PAN_SPEED);
        if (keyPressed(KeyEvent.VK_A)) translatePainter(PAN_SPEED, 0);
        if (keyPressed(KeyEvent.VK_D)) translatePainter(-PAN_SPEED, 0);
        
        bullets[0].drawAll();
        bullets[1].drawAll();
        ships[0].drawAll();
        ships[1].drawAll();
        
        for (Sprite ship : ships[player].getAll()) ((Ship) ship).drawStats();
        for (Sprite ship : ships[player].getAll()) if (((Ship) ship).selected) ((Ship) ship).drawMenu();
        
        prevPainter = getPainterCenter();
        centerPainterOn(getCenter());
        drawActions();
    }
    
    static void focusOnNextShip() {
        boolean found = false, done = false;
        for (Sprite sprite : RTSGame.ships[player].getAll()) {
            Ship ship = (Ship) sprite;
            if (!found && !done && ship.selected) {
                ship.selected = false;
                found = true;
            }
            else if (found && !done && ship.getTurns() < 1) ship.selected = false;
            else if (found && !done && ship.getTurns() > 0) {
                ship.selected = true;
                centerPainterOn(ship);
                done = true;
            }
            else if (done) ship.selected = false;
        }
        
        if (!found || !done) {
            for (Sprite sprite : RTSGame.ships[player].getAll()) {
                Ship ship = (Ship) sprite;
                if (ship.getTurns() > 0) {
                    ship.selected = true;
                    centerPainterOn(ship);
                    break;
                }
            }
        }
    }
    
    private void drawActions() {
        int totalWidth = 0;
        for (int i = 0; i < ACTION_KEYS.size(); i++) totalWidth += stringWidth("[" + ACTION_KEYS.get(i) + "]  " + GLOBAL_ACTIONS.get(i));
        for (int i = 0; i < ACTION_KEYS.size() - 1; i++) totalWidth += stringWidth("   ");
        int offset = (Game.getWidth() - totalWidth) / 2;
        for (int i = 0; i < ACTION_KEYS.size(); i++) {
            painter().setColor(Color.GRAY);
            painter().drawString("[" + ACTION_KEYS.get(i) + "]  ", offset, Game.getHeight() - 30);
            offset += stringWidth("[" + ACTION_KEYS.get(i) + "]  ");
            painter().setColor(Color.WHITE);
            painter().drawString(GLOBAL_ACTIONS.get(i), offset, Game.getHeight() - 30);
            offset += stringWidth(GLOBAL_ACTIONS.get(i) + "   ");
        }
    }
    
    static final int stringWidth(String string) {
        FontMetrics font = painter().getFontMetrics();
        Rectangle2D rect = font.getStringBounds(string, painter());
        return (int) rect.getWidth();
    }
    
    static BufferedImage teamColor(BufferedImage image, int team) {
        if (team == 0) return image;
        else return RTSGame.colorImage(image, new int[] {255, 0, 0}, new int[] {0, 0, 255});
    }
    
    static BufferedImage colorImage(BufferedImage image, int[] old, int[] rep) {
        ColorModel cm = image.getColorModel();
        boolean al = cm.isAlphaPremultiplied();
        WritableRaster ra = image.copyData(image.getRaster().createCompatibleWritableRaster());
        BufferedImage copy = new BufferedImage(cm, ra, al, null);
        
        WritableRaster raster = copy.getRaster();
        for (int x = 0; x < copy.getWidth(); x++) {
            for (int y = 0; y < copy.getHeight(); y++) {
                int[] pixels = raster.getPixel(x, y, (int[]) null);
                if (pixels[0] == old[0] && pixels[1] == old[1] && pixels[2] == old[2]) {
                    pixels[0] = rep[0];
                    pixels[1] = rep[1];
                    pixels[2] = rep[2];
                }
                raster.setPixel(x, y, pixels);
            }
        }
        return copy;
    }
}
