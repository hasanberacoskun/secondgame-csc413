package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * Based on whether a blade is horizontal or vertical, it will move back and forth unless it collides with another object.
 * A blade can be destroyed by a Boulder.
 */
public class Blade extends MapBlock implements Destroyable{

    // false means horizontal motion ('V')
    // true means vertical motion ('H')
    private boolean axis;
    // false means negative direction
    // true means positive direction
    private boolean direction;
    private boolean toBeDestroyed = false;
    private int R = 2;
    private int vx;
    private int vy;

    Blade (int x, int y, BufferedImage img, boolean axis) {
       super(x, y, img);
       this.axis = axis;
       this.direction = true;
       if (axis == true) {
           setAngle(90);
       }
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(getAngle())));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(getAngle())));
        setX(getX() + vx);
        setY(getY() + vy);
        checkBorder();
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(getAngle())));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(getAngle())));
        setX(getX() - vx);
        setY(getY() - vy);
        checkBorder();
    }

    private void checkBorder() {
        if (getX() < 20) {
            setX(40);
            changeDirection();
        }
        if (getX() >= Main.SCREEN_WIDTH - 80) {
            setX(Main.SCREEN_WIDTH - 100);
            changeDirection();
        }
        if (getY() < 20) {
            setY(40);
            changeDirection();
        }
        if (getY() >= Main.SCREEN_HEIGHT - 80) {
            setY(Main.SCREEN_HEIGHT - 100);
            changeDirection();
        }
    }

    // When collisions are handled, direction must be changed using this method.
    public void changeDirection() {
        System.out.println("changing direction");
        direction = !direction;
        System.out.println(direction);
    }

    /**
     * The direction of movement depends on the direction variable, set during collision handling.
     */
    void update() {
        checkBorder();
        if (direction) {
            moveForwards();
        } else {
            moveBackwards();
        }
    }

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * A collision will incite a direction change. If collision is with a Boulder, the blade is set to be destroyed.
     */
    public boolean handleCollisions(Collideable toCompare) {
        changeDirection();
        if(direction) {
            moveForwards();
            moveForwards();
        } else {
            moveBackwards();
            moveBackwards();
        }
        if(toCompare instanceof Boulder){
            toBeDestroyed = true;
        }
        return true;
    }

    public boolean getToBeDestroyed() {
        return toBeDestroyed;
    }
}
