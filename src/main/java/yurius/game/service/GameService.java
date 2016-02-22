package yurius.game.service;

import yurius.game.controller.TurnController;
import yurius.game.controller.TurnResult;
import yurius.game.model.*;
import yurius.game.service.rest.exceptions.GameDoesNotExistException;
import yurius.game.service.rest.exceptions.WrongUserTurnException;
import yurius.game.storage.GameStorage;

import java.util.Optional;
import java.util.UUID;

public class GameService {
    private final GameStorage gameStorage;

    public GameService(GameStorage gameStorage) {
        this.gameStorage = gameStorage;
    }

    public synchronized GamePlayer createOrJoin() {
        Optional<GameState> waitingGameOptional = gameStorage.findWaitingGame();
        if (!waitingGameOptional.isPresent())
            return new GamePlayer(createWaitingGame().getGameId(), Player.FIRST);
        else
            return new GamePlayer(updateWaitingGame(waitingGameOptional.get()).getGameId(), Player.SECOND);
    }

    private GameState updateWaitingGame(GameState gameState) {
        gameState.setStatus(Status.FIRST_PLAYER_TURN);
        gameState.setMessage("Second player joined. First player can take their turn.");
        gameStorage.save(gameState);
        return gameState;
    }

    private GameState createWaitingGame() {
        GameState gameWaitingForPlayer = new GameState(
                UUID.randomUUID().toString(),
                BoardState.createDefault(),
                Status.WAITING_FOR_SECOND_PLAYER,
                "Waiting for second player");
        gameStorage.save(gameWaitingForPlayer);
        return gameWaitingForPlayer;
    }

    public GameState getGame(String gameId) throws GameDoesNotExistException {
        Optional<GameState> gameState = gameStorage.retrieve(gameId);
        if (!gameState.isPresent())
            throw new GameDoesNotExistException();
        return gameState.get();
    }

    public void makeMove(String gameId, Player player, int houseNumber)
            throws GameDoesNotExistException, WrongUserTurnException {
        GameState gameState = getGame(gameId);

        if (gameState.getStatus() == Status.GAME_OVER)
            return;

        if (wrongUserTakingTurn(player, gameState))
            throw new WrongUserTurnException();

        if (turnWhileWaitingForSecondUser(gameState))
            throw new WrongUserTurnException();

        TurnController turnController = TurnController.create(gameState.getBoardState());

        TurnResult turnResult = turnController.takeTurn(player, houseNumber);

        BoardState boardState = turnController.getBoardState();

        gameState.setBoardState(boardState);
        gameState.setStatus(turnResult.getStatus().getStatus());
        gameState.setMessage(turnResult.getMessage());
        gameStorage.save(gameState);
    }

    private boolean turnWhileWaitingForSecondUser(GameState game) {
        return game.getStatus() == Status.WAITING_FOR_SECOND_PLAYER;
    }

    private boolean wrongUserTakingTurn(Player player, GameState game) {
        return game.getStatus() == Status.FIRST_PLAYER_TURN && player == Player.SECOND ||
                game.getStatus() == Status.SECOND_PLAYER_TURN && player == Player.FIRST;
    }

}
