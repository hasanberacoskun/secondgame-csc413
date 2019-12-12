package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A Collideable object must be able to handle collisions. Collision checking does not occur within a Collideable object.
 * Collision checking must be handled elseware.
 */
public abstract class Collideable extends WorldObject{

    public Collideable(int x, int y, BufferedImage img) {
        super(x, y, img);
    }
    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * @return true when the collisions have been handled.
     *
     * Implementing classes must have a method to properly handle collisions.
     */
    abstract boolean handleCollisions(Collideable toCompare);

    /*
    This is a useful method for pushing back objects based on the object with which it has collided with.
     */
    public void pushBack(Collideable toPush) {
        double calculatedAngle;
        try {
            calculatedAngle = Math.atan((getY() - toPush.getY()) / (getX() - toPush.getX()));
        } catch (ArithmeticException arithmeticException){
            calculatedAngle = Math.atan((getY() - toPush.getY()) / (getX() + 1 - toPush.getX()));
        }
        int vx = (int) Math.round(Math.cos(calculatedAngle));
        int vy = (int) Math.round(Math.sin(calculatedAngle));
        if (getX() < toPush.getX()) {
            setX(getX() - vx);
        } else {
            setX(getX() + vx);
        }
        if (getY() < toPush.getY()) {
            System.out.println("vy is: " + vy);
            setY(getY() - vy);
        } else {
            setY(getY() + vy);
        }
    }

}
