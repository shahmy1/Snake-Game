import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP){
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN ){
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
            velocityX = 1;
            velocityY = 0;

        }

    }
    // no need these 2 types
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        int tileSize = 25;



    }

    ArrayList<Tile> snakeBody;
    private Tile snakeHead;

    private Tile food;
    private Random random;
    private boolean gameOver = false;
    private Timer gameloop;

    int velocityX;
    int velocityY;


    SnakeGame() {
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        food = new Tile(10, 10);
        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<>();
        random = new Random();  // Initialize random object here
        placeFood();

        velocityX = 0;
        velocityY = 0;
      gameloop = new Timer();
       gameloop.schedule(new TimerTask() {
            @Override
            public void run() {
                move();
                repaint();
            }
        }, 0, 100);  // 100 milliseconds delay

        // Handle game termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            gameloop.cancel();
            gameloop.purge();
        }));
    }

    public void placeFood() {
        food.x = random.nextInt(600 / 25);
        food.y = random.nextInt(600 / 25);
    }

    public boolean collision (Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Grid
        for (int i = 0; i < 600 / 25; i++) {
            g.drawLine(i * 25, 0, i * 25, 600);
            g.drawLine(0, i * 25, 600, i * 25);
        }

        // Food
        g.setColor(Color.red);
        //g.fillRect(food.x * 25, food.y * 25, 25, 25);
        g.fill3DRect(food.x * 25, food.y * 25, 25, 25,true);


        // Snake Head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * 25, snakeHead.y * 25, 25, 25);
        g.fill3DRect(snakeHead.x * 25, snakeHead.y * 25, 25, 25,true);

        // snake Body
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
           // g.fillRect(snakePart.x * 25, snakePart.y * 25, 25,25);//
            g.fill3DRect(snakePart.x * 25, snakePart.y * 25, 25,25,true);
        }

        // score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), 25-16,25);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), 25-16,25);
        }
    }

    public void move() {
        // eat food
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        // Snake Body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Tile snakePart = snakeBody.get(i);
            if (i == 0) {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } else {
                Tile prevSnakePart = snakeBody.get(i - 1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }
        //score


        // snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Wrap around the screen if the snake goes out of bounds
        /*if (snakeHead.x < 0) {
            snakeHead.x = 600 / 25 - 1;
        } else if (snakeHead.x >= 600 / 25) {
            snakeHead.x = 0;
        }

        if (snakeHead.y < 0) {
            snakeHead.y = 600 / 25 - 1;
        } else if (snakeHead.y >= 600 / 25) {
            snakeHead.y = 0;
        }
*/

        // game Over condition
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            if (collision(snakeHead, snakePart)) {
                gameOver = false;
            }
        }
        if (snakeHead.x*25<0 || snakeHead.x*25>600 || snakeHead.y*25<0 || snakeHead.y*25 >600){
            gameOver=true;
        }

    }

     @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameloop.cancel(); // Stop the gameloop
        }
    }



    public static void main(String[] args) {
        new SnakeGame();

        }
    }

