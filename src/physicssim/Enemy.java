package physicssim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

/*
 * File Name: Enemy.java
 *   Created: May 27, 2015
 *    Author: 
 */

public class Enemy extends PhysicsObject
{
  private Image enemy1, enemy2, enemy3;
  private boolean is1, is2, is3;
  private Random rng;
  public int savedH, savedV;
  private int trackCounter;
  public Enemy(int x, int y, double v, double d)
  {
    super(x,y,v,d);
    vertvelocity = -1*v;
    rng = new Random();
    int chosen = rng.nextInt(3);
    if (chosen == 0) is1 = true;
    if (chosen == 1) is2 = true;
    if (chosen == 2) is3 = true;
    enemy1 = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("enemy1.png"));
    enemy2 = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("enemy2.png"));
    enemy3 = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("enemy3.png"));
    savedH = 0;
    savedV = 0;
    trackCounter = 0;
  }
  @Override
  public void draw(Graphics g)
  {
    if (is1) g.drawImage(enemy1, (int)getX(), (int)getY(), size-8, size-8, null);
    else if (is2) g.drawImage(enemy2, (int)getX(), (int)getY(), size-8, size-8, null);
    else if (is3) g.drawImage(enemy3, (int)getX(), (int)getY(), size-8, size-8, null);
  }
  @Override
  public void update()
  {
      if (trackCounter < 40){
          trackCounter++;
      }
      else if (trackCounter >= 40){
        direction = -90+ Math.toDegrees(1.5 * Math.PI - Math.atan2(yLoc - MapForm.link.getY(),xLoc - MapForm.link.getX()));
      }
    xLoc = xLoc + horizvelocity*Math.cos(Math.toRadians(direction));
    yLoc = yLoc + vertvelocity*Math.sin(Math.toRadians(direction));
    checkCollision();
  }
  @Override
  public void checkCollision()
  {
    for(Obstacle o: PhysicsSim.map.obstacleList){
    if (o.contains((int)(xLoc+horizvelocity),(int)(yLoc),size,size)){
      direction+=180;
      if (xLoc+size < o.getX()){
          xLoc = o.getX()-25;
      }
      else if (xLoc > o.getX() + o.getWidth()){
          xLoc = o.getX()+o.getWidth()+10;
      }
    }
    if (o.contains((int)(xLoc),(int)(yLoc+vertvelocity),size,size)){
      direction+=180;
      if (yLoc<o.getY()){
          yLoc = o.getY()-10;
      }
      else if (yLoc>o.getY()+o.getHeight()){
          yLoc = o.getY()+o.getHeight()+10;
      }
    }
    }
  }
  public boolean contains(int x, int y, int x2, int y2)
  {
    Rectangle obs = new Rectangle((int)xLoc, (int)yLoc, size, size);
    Rectangle check = new Rectangle (x,y,x2,y2);
    if (obs.intersects(check)) return true;
    else return false;
  }
}
