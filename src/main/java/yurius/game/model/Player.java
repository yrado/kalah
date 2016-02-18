package yurius.game.model;

public enum Player
{
  FIRST(0),
  SECOND(1);

  private int index;

  Player(int index)
  {
    this.index = index;
  }

  public int getIndex()
  {
    return index;
  }

  public Player getOther()
  {
    return this == FIRST ? SECOND : FIRST;
  }

}
