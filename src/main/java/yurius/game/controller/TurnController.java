package yurius.game.controller;

import yurius.game.controller.exceptions.EmptyCellException;
import yurius.game.model.BoardState;
import yurius.game.model.Player;

public class TurnController {
    private Board board;

    TurnController(Board board) {
        this.board = board;
    }

    public static TurnController create(BoardState boardState) {
        return new TurnController(Board.create(boardState));
    }

    public TurnResult takeTurn(Player player, int houseNumber) {
        try {
            if (board.allHousesEmpty())
                return getGameOverStatus();

            LastSeedPlacement lastSeedPlacement = board.moveStones(player, houseNumber);

            if (board.allHousesEmpty())
                return getGameOverStatus();

            Player nextPlayer = LastSeedPlacement.PLAYER_STORE == lastSeedPlacement ? player : player.getOther();
            return new TurnResult(
                    TurnStatus.get(nextPlayer),
                    nextPlayer == player ? "one more turn." : null);
        } catch (EmptyCellException e) {
            return new TurnResult(TurnStatus.get(player));
        }
    }

    private TurnResult getGameOverStatus() {
        return new TurnResult(TurnStatus.GAME_OVER, getWinnerMessage());
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

    public BoardState getBoardState() {
        return new BoardState(board.getPlayerPits(Player.FIRST), board.getPlayerPits(Player.SECOND));
    }
}
