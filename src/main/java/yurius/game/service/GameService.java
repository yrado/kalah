package yurius.game.service;

import yurius.game.controller.TurnController;
import yurius.game.controller.TurnResult;
import yurius.game.model.GameState;
import yurius.game.model.Player;
import yurius.game.model.Status;
import yurius.game.service.rest.dto.GamePlayer;
import yurius.game.service.rest.exceptions.GameDoesNotExistException;
import yurius.game.service.rest.exceptions.WrongUserTurnException;
import yurius.game.storage.GameStorage;

import java.util.Optional;

public class GameService {
    private final GameStorage gameStorage;

    public GameService(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    public synchronized GamePlayer createOrJoin() {
        GameState game = getWaitingOrCreateNewGame();
        gameStorage.save(game);
        return new GamePlayer(game.getGameId(), getNextPlayer(game));
    }

    private Player getNextPlayer(GameState game) {
        return game.getStatus() == Status.WAITING_FOR_SECOND_PLAYER ? Player.FIRST : Player.SECOND;
    }

    private GameState getWaitingOrCreateNewGame() {
        Optional<GameState> waitingGame = gameStorage.findWaitingGame();

        if (!waitingGame.isPresent())
            return GameState.createWaitingGame();

        return GameState.withSecondPlayerJoined(waitingGame.get());
    }

    public yurius.game.service.rest.dto.GameState getGame(String gameId) throws GameDoesNotExistException {
        return new yurius.game.service.rest.dto.GameState(getGameInternal(gameId));
    }

    private GameState getGameInternal(String gameId) {
        Optional<GameState> gameState = gameStorage.retrieve(gameId);
        if (!gameState.isPresent())
            throw new GameDoesNotExistException();
        return gameState.get();
    }

    public void makeMove(String gameId, Player player, int houseNumber)
            throws GameDoesNotExistException, WrongUserTurnException {
        GameState gameState = getGameInternal(gameId);

        if (gameState.getStatus() == Status.GAME_OVER)
            return;

        if (wrongUserTakingTurn(player, gameState))
            throw new WrongUserTurnException();

        if (turnWhileWaitingForSecondUser(gameState))
            throw new WrongUserTurnException();

        TurnController turnController = TurnController.create(gameState.getBoardState());

        TurnResult turnResult = turnController.takeTurn(player, houseNumber);

        gameStorage.save(new GameState(
                gameState.getGameId(),
                turnController.getBoardState(),
                turnResult.getStatus().getStatus(),
                turnResult.getMessage()));
    }

    private boolean turnWhileWaitingForSecondUser(GameState game) {
        return game.getStatus() == Status.WAITING_FOR_SECOND_PLAYER;
    }

    private boolean wrongUserTakingTurn(Player player, GameState game) {
        return game.getStatus() == Status.FIRST_PLAYER_TURN && player == Player.SECOND ||
                game.getStatus() == Status.SECOND_PLAYER_TURN && player == Player.FIRST;
    }

}
