package yurius.game.storage;

import yurius.game.model.GameState;
import yurius.game.model.Status;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory implementation of the GameStorage interface.
 *
 * One of the other options is a database backed up storage.
 * */
public class GameStorageImpl implements GameStorage {
    private Map<String, GameState> games = new ConcurrentHashMap<>();

    @Override
    public void save(GameState gameState) {
        games.put(gameState.getGameId(), gameState);
    }

    @Override
    public Optional<GameState> retrieve(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }

    @Override
    public Optional<GameState> findWaitingGame() {
        return games.values().stream()
                .filter(p -> p.getStatus() == Status.WAITING_FOR_SECOND_PLAYER)
                .findFirst();
    }

    /**
     * This method is intended to be used in tests only
     */
    void clear() {
        games.clear();
    }
}
