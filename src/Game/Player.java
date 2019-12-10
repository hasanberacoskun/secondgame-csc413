package Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends MapBlock implements Destroyable{

    private int vx;
    private int vy;
    private int angle;
    private boolean toBeDestroyed = false;
    private int health;
    private boolean exited;

    private int defaultX;
    private int defaultY;

    private final int R = 2;
    private final int ROTATIONSPEED = 3;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;

    private static final int DEFAULT_TIMER_VALUE = 30;


    Player(int x, int y, int vx, int vy, int angle, BufferedImage playerImg) {
        super (x, y, playerImg);
        this.defaultX = x;
        this.defaultY = y;
        this.angle = angle;
        this.vx = vx;
        this.vy = vy;
        this.health = 1;
        this.exited = false;
    }


    void toggleUpPressed() {
        this.UpPressed = true;
    }
    void toggleDownPressed() {
        this.DownPressed = true;
    }
    void toggleRightPressed() {
        this.RightPressed = true;
    }
    void toggleLeftPressed() {
        this.LeftPressed = true;
    }
    void unToggleUpPressed() {
        this.UpPressed = false;
    }
    void unToggleDownPressed() {
        this.DownPressed = false;
    }
    void unToggleRightPressed() {
        this.RightPressed = false;
    }
    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    @Override
    public boolean handleCollisions(Collideable toCompare){
        // modify this later to be Wall instead of MapBlock
        if ((toCompare instanceof Wall || toCompare instanceof Boulder)){
            pushBack(toCompare);
            checkBorder();
        } else if (toCompare instanceof Player){
            pushBack(toCompare);
            checkBorder();
        } else if ((toCompare instanceof TNT) || (toCompare instanceof Blade)) {
            System.out.println("Player collided with blade.");
            toBeDestroyed = true;
        } else if (toCompare instanceof Lock) {
            System.out.println("Locked status: " + ((Lock) toCompare).getLockedStatus());
            if (((Lock) toCompare).getLockedStatus()) {
                System.out.println("You shall not pass!");
                pushBack(toCompare);
                checkBorder();
            } else {
                System.out.println("You may pass.");
                // Allow player to pass lock.
            }
        }
        return true;
        // Add more.
    }

    public void update() {
        if (health <= 0) {
            health = 5;
            super.setX(defaultX);
            super.setY(defaultY);
        }

        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }
        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
    }

    private void rotateLeft() {
        angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        setX(getX() - vx);
        setY(getY() - vy);
        checkBorder();
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        setX(getX() + vx);
        setY(getY() + vy);
        checkBorder();
    }

    private void checkBorder() {
        if (getX() < 20) {
            setX(20);
        }
        if (getX() >= Main.SCREEN_WIDTH - 80) {
            setX(Main.SCREEN_WIDTH - 80);
        }
        if (getY() < 20) {
            setY(20);
        }
        if (getY() >= Main.SCREEN_HEIGHT - 80) {
            setY(Main.SCREEN_HEIGHT - 80);
        }
    }

    @Override
    public String toString() {
        return "x=" + getX() + ", y=" + getY() + ", angle=" + angle;
    }


    void drawImage(Graphics g) {
        //System.out.println("graphics: "+ g);
        AffineTransform rotation = AffineTransform.getTranslateInstance(getX(), getY());
        rotation.rotate(Math.toRadians(angle), getImg().getWidth() / 2.0, getImg().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }


    public void setHealth(int health) {
        this.health = health;
    }
    public int getHealth() {
        return health;
    }

    public boolean getToBeDestroyed() {
        return toBeDestroyed;
    }
    public void setToBeDestroyed() {
        toBeDestroyed = true;
    }

    public boolean getExited() {
        return exited;
    }
    public void setExited() {
        exited = true;
    }
}
