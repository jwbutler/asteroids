import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JApplet;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import jwbgl.*;

/* There is a LOT of trig weirdness in this game.
 * Most notably, we're using degrees starting with up = 0 degrees
 * and converting to standard radians.
 * Since the y-axis is flipped in game windows vs the cartesian plane,
 * we're inverting sins and stuff too.
 * How do we make this more logical?
 */

public class Game extends JApplet implements ActionListener, KeyListener, KeyEventDispatcher {
  private final int DEFAULT_WIDTH = 1366;
  private final int DEFAULT_HEIGHT = 768;
  private final int FPS = 50;
  private KeyboardFocusManager focusManager;
  private Ship playerShip;
  private ArrayList<Projectile> projectiles;
  private boolean upPressed, downPressed, leftPressed, rightPressed;
  private int ticks, score;
  private Timer t;
  private Random RNG;
  private JPanel gamePanel;
  private JLabel scoreLabel;
  public void init() {
    setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    //setBackground(Color.BLACK);
    //getContentPane().setBackground(Color.BLACK);
    getContentPane().setLayout(null);
    gamePanel = new GamePanel(this, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    getContentPane().add(gamePanel);
    scoreLabel = new JLabel("0", SwingConstants.RIGHT);
    scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
    scoreLabel.setForeground(Color.GREEN);
    scoreLabel.setPreferredSize(new Dimension(DEFAULT_WIDTH, 24));
    //getContentPane().add(scoreLabel);
    gamePanel.setLayout(new BorderLayout());
    gamePanel.add(scoreLabel, BorderLayout.NORTH);
    focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    focusManager.addKeyEventDispatcher(this);
    addKeyListener(this);
    t = new Timer(1000/FPS, this);
    RNG = new Random();
    upPressed = downPressed = leftPressed = rightPressed = false;
    reset();
    setVisible(true);
  }
  public void reset() {
    ticks = 0;
    score = 0;
    playerShip = new Ship(this, getWidth()/2, getHeight()/2);
    projectiles = new ArrayList<Projectile>();
    t.start();
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    long t1 = System.nanoTime();
    doEvents();
    gamePanel.repaint();
    
    if (ticks % FPS == 0) {
      score += projectiles.size();
      //System.out.println("Score: " + score);
      scoreLabel.setText(""+score);
    }
    long t2 = System.nanoTime();
    //System.out.println(((double)(t2-t1))/1000000);
  }
  
  public void doEvents() {
    // sanity checks
    if (upPressed && downPressed) {
      upPressed = downPressed = false;
    }
    if (leftPressed && rightPressed) {
      leftPressed = rightPressed = false;
    }
    if (upPressed) playerShip.accelerate();
    if (downPressed) playerShip.decelerate();
    if (leftPressed) playerShip.turnLeft();
    if (rightPressed) playerShip.turnRight();
    playerShip.update();
    boolean restartGame = false;
    for (Projectile p : projectiles) {
      p.update();
      if (p.getRect().collideRect(playerShip.getRect())) {
        restartGame = true;
      }
    }
    ticks++;
    if (restartGame) reset();
  }
  
  @Override
  public boolean dispatchKeyEvent(KeyEvent e) {
    switch (e.getID()) {
      case KeyEvent.KEY_PRESSED:
        keyPressed(e);
        return true;
      case KeyEvent.KEY_RELEASED:
        keyReleased(e);
        return true;
      case KeyEvent.KEY_TYPED:
        keyTyped(e);
        return true;
      default:
        return false;
    }
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    
    switch (e.getKeyCode()) {
      case 38: // up
        upPressed = true;
        break;
      case 40: // down
        downPressed = true;
        break;
      case 37: // left
        leftPressed = true;
        break;
      case 39: // right
        rightPressed = true;
        break;
      case 8: // backspace, DEBUG
        reset();
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case 38: // up
        upPressed = false;
        break;
      case 40: // down
        downPressed = false;
        break;
      case 37: // left
        leftPressed = false;
        break;
      case 39: // right
        rightPressed = false;
        break;
      case 32: // spacebar
        if (!playerShip.shotOnCooldown(ticks)) {
          playerShip.fireProjectile();
        }
    }
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    // TODO Auto-generated method stub 
  }
  
  public int getFPS() { return FPS; }
  public void addProjectile(Projectile p) { projectiles.add(p); }
  public int getTicks() { return ticks; }
  public Random getRNG() { return RNG; }
  public ArrayList<Projectile> getProjectiles() { return projectiles; }
  public Ship getPlayerShip() { return playerShip; }
}
