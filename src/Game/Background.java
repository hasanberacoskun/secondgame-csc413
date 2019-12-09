package Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Background extends WorldObject{
    private BufferedImage img;

    Background(BufferedImage img) {
        super(0, 0, img);
        this.img = img;
    }

    @Override
    void drawImage(Graphics g) {
        for (int x = 0; x <= Main.SCREEN_WIDTH; x += this.img.getWidth()){
            for (int y = 0; y <= Main.SCREEN_HEIGHT; y += this.img.getHeight()){
                AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
                rotation.rotate(Math.toRadians(90), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(this.img, rotation, null);
            }
        }

    }

}
