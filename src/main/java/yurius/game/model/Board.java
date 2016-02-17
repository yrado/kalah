package yurius.game.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board
{
  public static final int NUM_HOUSE_CELLS  = 6;
  public static final  int NUM_PLAYER_CELLS = NUM_HOUSE_CELLS + 1;
  public static final  int NUM_TOTAL_CELLS  = NUM_PLAYER_CELLS * 2;

  private int[] cells;

  // TODO IR add Javadoc
  public Board(int[] initialHouses)
  {
    if (initialHouses.length != NUM_TOTAL_CELLS)
      throw new IllegalArgumentException(String.format(
          "Board has to be populated with 14 values (7 per player, 6 house values and one store value). Got %d value(s)",
          initialHouses.length));

    cells = Arrays.copyOf(initialHouses, initialHouses.length);
  }


  public Board(Board board)
  {
//    this(board.getHouseState());
    this(board.cells);
  }

  public LastSeedPlacement moveStones(final Player player, final int houseNumber) throws EmptyCellException
  {
    if (houseNumber < 1 || houseNumber > NUM_HOUSE_CELLS)
      throw new IllegalArgumentException(
          String.format("House number must be between %d and %d. Got: %s.", 1, NUM_HOUSE_CELLS, houseNumber));

    int startCellIndex = player.getIndex() * NUM_PLAYER_CELLS + houseNumber - 1;
    int numStones = cells[startCellIndex];

    if (numStones == 0)
      throw new EmptyCellException("Cannot move stones from empty cell");

    cells[startCellIndex] = 0;

    int houseShift = 0;
    int lastCellIndex = 0;
    for (int i = 1; i <= numStones; i++)
    {
      int cellIndex = (startCellIndex + i + houseShift) % cells.length;
      if (houseOfOtherPlayer(player, cellIndex))
      {
        cellIndex = (cellIndex + 1) % cells.length;
        houseShift++;
      }

      cells[cellIndex] = cells[cellIndex] + 1;

      if (isLastCell(numStones, i))
      {
        lastCellIndex = cellIndex;

        if (finishedInEmptyCell(lastCellIndex) && isPlayerHouse(player, lastCellIndex))
        {
          int storeIndex = player.getIndex() * NUM_PLAYER_CELLS + NUM_PLAYER_CELLS - 1;

          int oppositeCellIndex = NUM_TOTAL_CELLS - 2 - lastCellIndex;
          cells[storeIndex] = cells[storeIndex] + cells[lastCellIndex] + cells[oppositeCellIndex];

          cells[lastCellIndex] = 0;
          cells[oppositeCellIndex] = 0;
        }
      }
    }

    moveStonesIfEmptyHouses();

    return getLastStonePlacement(player, lastCellIndex);
  }

  private boolean moveStonesIfEmptyHouses()
  {
    for (Player player : Player.values())
    {
      if (playerHousesEmpty(player))
      {
        moveHouseToStore(player.getOther());
        return true;
      }
    }
    return false;
  }

  static boolean isPlayerHouse(Player player, int cellIndex)
  {
    int houseStartIndex = player.getIndex() * NUM_PLAYER_CELLS;
    return houseStartIndex <= cellIndex && cellIndex < houseStartIndex + NUM_HOUSE_CELLS;
  }

  private void moveHouseToStore(Player player)
  {
    for (int i = player.getIndex() * NUM_PLAYER_CELLS; i < player.getIndex() * NUM_PLAYER_CELLS + NUM_PLAYER_CELLS - 1; i++)
    {
      cells[player.getIndex() * NUM_PLAYER_CELLS + NUM_PLAYER_CELLS - 1] = cells[player.getIndex() * NUM_PLAYER_CELLS + NUM_PLAYER_CELLS - 1] + cells[i];
      cells[i] = 0;
    }

  }

  private boolean playerHousesEmpty(Player player)
  {
    int sum = 0;
    for (int i = player.getIndex() * NUM_PLAYER_CELLS; i < player.getIndex() * NUM_PLAYER_CELLS + NUM_HOUSE_CELLS; i++)
      sum += cells[i];

    return sum == 0;
  }

  private boolean finishedInEmptyCell(int cellIndex)
  {
    return cells[cellIndex] == 1;
  }

  private boolean isLastCell(int numStones, int i)
  {
    return i == numStones;
  }

  private LastSeedPlacement getLastStonePlacement(Player player, int lastCellIndex)
  {

    if (Player.SECOND == player)
      lastCellIndex = (lastCellIndex + NUM_PLAYER_CELLS) % NUM_TOTAL_CELLS;

    int playerStoreIndex = NUM_PLAYER_CELLS - 1;

    if (lastCellIndex < playerStoreIndex)
      return LastSeedPlacement.PLAYER_HOUSE;
    if (lastCellIndex > playerStoreIndex)
      return LastSeedPlacement.OPPONENT_HOUSE;
    return LastSeedPlacement.PLAYER_STORE;
  }

  private boolean houseOfOtherPlayer(final Player player, final int index)
  {
    return index == player.getOther().getStoreIndex();
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Board board = (Board) o;

    return Arrays.equals(cells, board.cells);
  }

  @Override
  public int hashCode()
  {
    return Arrays.hashCode(cells);
  }

  // TODO IR
  @Override
  public String toString()
  {
    return "Board{" + Arrays.toString(cells) + '}';
  }

  public static Board create(yurius.game.service.rest.dto.Board board)
  {
    int[] array = new int[NUM_TOTAL_CELLS];

    for (int i = 0; i < NUM_PLAYER_CELLS; i++)
      // TODO IR optimize, add checks
      array[i] = board.getFirstPlayer().get(i);

    // TODO IR make generic
    for (int i = 0; i < NUM_PLAYER_CELLS; i++)
      array[i + NUM_PLAYER_CELLS] = board.getSecondPlayer().get(i);

    return new Board(array);
  }

  public List<Integer> getPitsPlayer(Player player)
  {
    ArrayList<Integer> result = new ArrayList<>();
    for (int i = 0; i < NUM_PLAYER_CELLS; i++)
      result.add(cells[player.getIndex() * NUM_PLAYER_CELLS + i]);

    return result;
  }

  public boolean allHousesEmpty()
  {
    int sum = 0;
    for (Player player : Player.values())
      for (int i = 0; i < NUM_HOUSE_CELLS; i++)
        sum+=cells[player.getIndex() * NUM_PLAYER_CELLS + i];

    return sum == 0;
  }

  public int getStore(Player first) {
    return cells[first.getIndex()* NUM_PLAYER_CELLS + NUM_HOUSE_CELLS];
  }
}
