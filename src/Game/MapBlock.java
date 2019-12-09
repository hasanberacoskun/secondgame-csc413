package Game;

import java.awt.image.BufferedImage;

public abstract class MapBlock extends Collideable{
    private BufferedImage img;

    MapBlock(int x, int y, BufferedImage img) {
        super(x, y, img);
        this.img = img;
    }

    @Override
    public String toString(){
        return "x=" + getX() + ", y=" + getY() + ", image=" + getImg();
    }

    abstract void update();
}
