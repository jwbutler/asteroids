import jwbgl.*;

public abstract class GameObject {
  protected Rect rect;
  protected Game game;
  protected int x, y;
  protected double dx, dy;
  private final int ERRATIC_BOUNCE_DEGREES = 5;
  
  public GameObject(Game game, int x, int y, double dx, double dy) {
    this.game = game;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }
 
  public boolean collideWithEdge() {
    Rect gameRect = new Rect(game.getContentPane().getBounds());
    return !gameRect.contains(rect);
  }
  
  public boolean collideWithLeft() {
    Rect gameRect = new Rect(game.getContentPane().getBounds()); 
    return (rect.getLeft() < gameRect.getLeft());
  }
  public boolean collideWithRight() {
    Rect gameRect = new Rect(game.getContentPane().getBounds());
    return (rect.getRight() > gameRect.getRight());
  }
  public boolean collideWithTop() {
    Rect gameRect = new Rect(game.getContentPane().getBounds());
    return (rect.getTop() < gameRect.getTop());
  }
  public boolean collideWithBottom() {
    Rect gameRect = new Rect(game.getContentPane().getBounds());
    return (rect.getBottom() > gameRect.getBottom());
  }
  
  /* Erratic bouncing! */
  public void bounceX() {
    dx = -dx;
  }
  public void bounceY() {
    dy = -dy;
  }
  
  public void erraticBounce() {
    double angle = Math.atan2(-dy,dx);
    //System.out.println((int)dx + " " + (int)dy + " " + angle);
    double speed = Math.sqrt(dx*dx + dy*dy);
    double erraticBounceDegrees = game.getRNG().nextDouble()*ERRATIC_BOUNCE_DEGREES*2 - ERRATIC_BOUNCE_DEGREES;
    double erraticBounceRadians = erraticBounceDegrees*Math.PI/180;
    angle += erraticBounceRadians;
    dx = speed*Math.cos(angle);
    dy = speed*-Math.sin(angle);
    //System.out.println(">>>"+(int)dx + " " + (int)dy);
  }
  
  public void update() {
    x += dx/game.getFPS();
    y += dy/game.getFPS();
  }
  public Rect getRect() { return rect; }
}
