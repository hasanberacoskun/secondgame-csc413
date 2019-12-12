package Game;

import java.awt.image.BufferedImage;

/**
 * @author bera h. coskun
 *
 * A Lock is always accompanied by a switch. A lock is opened when a player collides with the corresponding switch.
 * When opened, a lock allows players to pass through.
 */
public class Lock extends MapBlock {

    /*
    If locked, player cannot move past lock.
     */
    boolean locked;
    BufferedImage unlockedimg;
    /*
    Stores an int that two consecutive characters share. This is used to match Locks and Switches. Ex: 'a' and 'b'
    will have the same member value. 'a' will always be a lock since it corresponds to an odd ASCII value. 'b' will
    always be a switch since it corresponds to an even ASCII value.
    */
    int member;

    Lock(int x, int y, BufferedImage lockedimg, BufferedImage unlockedimg, int member) {
        super(x, y, lockedimg);
        this.unlockedimg = unlockedimg;
        locked = true;
        this.member = member;
    }

    @Override
    void update() {
        // No updated needed since all changes accounted for during collision checks and drawing.
    }

    // Getter and setter methods:
    public int getMember() {
        return member;
    }
    public void setUnlocked() {
        System.out.println("Setting to unlocked.");
        System.out.println(this);
        locked = false;
        super.setImg(unlockedimg);
    }
    public boolean getLockedStatus() {
        return locked;
    }

    @Override
    boolean handleCollisions(Collideable toCompare) {
        if (toCompare instanceof Player) {
            // The player's handleCollisions method will handle what happens if the Lock is locked or not.
        }
        return true;
    }
}
