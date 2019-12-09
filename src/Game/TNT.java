package Game;

import java.awt.image.BufferedImage;

public class TNT extends MapBlock implements Destroyable{

    boolean toBeDestroyed = false;

    TNT (int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    void update() {
    }

    boolean handleCollisions(Collideable toCompare) {
        if (toCompare instanceof Player) {
            toBeDestroyed = true;
        }
        return true;
    }

    public boolean getToBeDestroyed() {
        return toBeDestroyed;
    }
}
