package Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * @author bera h. coskun
 *
 * An exit has 3 statuses. Red, Blue, and Closed. An unlimited number of exits can be created.
 */
public class Exit extends MapBlock {

    // If true, multiple players are accepted.
    int playerAcceptanceCapacity;
    int playersAccepted;
    /*
    Red: Exit can accept more than 1 player.
    Blue: Exit can accept only 1 player.
    Closed: Exit cannot accept any players.
     */
    BufferedImage imgRed;
    BufferedImage imgBlue;
    BufferedImage imgClosed;
    ArrayList<Player> acceptedPlayers;

    Exit(int x, int y, BufferedImage imgRed, BufferedImage imgBlue, BufferedImage imgClosed, int playerAcceptanceCapacity) {
        super(x, y, imgRed);
        this.playerAcceptanceCapacity = playerAcceptanceCapacity;
        playersAccepted = 0;
        this.imgRed = imgRed;
        this.imgBlue = imgBlue;
        this.imgClosed = imgClosed;
        acceptedPlayers = new ArrayList<>();
    }

    /**
     * The exit will become blue or closed based on the number of players it has accepted.
     */
    void update() {
        // If only one more koala can be accepted, the door will be set to look blue;
        if (playersAccepted + 1 == playerAcceptanceCapacity) {
            setImg(imgBlue);
        } else if (playersAccepted >= playerAcceptanceCapacity) {
            setImg(imgClosed);
        }
    }

    /**
     *
     * @param toCompare is the object that has been collided with. The information about this object can be useful for
     *                  handling collisions.
     * @return true when complete.
     *
     * If a player has collided with the door, and the door is not closed, the player is set as exited and the exit's
     * condition is updated accordingly. Exit does not handle what happens when a player is set to be exited. This is
     * handled elsewhere.
     */
    boolean handleCollisions(Collideable toCompare) {
        if (toCompare instanceof Player) {
            if ((playersAccepted < playerAcceptanceCapacity) && !acceptedPlayers.contains(toCompare)){
                acceptedPlayers.add((Player)toCompare);
                ((Player) toCompare).setExited();
                // Main will handle when a koala has been set as 'exited'.
                playersAccepted++;
            }
        }
        return true;
    }
}
