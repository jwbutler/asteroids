import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import jwbgl.*;

public class Projectile extends GameObject {
  private static final int SPEED = 800;
  private final int RADIUS = 4;
  public Projectile(Game game, int x, int y, double theta) {
    super(game,
          x,
          y,
          SPEED*Math.cos((-theta+90)*Math.PI/180),
          -SPEED*Math.sin((-theta+90)*Math.PI/180));
    
    updateRect();
  }
  
  public void draw(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(new Color(128,0,128));
    int farX = (int)(x + dx*1000);
    int farY = (int)(y + dy*1000);
    Graphics2D g2 = (Graphics2D)g;
    RenderingHints rh = new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHints(rh);
    g.drawLine(x, y, farX, farY);
    g.setColor(Color.GREEN);
    g.fillOval(rect.getLeft(),rect.getTop(),rect.getWidth(),rect.getHeight());
    g.setColor(oldColor);
  }
  
  public void update() {
    super.update();
    updateRect();
    if (collideWithLeft()) {
      bounceX();
      erraticBounce();
      x = RADIUS;
    } else if (collideWithRight()) {
      bounceX();
      erraticBounce();
      x = game.getContentPane().getWidth() - RADIUS;
    }
    if (collideWithTop()) {
      bounceY();
      erraticBounce();
      y = RADIUS;
    } else if (collideWithBottom()) {
      bounceY();
      erraticBounce();
      y = game.getContentPane().getHeight() - RADIUS;
    }
    
    updateRect();
  }
  public void updateRect() {
    rect = new Rect(x-RADIUS,y-RADIUS,2*RADIUS+1,2*RADIUS+1);
  }

}
