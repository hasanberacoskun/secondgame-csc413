package Game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Exit extends MapBlock {

    // If true, multiple players are accepted.
    int playerAcceptanceCapacity;
    int playersAccepted;
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

    void update() {
        // If only one more koala can be accepted, the door will be set to look blue;
        if (playersAccepted + 1 == playerAcceptanceCapacity) {
            setImg(imgBlue);
        } else if (playersAccepted >= playerAcceptanceCapacity) {
            setImg(imgClosed);
        }
    }

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
