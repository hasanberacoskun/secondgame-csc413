package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * TNT does not move and destroys a player and itself on contact.
 * A TNT can be destroyed by a Boulder.
 */
public class TNT extends MapBlock implements Destroyable{

    boolean toBeDestroyed = false;

    TNT (int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    void update() {
    }

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * If collision is with a Player or Boulder, the TNT will be set to be destroyed. A player handles its own destruction
     *                  on TNT contact.
     */
    boolean handleCollisions(Collideable toCompare) {
        // Redundant but for readability.
        if (toCompare instanceof Boulder) {
            toBeDestroyed = true;
        } else if (toCompare instanceof Player) {
            toBeDestroyed = true;
        }
        return true;
    }

    public boolean getToBeDestroyed() {
        return toBeDestroyed;
    }
}
