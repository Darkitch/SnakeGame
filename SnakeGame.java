import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
/*import java.util.Random;*/

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;

    private final ArrayList<Integer> snakeX;
    private final ArrayList<Integer> snakeY;
    private int bodyParts;
    private int applesEaten;
    private int appleX;
    private int appleY;
    private char direction;
    private boolean running;
    private Timer timer;

    public SnakeGame() {
        snakeX = new ArrayList<>();
        snakeY = new ArrayList<>();
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        running = false;
        timer = new Timer(DELAY, this);

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    private void startGame() {
        running = true;
        snakeX.clear();
        snakeY.clear();
        snakeX.add(0);
        snakeY.add(0);
        spawnApple();
        timer.start();
    }

    private void spawnApple() {
        Random random = new Random();
        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    private void move() {
        for (int i = bodyParts - 1; i > 0; i--) {
            snakeX.set(i, snakeX.get(i - 1));
            snakeY.set(i, snakeY.get(i - 1));
        }

        switch (direction) {
            case 'U':
                snakeY.set(0, snakeY.get(0) - UNIT_SIZE);
                break;
            case 'D':
                snakeY.set(0, snakeY.get(0) + UNIT_SIZE);
                break;
            case 'L':
                snakeX.set(0, snakeX.get(0) - UNIT_SIZE);
                break;
            case 'R':
                snakeX.set(0, snakeX.get(0) + UNIT_SIZE);
                break;
        }
    }

    private void checkApple() {
        if (snakeX.get(0) == appleX && snakeY.get(0) == appleY) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
    }

    private void checkCollisions() {
        // Check if the head collides with the body
        for (int i = bodyParts; i > 0; i--) {
            if (snakeX.get(0) == snakeX.get(i) && snakeY.get(0) == snakeY.get(i)) {
                running = false;
                break;
            }
        }

        // Check if the head collides with the boundaries
        if (snakeX.get(0) < 0 || snakeX.get(0) >= WIDTH || snakeY.get(0) < 0 || snakeY.get(0) >= HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - metrics.stringWidth("Game Over")) / 2, HEIGHT / 2);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        g.drawString("Score: " + applesEaten, (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, HEIGHT / 2 + 100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(snakeX.get(i), snakeY.get(i), UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            g.drawString("Score: " + applesEaten, 20, 40);
        } else {
            gameOver(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && direction != 'R') {
            direction = 'L';
        } else if (key == KeyEvent.VK_RIGHT && direction != 'L') {
            direction = 'R';
        } else if (key == KeyEvent.VK_UP && direction != 'D') {
            direction = 'U';
        } else if (key == KeyEvent.VK_DOWN && direction != 'U') {
            direction = 'D';
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
