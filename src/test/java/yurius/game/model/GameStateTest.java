package yurius.game.model;

import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void testCopyConstructor() throws Exception {

        // GIVEN
        GameState game1 = new GameState(
                "gameId1",
                new BoardState(createList(1, 2), createList(3, 4)),
                Status.FIRST_PLAYER_TURN,
                "message1");

        // WHEN
        GameState game2 = new GameState(game1);
        game1.setGameId("gameId2");
        game1.setStatus(Status.SECOND_PLAYER_TURN);
        game1.setMessage("message2");
        game1.getBoardState().getFirstPlayer().add(10);

        // THEN
        assertThat(game2.getGameId(), is("gameId1"));
        assertThat(game2.getStatus(), is(Status.FIRST_PLAYER_TURN));
        assertThat(game2.getMessage(), is("message1"));
        assertThat(game2.getBoardState().getFirstPlayer(), is(createList(1, 2)));
    }

    private ArrayList<Integer> createList(int... ints) {
        ArrayList<Integer> result = new ArrayList<>(ints.length);
        for (int anInt : ints) {
            result.add(anInt);
        }
        return result;
    }
}