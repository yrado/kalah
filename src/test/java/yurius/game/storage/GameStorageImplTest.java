package yurius.game.storage;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import yurius.game.model.BoardState;
import yurius.game.model.GameState;
import yurius.game.model.Status;

public class GameStorageImplTest {
	private GameStorageImpl gameStorage;

	@Before
	public void setUp() throws Exception {
		gameStorage = new GameStorageImpl();
	}

	@Test
	public void testSaveAndRetrieve() throws Exception {
		// GIVEN
		GameState gameState = new GameState("id1", new BoardState(), Status.WAITING_FOR_SECOND_PLAYER, "");
		assertThat(gameStorage.retrieve("id1").isPresent(), is(false));

		// WHEN
		gameStorage.save(gameState);

		// THEN
		assertThat(gameStorage.retrieve("id1").get(), is(gameState));
	}

	@Test
	public void testFindWaitingGame_gameExists() throws Exception {
		// GIVEN
		GameState gameState1 = new GameState("id1", new BoardState(), Status.FIRST_PLAYER_TURN, "");
		GameState gameState2 = new GameState("id2", new BoardState(), Status.WAITING_FOR_SECOND_PLAYER, "");
		gameStorage.save(gameState1);
		gameStorage.save(gameState2);

		// WHEN
		Optional<GameState> waitingGame = gameStorage.findWaitingGame();

		// THEN
		assertThat(waitingGame.get().getGameId(), is("id2"));
	}

	@Test
	public void testFindWaitingGame_gameDoesNotExist() throws Exception {
		// WHEN
		Optional<GameState> waitingGame = gameStorage.findWaitingGame();

		// THEN
		assertThat(waitingGame.isPresent(), is(false));
	}

}