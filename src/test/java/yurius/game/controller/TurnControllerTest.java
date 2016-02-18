package yurius.game.controller;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import yurius.game.model.Player;

public class TurnControllerTest
{
  private TurnController turnController;
  private TurnResult result;

  @Test
  public void testTakeTurn_turnGoesFromFirstToSecondPlayer() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 1);

    //THEN
    assertThat(result.getStatus(), is(TurnStatus.SECOND_PLAYER_TURN));
  }

  @Test
  public void testTakeTurn_turnGoesFromSecondToFirstPlayer() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}));

    // WHEN
    result = turnController.takeTurn(Player.SECOND, 1);

    //THEN
    assertThat(result.getStatus(), is(TurnStatus.FIRST_PLAYER_TURN));
  }

  @Test
  public void testTakeTurn_turnStaysWithFirstPlayerWhenLastSeedGoesToStore() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 6);

    //THEN
    assertThat(result.getStatus(), is(TurnStatus.FIRST_PLAYER_TURN));
  }

  @Test
  public void testTakeTurn_gameFinishesFirstPlayerHasNoMoreStonesInHouses() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 6);

    //THEN
    assertThat(result.getStatus(), is(TurnStatus.GAME_OVER));
  }

  @Test
  public void testTakeTurn_gameOverFirstPlayerTakerTurn() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 1);

    //THEN
    assertThat(result.getStatus(), is(TurnStatus.GAME_OVER));
  }

  @Test
  public void testTakeTurn_gameOverFirstPlayerWins() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 1}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 1);

    //THEN
    assertThat(result.getMessage(), containsString("First player won!"));
  }

  @Test
  public void testTakeTurn_gameOverSecondPlayerWins() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 2}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 1);

    //THEN
    assertThat(result.getMessage(), containsString("Second player won!"));
  }

  @Test
  public void testTakeTurn_gameOverDrawGame() throws Exception
  {
    // GIVEN
    turnController = new TurnController(new Board(new int[]{0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 2}));

    // WHEN
    result = turnController.takeTurn(Player.FIRST, 1);

    //THEN
    assertThat(result.getMessage(), containsString("draw game!"));
  }

}