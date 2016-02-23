package yurius.game.controller;

import org.junit.Test;
import yurius.game.controller.exceptions.EmptyCellException;
import yurius.game.model.BoardState;
import yurius.game.model.Player;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class BoardTest
{
  private final IntFactory intFactory = new IntFactory(Constants.TOTAL_CELLS_COUNT);

  private Board board;

  @Test
  public void testMoveStones_oneStone() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 0, 1));

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(1, 0, 0, 1, 2))));
  }

  @Test
  public void testMoveStones_twoStones() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 0, 2));

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(1, 0, 0, 1, 2, 2, 2))));
  }

  @Test
  public void testMoveStones_storeIsPopulated() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 5, 1));

    // WHEN
    board.moveStones(Player.FIRST, 6);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(1, 5, 0, 6, 2))));
  }

  @Test
  public void testMoveStones_fullCircle() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 0, 14));

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(2, 0, 1, 1, 3, 13, 1))));
  }

  @Test
  public void testMoveStones_twoFullCircles() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 0, 28));

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(3, 0, 2, 1, 4, 2, 4, 13, 1))));
  }

  @Test
  public void testMoveStones_houseOfSecondPlayerIsPopulated() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1});

    // WHEN
    board.moveStones(Player.FIRST, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{1, 1, 1, 1, 1, 0, 2, 2, 1, 1, 1, 1, 1, 1})));
  }

  @Test
  public void testMoveStones_storeOfSecondPlayerIsNotPopulated() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 5, 8));

    // WHEN
    board.moveStones(Player.FIRST, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{2, 1, 1, 1, 1, 0, 2, 2, 2, 2, 2, 2, 2, 1})));
  }

  @Test
  public void testMoveStones_secondPlayer_storeIsPopulated() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1));

    // WHEN
    board.moveStones(Player.SECOND, 6);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(1, 12, 0, 13, 2))));
  }

  @Test
  public void testMoveStones_secondPlayer_houseOfFirstPlayerIsPopulated() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 12, 2));

    // WHEN
    board.moveStones(Player.SECOND, 6);

    //THEN
    assertThat(board, is(new Board(intFactory.getInts(1, 12, 0, 13, 2, 0, 2))));
  }

  @Test
  public void testMoveStones_secondPlayer_storeOfFirstPlayerIsNotPopulated() throws Exception
  {
    // GIVEN
    board = new Board(intFactory.getInts(1, 12, 8));

    // WHEN
    board.moveStones(Player.SECOND, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1, 0, 2})));
  }

  @Test
  public void testMoveStones_houseNumberLessThanOne() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    try
    {
      board.moveStones(Player.SECOND, 0);
      fail();
    }
    //THEN
    catch (IllegalArgumentException e)
    {
      assertThat(e.getMessage(), is("House number must be between 1 and 6. Got: 0."));
    }
  }

  @Test
  public void testMoveStones_houseNumberMoreThanSix() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    try
    {
      board.moveStones(Player.SECOND, 7);
      fail();
    }
    //THEN
    catch (IllegalArgumentException e)
    {
      assertThat(e.getMessage(), is("House number must be between 1 and 6. Got: 7."));
    }
  }

  @Test
  public void testConstructor_numberOfElementsLessThen14() throws Exception
  {
    // WHEN
    try
    {
      new Board(new int[]{0});
      fail();
    }
    //THEN
    catch (IllegalArgumentException e)
    {
      assertThat(
          e.getMessage(),
          is("Board has to be populated with 14 values (7 per player, 6 house values and one store value). Got 1 value(s)"));
    }
  }

  @Test
  public void testConstructor_numberOfElementsMoreThan14() throws Exception
  {
    // WHEN
    try
    {
      new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
      fail();
    }
    //THEN
    catch (IllegalArgumentException e)
    {
      assertThat(
          e.getMessage(),
          is("Board has to be populated with 14 values (7 per player, 6 house values and one store value). Got 15 value(s)"));
    }
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsPlayersHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0});

    // WHEN
    boolean isPlayerStore = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(isPlayerStore, is(false));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsPlayerStore() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0});

    // WHEN
    boolean isPlayerStore = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(isPlayerStore, is(true));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsOpponentsHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    boolean isPlayerStore = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(isPlayerStore, is(false));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsNotOpponentsStore() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    boolean isPlayerStore = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(isPlayerStore, is(false));
  }

  @Test
  public void testMoveStones_secondPlayer_lastSeedPlacementIsOpponentsHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0});

    // WHEN
    boolean isPlayerStore = board.moveStones(Player.SECOND, 1);

    //THEN
    assertThat(isPlayerStore, is(false));
  }

  @Test
  public void testMoveStones_canNotMoveStonesFromEmptyCells() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    try
    {
      board.moveStones(Player.FIRST, 1);
      fail();
    }
    //THEN
    catch (EmptyCellException e)
    {
      assertThat(
          e.getMessage(),
          is("Cannot move stones from empty cell"));
    }
  }

  @Test
  public void testMoveStones_lastSeedIsPlacedOnOwnEmptyHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0});

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(new int[]{0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0})));
  }

  @Test
  public void testMoveStones_lastSeedIsPlacedOnOpponentsEmptyHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    board.moveStones(Player.FIRST, 5);

    //THEN
    assertThat(board, is(new Board(new int[]{0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0})));
  }

  @Test
  public void testMoveStones_lastSeedIsPlacedInOwnEmptyStore() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0});

    // WHEN
    board.moveStones(Player.FIRST, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0})));
  }

  @Test
  public void testMoveStones_firstPlayerHasHoMoreSeedsInHouses() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 0});

    // WHEN
    board.moveStones(Player.FIRST, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 6})));
  }

  @Test
  public void testMoveStones_secondPlayerHasHoMoreSeedsInHouses() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0});

    // WHEN
    board.moveStones(Player.SECOND, 6);

    //THEN
    assertThat(board, is(new Board(new int[]{0, 0, 0, 0, 0, 0, 6, 0, 0, 0, 0, 0, 0, 1})));
  }

  @Test
  public void testMoveStones_firstPlayerTurnSecondPlayerHasHoMoreSeedsInHouses() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0});

    // WHEN
    board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(board, is(new Board(new int[]{0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0})));
  }

  @Test
  public void testIsPlayerHouse() throws Exception
  {
    // EXPECT
    assertThat(Board.isPlayerHouse(0, Player.FIRST), is(true));
    assertThat(Board.isPlayerHouse(5, Player.FIRST), is(true));
    assertThat(Board.isPlayerHouse(6, Player.FIRST), is(false));
    assertThat(Board.isPlayerHouse(7, Player.FIRST), is(false));
    assertThat(Board.isPlayerHouse(12, Player.FIRST), is(false));
    assertThat(Board.isPlayerHouse(13, Player.FIRST), is(false));

    assertThat(Board.isPlayerHouse(0, Player.SECOND), is(false));
    assertThat(Board.isPlayerHouse(5, Player.SECOND), is(false));
    assertThat(Board.isPlayerHouse(6, Player.SECOND), is(false));
    assertThat(Board.isPlayerHouse(7, Player.SECOND), is(true));
    assertThat(Board.isPlayerHouse(12, Player.SECOND), is(true));
    assertThat(Board.isPlayerHouse(13, Player.SECOND), is(false));
  }

  @Test
  public void testCreate_OK() throws Exception
  {
    // EXPECT
    Board.create(new BoardState(Arrays.asList(1,2,3,4,5,6,7), Arrays.asList(1,2,3,4,5,6,7)));
  }

  @Test
  public void testCreate_firstArrayShorter() throws Exception
  {
    // EXPECT
    try
    {
      Board.create(new BoardState(Arrays.asList(1,2,3,4,5,6), Arrays.asList(1,2,3,4,5,6,7)));
      fail();
    }
    catch (Exception ignored)
    {
    }
  }

  @Test
  public void testCreate_secondArrayLonger() throws Exception
  {
    // EXPECT
    try
    {
      Board.create(new BoardState(Arrays.asList(1,2,3,4,5,6,7), Arrays.asList(1,2,3,4,5,6,7,8)));
      fail();
    }
    catch (Exception ignored)
    {
    }
  }
}