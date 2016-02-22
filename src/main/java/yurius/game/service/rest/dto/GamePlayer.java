package yurius.game.service.rest.dto;

import yurius.game.model.Player;

public class GamePlayer
{
  private String gameId;
  private Player player;

  public GamePlayer()
  {
  }

  public GamePlayer(String gameId, Player player)
  {
    this.gameId = gameId;
    this.player = player;
  }

  public String getGameId()
  {
    return gameId;
  }

  public void setGameId(String gameId)
  {
    this.gameId = gameId;
  }

  public Player getPlayer()
  {
    return player;
  }

  public void setPlayer(Player player)
  {
    this.player = player;
  }
}

