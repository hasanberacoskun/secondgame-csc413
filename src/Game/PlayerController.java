package Game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class PlayerController implements KeyListener {
    private Player p;
    private ArrayList<Player> players;
    private int up;
    private int down;
    private int right;
    private int left;
    private int playerCount;

    public PlayerController(Player p, int up, int down, int left, int right) {
        this.players.add(p);
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
    }
    public PlayerController(ArrayList<Player> players, int up, int down, int left, int right) {
        this.players = players;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.playerCount = players.size();
    }

    public void decreasePlayerCount() {
        playerCount--;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        for (Player player: players) {
            if (keyPressed == up) {
                player.toggleUpPressed();
            }
            if (keyPressed == down) {
                player.toggleDownPressed();
            }
            if (keyPressed == left) {
                player.toggleLeftPressed();
            }
            if (keyPressed == right) {
                player.toggleRightPressed();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        for (Player player: players) {
            if (keyReleased  == up) {
                player.unToggleUpPressed();
            }
            if (keyReleased == down) {
                player.unToggleDownPressed();
            }
            if (keyReleased  == left) {
                player.unToggleLeftPressed();
            }
            if (keyReleased  == right) {
                player.unToggleRightPressed();
            }
        }

    }
}
