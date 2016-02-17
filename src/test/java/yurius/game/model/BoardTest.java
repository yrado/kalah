package yurius.game.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BoardTest
{
  private final IntFactory intFactory = new IntFactory(Board.NUM_TOTAL_CELLS);

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
    LastSeedPlacement result = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(result, is(LastSeedPlacement.PLAYER_HOUSE));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsPlayerStore() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{6, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0});

    // WHEN
    LastSeedPlacement result = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(result, is(LastSeedPlacement.PLAYER_STORE));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsOpponentsHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    LastSeedPlacement result = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(result, is(LastSeedPlacement.OPPONENT_HOUSE));
  }

  @Test
  public void testMoveStones_lastSeedPlacementIsNotOpponentsStore() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{13, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});

    // WHEN
    LastSeedPlacement result = board.moveStones(Player.FIRST, 1);

    //THEN
    assertThat(result, is(LastSeedPlacement.PLAYER_HOUSE));
  }

  @Test
  public void testMoveStones_secondPlayer_lastSeedPlacementIsOpponentsHouse() throws Exception
  {
    // GIVEN
    board = new Board(new int[]{0, 0, 0, 0, 0, 0, 0, 7, 0, 0, 0, 0, 0, 0});

    // WHEN
    LastSeedPlacement result = board.moveStones(Player.SECOND, 1);

    //THEN
    assertThat(result, is(LastSeedPlacement.OPPONENT_HOUSE));
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
    assertThat(Board.isPlayerHouse(Player.FIRST, 0), is(true));
    assertThat(Board.isPlayerHouse(Player.FIRST, 5), is(true));
    assertThat(Board.isPlayerHouse(Player.FIRST, 6), is(false));
    assertThat(Board.isPlayerHouse(Player.FIRST, 7), is(false));
    assertThat(Board.isPlayerHouse(Player.FIRST, 12), is(false));
    assertThat(Board.isPlayerHouse(Player.FIRST, 13), is(false));

    assertThat(Board.isPlayerHouse(Player.SECOND, 0), is(false));
    assertThat(Board.isPlayerHouse(Player.SECOND, 5), is(false));
    assertThat(Board.isPlayerHouse(Player.SECOND, 6), is(false));
    assertThat(Board.isPlayerHouse(Player.SECOND, 7), is(true));
    assertThat(Board.isPlayerHouse(Player.SECOND, 12), is(true));
    assertThat(Board.isPlayerHouse(Player.SECOND, 13), is(false));
  }
}