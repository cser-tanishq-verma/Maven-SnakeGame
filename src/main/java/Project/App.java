package Project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Realistic Crazy Snake Game
 */
public class App extends JPanel implements ActionListener, KeyListener {

    // ================= SCREEN =================
    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS =
            (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // ================= SNAKE =================
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;

    // ================= FOOD =================
    int appleX;
    int appleY;

    // ================= GAME =================
    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    int speed = 120;

    // Animation
    int glow = 0;
    boolean glowUp = true;

    // ================= CONSTRUCTOR =================
    App() {

        random = new Random();

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(new Color(15, 15, 15));
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    // ================= START =================
    public void startGame() {

        // Snake starting position
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 100 - (i * UNIT_SIZE);
            y[i] = 100;
        }

        newApple();

        running = true;

        timer = new Timer(speed, this);
        timer.start();
    }

    // ================= NEW APPLE =================
    public void newApple() {

        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    // ================= DRAW =================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Smooth rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        draw(g2d);
    }

    public void draw(Graphics2D g) {

        if (running) {

            // ===== GRID =====
            g.setColor(new Color(35, 35, 35));

            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {

                g.drawLine(i * UNIT_SIZE, 0,
                        i * UNIT_SIZE, SCREEN_HEIGHT);

                g.drawLine(0, i * UNIT_SIZE,
                        SCREEN_WIDTH, i * UNIT_SIZE);
            }

            // ===== FOOD GLOW =====
            glowAnimation();

            g.setColor(new Color(255, 50 + glow, 50));

            g.fillOval(appleX - 2,
                    appleY - 2,
                    UNIT_SIZE + 4,
                    UNIT_SIZE + 4);

            // ===== FOOD =====
            g.setColor(Color.RED);

            g.fillOval(appleX,
                    appleY,
                    UNIT_SIZE,
                    UNIT_SIZE);

            // ===== SNAKE =====
            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {

                    // HEAD
                    GradientPaint headGradient =
                            new GradientPaint(
                                    x[i],
                                    y[i],
                                    new Color(0, 255, 100),

                                    x[i] + UNIT_SIZE,
                                    y[i] + UNIT_SIZE,
                                    new Color(0, 150, 60));

                    g.setPaint(headGradient);

                    g.fillRoundRect(
                            x[i],
                            y[i],
                            UNIT_SIZE,
                            UNIT_SIZE,
                            10,
                            10);

                    // Eyes
                    g.setColor(Color.WHITE);

                    g.fillOval(x[i] + 5,
                            y[i] + 5,
                            5,
                            5);

                    g.fillOval(x[i] + 15,
                            y[i] + 5,
                            5,
                            5);

                    g.setColor(Color.BLACK);

                    g.fillOval(x[i] + 6,
                            y[i] + 6,
                            2,
                            2);

                    g.fillOval(x[i] + 16,
                            y[i] + 6,
                            2,
                            2);

                } else {

                    // BODY GRADIENT
                    int greenShade = Math.max(50, 255 - (i * 10));

                    GradientPaint bodyGradient =
                            new GradientPaint(
                                    x[i],
                                    y[i],
                                    new Color(0, greenShade, 0),

                                    x[i] + UNIT_SIZE,
                                    y[i] + UNIT_SIZE,
                                    new Color(0, greenShade / 2, 0));

                    g.setPaint(bodyGradient);

                    g.fillRoundRect(
                            x[i],
                            y[i],
                            UNIT_SIZE,
                            UNIT_SIZE,
                            10,
                            10);
                }
            }

            // ===== SCORE =====
            g.setColor(Color.WHITE);

            g.setFont(new Font("Consolas",
                    Font.BOLD,
                    30));

            g.drawString("Score : " + applesEaten,
                    20,
                    40);

            // ===== SPEED =====
            g.setColor(Color.CYAN);

            g.setFont(new Font("Consolas",
                    Font.PLAIN,
                    20));

            g.drawString("Speed : " + (200 - speed),
                    20,
                    70);

        } else {

            gameOver(g);
        }
    }

