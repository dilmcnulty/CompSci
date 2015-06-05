/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physicssim;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

/**
 *
 * @author Kingdom Ent-Dave
 */
public class PowerUp {
    private boolean is1, is2, is3;
    private int xLoc, yLoc;
    private Image heart, speed, invincibility;
    private Random type;
    private int pick;
    public PowerUp(int x,int y)
    {
        xLoc = x;
        yLoc = y;
        heart = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("heart.png"));
        speed = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("lightning.png"));
        type = new Random();
        pick = type.nextInt(2);
        if (pick == 0) is1 = true;
        else if (pick == 1) is2 = true;
    }
    public int getX()
    {
        return xLoc;
    }
    public int getY()
    {
        return yLoc;
    }
    public String getType()
    {
        if (is1) return "health";
        else if (is2) return "speed";
        else return "";
    }
    public void draw(Graphics g)
    {
        if (is1) g.drawImage(heart, xLoc, yLoc,25,25, null);
        else if (is2) g.drawImage(speed, xLoc, yLoc,25,25, null);
    }
    public boolean contains(int x, int y, int x2, int y2)
    {
    Rectangle obs = new Rectangle((int)xLoc, (int)yLoc, 25, 25);
    Rectangle check = new Rectangle (x,y,x2,y2);
    if (obs.intersects(check)) return true;
    else return false;
    }
}
