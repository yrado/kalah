package yurius.game.model;


public class GameState {
    private String gameId;
    private BoardState boardState;
    private Status status;
    private String message;

    public GameState() {
    }

    public GameState(String gameId, BoardState boardState, Status status, String message) {
        this.gameId = gameId;
        this.boardState = boardState;
        this.status = status;
        this.message = message;
    }

    public GameState(GameState gameState) {
        this(
                gameState.getGameId(),
                new BoardState(gameState.getBoardState()),
                gameState.getStatus(),
                gameState.getMessage()
        );
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public BoardState getBoardState() {
        return boardState;
    }

    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
