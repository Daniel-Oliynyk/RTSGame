package rtsgame;

import gametools.*;
import static gametools.Game.painter;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends Sprite {
    final int MOVE_RANGE, ORIGINAL_SIZE, ACTION_AMOUNT, MAX_TURNS;
    final List<String> GLOBAL_ACTIONS = Arrays.asList("Cancel", "Next", "End Turn"),
            ACTION_KEYS = Arrays.asList("Esc", "Tab", "Enter", "1", "2", "3", "4", "5", "6");
    private int mode = 1, turns;
    boolean selected, arrived = true;
    private String name = "Ship";
    private List<String> actions;
    Position moveLocation;
    
    public Ship(double x, double y, int range, int turns, int actionAmount, BufferedImage image) {
        super(image);
        centerOn(x, y);
        moveLocation = getCenter();
        MOVE_RANGE = range;
        ORIGINAL_SIZE = image.getWidth();
        ACTION_AMOUNT = actionAmount < 2? 2 : actionAmount > 6? 6 : actionAmount;
        MAX_TURNS = turns < 1? 1 : turns;
        resetTurn();
    }
    
    @Override
    protected final void update() {
        if (selected) {
            if (Game.keyEngaged(KeyEvent.VK_ESCAPE) || Game.mouseEngaged(MouseEvent.BUTTON3)) deselect();
            if (Game.keyEngaged(KeyEvent.VK_1)) mode = 1;
            if (Game.keyEngaged(KeyEvent.VK_2)) mode = 2;
            if (Game.keyEngaged(KeyEvent.VK_3) && ACTION_AMOUNT > 2) mode = 3;
            if (Game.keyEngaged(KeyEvent.VK_4) && ACTION_AMOUNT > 3) mode = 4;
            if (Game.keyEngaged(KeyEvent.VK_5) && ACTION_AMOUNT > 4) mode = 5;
            if (Game.keyEngaged(KeyEvent.VK_6) && ACTION_AMOUNT > 5) mode = 6;
            if (arrived) selected();
        }
        if (!arrived) moveTo(moveLocation);
        if (moveLocation.dist(getCenter()) < 2) arrived = true;
        if (click() && Game.mouseWithin(this) && arrived && turns > 0) {
            boolean otherShipSelected = false;
            for (Sprite ship : RTSGame.ships.getAll()) if (((Ship) ship).selected) otherShipSelected = true;
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
        drawPointer(mouseConstraint(MOVE_RANGE * turns));
        if (click()) {
            moveLocation = mouseConstraint(MOVE_RANGE * turns);
            arrived = false;
            decreaseTurns((int) Math.ceil(getCenter().dist(moveLocation) / MOVE_RANGE));
            mode = 2;
        }
    }
    
    protected void actionTwo() {
        face(mouse());
        painter().setColor(Color.RED);
        drawRangePointer(700);
        if (click()) {
            RTSGame.bullets.add(new Bullet(getCenter(), mouseConstraint(600)));
            decreaseTurns(1);
        }
    }
    
    protected void actionThree() {}
    
    protected void actionFour() {}
    
    protected void actionFive() {}
    
    protected void actionSix() {}
    
    protected final void shipInformation(String... info) {
        name = info[0];
        this.actions = new ArrayList<>();
        this.actions.addAll(GLOBAL_ACTIONS);
        this.actions.addAll(Arrays.asList(info));
        this.actions.remove(name);
    }
    
    protected final int stringWidth(String string) {
        FontMetrics font = painter().getFontMetrics();
        Rectangle2D rect = font.getStringBounds(string, painter());
        return (int) rect.getWidth();
    }
    
    protected final boolean click() {
        return Game.mouseEngaged(MouseEvent.BUTTON1) || Game.keyEngaged(KeyEvent.VK_SPACE);
    }
    
    protected final Position mouse() {
        return Game.mousePosition();
    }
    
    protected final Position mouseConstraint(double length) {
        if (getCenter().dist(mouse()) < length) return mouse();
        double mX = getCenter().x() + Math.cos(getCenter().angleTo(mouse())) * length;
        double mY = getCenter().y() + Math.sin(getCenter().angleTo(mouse())) * length;
        return new Position(mX, mY);
    }
    
    final void drawInformation() {
        Position prev = Game.getPainterCenter();
        Game.centerPainterOn(Game.getCenter());
        painter().setColor(Color.YELLOW);
        painter().drawString(name, (Game.getWidth() - stringWidth(name)) / 2, Game.getHeight() - 80);
        painter().setColor(Color.WHITE);
        int totalWidth = 0;
        for (String action : actions) totalWidth += stringWidth(action);
        for (int i = 0; i < actions.size(); i++) totalWidth += stringWidth("[" + ACTION_KEYS.get(i) + "] ");
        for (int i = 0; i < actions.size() - 1; i++) totalWidth += stringWidth("   ");
        int offset = (Game.getWidth() - totalWidth) / 2;
        for (int i = 0; i < actions.size(); i++) {
            painter().setColor(Color.GRAY);
            painter().drawString("[" + ACTION_KEYS.get(i) + "] ", offset, Game.getHeight() - 40);
            offset += stringWidth("[" + ACTION_KEYS.get(i) + "] ");
            painter().setColor(mode + GLOBAL_ACTIONS.size() - 1 == i? Color.YELLOW : Color.WHITE);
            painter().drawString(actions.get(i), offset, Game.getHeight() - 40);
            offset += stringWidth(actions.get(i) + "   ");
        }
        Game.centerPainterOn(prev);
    }
    
    final void drawTurnCount() {
        painter().setColor(turns > 0? Color.YELLOW : Color.GRAY);
        painter().drawString(turns + "", (int) (getCenter().x() + ORIGINAL_SIZE / 2), (int) (getCenter().y() + ORIGINAL_SIZE / 2));
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
        turns = MAX_TURNS;
        mode = 1;
    }
    
    final int getMode() {
        return mode;
    }
    
    final int getTurns() {
        return turns;
    }
    
    final void setMode(int mode) {
        this.mode = mode > ACTION_AMOUNT? ACTION_AMOUNT : mode < 2? 2 : mode;
    }
    
    final void decreaseTurns(int amount) {
        turns -= amount;
        if (turns < 1) {
            turns = 0;
            deselect();
        }
    }
}
