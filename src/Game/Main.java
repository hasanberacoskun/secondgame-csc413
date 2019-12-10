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
 *
 * @author anthony-pc
 * @author bera h. coskun
 */
public class Main extends JPanel  {


    public static int SCREEN_WIDTH = 0;
    public static int SCREEN_HEIGHT = 0;
    private BufferedImage world;
    private BufferedReader br;
    private Graphics2D buffer;
    private JFrame jf;
    //private Player player;
    private Background background;
    private boolean gameEnd = false;

    private ArrayList<MapBlock> blocks = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Player> exitedPlayers = new ArrayList<>();
    private PlayerController pc;


    public static void main(String[] args) {
        Thread x;
        Main gameInstance = new Main();
        gameInstance.init();
        try {

            while (!(gameInstance.gameEnd)) {
                gameInstance.checkGameEnd();
                gameInstance.testCollisions();
                gameInstance.updatePlayers();
                gameInstance.updateBlocks();
                gameInstance.repaint();
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {

        }


    }

    /**
     *
     * @return 0 means game is won.
     *         1 means game is lost.
     *         2 means level is won.
     */
    private int checkGameEnd() {
        if (!(players.size() + exitedPlayers.size() >= 3)) {
            System.out.println("Game Lost");
            return 1;
        }
        if (gameEnd) {
            gameEnd = true;
            return 0;
        }
        if (exitedPlayers.size() == 3) {
            System.out.println("Level Won");
            return 2;
        }
        return 0;
    }


    private void init() {
        this.jf = new JFrame("Koalabr8");
        this.jf.setFocusable(true);
        this.jf.requestFocus();
        BufferedImage backgroundimg = null, k1img = null, k2img = null, k3img = null, wallimg = null, bladeimg = null,
                boulderimg = null, tntimg = null, locklockedimg = null, lockunlockedimg = null, switchimg = null
                , exitredimg = null, exitblueimg = null, exitclosedimg = null;
        try {
            BufferedImage tmp;
            System.out.println(System.getProperty("user.dir"));
            /*
             * note class loaders read files from the out folder (build folder in netbeans) and not the
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
            File mapData = new File("resources/map.txt");
            br = new BufferedReader(new FileReader(mapData));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        background = new Background(backgroundimg);
        try {
            generateBlocks(k1img, k2img, k3img, wallimg, bladeimg, boulderimg, tntimg, locklockedimg, lockunlockedimg,
                    switchimg, exitredimg, exitblueimg, exitclosedimg);
            System.out.println("Blocks loaded");
        } catch(IOException ex){
            System.out.println("Blocks not loaded: " + ex);
        }
        System.out.println("Screen height will be: " + SCREEN_HEIGHT);
        System.out.println("Screen width will be: " + SCREEN_WIDTH);
        this.world = new BufferedImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        pc = new PlayerController(players, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);

        this.jf.addKeyListener(pc);
        // Loop lists the key listeners with their addresses.
        /*String listedKeyListeners = "";
        for (int i = 0; i < this.jf.getKeyListeners().length; i++) {
            listedKeyListeners += this.jf.getKeyListeners()[i].toString() + ", ";
        }
        System.out.println("key listeners= " + listedKeyListeners);*/

        this.jf.setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT + 30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setVisible(true);

    }

    // Generates all block objects and stores in a list. This will later be accessed when drawing and updating.
    private void generateBlocks(BufferedImage k1img, BufferedImage k2img, BufferedImage k3img, BufferedImage wallimg,
                                BufferedImage bladeimg, BufferedImage boulderimg, BufferedImage tntimg, BufferedImage locklockedimg,
                                BufferedImage lockunlockedimg, BufferedImage switchimg, BufferedImage exitredimg,
                                BufferedImage exitblueimg, BufferedImage exitclosedimg) throws IOException {
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
                        } else if(lineChar == 'V'){
                            blocks.add(new Blade(blockXPostion, blockYPosition, bladeimg, false));
                        } else if(lineChar == 'H') {
                            blocks.add(new Blade(blockXPostion, blockYPosition, bladeimg, true));
                        } else if(lineChar == 'B') {
                            blocks.add(new Boulder(blockXPostion, blockYPosition, boulderimg));
                        } else if(lineChar == 'T') {
                            blocks.add(new TNT(blockXPostion, blockYPosition, tntimg));
                        } else if (lineChar == '#') {
                            blocks.add(new Exit(blockXPostion, blockYPosition, exitredimg, exitblueimg, exitclosedimg, 3));
                        } else if (lineChar == '$') {
                            blocks.add(new Exit(blockXPostion, blockYPosition, exitredimg, exitblueimg, exitclosedimg, 1));
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
                        } else if(lineChar == '1') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 0, k1img));
                        } else if(lineChar == '2') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 0, k2img));
                        } else if(lineChar == '3') {
                            players.add(new Player(blockXPostion, blockYPosition, 0, 0, 0, k3img));
                        }
                    }
                }
            }
            SCREEN_HEIGHT *= 50;
            SCREEN_WIDTH *= 50;
        } catch (IOException ex) {
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
        this.background.drawImage(buffer);
        for (Collideable block: blocks){
            block.drawImage(buffer);
        }
        for (Player player: players){
            player.drawImage(buffer);
        }
        g2.drawImage(world,0,0,null);
    }

    private void testCollisions() {
        // HANDLE COLLISION TESTING HERE
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
        /*for(Collideable a: blocks) {
            // Test block to block collisions
            for(Collideable b: blocks) {
                if(hasCollided(a, b)) {
                    System.out.println("Two blocks have collided.");
                    a.handleCollisions(b);
                    b.handleCollisions(a);
                }
            }
            // Test player block collisions.
            for(Collideable p: players) {
                if(hasCollided(a, p)) {
                    a.handleCollisions(p);
                    p.handleCollisions(a);
                }
            }
        }
        for(Collideable a: players) {
            for(Collideable b: players) {
                if(hasCollided(a, b)) {
                    a.handleCollisions(b);
                    b.handleCollisions(a);
                }
            }
        }*/

    }

    private boolean hasCollided(Collideable a, Collideable b) {

        if (a != b && !((a instanceof Wall) && (b instanceof Wall))) {
            /*if (a.getX() + a.getImg().getWidth() > b.getX()){
                //System.out.println("has not collided");
                return true;
            } else if (b.getX() + b.getImg().getWidth() > a.getX()) {
                //System.out.println("has not collided");
                return true;
            } else if (a.getY() + a.getImg().getHeight() > b.getY()) {
                //System.out.println("has not collided");
                return true;
            } else if (b.getY() + b.getImg().getHeight() > a.getY()) {
                //System.out.println("has not collided");
                return true;
            }*/
//            System.out.println("a height: " + a.getImg().getHeight());
//            System.out.println("a y value: " + a.getY());
            if ((a.getX() + a.getImg().getWidth() >= b.getX()) && (a.getX() + a.getImg().getWidth() <= b.getX() + b.getImg().getWidth())) {
                if ((a.getY() + a.getImg().getHeight() >= b.getY()) && (a.getY() + a.getImg().getHeight() <= b.getY() + b.getImg().getHeight())) {
                    return true;
                } else if ((b.getY() + b.getImg().getHeight() >= a.getY()) && (b.getY() + b.getImg().getHeight() <= a.getY() + a.getImg().getHeight())) {
                    return true;
                }
            } else if ((b.getX() + b.getImg().getWidth() >= a.getX()) && (b.getX() + b.getImg().getWidth() <= a.getX() + a.getImg().getWidth())) {
                if ((a.getY() + a.getImg().getHeight() >= b.getY()) && (a.getY() + a.getImg().getHeight() <= b.getY() + b.getImg().getHeight())) {
                    return true;
                } else if ((b.getY() + b.getImg().getHeight() >= a.getY()) && (b.getY() + b.getImg().getHeight() <= a.getY() + a.getImg().getHeight()))
                    return true;
            } else {
                return false;
            }
        }
        return false;
    }

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
