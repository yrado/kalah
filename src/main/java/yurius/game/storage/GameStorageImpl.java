package yurius.game.storage;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import yurius.game.model.GameState;
import yurius.game.model.Status;

public class GameStorageImpl implements GameStorage {
	private Map<String, GameState> games = new ConcurrentHashMap<>();

	@Override
	public void save(GameState gameState) {
		GameState copy = new GameState(gameState);
		games.put(copy.getGameId(), copy);
	}

	@Override
	public Optional<GameState> retrieve(String gameId) {
		GameState gameState = games.get(gameId);
		return gameState == null ? Optional.empty() : Optional.of(new GameState(gameState));
	}

	@Override
	public Optional<GameState> findWaitingGame() {
		return games.values().stream()
				.filter(p -> p.getStatus() == Status.WAITING_FOR_SECOND_PLAYER)
				.map(GameState::new)
				.findFirst();
	}

	/**
	 * This method is intended to be used in tests only
	 * */
	void clear()
	{
		games.clear();
	}
}
