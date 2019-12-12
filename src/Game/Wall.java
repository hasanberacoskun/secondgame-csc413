package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A wall does nothing except be Collideable and get drawn.
 * I have decided to retain an abstract class for MapBlock for scaleability. Otherwise, other MapBlocks would be ok just
 * extending Wall if MapBlock functionality was transfered to Wall.
 */
public class Wall extends MapBlock {

    public Wall(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    void update() {
        // No updates are necessary after generation.
    }

    public boolean handleCollisions(Collideable toCompare) {
        return true;
    }

}
