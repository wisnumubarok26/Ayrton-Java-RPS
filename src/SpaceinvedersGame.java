import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SpaceinvedersGame extends JPanel implements ActionListener, KeyListener{
    Timer timer;

    int playerX = 350;
    int playerY = 500;
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

    int score = 0;

    public SpaceinvedersGame(){
        setPreferredSize(new Dimension(800,600));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        createEnemies();

        timer = new Timer(20,this);
        timer.start();
    }

    public void createEnemies(){
        for (int row =0; row<3;row++){
            for(int col = 0; col<8;col++){
                enemies.add(new Rectangle(80+col*80,50+row*50,40,30));
            }
        }
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

        g.setColor(Color.white);
        g.drawString("Score : "+ score,10,20 );

        if(enemyBulletActive){
            g.setColor(Color.white);
            g.fillRect(enemyBulletX, enemyBulletY, 5, 10);
        }
    }

    public void actionPerformed(ActionEvent e){
        if(bulletActive){
            bulletY -=10;

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
        //enemy comoponent
        if(!enemyBulletActive && enemies.size()>0){
            int randomindex = (int)(Math.random() * enemies.size());

            Rectangle shooter = enemies.get(randomindex);

            enemyBulletX = shooter.x + shooter.width/2;
            enemyBulletY = shooter.y;

            enemyBulletActive = true;
        }
        //bullet move
        if(enemyBulletActive){
            enemyBulletY +=5;

            if(enemyBulletY > 600){
                enemyBulletActive = false;
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
    }

    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            playerX -=20;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            playerX +=20;
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
        JFrame frame = new JFrame("Space inveders");

        SpaceinvedersGame game = new SpaceinvedersGame();       

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}