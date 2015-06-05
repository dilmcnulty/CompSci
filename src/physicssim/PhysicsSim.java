package physicssim;

import java.awt.Button;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import static physicssim.MapForm.obstacleList;

/*
 * File Name: PhysicsSim.java
 *   Created: May 8, 2015
 *    Author: 
 */


public class PhysicsSim extends JPanel implements ActionListener
{
  // Declare instance variables here...
  private PhysicsObject test;
  private Image background;
  public static int speed;
  public static MapForm map;
  private JMenu mainMenu;
  private JMenuBar menuBar;
  private JMenuItem loadMap, saveMap;
  private JFileChooser fc;
  private Scanner in;
  private File currentFile;
  private String mapText;
  private Button start;
  // Constructor
  public PhysicsSim(int w, int h, JFrame f)
  {
    super.setOpaque(true);
    super.setPreferredSize(new Dimension(w, h));
    super.setBackground(new Color(225, 225, 225));
    super.setLayout(null);
    speed = 7;
    mainMenu = new JMenu();
    mainMenu.setText("File  ");
    background = Toolkit.getDefaultToolkit().getImage(PhysicsSim.class.getResource("background.png"));
    
    loadMap = new JMenuItem("Load Map...");
    loadMap.addActionListener(this);
    saveMap = new JMenuItem("Save Map...");
    saveMap.addActionListener(this);
    mainMenu.add(loadMap);
    mainMenu.add(saveMap);
    start = new Button("START!");
    start.setBounds(250,200,250,75);
    start.setFont(new Font("Arial",1,50));
    start.addActionListener(this);
    super.add(start);
    
    menuBar = new JMenuBar();
    menuBar.add(mainMenu);
    f.setJMenuBar(menuBar);
    
    
    fc = new JFileChooser(new File("."));
    fc.removeChoosableFileFilter(fc.getFileFilter());
    fc.addChoosableFileFilter(new FileFilter()
    {
      @Override
      public String getDescription()
      {
        return "Plain Text Files (.txt)";
      }
      @Override
      public boolean accept(File f)
      {
        return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
      }
    });
  }
  
  // Perform any custom painting (if necessary) in this method
  @Override  
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(background,0,0,super.getWidth(),super.getHeight(),this);
    
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
    if (e.getSource() == saveMap) saveMap();
    if (e.getSource() == loadMap) loadMap();
    if (e.getSource() == start){
        mapText = "";
        map = new MapForm(this);
        map.setBounds(0,0,super.getWidth(),super.getHeight());
        super.add(map);
        loadMap();
        map.pauseGame();
        /*try{
            Scanner in = new Scanner(new FileReader("C:\\Users\\Kingdom Ent-Dave\\Documents\\NetBeansProjects\\PhysicsSim\\src\\physicssim\\map1.txt"));
        }
        catch (FileNotFoundException ex){
            System.out.println("Error: File Not Found!");
        }
        while(in.hasNext()){
            mapText+=in.next();
        }
        in.close();*/
        for (Obstacle o: obstacleList){
        mapText+=o.getX() + " ";
        mapText+=o.getY() + " ";
        mapText+=o.getWidth() + " ";
        mapText+=o.getHeight() + " ";
        }
        start.setVisible(false);
        start.setFocusable(false);
    }
    super.repaint();
  }
  public void reset()
  {
      this.remove(map);
      map = new MapForm(this);
      map.setBounds(0,0,super.getWidth(),super.getHeight());
      super.add(map);
      map.requestFocus();
      map.t.restart();
      map.enemyTimer.restart();
      map.puTimer.restart();
  }
  public int saveMap()
  {
    for (Obstacle o: obstacleList){
      mapText+=o.getX() + " ";
      mapText+=o.getY() + " ";
      mapText+=o.getWidth() + " ";
      mapText+=o.getHeight() + " ";
    }
    System.out.println(mapText);
    int val = fc.showSaveDialog(this);
    if (val != JFileChooser.APPROVE_OPTION) return -1;
    else
    { 
      String name = "";
      try
      {
        File chosen = fc.getSelectedFile();
        name = chosen.getName();
        if (!name.endsWith(".txt")) 
        {
          chosen = new File(""+fc.getSelectedFile()+".txt");
          name += ".txt";
        }
        if (chosen.exists() && !chosen.equals(currentFile))
        {
          int answer = JOptionPane.showConfirmDialog(this, "Overwrite "+chosen.getName()+"?", "Warning!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
          if (answer != JOptionPane.YES_OPTION)
          {            
            return saveMap();
          }
        }
        currentFile = chosen;
        Scanner in = new Scanner(mapText);
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(chosen)));
        while (in.hasNextLine())
        {
          String s = in.nextLine();
          System.out.println(s);
          out.print(s);
          out.println();
        }
        out.close();
      }
      catch (IOException e)
      {
        JOptionPane.showMessageDialog(this, "Cannot save as "+name+" because this file\nis being used concurrently by another process.", "File Save Error:", JOptionPane.ERROR_MESSAGE);
        return -1;
      }
    }
    return 0;
  }
  public void loadMap()
  {
      obstacleList = new ArrayList<Obstacle>();
    /*try{
      in = new Scanner(f);
    }
    catch (FileNotFoundException e){}
    while (in.hasNextInt()){
      obstacleList.add(new Obstacle(in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt()));
    }*/
    if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
      String name;
      File chosen = null;
      try
      {
        chosen = fc.getSelectedFile();
        name = chosen.getName();
        if (!name.endsWith(".txt")) 
        {
          chosen = new File(""+fc.getSelectedFile()+".txt");
          name += ".txt";
        }
        Scanner in = new Scanner(chosen);
        
        while (in.hasNextInt())
        {
          obstacleList.add(new Obstacle(in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt()));
        }
        in.close();
        this.currentFile = fc.getSelectedFile();
        super.repaint();
      }
      catch (FileNotFoundException e)
      {
        JOptionPane.showMessageDialog(this, "The file "+chosen+"\nyou selected does not exist.", "File Not Found Error:", JOptionPane.ERROR_MESSAGE);
      }
      
    }
  }
  public void loadMap(String s)
  {
      Scanner in = new Scanner(s);
      while (in.hasNextInt())
        {
          obstacleList.add(new Obstacle(in.nextInt(),in.nextInt(),in.nextInt(),in.nextInt()));
        }
        in.close();
  }
  //<editor-fold defaultstate="collapsed" desc="--This method will launch your application--">
  public static void main(String[] args)
  {
    JFrame.setDefaultLookAndFeelDecorated(false);
    JFrame fr = new JFrame("Application: PhysicsSim");
    fr.setContentPane(new PhysicsSim(800, 600, fr));
    fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fr.setLocation(10, 10);
    fr.setResizable(false);
    fr.pack();
    fr.setVisible(true);  
  }
  //</editor-fold>  

}
