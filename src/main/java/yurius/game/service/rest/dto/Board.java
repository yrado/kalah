package yurius.game.service.rest.dto;

import java.util.ArrayList;
import java.util.List;

import yurius.game.model.Player;

public class Board
{
  public static final int           DEFAULT_STONE_COUNT = 6;
  private             List<Integer> firstPlayer         = new ArrayList<>(yurius.game.model.Board.NUM_PLAYER_CELLS);
  private             List<Integer> secondPlayer        = new ArrayList<>(yurius.game.model.Board.NUM_PLAYER_CELLS);

  public Board()
  {
  }

  private Board(List<Integer> firstPlayer, List<Integer> secondPlayer)
  {
    this.firstPlayer = firstPlayer;
    this.secondPlayer = secondPlayer;
  }

  public List<Integer> getFirstPlayer()
  {
    return firstPlayer;
  }

  public void setFirstPlayer(List<Integer> firstPlayer)
  {
    this.firstPlayer = firstPlayer;
  }

  public List<Integer> getSecondPlayer()
  {
    return secondPlayer;
  }

  public void setSecondPlayer(List<Integer> secondPlayer)
  {
    this.secondPlayer = secondPlayer;
  }

  public static Board create(yurius.game.model.Board board)
  {
    return new Board(board.getPitsPlayer(Player.FIRST), board.getPitsPlayer(Player.SECOND));
  }

  public static Board createDefault()
  {

    return new Board(createDefaultLayout(), createDefaultLayout());
  }

  private static List<Integer> createDefaultLayout()
  {

    ArrayList<Integer> integers = new ArrayList<>(yurius.game.model.Board.NUM_PLAYER_CELLS);
    for (int i = 0; i < yurius.game.model.Board.NUM_HOUSE_CELLS; i++)
    {
      integers.add(DEFAULT_STONE_COUNT);
    }
    integers.add(0);
    return integers;
  }
}
