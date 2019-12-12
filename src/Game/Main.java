/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Iterator;

import static javax.imageio.ImageIO.read;

/**
 * @author bera h. coskun
 *
 * The main class handles much of the world generation (creation of objects) and also checks for collisions.
 * It also handles game win and loss conditions. WorldObjects draw themselves.
 *
 */
public class Main extends JPanel  {

    /*
    Screen Height and width are used as world height and width in reality. However, in our case, screen width and height
    is always the same as world width and height. Variable name should be adjusted accordingly if changes are made to this
    fact.
     */
    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    private BufferedImage world;
    private BufferedReader br;
    private Graphics2D buffer;
    private JFrame jf;

    /*
    A Background object generates itself from tiles. It is not considered to be a Map Block.
     */
    private Background background;
    /*
    blocks stores all MapBlocks except for players.
     */
    private ArrayList<MapBlock> blocks = new ArrayList<>();
    /*
    players are stored separately from other blocks.
     */
    private ArrayList<Player> players = new ArrayList<>();
    /*
    Players who have exited are kept track of.
     */
    private ArrayList<Player> exitedPlayers = new ArrayList<>();
    /*
    A single player controller is needed for this game in particular.
     */
    private PlayerController pc;
    /*
    The level count is iterated as we move from level to level. Level count is used when finding the text file for the
    next level. The level should be named as map[consecutive number starting at 0].txt
     */
    private int levelCount = 0;
    private boolean gameEnd = false;


    public static void main(String[] args) {
        Thread x;
        Main gameInstance = new Main();
        /*
        Initialization will only occur if the game has not ended.
         */
        if (!gameInstance.gameEnd){
            gameInstance.init();
        }
        try {
            while (!(gameInstance.gameEnd) && !gameInstance.gameLost()) {
                /*
                If the level is won, we reset and reinitialize. Since levelCount has been iterated, a new level is loaded
                during initialization.
                 */
                if (gameInstance.levelWon()) {
                    gameInstance.levelCount++;
                    gameInstance.reset();
                    gameInstance.init();
                }
                /*
                The following happens under normal conditions. Collisions are tested before any updates or repainting.
                 */
                gameInstance.testCollisions();
                gameInstance.updatePlayers();
                gameInstance.updateBlocks();
                gameInstance.repaint();
                Thread.sleep(1000 / 144);
            }
            /*
            World will be drawn one last time before we end the game when the player has lost.
             */
            if (gameInstance.gameLost()) {
                gameInstance.repaint();
            }
        } catch (InterruptedException ignored) {

        }


    }

