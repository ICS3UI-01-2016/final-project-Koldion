package pkgfinal.project.kevin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 *
 * @author wangk9757
 */
public class Fall extends JComponent implements KeyListener {

    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    Color skyColour = new Color(58, 214, 196);
    Rectangle dude = new Rectangle(100, 200, 50, 50);
    int gravity = 1;
    boolean start = false;
    boolean dead = false;
    int dy = 0;
    int jumpVelocity = -15;
    // jump key variable
    boolean jump = false;
    Rectangle[] LeftLine = new Rectangle[5];
    Rectangle[] RightLine = new Rectangle[5];
    boolean[] passedLine = new boolean[5];
    // the gap between top and bottom
    int lineGap = 150;
    // distance between the pipes
    int lineSpacing = 200;
    // the width of a single pipe
    int lineWidth = randInt(0, 500);
    // the height of a pipe
    int lineHeight = 5;
    // minimum distance from edge
    int lineDistance = 200;
    int minDistance = 100;
    // speed of the game
    int speed = 1;
    boolean right = false;
    boolean left = false;

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);


        // GAME DRAWING GOES HERE 


        g.setColor(skyColour);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        // GAME DRAWING GOES HERE 
        g.setColor(Color.BLACK);
        for (int i = 0; i < LeftLine.length; i++) {
            g.fillRect(RightLine[i].x, RightLine[i].y, RightLine[i].width, RightLine[i].height);
            g.fillRect(LeftLine[i].x, LeftLine[i].y, LeftLine[i].width, LeftLine[i].height);

            g.setColor(Color.BLACK);
            g.fillRect(dude.x, dude.y, 25, 25);
        }

        // GAME DRAWING ENDS HERE
    }

    public BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            File file = new File(filename);
            img = ImageIO.read(file);
        } catch (Exception e) {
            //if there is error, print
            e.printStackTrace();
        }
        return img;
    }

    public void reset() {
        int pipeX = 600;
    }

    public int randInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public void setline(int linePosition) {
        // a random number generator
        Random randGen = new Random();
        // generate a random X position
        int lineX = randInt(0, 400);
        // generate the new pipe Y coordinate
        int lineY = (600);
        lineX = lineX + (lineWidth + lineSpacing) * LeftLine.length;
        LeftLine[linePosition].setBounds(lineX, lineY, WIDTH, HEIGHT);
        RightLine[linePosition].setBounds(lineX- lineGap - lineWidth, lineY, WIDTH,HEIGHT);

    }
    // The main game loop
    // In here is where all the logic for my game will go

    public void run() {

        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;
        int lineX = 0;
        Random randGen = new Random();
        for (int i = 0; i < LeftLine.length; i++) {
            // generating a random y position
            int lineY = (500);
            RightLine[i] = new Rectangle(lineX, lineY, lineWidth, lineHeight);
            LeftLine[i] = new Rectangle(lineX, lineY, lineWidth, lineHeight);
            lineX = lineX + lineWidth;
        }
        // the main game loop section
        // game will end if you set done = false;
        boolean done = false;
        if (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 

            if (start) {
                if (!dead) {

                    for (int i = 0; i < RightLine.length; i++) {
                        RightLine[i].y = RightLine[i].y - speed;
                        LeftLine[i].y = LeftLine[i].y - speed;
                        // check if a pipe is off the screenfor(int i = 0; i < topPipes.length
                       if(RightLine[i].x + lineWidth < 0){
                    // move the pipe
                    setline(i);
                        }
                    }
                    boolean grav = true;
                   if (grav) {
                        dy = dy + gravity;
                        // apply the change in y to the dude
                        dude.y = dude.y + dy;
                    }
                    // get the dude to fall
                    for (int i = 0; i < RightLine.length; i++) {
                        if (dude.y == HEIGHT) {
                            done = true;

                            // apply gravity
                            if (left) {
                                dude.x = dude.x - 8;
                            }
                            if (right) {
                                dude.x = dude.x + 8;
                            }
                          
                            if (dude.x < 0 || dude.x > 800) {
                                dude.x = 0;
                            } else if (dude.x + dude.width > WIDTH || dude.x + dude.width < 0) {
                                dude.x = WIDTH - dude.width;
                            }
                            if (dude.y < 0) {
                                dude.y = 0;
                            } else if (dude.y + dude.height > HEIGHT) {
                                dude.y = HEIGHT - dude.height;
                            }

                            for (int j = 0; j < RightLine.length; j++) {
                                if (dude.intersects(RightLine[i])) {
                                    grav = false;
                                } else if (dude.intersects(LeftLine[i])) {
                                    grav = false;
                                    // GAME LOGIC ENDS HERE 


                                    // update the drawing (calls paintComponent)
                                    repaint();



                                    // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
                                    // USING SOME SIMPLE MATH
                                    deltaTime = System.currentTimeMillis() - startTime;
                                    try {
                                        if (deltaTime > desiredTime) {
                                            //took too much time, don't wait
                                            Thread.sleep(1);
                                        } else {
                                            // sleep to make up the extra time
                                            Thread.sleep(desiredTime - deltaTime);
                                        }
                                    } catch (Exception e) {
                                    };
                                
                                }
                            }
                        }
                    }
                }
            }
                        
        }
    }
                                                                                               
                  
                   /** 
                    * @param args the command line arguments
                  */   
    public static void main(String[] args) {
        // creates a windows to show my game
        JFrame frame = new JFrame("My Game");

        // creates an instance of my game
        Fall game = new Fall();

        // sets the size of my game
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(game);

        // add the key listener 
        frame.addKeyListener(game);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // starts my game loop 
        game.run();
    
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
            start = true;
        }

        if (key == KeyEvent.VK_RIGHT) {
            right = true;
            start = true;

        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            right = false;

        }
    }
}