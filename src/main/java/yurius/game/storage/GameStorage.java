package yurius.game.storage;

import yurius.game.model.GameState;

import java.util.Optional;

public interface GameStorage {
    void save(GameState gameState);

    GameState retrieve(String gameId);

    void clear();

    void update(GameState game);

    Optional<GameState> findWaitingGame();
}
