package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A Switch is always accompanied by a Lock. A lock is opened when a player collides with the corresponding switch.
 * When opened, a lock allows players to pass through. The switch is destroyed after opening a lock.
 */

public class Switch extends MapBlock implements Destroyable {

    boolean toBeDestroyed;
    Lock lock;
    /*
    Stores an int that two consecutive characters share. This is used to match Locks and Switches. Ex: 'a' and 'b'
    will have the same member value. 'a' will always be a lock since it corresponds to an odd ASCII value. 'b' will
    always be a switch since it corresponds to an even ASCII value.
    */
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
        // No updated needed since all changes accounted for during collision checks and drawing.
    }

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * @return true when the collisions have been handled.
     *
     * Switch owns a lock. The lock will be set as unlocked before the switch sets itself to be destroyed.
     */
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
