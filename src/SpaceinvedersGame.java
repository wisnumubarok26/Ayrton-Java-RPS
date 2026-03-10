import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceinvedersGame extends JPanel implements ActionListener, KeyListener{
    Timer timer;

    int playerX = 350;
    int playerY = 625;
    int playerWidht = 50;
    int playerHeight = 30;

    //move enemy variable
    int enemyDirection = 2;

    int bulletX = -1;
    int bulletY = -1;
    boolean bulletActive = false;

    //enemy attack

    int enemyBulletX = -1;
    int enemyBulletY = -1;
    boolean enemyBulletActive = false;

    ArrayList<Rectangle> enemies = new ArrayList<>();
    ArrayList<Rectangle> barriers = new ArrayList<>();
    ArrayList<Integer> barrierHealth = new ArrayList<>();


    int score = 0;
    int Player_Lives = 3;

    

    String username;

    public SpaceinvedersGame( String u){
        username = u;
        setPreferredSize(new Dimension(1000,700));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        setLayout(null);

        createEnemies();
        createBarriers();

        timer = new Timer(20,this);
        timer.start();

        //btn reset obj and exit
        JButton reseButton = new JButton("Reset");
        reseButton.setBounds(750,10,100,30);
        add(reseButton);
        
        reseButton.addActionListener(e-> resetGame());

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(880,10,100,30);
        add(exitButton);
        
        exitButton.addActionListener(e->{
            timer.stop();
            new HomeForm(username);
            SwingUtilities.getWindowAncestor(this).dispose();
        });
    }

    public void createEnemies(){
        for (int row =0; row<3;row++){
            for(int col = 0; col<8;col++){
                enemies.add(new Rectangle(80+col*80,50+row*50,40,30));
            }
        }
    }


    public void createBarriers(){
    int barrierWidth = 80;
    int barrierHeight = 40;

    for(int i = 0; i < 4; i++){
        int x = 150 + i * 200;
        int y = 520;

        barriers.add(new Rectangle(x, y, barrierWidth, barrierHeight));
        barrierHealth.add(6); // each barrier can take 6 hits
    }
    }

    //reset game method
    public void resetGame(){

        playerX = 350;
        playerY = 625;

        score = 0;
        Player_Lives = 3;

        bulletActive = false;
        bulletX = -1;
        bulletY = -1;

        enemyBulletActive = false;
        enemyBulletX = -1;
        enemyBulletY = -1;

        enemyDirection = 2;

        enemies.clear();
        createEnemies();

        barriers.clear();
        barrierHealth.clear();
        createBarriers();

        if(!timer.isRunning()){
            timer.start();
        }

        repaint();
        requestFocusInWindow();
    }
        
    

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.setColor(Color.green);
        g.fillRect(playerX,playerY,playerWidht,playerHeight);

        if(bulletActive){
            g.setColor(Color.yellow);
            g.fillRect(bulletX,bulletY,5,10);
        }

        g.setColor(Color.red);

        for (Rectangle enemy : enemies){
            g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);
        }

        g.setColor(Color.cyan);

        for(Rectangle barrier : barriers){
             g.fillRect(barrier.x, barrier.y, barrier.width, barrier.height);
        }

        g.setColor(Color.white);
        g.drawString("Score : "+ score,10,20 );
        g.drawString("Lives : " + Player_Lives, 10, 40);

        if(enemyBulletActive){
            g.setColor(Color.white);
            g.fillRect(enemyBulletX, enemyBulletY, 5, 10);
        }
    }

    public void actionPerformed(ActionEvent e){
        if(bulletActive){
            bulletY -=15; // Players Bullet Speed

            if(bulletY < 10){
                bulletActive = false;
            }

            checkCollision();
        }

        //enemy move 
        for (Rectangle enemy : enemies){
            enemy.x += enemyDirection;
        }

        //chck wall collision

        for (Rectangle enemy : enemies ){
            if(enemy.x <=0 || enemy.x + enemy.width >=800){
                enemyDirection *=-1;

                // for (Rectangle e2 : enemies){
                // e2.y +=20;
            // }
            
            }

            break;
        }
        //Enemy Shooting System
        if(!enemyBulletActive && enemies.size() > 0){

    Rectangle bestShooter = null;
    int closestDistance = Integer.MAX_VALUE;

    // Find enemy closest to player X
    for(Rectangle enemy : enemies){

        int distance = Math.abs((enemy.x + enemy.width/2) - (playerX + playerWidht/2));

        if(distance < closestDistance){
            closestDistance = distance;
            bestShooter = enemy;
        }
    }

    if(bestShooter != null){
        enemyBulletX = bestShooter.x + bestShooter.width/2;
        enemyBulletY = bestShooter.y + bestShooter.height;
        enemyBulletActive = true;
    }
}
        //bullet move
    if(enemyBulletActive){

    enemyBulletY += 10;

    Rectangle enemyBullet = new Rectangle(enemyBulletX, enemyBulletY, 5, 10);
    Rectangle player = new Rectangle(playerX, playerY, playerWidht, playerHeight);

    for(int i = 0; i < barriers.size(); i++){
    Rectangle barrier = barriers.get(i);

    if(enemyBullet.intersects(barrier)){
        int hp = barrierHealth.get(i) - 1;
        barrierHealth.set(i, hp);

        enemyBulletActive = false;

        if(hp <= 0){
            barriers.remove(i);
            barrierHealth.remove(i);
        }

        break;
    }
}

    if(enemyBulletY > 650){
        enemyBulletActive = false;
    }
    else if(enemyBullet.intersects(player)){
        enemyBulletActive = false;
        Player_Lives--;

        System.out.println("Player Hit! Lives left: " + Player_Lives);

        if(Player_Lives <= 0){
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Score: " + score);
            resetGame();
        }
    }
}
        repaint();
    }

    public void checkCollision(){
        Rectangle bullet = new Rectangle(bulletX,bulletY,5,10);
        for (int i = 0; i<enemies.size(); i++){
            if(bullet.intersects(enemies.get(i))){
                enemies.remove(i);
                bulletActive = false;
                score+=10;
                break;
            }
        }
        for(int i = 0; i < barriers.size(); i++){
    if(bullet.intersects(barriers.get(i))){
        int hp = barrierHealth.get(i) - 1;
        barrierHealth.set(i, hp);

        bulletActive = false;

        if(hp <= 0){
            barriers.remove(i);
            barrierHealth.remove(i);
        }
        return;
    }
}
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            playerX -=15;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            playerX +=15;
        }

        if(e.getKeyCode()== KeyEvent.VK_SPACE){
            if(!bulletActive){
                bulletX = playerX + playerWidht/2;
                bulletY = playerY;
                bulletActive=true;
            }
        }
    }

    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e){}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Space Invaders - Beta Version Release 1.0.1");

        SpaceinvedersGame game = new SpaceinvedersGame("wisnu");       

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}