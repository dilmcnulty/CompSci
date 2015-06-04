package physicssim;

import java.awt.Rectangle;

/*
 * File Name: Obstacle.java
 *   Created: May 22, 2015
 *    Author: 
 */

public class Obstacle extends Object
{
  private int xLoc, yLoc, width, height;
  public Obstacle(int x,int y,int w,int h)
  {
    xLoc = x;
    yLoc = y;
    width = w;
    height = h;
  }
  @Override
  public String toString()
  {
    return "(" + xLoc + "," + yLoc + ") :" + width + "," + height;
  }
  public int getX()
  {
    return xLoc;
  }
  public int getY()
  {
    return yLoc;
  }
  public int getWidth()
  {
    return width;
  }
  public int getHeight()
  {
    return height;
  }
  public boolean contains(int x, int y, int x2, int y2)
  {
    Rectangle obs = new Rectangle(xLoc, yLoc, width, height);
    Rectangle check = new Rectangle (x,y,x2,y2);
    if (obs.intersects(check)) return true;
    else return false;
  }
  public boolean contains(int x, int y)
  {
    if ((x>getX() && x<getX()+width) && (y>getY() && y<getY()+height)) return true;
    else return false;
  }
}