    // ================= MOVE =================
    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] -= UNIT_SIZE;
                break;

            case 'D':
                y[0] += UNIT_SIZE;
                break;

            case 'L':
                x[0] -= UNIT_SIZE;
                break;

            case 'R':
                x[0] += UNIT_SIZE;
                break;
        }
    }

    // ================= FOOD CHECK =================
    public void checkApple() {

        if ((x[0] == appleX) &&
                (y[0] == appleY)) {

            bodyParts++;
            applesEaten++;

            // Increase speed gradually
            if (speed > 45) {

                speed -= 3;

                timer.setDelay(speed);
            }

            newApple();
        }
    }

    // ================= COLLISION =================
    public void checkCollisions() {

        // Body collision
        for (int i = bodyParts; i > 0; i--) {

            if ((x[0] == x[i]) &&
                    (y[0] == y[i])) {

                running = false;
            }
        }

        // Wall collision
        if (x[0] < 0 ||
                x[0] >= SCREEN_WIDTH ||
                y[0] < 0 ||
                y[0] >= SCREEN_HEIGHT) {

            running = false;
        }

        if (!running && timer != null) {

            timer.stop();
        }
    }

    // ================= GAME OVER =================
    public void gameOver(Graphics2D g) {

        // Dark overlay
        g.setColor(new Color(0, 0, 0, 170));

        g.fillRect(0,
                0,
                SCREEN_WIDTH,
                SCREEN_HEIGHT);

        // GAME OVER
        g.setColor(Color.RED);

        g.setFont(new Font("Arial",
                Font.BOLD,
                70));

        FontMetrics metrics1 =
                getFontMetrics(g.getFont());

        g.drawString(
                "GAME OVER",
                (SCREEN_WIDTH - metrics1.stringWidth("GAME OVER")) / 2,
                SCREEN_HEIGHT / 2 - 50);

        // Score
        g.setColor(Color.WHITE);

        g.setFont(new Font("Consolas",
                Font.BOLD,
                35));

        FontMetrics metrics2 =
                getFontMetrics(g.getFont());

        g.drawString(
                "Final Score : " + applesEaten,
                (SCREEN_WIDTH - metrics2.stringWidth(
                        "Final Score : " + applesEaten)) / 2,
                SCREEN_HEIGHT / 2 + 20);

        // Restart
        g.setColor(Color.GREEN);

        g.setFont(new Font("Consolas",
                Font.PLAIN,
                25));

        g.drawString(
                "Press SPACE to Restart",
                SCREEN_WIDTH / 2 - 170,
                SCREEN_HEIGHT / 2 + 80);
    }

    // ================= GLOW EFFECT =================
    public void glowAnimation() {

        if (glowUp) {

            glow += 5;

            if (glow >= 100) {
                glowUp = false;
            }

        } else {

            glow -= 5;

            if (glow <= 0) {
                glowUp = true;
            }
        }
    }

    // ================= TIMER =================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();

            checkApple();

            checkCollisions();
        }

        repaint();
    }

    // ================= KEYBOARD =================
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:

                if (direction != 'R') {
                    direction = 'L';
                }

                break;

            case KeyEvent.VK_RIGHT:

                if (direction != 'L') {
                    direction = 'R';
                }

                break;

            case KeyEvent.VK_UP:

                if (direction != 'D') {
                    direction = 'U';
                }

                break;

            case KeyEvent.VK_DOWN:

                if (direction != 'U') {
                    direction = 'D';
                }

                break;

            case KeyEvent.VK_SPACE:

                if (!running) {
                    restartGame();
                }

                break;
        }
    }

    // ================= RESTART =================
    public void restartGame() {

        bodyParts = 6;

        applesEaten = 0;

        direction = 'R';

        speed = 120;

        for (int i = 0; i < GAME_UNITS; i++) {

            x[i] = 0;
            y[i] = 0;
        }

        startGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // ================= MAIN =================
    public static void main(String[] args) {

        // ================= HEADLESS CHECK =================
        // Jenkins usually runs without GUI/Desktop access
        if (GraphicsEnvironment.isHeadless()) {

            System.out.println("Running in headless mode.");
            System.out.println("GUI cannot be displayed in Jenkins.");
            System.out.println("Build executed successfully.");

            return;
        }

        // ================= NORMAL DESKTOP MODE =================
        JFrame frame = new JFrame();

        App game = new App();

        frame.add(game);

        frame.setTitle("Realistic Crazy Snake Game 🐍");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);

        frame.pack();

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
