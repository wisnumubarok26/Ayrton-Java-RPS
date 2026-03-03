import javax.swing.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener {
    String username;

    //Game propoerties
    static final int GAME_WIDTH = 600;
    static final int GAME_HEIGHT = 500;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (GAME_WIDTH * GAME_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 100;


    int x[] = new int[GAME_UNITS];
    int y[] = new int[GAME_UNITS];

    int bodyParts = 6;
    int applesEaten = 0;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    JLabel lblScore, lblTitle;
    JButton btnBack;

    GamePanel panelGame;

    public SnakeGame(String u){
        username = u;

        setTitle("Snake Game");
        setSize(GAME_WIDTH, GAME_HEIGHT+100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // 2 rows, 1 column

        JPanel topPanel = new JPanel(null);
        topPanel.setPreferredSize(new Dimension(GAME_WIDTH, 80));
        topPanel.setBackground(Color.darkGray);

        lblTitle = new JLabel("Snake Game");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitle.setForeground(Color.white);
        lblTitle.setBounds(20, 20, 200, 40);
        topPanel.add(lblTitle);

        lblScore = new JLabel("Score: 0");
        lblScore.setFont(new Font("Arial", Font.PLAIN, 18));
        lblScore.setForeground(Color.white);
        lblScore.setBounds(250, 20, 150, 30);
        topPanel.add(lblScore);

        btnBack = new JButton("Back to Home");
        btnBack.setBounds(450, 20, 120, 30);
        topPanel.add(btnBack);

        btnBack.addActionListener(e -> {
            new HomeForm(username);
            dispose();
        });

        add(topPanel, BorderLayout.NORTH);

        panelGame = new GamePanel();
        add(panelGame, BorderLayout.CENTER);

        addKeyListener(new MyKeyAdapter());
        setFocusable(true);

        random = new Random();
        startGame();

        setVisible(true);
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newApple(){
        appleX = random.nextInt((int)(GAME_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(GAME_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){
        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            lblScore.setText("Score: " + applesEaten);
            newApple();
            updateScore(username);
        }
    }

    public void checkCollisions(){
        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])){
                running = false;
            }
        }

        // collision with edges

        if(x[0] < 0 || x[0] >= GAME_WIDTH || y[0] < 0 || y[0] >= GAME_HEIGHT){
            running = false;
        }
        if(!running){
            timer.stop();
        }
    }
    @Override
        public void actionPerformed(ActionEvent e){
            if(running){
                move();
                checkApple();
                checkCollisions();
            }
            panelGame.repaint();
        }

    class GamePanel extends JPanel{
        public GamePanel(){
            setPreferredSize(new Dimension(GAME_WIDTH,GAME_HEIGHT));
            setBackground(Color.BLACK);
        }

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            drawgame(g);
        }

        public void drawgame(Graphics g){
            if(running){
                // draw apple
                g.setColor(Color.red);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

                // draw snake
                for(int i = 0; i < bodyParts; i++){
                    if(i == 0){
                        g.setColor(Color.green);
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(new Color(45,180,0));
                        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            } else {
                gameOver(g);
            }
        }

        public void gameOver(Graphics g){
            // Game Over text
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (GAME_WIDTH - metrics.stringWidth("Game Over"))/2, GAME_HEIGHT/2);
        }
    }

    class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
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

    private void updateScore(String username) {
        try {
            Connection conn = Koneksi.connect();
            String sql = "UPDATE users SET score_snakegame = score_snakegame + 2 WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("Updating score for: " + username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
   
}