package yurius.game.storage;

import yurius.game.model.GameState;
import yurius.game.model.Status;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class GameStorageImpl implements GameStorage {
    private Map<String, GameState> games = new HashMap<>();

    @Override
    public void save(GameState gameState) {
        GameState copy = new GameState(gameState);
        games.put(copy.getGameId(), copy);
    }

    @Override
    // TODO add Optional
    public GameState retrieve(String gameId) {
        GameState gameState = games.get(gameId);
        return gameState == null ? null : new GameState(gameState);
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void update(GameState gameState) {
        save(gameState);
    }

    @Override
    public Optional<GameState> findWaitingGame() {
        return games.values().stream().filter(p -> p.getStatus() == Status.WAITING_FOR_SECOND_PLAYER).findFirst();
    }
}
