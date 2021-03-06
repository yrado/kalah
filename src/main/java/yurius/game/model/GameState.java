package yurius.game.model;


import java.util.UUID;

public class GameState {
    private final String gameId;
    private final BoardState boardState;
    private final Status status;
    private final String message;

    public GameState(String gameId, BoardState boardState, Status status, String message) {
        this.gameId = gameId;
        this.boardState = boardState;
        this.status = status;
        this.message = message;
    }

    public static GameState withSecondPlayerJoined(GameState gameState) {
        return new GameState(
                gameState.getGameId(),
                gameState.getBoardState(),
                Status.FIRST_PLAYER_TURN,
                "Second player joined. First player can take their turn.");
    }

    public static GameState createWaitingGame() {
        return new GameState(
                UUID.randomUUID().toString(),
                BoardState.createDefault(),
                Status.WAITING_FOR_SECOND_PLAYER,
                "Waiting for second player");
    }

    public Status getStatus() {
        return status;
    }

    public String getGameId() {
        return gameId;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameState gameState = (GameState) o;

        return gameId.equals(gameState.gameId)
                && boardState.equals(gameState.boardState)
                && status == gameState.status
                && message.equals(gameState.message);
    }

    @Override
    public int hashCode() {
        int result = gameId.hashCode();
        result = 31 * result + boardState.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }
}
