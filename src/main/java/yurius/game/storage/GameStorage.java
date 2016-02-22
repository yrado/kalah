package yurius.game.storage;

import java.util.Optional;

import yurius.game.model.GameState;

public interface GameStorage {
    void save(GameState gameState);

    Optional<GameState> retrieve(String gameId);

    void clear();

    void update(GameState game);

    Optional<GameState> findWaitingGame();
}