    /**
     *
     * @return true if the game has been lost.
     */
    private boolean gameLost() {
        if (!(players.size() + exitedPlayers.size() >= 3)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return true if the level has been won.
     * reset() will ensure exitedPlayers is cleared.
     */
    private boolean levelWon() {
        if (exitedPlayers.size() == 3) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Clears all relevant variables within main. Width and height are reset to 0 so that they may be recalculated.
     */
    private void reset() {
        blocks.clear();
        players.clear();
        exitedPlayers.clear();
        SCREEN_WIDTH = 0;
        SCREEN_HEIGHT = 0;
    }

    /**
     * Initializes the world.
     * This method does the following:
     * - Creates the JFrame.
     * - Loads images and map file.
     * - Creates background.
     * - Loads MapBlocks (done via a separate method).
     * - Creates a keyListener.
     * - Creates a world.
     */
    private void init() {
        System.out.println("Initializing Level");
        /*
        A new JFrame is only created for the first level. This JFrame is modified for all other levels and end screen.
         */
        if (levelCount == 0) {
            this.jf = new JFrame("Koalabr8");
        }
        this.jf.setFocusable(true);
        this.jf.requestFocus();
        BufferedImage backgroundimg = null, k1img = null, k2img = null, k3img = null, wallimg = null, bladeimg = null,
                boulderimg = null, tntimg = null, locklockedimg = null, lockunlockedimg = null, switchimg = null
                , exitredimg = null, exitblueimg = null, exitclosedimg = null;
        try {
            BufferedImage tmp;
            System.out.println(System.getProperty("user.dir"));
            /*
             * Note how class loaders read files from the out folder (build folder in netbeans) and not the
             * current working directory.
             */
            k1img = read(new File("resources/koala1.png"));
            k2img = read(new File("resources/koala2.png"));
            k3img = read(new File("resources/koala3.png"));
            wallimg = read(new File("resources/wallTile.png"));
            bladeimg = read(new File("resources/saw.png"));
            boulderimg = read(new File("resources/boulder.png"));
            tntimg = read(new File("resources/tnt.png"));
            locklockedimg = read(new File("resources/lockLocked.png"));
            lockunlockedimg = read(new File("resources/lockUnlocked.png"));
            switchimg = read(new File("resources/switch.png"));
            exitredimg = read(new File("resources/exitRed.png"));
            exitblueimg = read(new File("resources/exitBlue.png"));
            exitclosedimg = read(new File("resources/exitClosed.png"));
            backgroundimg = read(new File("resources/backgroundTile.png"));
            // Read the map text file and create a buffered reader to easily read the text.
            try {
                File mapData = new File("resources/map" + levelCount + ".txt");
                br = new BufferedReader(new FileReader(mapData));
                background = new Background(backgroundimg);
                try {
                    generateBlocks(k1img, k2img, k3img, wallimg, bladeimg, boulderimg, tntimg, locklockedimg, lockunlockedimg,
                            switchimg, exitredimg, exitblueimg, exitclosedimg);
                    System.out.println("Blocks loaded");
                } catch (IOException ex) {
                    System.out.println("Blocks not loaded: " + ex);
                }
                System.out.println("Screen height will be: " + SCREEN_HEIGHT);
                System.out.println("Screen width will be: " + SCREEN_WIDTH);
                this.world = new BufferedImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
                pc = new PlayerController(players, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
                this.jf.setLayout(new BorderLayout());
                this.jf.add(this);
                this.jf.addKeyListener(pc);
                this.jf.setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT + 30);
                this.jf.setResizable(false);
                jf.setLocationRelativeTo(null);
                this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.jf.setVisible(true);
            } catch (IOException ex) {
                /*
                 * If we have entered here, the next map file has not been found, which means there are no more levels
                 * to be played. The game has been won.
                 */
                this.world = new BufferedImage(450, 200, BufferedImage.TYPE_INT_RGB);
                this.jf.setLayout(new BorderLayout());
                this.jf.add(this);
                this.jf.setSize(450, 200);
                this.jf.setResizable(false);
                jf.setLocationRelativeTo(null);
                this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.jf.setVisible(true);
                gameEnd = true;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * We take in the following images so that we may create appropriate tiles:
     * @param k1img
     * @param k2img
     * @param k3img
     * @param wallimg
     * @param bladeimg
     * @param boulderimg
     * @param tntimg
     * @param locklockedimg
     * @param lockunlockedimg
     * @param switchimg
     * @param exitredimg
     * @param exitblueimg
     * @param exitclosedimg
     * @throws IOException If something goes wrong, map will not be loaded.
     */
    private void generateBlocks(BufferedImage k1img, BufferedImage k2img, BufferedImage k3img, BufferedImage wallimg,
                                BufferedImage bladeimg, BufferedImage boulderimg, BufferedImage tntimg, BufferedImage locklockedimg,
                                BufferedImage lockunlockedimg, BufferedImage switchimg, BufferedImage exitredimg,
                                BufferedImage exitblueimg, BufferedImage exitclosedimg) throws IOException {
        /*
        Array lists of locks and switches are created to keep track of those who have yet to find a pair when reading
        the map file.
         */
        ArrayList<Lock> locks = new ArrayList<>();
        ArrayList<Switch> switches = new ArrayList<>();
        String line;
        // Height count represents y.
        int heightCount = 0;
        try {
            // While we haven't exceeded our map's block width (each block is 50px) and our file has another line,
            // we continue to read.
            while (((line = br.readLine()) != null) /*&& heightCount <= ((SCREEN_HEIGHT / 50)) - 1*/) {
                SCREEN_HEIGHT++;
                if (SCREEN_WIDTH <= line.length()) {
                    SCREEN_WIDTH = line.length();
                }
                System.out.println(line);
                // Width count should only have the scope of line since it represents x.
                int widthCount = 0;
                heightCount++;
                // The line is split into an array of chars so that we can easily iterate through.
                char[] lineChars = line.toCharArray();
                for (char lineChar: lineChars) {
                    if(true/*widthCount <= (SCREEN_WIDTH / 50 - 1)*/) {
                        widthCount++;
                        //System.out.println(widthCount);
                        int blockXPostion = (widthCount - 1) * 50;
                        int blockYPosition = (heightCount - 1) * 50;
                        if (lineChar == 'W') {
                            blocks.add(new Wall(blockXPostion, blockYPosition, wallimg));
                        } else if(lineChar == 'H'){
                            blocks.add(new Blade(blockXPostion, blockYPosition, bladeimg, false));
                        } else if(lineChar == 'V') {
                            blocks.add(new Blade(blockXPostion, blockYPosition, bladeimg, true));
                        } else if(lineChar == 'B') {
                            blocks.add(new Boulder(blockXPostion, blockYPosition, boulderimg));
                        } else if(lineChar == 'T') {
                            blocks.add(new TNT(blockXPostion, blockYPosition, tntimg));
                        } else if (lineChar == '#') {
                            blocks.add(new Exit(blockXPostion, blockYPosition, exitredimg, exitblueimg, exitclosedimg, 3));
                        } else if (lineChar == '$') {
                            blocks.add(new Exit(blockXPostion, blockYPosition, exitredimg, exitblueimg, exitclosedimg, 1));
                        // Switches and Locks are signified using lowercase characters.
                        } else if (lineChar >= 97 && lineChar <= 122) {
                            // Odd ASCII values correspond to locks. Consecutive character represents the corresponding switch.
                            if (lineChar % 2 == 1) {
                                boolean lockAdded = false;
                                // Search through our cached switches to see if any correspond to our lock. If yes, both are added to the game.
                                // The lock is added to the switch. The switch must know about the corresponding lock.
                                for (Switch aswitch: switches) {
                                    if (aswitch.getMember() == (int)lineChar) {
                                        // A new lock is created.
                                        Lock lock = new Lock(blockXPostion, blockYPosition, locklockedimg, lockunlockedimg,(int)lineChar);
                                        // The lock is added to the world.
                                        blocks.add(lock);
                                        // Our switch gets it's corresponding lock set.
                                        aswitch.setLock(lock);
                                        // Our switch (with the correct lock set) is added to our world.
                                        blocks.add(aswitch);
                                        lockAdded = true;
                                    }
                                }
                                // Add a fresh lock to the cache (array list of locks). This lock does not yet have a corresponding switch.
                                // If no switch is found, it will not be added to the game (blocks array list).
                                if(!lockAdded) {
                                    locks.add(new Lock(blockXPostion, blockYPosition, locklockedimg, lockunlockedimg, (int)lineChar));
                                }
                            } else if (lineChar % 2 == 0) {
                                boolean switchAdded = false;
                                // Search through our cached locks to see if any correspond to our switch. If yes, both are added to the game.
                                int locksSize = locks.size();
                                for (Lock lock: locks) {
                                    // Note that we subtract 1 from the char, so that consecutive chars have the same member value.
                                    if (lock.getMember() == (int)lineChar - 1) {
                                        blocks.add(lock);
                                        blocks.add(new Switch(blockXPostion, blockYPosition, switchimg, lock, (int)lineChar - 1));
                                        switchAdded = true;
                                    }
                                }
                                if (!switchAdded) {
                                    // Add a fresh switch to the cache (array list of switches). This switch does not yet have a corresponding lock.
                                    // If no lock is found, it will not be added to the game (blocks array list).
                                    switches.add(new Switch(blockXPostion, blockYPosition, switchimg, (int)lineChar - 1));
                                }
                            }
                        /*
                        Players are signified using numbers. Currently, this is hardcoded into accepting 3 players.
                        To extend to more or less players, modification to more than just the following code should be
                        made.
                         */
                        } else if(lineChar == '1') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 270, k1img));
                        } else if(lineChar == '2') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 270, k2img));
                        } else if(lineChar == '3') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 270, k3img));
                        }
                    }
                }
            }
            /*
            Note how all tiles are assumed to be 50x50. All MapBlocks generated through the map[0..].txt file should be
            50px by 50px. Complications arise otherwise. The following should be modified if tile size is increased or
            decreased.
             */
            SCREEN_HEIGHT *= 50;
            SCREEN_WIDTH *= 50;
        } catch (IOException ex) {
            // Default size.
            SCREEN_HEIGHT = 500;
            SCREEN_WIDTH = 500;
            throw(ex);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);
        /*
        When the game ends and we have won, a unique text is printed.
         */
        if (gameEnd) {
            g2.setColor(Color.BLACK);
            Font font = new Font("Helvetica", Font.BOLD, 45);
            g2.setFont(font);
            g2.drawString("Game Won!", 80, 80);
            return;
        }
        // The background is drawn first.
        this.background.drawImage(buffer);
        // All blocks are drawn.
        for (Collideable block: blocks){
            block.drawImage(buffer);
        }
        // All players are drawn.
        for (Player player: players){
            player.drawImage(buffer);
        }
        g2.drawImage(world,0,0,null);
        g2.setColor(Color.WHITE);
        Font font = new Font("Helvetica", Font.BOLD, 24);
        g2.setFont(font);
        g2.drawString("Roombas Rescued: " + exitedPlayers.size(), 25, 30);
        if (gameLost()) {
            g2.drawString("Game Lost!", SCREEN_WIDTH - 200, 30);
        }
    }

    /**
     * Iterate through all Collideable objects and compare them to each other.
     */
    private void testCollisions() {
        Iterator iterA = blocks.iterator();
        while (iterA.hasNext()) {
            Collideable a = (Collideable)iterA.next();
            Iterator iterB = blocks.iterator();
            while (iterB.hasNext()) {
                Collideable b = (Collideable)iterB.next();
                if(hasCollided(a, b)) {
                    System.out.println("Two blocks have collided.");
                    a.handleCollisions(b);
                    b.handleCollisions(a);
                }
            }
            Iterator iterP = players.iterator();
            while (iterP.hasNext()) {
                Collideable p = (Collideable)iterP.next();
                if(hasCollided(a, p)) {
                    a.handleCollisions(p);
                    p.handleCollisions(a);
                }
            }
        }
        Iterator iterPA = players.iterator();
        while (iterPA.hasNext()) {
            Collideable pa = (Collideable)iterPA.next();
            Iterator iterPB = players.iterator();
            while (iterPB.hasNext()) {
                Collideable pb = (Collideable)iterPB.next();
                if(hasCollided(pa, pb)) {
                    System.out.println("Two blocks have collided.");
                    pa.handleCollisions(pb);
                    pb.handleCollisions(pa);
                }
            }
        }
    }

    /**
     *
     * @param a a Collideable object
     * @param b another Collideable object
     * @return true if the two objects have collided. (Their coordinates intersect).
     *
     * Collisions are calculated under the assumption that all objects are rectangular. Collisions work for different
     * sized objects and are calculated based on the size of their corresponding images.
     */
    private boolean hasCollided(Collideable a, Collideable b) {
        int padding = 1;
        if (a != b && !((a instanceof Wall) && (b instanceof Wall))) {
            if ((a.getX() - padding + a.getImg().getWidth() >= b.getX()) && (a.getX() - padding + a.getImg().getWidth() <= b.getX() + b.getImg().getWidth())) {
                if ((a.getY() - padding + a.getImg().getHeight() >= b.getY()) && (a.getY() - padding + a.getImg().getHeight() <= b.getY() + b.getImg().getHeight())) {
                    return true;
                } else if ((b.getY() - padding + b.getImg().getHeight() >= a.getY()) && (b.getY() - padding + b.getImg().getHeight() <= a.getY() + a.getImg().getHeight())) {
                    return true;
                }
            } else if ((b.getX() - padding + b.getImg().getWidth() >= a.getX()) && (b.getX() - padding + b.getImg().getWidth() <= a.getX() + a.getImg().getWidth())) {
                if ((a.getY() - padding + a.getImg().getHeight() >= b.getY()) && (a.getY() - padding + a.getImg().getHeight() <= b.getY() + b.getImg().getHeight())) {
                    return true;
                } else if ((b.getY() - padding + b.getImg().getHeight() >= a.getY()) && (b.getY() - padding + b.getImg().getHeight() <= a.getY() + a.getImg().getHeight()))
                    return true;
            }
        }
        return false;
    }

    /**
     * Updates the blocks. Handles the destruction of blocks from the ArrayList.
     */
    private void updateBlocks() {
        Iterator<MapBlock> iterator = blocks.iterator();
        while(iterator.hasNext()){
            Collideable block = iterator.next();
            ((MapBlock) block).update();
            if (block instanceof Destroyable && ((Destroyable) block).getToBeDestroyed()){
                System.out.println("Block will be removed");
                iterator.remove();
            }
        }
    }

    /**
     * Updates the players. Handles the destruction of players from the ArrayList.
     * Handles player exiting.
     */
    private void updatePlayers() {
        Iterator<Player> iterator = players.iterator();
        while(iterator.hasNext()){
            Player player = iterator.next();
            player.update();
            if (player.getExited()) {
                exitedPlayers.add(player);
                // A player is a special type of destroyable in which other members can set it as to be destroyed.
                // It cannot be set to be not destroyed.
                player.setToBeDestroyed();
            }
            if (player instanceof Destroyable && ((Destroyable) player).getToBeDestroyed()){
                System.out.println("Player will be removed");
                if (!(this.pc.getPlayerCount() <= 0)) {
                    this.pc.decreasePlayerCount();
                }
                System.out.println("Player count: " + this.pc.getPlayerCount());
                iterator.remove();
            }
        }
    }

}
