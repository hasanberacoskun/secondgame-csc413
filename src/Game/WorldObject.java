package Game;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A World Object is any object that is seen by the user and needs to be drawable.
 * It requires an x position, y position, and BufferedImage to be drawn.
 *
 */
public abstract class WorldObject {

    private int x;
    private int y;

    // Default angle is provided.
    private int angle = 360;

    private BufferedImage img;

    // Default no-arg constructor.
    WorldObject(){
        this.x = Main.SCREEN_WIDTH / 2;
        this.y = Main.SCREEN_HEIGHT / 2;
        this.angle = 0;

    }

    WorldObject(int x, int y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
    }

    // Draws the image.
    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    // Getter and setter methods:
    public BufferedImage getImg(){
        return img;
    }
    public void setImg(BufferedImage img) {
        this.img = img;
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getAngle() {
        return angle;
    }
    public void setAngle(int angle){
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle + ", bufferedimage=" + img.toString();
    }
}
