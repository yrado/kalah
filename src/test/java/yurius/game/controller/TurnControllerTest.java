package yurius.game.controller;

import org.junit.Test;
import yurius.game.model.GameState;
import yurius.game.model.Player;
import yurius.game.model.Status;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class TurnControllerTest {
    private TurnController turnController;
    private GameState result;

    @Test
    public void testTakeTurn_turnGoesFromFirstToSecondPlayer() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 1);

        //THEN
        assertThat(result.getStatus(), is(Status.SECOND_PLAYER_TURN));
    }

    @Test
    public void testTakeTurn_turnGoesFromSecondToFirstPlayer() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0}));

        // WHEN
        result = turnController.takeTurn(Player.SECOND, 1);

        //THEN
        assertThat(result.getStatus(), is(Status.FIRST_PLAYER_TURN));
    }

    @Test
    public void testTakeTurn_turnStaysWithFirstPlayerWhenLastSeedGoesToStore() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 6);

        //THEN
        assertThat(result.getStatus(), is(Status.FIRST_PLAYER_TURN));
    }

    @Test
    public void testTakeTurn_gameFinishesFirstPlayerHasNoMoreStonesInHouses() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 6);

        //THEN
        assertThat(result.getStatus(), is(Status.GAME_OVER));
    }

    @Test
    public void testTakeTurn_gameOverFirstPlayerTakesTurn() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 1);

        //THEN
        assertThat(result.getStatus(), is(Status.GAME_OVER));
    }

    @Test
    public void testTakeTurn_gameOverSecondPlayerTakesTurn() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1}));

        // WHEN
        result = turnController.takeTurn(Player.SECOND, 1);

        //THEN
        assertThat(result.getStatus(), is(Status.GAME_OVER));
    }

    @Test
    public void testTakeTurn_gameOverFirstPlayerWins() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 6);

        //THEN
        assertThat(result.getMessage(), containsString("First player won!"));
    }

    @Test
    public void testTakeTurn_gameOverSecondPlayerWins() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 6);

        //THEN
        assertThat(result.getMessage(), containsString("Second player won!"));
    }

    @Test
    public void testTakeTurn_gameOverDrawGame() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 6);

        //THEN
        assertThat(result.getMessage(), containsString("draw game!"));
    }

    @Test
    public void testTakeTurn_turnStaysOnEmptyCellMove() throws Exception {
        // GIVEN
        turnController = new TurnController("id", new Board(new int[]{1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}));

        // WHEN
        result = turnController.takeTurn(Player.FIRST, 2);

        //THEN
        assertThat(result.getStatus(), is(Status.FIRST_PLAYER_TURN));
    }

}