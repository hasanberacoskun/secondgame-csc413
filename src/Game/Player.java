package Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A Player is controlled by a playerController. More details on collisions are provided in further comments. Player
 * placement is handled by the map[0..].txt files.
 */
public class Player extends MapBlock implements Destroyable{

    private int vx;
    private int vy;
    private int angle;
    private int defaultX;
    private int defaultY;
    private int formerx;
    private int formery;

    private boolean toBeDestroyed = false;
    private boolean exited;
    private int health;

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
        this.formerx = x;
        this.formery = y;
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

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * @return true when completed.
     *
     * If collision with the following:
     * -Wall -> Push back
     * -Boulder -> (the boulder handles this)
     * -Player -> Push back
     * -TNT -> Set toBeDestroyed
     * -Lock -> Push back if locked.
     * -Switch -> (the switch handles this)
     *
     * Other collisions are handled by those objects. Only collisions that directly effect player are handled by player.
     */
    @Override
    public boolean handleCollisions(Collideable toCompare){
        // modify this later to be Wall instead of MapBlock
        if ((toCompare instanceof Wall)){
            this.setX(formerx);
            this.setY(formery);
            //pushBack(toCompare);
            checkBorder();
        } else if (toCompare instanceof Player){
            this.setX(formerx);
            this.setY(formery);
            //pushBack(toCompare);
            checkBorder();
        } else if ((toCompare instanceof TNT) || (toCompare instanceof Blade)) {
            System.out.println("Player collided with blade.");
            toBeDestroyed = true;
        } else if (toCompare instanceof Lock) {
            System.out.println("Locked status: " + ((Lock) toCompare).getLockedStatus());
            if (((Lock) toCompare).getLockedStatus()) {
                System.out.println("You shall not pass!");
                //pushBack(toCompare);
                this.setX(formerx);
                this.setY(formery);
                checkBorder();
            } else {
                System.out.println("You may pass.");
                // Allow player to pass lock.
            }
        }
        return true;
        // Add more.
    }

    /**
     * The player's position is updated.
     */
    public void update() {
        formerx = this.getX();
        formery = this.getY();
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

    /**
     * Image is drawn uniquely for Players since there is rotation.
     */
    @Override
    void drawImage(Graphics g) {
        //System.out.println("graphics: "+ g);
        AffineTransform rotation = AffineTransform.getTranslateInstance(getX(), getY());
        rotation.rotate(Math.toRadians(angle), getImg().getWidth() / 2.0, getImg().getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(getImg(), rotation, null);
    }

    // Setter and getter methods:
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
