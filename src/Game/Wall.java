package Game;

import java.awt.image.BufferedImage;

public class Wall extends MapBlock {

    public Wall(int x, int y, BufferedImage img) {
        super(x, y, img);
    }

    void update() {
    }

    public boolean handleCollisions(Collideable toCompare) {
        return true;
    }

}
