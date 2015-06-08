/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package physicssim;

import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;
import java.util.Scanner;

/**
 *
 * @author 166738
 */
public class MapForm extends javax.swing.JPanel implements ActionListener, KeyListener, MouseListener
{
  private Image background,rock;
  public static PhysicsObject link;
  public Timer t,enemyTimer,puTimer,obstacleTimer;
  private boolean editing,paused;
  public static ArrayList<Obstacle> obstacleList;
  public static ArrayList<Enemy> enemyList;
  public static ArrayList<PowerUp> puList;
  private int firstX, firstY;
  private Scanner in;
  private Button playAgain;
  private boolean gameOver;
  private PhysicsSim p;
  public int killCounter;
  /** Creates new form MapForm */
  public MapForm(PhysicsSim p)
  {
    initComponents();
    killCounter = 0;
    background = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("background.png"));
    rock = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("rock.png"));
    addKeyListener(this);
    link = new PhysicsObject(250,250,0,0);
    t = new Timer(25,this);
    t.start();
    mapE.setBounds(10,10,84,20);
    editing = false;
    obstacleList = new ArrayList<Obstacle>();
    puList = new ArrayList<PowerUp>();
    enemyList = new ArrayList<Enemy>();
    enemyTimer = new Timer(2000,this);
    enemyTimer.start();
    puTimer = new Timer(20000,this);
    puTimer.start();
    obstacleTimer = new Timer(45000,this);
    obstacleTimer.start();
    playAgain = new Button("Play Again?");
    playAgain.setFont(new Font("Arial",1,25));
    playAgain.setVisible(false);
    playAgain.setBounds(300,265,150,75);
    link.locked = false;
    this.p = p;
    super.add(playAgain);
    super.setFocusable(true);
    addMouseListener(this);
  }
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(background,0,0,800,600,this);
    g.setFont(new Font("Monospaced",1,12));
    g.drawString("Press ESC to pause.", 15,575);
    g.setFont(new Font("Monospaced",1,25));
    g.setColor(Color.BLACK);
    g.drawString("SCORE:" + killCounter, 660, 580);
    for (Obstacle o: obstacleList)
    {
        if (o.getWidth()<20 || o.getHeight()<20){
            int draw = o.getHeight()/10;
            if (draw == 0) draw = 1;
            int drawW = o.getWidth()/10;
            if (drawW == 0)draw = 1;
            for (int i = 0; i<drawW; i++){
                for (int k = 0; k < draw; k++){
                    g.drawImage(rock,o.getX()+i*10,o.getY()+k*10,10,10,this);
                }
            }
        }
        else{
            g.drawImage(rock,o.getX(),o.getY(),o.getWidth(),o.getHeight(),this);
        }
    }
    for (Enemy e: enemyList)
    {
      e.draw(g);
    }
    for (PowerUp p: puList)
    {
      p.draw(g);
    }
    link.draw(g);
    if (paused)
    {
      g.setColor(Color.BLACK);
      g.setFont(new Font("Monospaced",1,80));
      g.drawString("PAUSED.",250,325);
      g.setFont(new Font("Monospaced",1,30));
      g.drawString("Press ESC to resume.",232,360);
    }
    else{}
    if (gameOver){
        link.locked = true;
        g.setFont(new Font("Arial",3,65));
        g.drawString("GAME OVER!", 200,200);
        playAgain.setVisible(true);
        playAgain.addActionListener(this);
    }
    if (killCounter >= 10 && killCounter<30){
        enemyTimer.setDelay(1500);
    }
    else if (killCounter >= 30 && killCounter <40){
        enemyTimer.setDelay(1200);
    }
    else if (killCounter >= 40 && killCounter <50){
        enemyTimer.setDelay(900);
    }
    else if (killCounter >=50){
        enemyTimer.setDelay(600);
    }
    if (killCounter >= 15){
        obstacleTimer.setDelay(20000);
    }
  }
  @Override
  public void update(Graphics g)
  {

    this.paintComponent(g);
  }
  // Process GUI input in this method
  @Override  
  public void actionPerformed(ActionEvent e)
  {
    Random rng = new Random();
    link.update();
    if (e.getSource() == enemyTimer){
        System.out.println("enemy drawn");
        int genX = rng.nextInt(700);
        int genY = rng.nextInt(500);
        for (Obstacle o: obstacleList){
            if (o.contains(genX, genY)){
                genX = rng.nextInt(700);
                genY = rng.nextInt(500);
            }
        }
        enemyList.add(new Enemy(genX,genY,5,rng.nextInt(360)));
    }
    if (e.getSource() == puTimer){
        puList.add(new PowerUp(20+rng.nextInt(750),20+rng.nextInt(550)));
    }
    for (Enemy en: enemyList)
    {
      en.update();
    }
    if (e.getSource() == obstacleTimer){
        obstacleList.add(new Obstacle(rng.nextInt(700),rng.nextInt(500),50+rng.nextInt(50),50+rng.nextInt(50)));
    }
    if (link.health == 0){
        gameOver = true;
        t.stop();
        enemyTimer.stop();
        puTimer.stop();
    }
    if (e.getSource() == playAgain){
        gameOver = false;
        playAgain.setVisible(false);
        p.reset();
    }
    super.repaint();
  }
  
  @Override
  public void keyPressed(KeyEvent e)
  {
    if (e.getKeyCode()== KeyEvent.VK_DOWN) link.down = true;
    if (e.getKeyCode() == KeyEvent.VK_UP) link.up = true;
    if (e.getKeyCode() == KeyEvent.VK_LEFT) { link.left = true;}
    if (e.getKeyCode() == KeyEvent.VK_RIGHT){ link.right = true;}
    if (e.getKeyCode() == KeyEvent.VK_SPACE){ 
      if(link.getHVelocity()>0) link.swordright = true; 
      else if (link.getHVelocity()<0) link.swordleft = true;
      else if (link.getHVelocity()==0) link.swordright = true;
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE && !paused) pauseGame();
    else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && paused) resumeGame();
  }
  public void pauseGame()
  {
    paused = true;
    link.locked = true;
    enemyTimer.stop();
    obstacleTimer.stop();
    puTimer.stop();
    for (Enemy e: enemyList)
    {
      e.savedH = (int)e.getHVelocity();
      e.savedV = (int)e.getVVelocity();
      e.setHVelocity(0);
      e.setVVelocity(0);
    }
  }
  public void resumeGame()
  {
    paused = false;
    link.locked = false;
    enemyTimer.start();
    puTimer.start();
    obstacleTimer.start();
    for (Enemy e: enemyList)
    {
      e.setHVelocity(e.savedH);
      e.setVVelocity(e.savedV);
    }
  }
  @Override
  public void keyReleased(KeyEvent e)
  {
    if (e.getKeyCode() == KeyEvent.VK_LEFT) { link.left = false;}
    if (e.getKeyCode() == KeyEvent.VK_RIGHT){ link.right = false;}
    if (e.getKeyCode() == KeyEvent.VK_UP){ link.up = false;}
    if (e.getKeyCode() == KeyEvent.VK_DOWN){ link.down = false;}
    if (e.getKeyCode() == KeyEvent.VK_SPACE){ link.swordright = false; link.swordleft = false;}
  }
  @Override
  public void keyTyped(KeyEvent e){}
  @Override
  public void mouseExited(MouseEvent e){}
  @Override
  public void mouseEntered(MouseEvent e){}
  @Override
  public void mouseReleased(MouseEvent e){
  if (editing && e.getButton() == MouseEvent.BUTTON1){
    Obstacle drawn = new Obstacle(firstX,firstY,e.getX()-firstX,e.getY()-firstY);
    obstacleList.add(drawn);
    }
  }
  @Override
  public void mousePressed(MouseEvent e){
    firstX = e.getX();
    firstY = e.getY();
    
  }
  @Override
  public void mouseClicked(MouseEvent e){
    System.out.println("(" + e.getX() + "," + e.getY() + ")");
    int removed = -1;
    if (editing && e.getButton() == MouseEvent.BUTTON3){
      for (Obstacle o: obstacleList)
      {
        if (o.contains(e.getX(), e.getY()))removed = obstacleList.indexOf(o); 
      }
      if (removed != -1) obstacleList.remove(removed);
      super.repaint();
    }
  }
  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents()
  {

    mapE = new java.awt.Checkbox();

    mapE.setLabel("Map Editor");
    mapE.addItemListener(new java.awt.event.ItemListener()
    {
      public void itemStateChanged(java.awt.event.ItemEvent evt)
      {
        mapEItemStateChanged(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(mapE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(706, Short.MAX_VALUE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(mapE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(570, Short.MAX_VALUE))
    );
  }// </editor-fold>//GEN-END:initComponents

  private void mapEItemStateChanged(java.awt.event.ItemEvent evt)//GEN-FIRST:event_mapEItemStateChanged
  {//GEN-HEADEREND:event_mapEItemStateChanged
    // TODO add your handling code here:
    editing = !editing;
    if (!editing) super.requestFocus();
  }//GEN-LAST:event_mapEItemStateChanged


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private java.awt.Checkbox mapE;
  // End of variables declaration//GEN-END:variables
}
