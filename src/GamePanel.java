
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.io.StringReader;
import java.util.Random;
import javax.swing.Timer;


public class GamePanel extends JPanel implements ActionListener { // JPanel use to organize components

    static final int SCREEN_WIDTH = 600; // to decided width of window
    static final int SCREEN_HEIGHT = 600; // to decided height of window
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    int DELAY = 100; // default 75
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    // for normal apple
    int appleX;
    int appleY;

    // for bad apple

    int badAppleX;
    int badAppleY;

    // for rock
    int rockY;
    int rockX;
    int rockY2;
    int rockX2;
    int rockX3;
    int rockY3;
    int icepathX;
    int icepathY;

    char direction = 'R'; // first direction for start
    boolean running = false;
    Timer timer;
    Random random;
    private boolean isGameStart = false;

    GamePanel() {
        if (running) {  // if game start then generate components
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.decode("#4F7942"));

            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());
            startGame();
        } else {
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.decode("#4F7942"));

            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());
            startGame();
        }
    }

    public void startGame() {
        running = true;
        if(isGameStart) { // if game start then excute following class
            newApple();
            newRock();
            newBadApple();
            newIcePath();
            timer = new Timer(DELAY, this);
            timer.start();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    private boolean gameIsOver = false;
    public void draw(Graphics g) {

        if (running) {
//
            // draw grid
//            g.setColor(Color.CYAN);
//            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
//                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw grid for things in game
//                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
//            }

            // draw  apple when game start
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // generate apple


            // draw spike
            if (bodyParts < 100) {
                //for draw spike when bodypart is lower than 70~
                g.setColor(Color.gray);
                g.fillOval(rockX, rockY, UNIT_SIZE, UNIT_SIZE);
                g.fillOval(rockX2, rockY2, UNIT_SIZE, UNIT_SIZE);
                g.fillOval(rockX3, rockY3, UNIT_SIZE, UNIT_SIZE);
                //for draw ice path
                g.setColor(Color.blue);
                g.fillOval(icepathX, icepathY, UNIT_SIZE, UNIT_SIZE);
            } else if (bodyParts < 150 && bodyParts > 100) {
                g.setColor(Color.gray);
                g.fillOval(rockX, rockY, UNIT_SIZE, UNIT_SIZE);
            }

            // draw bad apple
            if ((applesEaten != 0 && applesEaten % 50 == 0 && bodyParts < 100) // a lot of condition in order to make the game not much hard
                    || (applesEaten % 55 == 0 && bodyParts < 100)
                    || (applesEaten % 45 == 0 && bodyParts < 100)
                    || (applesEaten % 65 == 0 && bodyParts < 100)) {
                g.setColor(Color.red);
                g.fillOval(badAppleX, badAppleY, UNIT_SIZE, UNIT_SIZE);
            }

            for (int i = 0; i < bodyParts; i++) { // if body parts is 0 then generate 'head'
                if (i == 0) {
                    g.setColor(Color.decode("#228B22"));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // generate head at 'i' cord which is '1'
                } else { // to generate tail
                    g.setColor(new Color(50, 205, 50));
//                     g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); // To random snake color
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // if already has 'head' then generate 'tail'
                }
                g.setColor(Color.red); // generate scoreboard
                g.setFont(new Font("Fira Code Regular", Font.BOLD, 40));
//                FontMetrics metrics = getFontMetrics(g.getFont());
//                g.drawString("SCORE : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE : " + applesEaten)) / 2, g.getFont().getSize());
                g.drawString("SCORE : " + applesEaten,0,600);
            }
        }
        else {
//            gameOver(g);
            gameIsOver = true;
        }

        if(gameIsOver){ // to promt text in window that player already lose
            g.setColor(Color.black);
            g.setFont(new Font("Courier New", Font.BOLD, 75));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Game Over!", (SCREEN_WIDTH - metrics1.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2);
//            g.setColor(Color.black);
//            g.setFont(new Font("Courier New", Font.BOLD, 20));
//            g.drawString("PRESS SPACE TO RESTART", 150 ,340);

            //SCORE
            g.setColor(Color.black);
            g.setFont(new Font("Courier New", Font.BOLD, 40));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("SCORE : " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE : " + applesEaten)) / 2, g.getFont().getSize());
        }
        if(!isGameStart){ // to promt user to press spacebar to start
            g.setColor(Color.black);
            g.setFont(new Font("Courier New", Font.BOLD, 75));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("SNAKE GAME!", (SCREEN_WIDTH - metrics1.stringWidth("Game Over!")) / 2, SCREEN_HEIGHT / 2);
            g.setColor(Color.black);
            g.setFont(new Font("Courier New", Font.BOLD, 20));
            g.drawString("PRESS SPACE TO START", 170 ,340);
        }
    }

    public void newApple() { // to generate apple at any X and Y in grid
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // random place for apple to spawn
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newBadApple() { // to generate bad apple at any X and Y in grid
        badAppleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        badAppleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void newRock() { // to generate rock at any X and Y in grid

        rockX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        rockY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        rockX2 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        rockY2 = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

        rockX3 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        rockX3 = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void newIcePath() { // to generate icepath at any X and Y in grid
        icepathY = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        icepathX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() { // this class made snake moving forward by 1 gird to 1 grid
        for (int i = bodyParts; i > 0; i--) {  // read go for each block/grid
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': // move up
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D': // move down
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L': // move left
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R': // move right
                x[0] = x[0] + UNIT_SIZE;
                break;

        }

    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) { // if position of 'head' and 'apple' in same position
            bodyParts++; // body longer
//            bodyParts = bodyParts+10;
            applesEaten = applesEaten + 10; // increment score
//            applesEaten = applesEaten +50;
            newApple();
            newRock();
            newIcePath();
        }
    }

    public void checkRock() {
        if ((x[0] == rockX) && (y[0] == rockY)
                || (x[0] == rockX2) && (y[0] == rockY2)
                || (x[0] == rockX3) && (y[0] == rockY3)) { // if snake 'head' in same 'cord' at rock
            bodyParts--;
            applesEaten = applesEaten - 5;
            newRock();
            newApple();
            newIcePath();
            newBadApple();
        }
    }

    public static boolean hitBad_icepath = false;

    public void checkIcepath() {
        if ((x[0] == icepathX) && (y[0] == icepathY)) { // if the snake head are the same position as icepath
            hitBad_icepath = true;
            newRock();
            newApple();
            newIcePath();
            newBadApple();

        }
    }

    public static boolean hitBad = false;
    public static boolean hitBad_reverse = false;

    public void checkBadApple() {

        if ((x[0] == badAppleX) && (y[0] == badAppleY)) { // if the snake head are the same position as badApple
            if (System.currentTimeMillis()%2 == 0) { // << This is how i random effect of apple to affect the snake
                hitBad = true;
                newApple();
                newRock();
                newIcePath();
                newBadApple();
            }
            else if(System.currentTimeMillis()%3 == 0) { // Have lower weight to get
                hitBad_reverse = true;
                newApple();
                newRock();
                newIcePath();
                newBadApple();
            }
        }
    }
//            DELAY--;
//            newApple();
//            newRock();
//            newBadApple();

//    }


    public void checkCollisions() {
        // check for hit snake body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) { // if head (x[0]) hit it bodypart
                running = false;
                break;// end the game
            }
        }

        // check if head hit border
        if (x[0] < 0) { // left border
            running = false;
        }
        if (x[0] > SCREEN_WIDTH) { // right border
            running = false;
        }
        if (y[0] < 0) { // top border
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) { //  bottom border
            running = false;
        }

        if (!running) { // if running false then stop timer
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        in = new FileInputStream(new File("src\\assets\\audio\\hit_spike.wav"));
        if (running) { // if running is true
            timer.setDelay(DELAY); // to be able to set DELAY
            if (hitBad) { // if hitbad is true ~~defalut is false
                hitBad_icepath = false; // disable other bad effect
                if (sp_delay < 50) {
                    DELAY = 75;
                    sp_delay++;
                    System.out.println("Speed Boost!");
                    checkBadApple();
                } else {
                    sp_delay = 0;
                    hitBad = false;
                    DELAY = 100;

                }
            }
            if (hitBad_icepath) {
                hitBad = false;
                if (sp_delay < 2) { // loop
                    DELAY = 1;
                    sp_delay++;
                    System.out.println("GO! slide forward!"); // to see that if this function work
                    checkBadApple();
                } else {
                    sp_delay = 0;
                    hitBad_icepath = false;
                    DELAY = 100;
                }
            }
            if (hitBad_reverse){
                hitBad = false;
                hitBad_icepath = false;
                if(sp_delay < 10){ // << use this loop as timer
                    control_reverse = true;
                    sp_delay++;
                    System.out.println("Nothing under control...");
                    checkBadApple();
                }else{ // loop end then set the things that change to default
                    sp_delay = 0;
                    control_reverse = false;
                    hitBad_reverse = false;

                }
            }

            move(); // check if game is running
            checkApple();
            checkRock();
            checkIcepath();
            checkCollisions();
            checkBadApple();

        }

        repaint();

    }

    public static int sp_delay = 0; // timer
    public static boolean control_reverse = false;

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) { // basic control using arrow keys
            if (!control_reverse) { // this one is for easier too deal with reverse control (i can only think about this)
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
//                    case KeyEvent.VK_SPACE:
//                        restart();
//                        System.out.println("I PRESS SPACE");
//                        break;
                    case KeyEvent.VK_SPACE: // press spacebar to excute stargame() class
                        isGameStart = true;
                        startGame();
                        System.out.println("I PRESS SPACE");
                        break;
                }
            }else {
                switch (e.getKeyCode()) {  // just reverse the normal one
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_LEFT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }
        }
    }
//    private void restart(){
//        gameIsOver = false;
//        move();
//        newApple();
//        newSpike();
//        newBadApple();
//        newIcePath();
//        applesEaten = 0;
//        bodyParts = 3;
//
//        running = true;
//        timer.start();
//    }

}
