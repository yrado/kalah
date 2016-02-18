package yurius.game.model;

public class Move
{
  private Player player;
  private int    houseNumber;

  public Move()
  {
  }

  public Move(Player player, int houseNumber)
  {
    this.player = player;
    this.houseNumber = houseNumber;
  }

  public Player getPlayer()
  {
    return player;
  }

  public void setPlayer(Player player)
  {
    this.player = player;
  }

  public int getHouseNumber()
  {
    return houseNumber;
  }

  public void setHouseNumber(int houseNumber)
  {
    this.houseNumber = houseNumber;
  }
}
