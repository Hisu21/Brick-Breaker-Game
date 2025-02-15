import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Color;
import java.util.Random;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean GameStarted = false;
    private int startingScore = 0;
    private int totalBricks = 28;
    private Timer timer;
    private int delay = 8;
    private int playerPosition = 310;
    private int ballPositionX = 120;
    private int ballPositionY = 350;
    private int ballDirectionX = -1;
    private int ballDirectionY = -2;

    private Map map;

    public Gameplay() {
        Random random = new Random();
        ballPositionX = random.nextInt(600) + 50;
        ballPositionY = 350;

        map = new Map(4,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public static class CustomColors{
        public static final Color purple = new Color(102,0,153);
        public static final Color green = new Color(0,153,0);
    }

    public void paint(Graphics g) {
        // for the background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // map
        map.draw((Graphics2D)g);

        // borders
        g.setColor(CustomColors.green);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // score
        g.setColor(CustomColors.green);
        g.setFont(new Font("Times New Roman", Font.BOLD, 25));
        g.drawString("" + startingScore, 590, 30);

        // paddle
        g.setColor(CustomColors.green);
        g.fillRect(playerPosition,550, 100, 8);

        // the ball

        g.setColor(CustomColors.green);
        g.fillOval(ballPositionX, ballPositionY, 20, 20);
        if (ballPositionY > 570){
            GameStarted = false;
            ballDirectionY = 0;
            ballDirectionX = 0;
            g.setColor(CustomColors.green);
            g.setFont(new Font("Times New Roman", Font.BOLD, 30));
            g.drawString("Game Over! Score: " + startingScore, 190, 300);

            g.setFont(new Font("Times New Roman", Font.BOLD, 20));
            g.drawString("Press \"Enter\" to Restart", 230, 350);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (GameStarted) {
            if (new Rectangle(ballPositionX, ballPositionY, 20, 20).intersects(new Rectangle(playerPosition, 550, 100, 8))) {
                ballDirectionY = -ballDirectionY;
            }
            A: for(int i=0; i<map.map.length; i++) {
                for(int j=0; j<map.map[0].length;j++){
                    if (map.map[i][j]>0){
                        int brickX = j*map.brickWidth + 80;
                        int brickY = i*map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPositionX, ballPositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            startingScore += 5;

                            if (ballPositionX + 19 <= brickRect.x || ballPositionX + 1 >= brickRect.x + brickRect.width){
                                ballDirectionX = -ballDirectionX;
                            } else {
                                ballDirectionY = -ballDirectionY;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPositionX += ballDirectionX;
            ballPositionY += ballDirectionY;
            if (ballPositionX < 0) {
                ballDirectionX = -ballDirectionX;
            }
            if (ballPositionY < 0) {
                ballDirectionY = -ballDirectionY;
            }
            if (ballPositionX > 670) {
                ballDirectionX = -ballDirectionX;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerPosition < 10) {
                playerPosition = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerPosition >= 600) {
                playerPosition = 600;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!GameStarted) {
                GameStarted = true;
                startingScore = 0;
                totalBricks = 28;
                playerPosition = 310;

                Random random = new Random();
                ballPositionX = random.nextInt(600) + 50;
                ballPositionY = 350;

                ballDirectionX = -1;
                ballDirectionY = -2;
                map = new Map(4,7);
                repaint();
            }
        }
    }

    public void moveLeft() {
        GameStarted = true;
        playerPosition -= 20;
    }
    public void moveRight() {
        GameStarted = true;
        playerPosition += 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    }




