package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A boulder can be pushed around by all objects. If it collides with a hostile object, it will destroy it.
 */
public class Boulder extends MapBlock {

    int vx;
    int vy;
    Boulder(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    public void update() {
        // Nothing needs to be updated.
    }

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     *
     * Collisions are handled in a way similar to pushBack() with some slight modifications.
     */
    public boolean handleCollisions(Collideable toCompare) {
        double calculatedAngle;
        try {
            calculatedAngle = Math.atan((getY() - toCompare.getY()) / (getX() - toCompare.getX()));
        } catch (ArithmeticException arithmeticException){
            calculatedAngle = Math.atan((getY() - toCompare.getY()) / (getX() + 1 - toCompare.getX()));
        }
        vx = (int) Math.round(Math.cos(calculatedAngle));
        vy = (int) Math.round(Math.sin(calculatedAngle));
        if (getX() < toCompare.getX()) {
            setX(getX() - vx);
        } else {
            setX(getX() + vx);
        }
        if (getY() < toCompare.getY()) {
            setY(getY() - vx);
        } else {
            setY(getY() + vx);
        }
        checkBorder();
        return true;
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
}
