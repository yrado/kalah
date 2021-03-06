package yurius.game.storage;

import org.junit.Before;
import org.junit.Test;
import yurius.game.model.BoardState;
import yurius.game.model.GameState;
import yurius.game.model.Status;

import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class GameStorageImplTest {
	private GameStorage gameStorage;

	@Before
	public void setUp() throws Exception {
		gameStorage = new GameStorageImpl();
	}

	@Test
	public void testSaveAndRetrieve() throws Exception {
		// GIVEN
		GameState gameState = new GameState("id1", BoardState.createDefault(), Status.WAITING_FOR_SECOND_PLAYER, "");
		assertThat(gameStorage.retrieve("id1").isPresent(), is(false));

		// WHEN
		gameStorage.save(gameState);

		// THEN
		assertThat(gameStorage.retrieve("id1").get(), is(gameState));
	}

	@Test
	public void testFindWaitingGame_gameExists() throws Exception {
		// GIVEN
		GameState gameState1 = new GameState("id1", BoardState.createDefault(), Status.FIRST_PLAYER_TURN, "");
		GameState gameState2 = new GameState("id2", BoardState.createDefault(), Status.WAITING_FOR_SECOND_PLAYER, "");
		gameStorage.save(gameState1);
		gameStorage.save(gameState2);

		// WHEN
		Optional<GameState> waitingGame = gameStorage.findWaitingGame();

		// THEN
		assertThat(waitingGame.get(), is(gameState2));
	}

	@Test
	public void testFindWaitingGame_gameDoesNotExist() throws Exception {
		// WHEN
		Optional<GameState> waitingGame = gameStorage.findWaitingGame();

		// THEN
		assertThat(waitingGame.isPresent(), is(false));
	}

}