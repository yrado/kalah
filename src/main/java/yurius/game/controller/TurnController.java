package yurius.game.controller;

import yurius.game.controller.exceptions.EmptyCellException;
import yurius.game.model.GameState;
import yurius.game.model.Player;
import yurius.game.model.Status;

public class TurnController {
    private final String gameId;
    private final Board board;

    TurnController(String gameId, Board board) {
        this.gameId = gameId;
        this.board = board;
    }

    public static TurnController create(GameState gameState) {
        return new TurnController(gameState.getGameId(), Board.create(gameState.getBoardState()));
    }

    private String getTurnMessage(Player player, String extraMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getText()).append(" player's turn");

        if (extraMessage != null)
            sb.append(": ").append(extraMessage);

        sb.append(".");
        return sb.toString();
    }

    public GameState takeTurn(Player player, int houseNumber) {
        try {
            if (board.isGameOver())
                return getGameStateGameOver();

            boolean finishedInPlayerStore = board.moveStones(player, houseNumber);

            if (board.isGameOver())
                return getGameStateGameOver();

            Player nextPlayer = finishedInPlayerStore ? player : player.getOther();
            return getGameState(
                    toNextPlayerStatus(nextPlayer),
                    getTurnMessage(nextPlayer, nextPlayer == player ? "one more turn" : null));
        } catch (EmptyCellException e) {
            return getGameState(
                    toNextPlayerStatus(player),
                    getTurnMessage(player, "cannot move stones from empty house. Please try again"));
        }
    }

    private GameState getGameState(Status newStatus, String message) {
        return new GameState(
                gameId,
                board.asBoardState(),
                newStatus,
                message);
    }

    private Status toNextPlayerStatus(Player player) {
        return player == Player.FIRST ? Status.FIRST_PLAYER_TURN : Status.SECOND_PLAYER_TURN;
    }

    private GameState getGameStateGameOver() {
        return getGameState(Status.GAME_OVER, getWinnerMessage());
    }

    private String getWinnerMessage() {
        int store1 = board.getStore(Player.FIRST);
        int store2 = board.getStore(Player.SECOND);

        if (store1 > store2)
            return "First player won!";
        if (store2 > store1)
            return "Second player won!";
        return "It's a draw game!";
    }

}
