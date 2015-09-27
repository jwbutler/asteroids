import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
  private Game game;
  public GamePanel(Game game, int width, int height) {
    this.game = game;
    setSize(new Dimension(width, height));
    setBackground(Color.BLACK);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.setColor(Color.WHITE);
    for (Projectile p: game.getProjectiles()) p.draw(g);
    game.getPlayerShip().draw(g);
  }

}
