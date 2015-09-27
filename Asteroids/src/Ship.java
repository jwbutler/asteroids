import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import jwbgl.*;

public class Ship extends GameObject {
  private Surface surfaceBase;
  private Surface surfaceRotated;
  /* Defining x and y as the center of the sprite */
  private double theta;
  private boolean rotateSurface;
  private final double ACCELERATION = 1800.0;
  private final double DECELERATION = -1200.0;
  private final double DTHETA = 540.0;
  private final double MAX_VELOCITY = 900;
  private final double SHOT_COOLDOWN = 0.5;
  private int lastShotTick;
  private int shotCooldown;
  
  
  public Ship(Game game, int x, int y) {
    super(game, x, y, 0, 0);
    shotCooldown = (int)(SHOT_COOLDOWN * game.getFPS());
    surfaceBase = new Surface("red_ship.png");
    //surfaceBase = surfaceBase.rotate(-90, 0, 0);
    surfaceBase.setColorkey(Color.WHITE);
    theta = 0.0;
    dx = dy = 0.0;
    lastShotTick = 0;
    rotateSurface = true;
    this.x = x;
    this.y = y;
    update();
  }
  public void accelerate() {
    double rads = (-theta+90)*Math.PI/180;
    dx += Math.cos(rads)*ACCELERATION/game.getFPS();
    dy -= Math.sin(rads)*ACCELERATION/game.getFPS();
    double velocity = Math.sqrt(dx*dx + dy*dy);
    if (velocity > MAX_VELOCITY) {
      double scale_factor = Math.pow(MAX_VELOCITY/velocity, 2);
      dx *= scale_factor;
      dy *= scale_factor;
    }
  }
  public void decelerate() {
    double rads = (-theta+90)*Math.PI/180;
    dx += Math.cos(rads)*DECELERATION/game.getFPS();
    dy -= Math.sin(rads)*DECELERATION/game.getFPS();
    double velocity = Math.sqrt(dx*dx + dy*dy);
    if (velocity > MAX_VELOCITY) {
      double scale_factor = Math.pow(MAX_VELOCITY/velocity, 2);
      dx *= scale_factor;
      dy *= scale_factor;
    }
  }
  
  public void turnLeft() {
    theta -= DTHETA/game.getFPS();
    theta %= 360;
    rotateSurface = true;
  }
  
  public void turnRight() {
    theta += DTHETA/game.getFPS();
    theta %= 360;
    rotateSurface = true;
  }
  
  public void update() {
    super.update();
    if (rotateSurface) {
      //double rads = (-theta+90)*Math.PI/180;
      surfaceRotated = surfaceBase.rotate(theta);
      rotateSurface = false;
    }

    updateRect();
    //System.out.println("("+(int)x + " " + (int)y+")");
    
    if (collideWithLeft()) {
      x = surfaceRotated.getTransparencyRect().getWidth()/2;
      bounceX();
    } else if (collideWithRight()) {
      x = game.getContentPane().getWidth() - surfaceRotated.getTransparencyRect().getWidth()/2;
      bounceX();
    }
    if (collideWithTop()) {
      y = surfaceRotated.getTransparencyRect().getHeight()/2;
      bounceY();
    } else if (collideWithBottom()) {
      y = game.getContentPane().getHeight() - surfaceRotated.getTransparencyRect().getHeight()/2;
      bounceY();
    }
  }
  
  public void draw(Graphics g) {
    int left = ((int)x) - surfaceRotated.getWidth()/2;
    int top = ((int)y) - surfaceRotated.getHeight()/2;
    Graphics2D g2 = (Graphics2D)g;
    RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHints(rh);
    surfaceRotated.draw(g2, left, top);
    
    /* Debugging
    Color oldColor = g.getColor();
    g.setColor(Color.WHITE);
    g.drawRect(rect.getLeft(),  rect.getTop(),  rect.getWidth(),  rect.getHeight());
    g.setColor(Color.RED);
    g.drawRect(left, top, surfaceRotated.getWidth(), surfaceRotated.getHeight());
    g.setColor(oldColor);
    g.drawLine(left, top, left+surfaceRotated.getWidth(), top+surfaceRotated.getHeight());
    g.drawLine(left+surfaceRotated.getWidth(), top, left, top+surfaceRotated.getHeight());*/
  }
  
  public void fireProjectile() {
    double x = this.x, y = this.y;
    double rads = (-theta+90)*Math.PI/180;
    Projectile p = new Projectile(game, (int)x, (int)y, theta);
    while (rect.collideRect(p.getRect())) {
      p.update();
    }

    game.addProjectile(p);
    lastShotTick = game.getTicks();
  }
  public boolean shotOnCooldown(int ticks) {
    return (ticks - lastShotTick <= shotCooldown);
  }
  
  public void stop() { dx = dy = 0; }
  
  public void updateRect() {
    rect = new Rect(
      (int)(x - (surfaceRotated.getTransparencyRect().getWidth()/2)),
      (int)(y - (surfaceRotated.getTransparencyRect().getHeight()/2)),
      surfaceRotated.getTransparencyRect().getWidth(),
      surfaceRotated.getTransparencyRect().getHeight()
    );
  }
  
}