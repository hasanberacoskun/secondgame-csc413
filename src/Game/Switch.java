package Game;

import java.awt.image.BufferedImage;

public class Switch extends MapBlock implements Destroyable {

    boolean toBeDestroyed;
    Lock lock;
    int member;

    Switch(int x, int y, BufferedImage img, int member) {
        super(x, y, img);
        this.toBeDestroyed = false;
        this.member = member;
    }

    Switch(int x, int y, BufferedImage img, Lock lock, int member) {
        super(x, y, img);
        this.lock = lock;
        this.toBeDestroyed = false;
        this.member = member;
    }

    public int getMember() {
        return member;
    }

        public void setLock(Lock lock) {
        this.lock = lock;
    }

    @Override
    public boolean getToBeDestroyed() {
        return toBeDestroyed;
    }

    @Override
    void update() {

    }

    @Override
    boolean handleCollisions(Collideable toCompare) {
        if (toCompare instanceof Player) {
            System.out.println(this.lock);
            this.lock.setUnlocked();
            System.out.println("Lock: " + this.lock + " has been set to unlocked.");
            toBeDestroyed = true;
        }
        return true;
    }
}
