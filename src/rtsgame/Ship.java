package rtsgame;

import gametools.*;
import static rtsgame.RTSGame.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends Sprite {
    final int MOVE_RANGE, MAX_TURNS, SNAP = 15, TEAM, ENEMY;
    final Dimension ORIGINAL_SIZE;
    private int mode = 1, turns;
    int health = 24, energy = 8;
    boolean selected, arrived = true;
    private String name = "Ship";
    private List<String> actions = Arrays.asList("Move", "Plasma Beam");
    Position moveLocation;
    
    public Ship(double x, double y, int range, int turns, BufferedImage image, int team) {
        super(teamColor(image, team));
        setSpeed(7);
        centerOn(x, y);
        moveLocation = getCenter();
        MOVE_RANGE = range;
        ORIGINAL_SIZE = new Dimension(image.getWidth(), image.getHeight());
        MAX_TURNS = turns < 1? 1 : turns;
        resetTurn();
        TEAM = team;
        ENEMY = team == 0? 1 : 0;
    }
    
    @Override
    protected final void update() {
        if (selected) {
            if (Game.keyEngaged(KeyEvent.VK_ESCAPE) || Game.mouseEngaged(MouseEvent.BUTTON3)) deselect();
            if (Game.keyEngaged(KeyEvent.VK_1)) mode = 1;
            if (Game.keyEngaged(KeyEvent.VK_2)) mode = 2;
            if (Game.keyEngaged(KeyEvent.VK_3) && actions.size() > 2) mode = 3;
            if (Game.keyEngaged(KeyEvent.VK_4) && actions.size() > 3) mode = 4;
            if (Game.keyEngaged(KeyEvent.VK_5) && actions.size() > 4) mode = 5;
            if (Game.keyEngaged(KeyEvent.VK_6) && actions.size() > 5) mode = 6;
            if (arrived) selected();
        }
        if (!arrived) moveTo(moveLocation);
        if (moveLocation.dist(getCenter()) < 1/* || ships.getAllWithin(this).size() > 1*/) arrived = true;
        if (player == TEAM && click() && Game.mouseWithin(this) && arrived && turns > 0) {
            boolean otherShipSelected = false;
            for (Sprite ship : ships[TEAM].getAll()) if (((Ship) ship).selected) otherShipSelected = true;
            selected = !otherShipSelected;
        }
    }
    
    private void selected() {
        if (mode == 1) actionOne();
        else if (mode == 2) actionTwo();
        else if (mode == 3) actionThree();
        else if (mode == 4) actionFour();
        else if (mode == 5) actionFive();
        else if (mode == 6) actionSix();
    }
    
    protected void actionOne() {
        face(mouse());
        painter().setColor(Color.WHITE);
        for (int i = 1; i <= turns; i++) drawRange(MOVE_RANGE * i);
        drawPointer(mouseConstraint(MOVE_RANGE * turns, MOVE_RANGE));
        if (click()) {
            moveLocation = mouseConstraint(MOVE_RANGE * turns, MOVE_RANGE);
            arrived = false;
            decreaseTurns((int) Math.ceil((getCenter().dist(moveLocation) - SNAP) / MOVE_RANGE));
            mode = 2;
        }
    }
    
    protected void actionTwo() {
        shootRange(300);
    }
    
    protected void actionThree() {}
    
    protected void actionFour() {}
    
    protected void actionFive() {}
    
    protected void actionSix() {}
    
    protected final void shootRange(int range) {
        shootRange(range, Color.RED, 1, 2);
    }
    
    protected final void shootRange(int range, Color col, int turns, int alt) {
        if (getTurns() < turns) {
            setMode(alt);
            selected();
        }
        else {
            face(mouse());
            painter().setColor(col);
            drawRangePointer(range);
            if (click()) {
                shootBullet(getCenter(), mouseConstraint(range), TEAM);
                decreaseTurns(turns);
            }
        }
    }
    
    protected final void shipInformation(String... info) {
        name = info[0];
        this.actions = new ArrayList<>(Arrays.asList(info));
        this.actions.remove(name);
    }
    
    protected final boolean click() {
        return Game.mouseEngaged(MouseEvent.BUTTON1) || Game.keyEngaged(KeyEvent.VK_SPACE);
    }
    
    protected final Position mouse() {
        return Game.mousePosition();
    }
    
    final void drawMenu() {
        Position prevPainter = Game.getPainterCenter();
        Game.centerPainterOn(Game.getCenter());
        
        painter().setColor(Color.YELLOW);
        painter().drawString(name, (Game.getWidth() - stringWidth(name)) / 2, Game.getHeight() - 120);
        
        Font prevFont = painter().getFont();
        Font small = new Font("Arial", Font.PLAIN, 12);
        painter().setFont(small);
        painter().setColor(new Color(0xbfbfbf));
        String stats = "Health: " + health + "  Energy: " + energy + "  Turns: " + turns;
        painter().drawString(stats, (Game.getWidth() - stringWidth(stats)) / 2, Game.getHeight() - 100);
        painter().setFont(prevFont);
        
        int totalWidth = 0;
        for (int i = 0; i < actions.size(); i++) totalWidth += stringWidth("[" + (i + 1) + "]  " + actions.get(i));
        for (int i = 0; i < actions.size() - 1; i++) totalWidth += stringWidth("   ");
        int offset = (Game.getWidth() - totalWidth) / 2;
        
        for (int i = 0; i < actions.size(); i++) {
            painter().setColor(Color.GRAY);
            painter().drawString("[" + (i + 1) + "]  ", offset, Game.getHeight() - 60);
            offset += stringWidth("[" + (i + 1) + "]  ");
            painter().setColor(mode - 1 == i? Color.YELLOW : Color.WHITE);
            painter().drawString(actions.get(i), offset, Game.getHeight() - 60);
            offset += stringWidth(actions.get(i) + "   ");
        }
        
        Game.centerPainterOn(prevPainter);
    }
    
    final void drawStats() {
        Font prev = painter().getFont();
        Font small = new Font("Arial", Font.PLAIN, 12);
        painter().setFont(small);
        
        int bottom = (int) getCenter().y() + ORIGINAL_SIZE.height / 2 + 15;
        String hs = health + " ", es = energy + " ", ts = turns + "";
        int offset = (int) getCenter().x() + ORIGINAL_SIZE.width / 2 - stringWidth(hs + es + ts);
        
        painter().setColor(selected? Color.GREEN : turns > 0? Color.WHITE : Color.GRAY);
        painter().drawString(hs, offset, bottom);
        offset += stringWidth(hs);
        painter().setColor(selected? Color.CYAN : turns > 0? Color.WHITE : Color.GRAY);
        painter().drawString(es, offset, bottom);
        offset += stringWidth(es);
        painter().setColor(selected? Color.YELLOW : turns > 0? Color.WHITE : Color.GRAY);
        painter().drawString(ts, offset, bottom);
        
        painter().setFont(prev);
    }
    
    protected final Position mouseConstraint(int length) {
        return mouseConstraint(length, 0);
    }
    
    protected final Position mouseConstraint(int length, int snap) {
        if (getCenter().dist(mouse()) < length) {
            if (getCenter().dist(mouse()) % snap < SNAP || getCenter().dist(mouse()) % snap > snap - SNAP
                    && getCenter().dist(mouse()) < length - SNAP) length = snap * (int) Math.round(getCenter().dist(mouse()) / snap);
            else return mouse();
        }
        double mX = getCenter().x() + Math.cos(getCenter().angleTo(mouse())) * length;
        double mY = getCenter().y() + Math.sin(getCenter().angleTo(mouse())) * length;
        return new Position(mX, mY);
    }
    
    protected final void drawRange(int range) {
        int centerX = (int) getCenter().x(), centerY = (int) getCenter().y();
        painter().drawOval(centerX - range, centerY - range, range * 2, range * 2);
    }
    
    protected final void drawPointer(Position pointer) {
        painter().drawLine((int) getCenter().x(), (int) getCenter().y(), (int) pointer.x(), (int) pointer.y());
        pointer.draw(5);
    }
    
    protected final void drawRangePointer(int range) {
        drawRange(range);
        drawPointer(mouseConstraint(range));
    }
    
    protected final void deselect() {
        selected = false;
        mode = 1;
    }
    
    final boolean turnComplete() {
        return turns < 1 && arrived;
    }
    
    final void resetTurn() {
        deselect();
        turns = MAX_TURNS;
    }
    
    final int getMode() {
        return mode;
    }
    
    final int getTurns() {
        return turns;
    }
    
    final void setMode(int mode) {
        this.mode = mode > actions.size()? actions.size() : mode < 1? 1 : mode;
    }
    
    final void decreaseTurns(int amount) {
        turns -= amount;
        if (turns < 1) {
            turns = 0;
            deselect();
        }
    }
    final void shootBullet(Position center, Position target, int team) {
        bullets[team].add(new Bullet(center, target, team == 0? 1 : 0));
    }
    
    final void shootBullet(Position center, Position target, BufferedImage image, int team) {
        bullets[team].add(new Bullet(center, target, image, team == 0? 1 : 0));
    }
}
