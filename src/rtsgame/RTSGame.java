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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RTSGame extends Game {
    static final List<String> GLOBAL_ACTIONS = Arrays.asList("Cancel", "Pan Camera", "Next", "End Turn"),
            ACTION_KEYS = Arrays.asList("Esc", "W] [A] [S] [D", "Tab", "Backspace");
    static final int PAN_SPEED = 15;
    static int player;
    static BufferedImage star, bullet, bolt, shieldSmall, shieldLarge;
    static Group[] ships, bullets;
    static Group asteroids, explosions;
    static Ship[] mothership;
    static Animation explosionSmall, explosionMedium, explosionLarge;
    static List<Message> overlay;
    static Position prevPainter;
    static Random ran;
    private Group stars;
    private int starCooldown;
    
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
        ran = new Random();
        player = 0;
        prevPainter = getPainterCenter();
        
        //<editor-fold defaultstate="collapsed" desc="Asteroid Spawning">
        asteroids = new Group();
        asteroids.add(new Asteroid(-100, 350, ran.nextInt(3), 3));
        asteroids.add(new Asteroid(-100, 450, ran.nextInt(3), 3));
        
        asteroids.add(new Asteroid(900, 100, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 200, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 300, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 400, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 500, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 600, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(900, 700, ran.nextInt(3), ran.nextInt(3) + 1));
        
        asteroids.add(new Asteroid(1000, 150, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1000, 250, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1000, 350, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1000, 450, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1000, 550, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1000, 650, ran.nextInt(3), ran.nextInt(3) + 1));
        
        asteroids.add(new Asteroid(1100, 100, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 200, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 300, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 400, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 500, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 600, ran.nextInt(3), ran.nextInt(3) + 1));
        asteroids.add(new Asteroid(1100, 700, ran.nextInt(3), ran.nextInt(3) + 1));
        
        asteroids.add(new Asteroid(2100, 350, ran.nextInt(3), 3));
        asteroids.add(new Asteroid(2100, 450, ran.nextInt(3), 3));
        //</editor-fold>
        
        bullets = new Group[2];
        bullets[0] = new Group();
        bullets[1] = new Group();
        
        //<editor-fold defaultstate="collapsed" desc="Ship Spawning">
        ships = new Group[2];
        mothership = new Ship[2];
        
        ships[0] = new Group();
        ships[1] = new Group();
        
        ships[0].add(new Miner(100, 350, 0));
        ships[0].add(new Miner(100, 450, 0));
        
        ships[0].add(new Battlecruiser(250, 250, 0));
        mothership[0] = new Mothership(250, 400, 0);
        ships[0].add(mothership[0]);
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
        mothership[1] = new Mothership(1750, 400, 1);
        ships[1].add(mothership[1]);
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
        shieldSmall = loadImage("img/shield-small.png");
        shieldLarge = loadImage("img/shield-large.png");
        
        //<editor-fold defaultstate="collapsed" desc="Explosion Animations">
        explosionSmall = new Animation(loadSpriteSheet("img/explosion-small.png", 32, 32));
        explosionSmall.setRepeatAmount(1);
        explosionSmall.setSpeed(2);
        explosionMedium = new Animation(loadSpriteSheet("img/explosion-medium.png", 64, 64));
        explosionMedium.setRepeatAmount(1);
        explosionMedium.setSpeed(2);
        explosionLarge = new Animation(loadSpriteSheet("img/explosion-large.png", 128, 128));
        explosionLarge.setRepeatAmount(1);
        explosionLarge.setSpeed(2);
        //</editor-fold>
        
        stars = new Group();
        for (int i = 0; i < 200; i++) stars.add(new Sprite(randomPosition(getArea()), star));
        starCooldown = 0;
        
        Font large = new Font("Arial", Font.PLAIN, 16);
        painter().setFont(large);
        
        explosions = new Group();
        overlay = new ArrayList<>();
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
        if (allComplete || keyEngaged(KeyEvent.VK_BACK_SPACE)) {
            for (Sprite ship : ships[player].getAll()) ((Ship) ship).resetTurn(false);
            player = player == 0? 1 : 0;
            bullets[player].clear(true);
            for (Sprite ship : ships[player].getAll()) ((Ship) ship).resetTurn(true);
            focusOnNextShip();
        }
        
        if (keyEngaged(KeyEvent.VK_TAB)) focusOnNextShip();
        if (keyPressed(KeyEvent.VK_W) || keyPressed(KeyEvent.VK_UP)) translatePainter(0, PAN_SPEED);
        if (keyPressed(KeyEvent.VK_S) || keyPressed(KeyEvent.VK_DOWN)) translatePainter(0, -PAN_SPEED);
        if (keyPressed(KeyEvent.VK_A) || keyPressed(KeyEvent.VK_LEFT)) translatePainter(PAN_SPEED, 0);
        if (keyPressed(KeyEvent.VK_D) || keyPressed(KeyEvent.VK_RIGHT)) translatePainter(-PAN_SPEED, 0);
        
        asteroids.drawAll();
        explosions.drawAll();
        ships[(player + 1) % 2].drawAll();
        ships[player].drawAll();
        bullets[(player + 1) % 2].drawAll();
        bullets[player].drawAll();
        
        for (Sprite ship : ships[player].getAll()) ((Ship) ship).drawStats();
        for (Sprite ship : ships[player].getAll()) if (((Ship) ship).selected) ((Ship) ship).drawMenu();
        
        for (Iterator<Message> it = overlay.iterator(); it.hasNext();) {
            Message message = it.next();
            message.draw();
            if (message.remove) it.remove();
        }
        
        prevPainter = getPainterCenter();
        centerPainterOn(getCenter());
        drawActions();
        painter().setColor(player == 0? Color.RED : Color.BLUE);
        painter().drawString("Player " + (player + 1), (int) Game.getCenter().x() - stringWidth("Player " + (player + 1)) / 2, 40);
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
    
    static void addMessage(String message, Color col, Position pos) {
        overlay.add(new Message(message, col, pos));
    }
}
