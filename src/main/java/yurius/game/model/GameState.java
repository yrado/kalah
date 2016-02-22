package yurius.game.model;


public class GameState {
	private String     gameId;
	private BoardState boardState;
	private Status     status;
	private String     message;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GameState gameState = (GameState) o;

		if (!gameId.equals(gameState.gameId)) return false;
		if (!boardState.equals(gameState.boardState)) return false;
		if (status != gameState.status) return false;
		return message.equals(gameState.message);
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
