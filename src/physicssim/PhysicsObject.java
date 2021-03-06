package physicssim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/*
 * File Name: PhysicsObject.java
 *   Created: May 8, 2015
 *    Author: 
 */

public class PhysicsObject
{
  protected double xLoc, yLoc, horizvelocity, vertvelocity,direction;
  public boolean up, down, left, right, space, drawn, drawn1, drawn2,swordleft,swordright,locked,isFast;
  private boolean invincible;
  public int size;
  private int invinciblecounter, blinkcounter, powercounter;
  public int health;
  private Image link, link2, stableft, stabright;

  public PhysicsObject(double x, double y, double v, double d)
  {
    xLoc = x;
    yLoc = y;
    horizvelocity = v;
    direction = d%360;
    size = 45;
    health = 100;
    drawn1 = true;
    drawn2 = false;
    invincible = false;
    invinciblecounter = 0;
    locked = false;
    isFast = false;
  }
  public double getX()
  {
    return xLoc;
  }
  public double getY()
  {
    return yLoc;
  }
  public double getHVelocity()
  {
    return horizvelocity;
  }
  public double getVVelocity()
  {
    return vertvelocity;
  }
  public double getDirection()
  {
    return direction;
  }
  public void setHVelocity(double d)
  {
    horizvelocity = d;
  }
  public void setVVelocity(double d)
  {
    vertvelocity = d;
  }
  public void setDirection(double d)
  {
    direction = d;
  }
  public void draw(Graphics g)
  {
    //g.setColor(Color.BLACK);
    //g.fillRect((int)xLoc,(int)yLoc,10,10);
    link = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("link.png"));
    link2 = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("link2.png"));
    stableft = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("swordleft.png"));
    stabright = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("swordright.png"));
    if (drawn1) g.drawImage(link, (int)xLoc, (int)yLoc, size, size, null);
    else if (drawn2) g.drawImage(link2, (int)xLoc,(int)yLoc,size,size,null);
    else if (swordright){
      g.drawImage(stabright,(int)xLoc,(int)yLoc,size+20,size,null);
    }
    else if (swordleft){
      g.drawImage(stableft,(int)xLoc-20,(int)yLoc,size+20,size,null);
    }
    g.drawRect(685,10,101,25);
    g.setColor(Color.RED);
    g.fillRect(686,11,health,24);
  }
  public void update()
  {
    xLoc = xLoc + horizvelocity*Math.cos(Math.toRadians(direction));
    yLoc = yLoc + vertvelocity*Math.sin(Math.toRadians(90));  
    if (yLoc + 25 >= 600){ horizvelocity = 0; yLoc = 600-25;}
    if (!locked){
      if (left){ horizvelocity = -1*PhysicsSim.speed;}
      if (right){ horizvelocity = PhysicsSim.speed;}
      if (left&&right || !left&&!right) horizvelocity = 0;
      if (up) { vertvelocity = -1*PhysicsSim.speed;}
      if (down){vertvelocity = PhysicsSim.speed;}
      if (up&&down || !up&&!down) vertvelocity = 0;
      if(horizvelocity!=0 || vertvelocity!=0){
      drawn1 = !drawn1;
      drawn2 = !drawn2;
      }
      if(horizvelocity==0 && vertvelocity ==0) drawn1 = true;
      if (drawn1&&drawn2) drawn1 = false;
      if (swordright){drawn1=false; drawn2 = false;}
      if (swordleft) {drawn1=false; drawn2 = false;}
      if (invincible && blinkcounter < 10){
          blinkcounter++;
      }
      else if (invincible && blinkcounter >= 10){
          blinkcounter = 0;
          drawn1 = !drawn1;
      }
      if (invincible && invinciblecounter < 80){
          invinciblecounter++;
      }
      else if (invincible && invinciblecounter>=80){
          invinciblecounter = 0;
          invincible = false;
          drawn1 = true;
      }
      if (isFast && powercounter<280){
          powercounter++;
      }
      else if (isFast && powercounter>=280){
          isFast = false;
          powercounter = 0;
      }
      if (!isFast) PhysicsSim.speed = 7;
    }
    else if (locked){
      horizvelocity = 0;
      vertvelocity = 0;
    }
    checkCollision();
  }
  public void checkCollision()
  {
    int removed = -1;
    int premoved = -1;
    boolean r = false;
    for(Obstacle o: PhysicsSim.map.obstacleList){
      if (o.contains((int)(xLoc+horizvelocity),(int)(yLoc+vertvelocity),size,size)){
        xLoc-=(horizvelocity*2); 
        yLoc-=(2*vertvelocity); 
        horizvelocity = 0; 
        vertvelocity = 0;
      }
    }
    for (Enemy e: PhysicsSim.map.enemyList){
      r = false;
      if (e.contains((int)(xLoc+horizvelocity/2), (int)(yLoc+vertvelocity/2), size, size)){
        if (swordright && e.getX()-getX()>0)
        {
          removed = PhysicsSim.map.enemyList.indexOf(e);
          r = true;
        }
        else if (swordleft && e.getX()-getX()<0)
        {
          removed = PhysicsSim.map.enemyList.indexOf(e);
          r = true;
        }
        if(!r && !invincible){
          health-=10;
          xLoc-=horizvelocity*5;
          invincible = true;
        }
        else if (invincible){}
      }
    }
    for (PowerUp p: PhysicsSim.map.puList){
        if (p.contains((int)(xLoc+horizvelocity/2), (int)(yLoc+vertvelocity/2), size, size))
        {
            if(p.getType().equals("health")){
                if (health>50) health = 100;
                else if (health<50) health += 50;
                premoved = PhysicsSim.map.puList.indexOf(p);
            }
            else if (p.getType().equals("speed")){
                PhysicsSim.speed = 12;
                isFast = true;
                premoved = PhysicsSim.map.puList.indexOf(p);
            }
        }
    }
    if (removed!=-1){
      PhysicsSim.map.enemyList.remove(removed);
      PhysicsSim.map.killCounter++;
    }
    if (premoved!=-1){
        PhysicsSim.map.puList.remove(premoved);
    }
  }
}
