import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.Timer;
public class GamePanel extends JPanel implements ActionListener{

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    int DELAY = 75;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 3;
    int applesEaten;
    // for normal apple
    int appleX;
    int appleY;

    // for bad apple

//    int badAppleX;
//    int badAppleY;

    // for spike
    int spikeY;
    int spikeX;

    // for spike2

    int spikeY2;
    int spikeX2;

    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        newSpike();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running) {
            g.setColor(Color.CYAN);
            // draw grid
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // draw grid for things in game
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            // draw color
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE); // generate apple
//

            for (int i = 0; i < bodyParts; i++) { // if body parts is 0 then generate 'head'
                if (i == 0) {
                    g.setColor(Color.decode("#228B22"));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // generate head at 'i' cord which is '1'
                } else { // to generate tail
                    g.setColor(new Color(50, 205, 50));
//                     g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255))); To random snake color
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // if already has 'head' then generate 'tail'
                }
                g.setColor(Color.red); // generate score
                g.setFont(new Font("Ink Free", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("SCORE : " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("SCORE : " + applesEaten)) / 2, g.getFont().getSize());
            }
            if (applesEaten != 0 &&applesEaten % 5 == 0) {
                g.setColor(Color.gray);
                g.fillOval(spikeX, spikeY, UNIT_SIZE, UNIT_SIZE);
                g.setColor(Color.gray);
            }
        }
        else {
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; // random place for apple to spawn
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

//        badAppleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; =
//        badAppleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
    }

    public void newSpike(){

        spikeX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        spikeY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

        spikeX2 = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        spikeY2 = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

    }

    public void move(){
        for(int i = bodyParts;i>0;i--){  // read go for each block/grid
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
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

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){ // if position of 'head' and 'apple' in same position
            bodyParts++; // body longer
            applesEaten++; // increment score
            newApple();
//            DELAY--; for increased snake speed
        }
    }

    public void checkSpike() {
        if ((x[0] == spikeX) && (y[0] == spikeY) || (x[0] == spikeX2) && (y[0] == spikeY2)) { // if snake 'head' in same 'cord' at spike
            bodyParts--;
            newSpike();
        }
    }
    public void checkCollisions(){
        // check for hit snake body
        for(int i = bodyParts; i>0 ; i--){
         if((x[0] == x[i]) && (y[0] == y[i])) { // if head (x[0]) hit it bodypart
             running = false; // end the game
         }
        }

        // check if head hit border
        if(x[0] < 0) { // left border
            running = false;
        }
        if(x[0] > SCREEN_WIDTH) { // right border
            running = false;
        }
        if(y[0] < 0) { // top border
            running = false;
        }
        if(y[0] > SCREEN_HEIGHT) { //  bottom border
            running = false;
        }

        if(!running){ // if running false then stop timer
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //GAME OVER INTERFACE
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics1.stringWidth("Game Over!"))/2,SCREEN_HEIGHT/2);

        //SCORE
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,40));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("SCORE : "+applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("SCORE : "+applesEaten))/2,g.getFont().getSize());
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move(); // check if game is running
            checkApple();
            checkSpike();
            checkCollisions();
        }

        repaint();

    }


    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
