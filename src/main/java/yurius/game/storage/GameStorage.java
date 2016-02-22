package yurius.game.storage;

import yurius.game.model.GameState;

import java.util.Optional;

/**
 * Interface that represents storage for the game state.
 *
 * */
public interface GameStorage {
    void save(GameState gameState);

    Optional<GameState> retrieve(String gameId);

    Optional<GameState> findWaitingGame();
}
