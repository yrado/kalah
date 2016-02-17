package yurius.game.service.rest.dto;


public class GameState
{
  private String gameId;
  private Board  board;
  private Status status;
  private String message;

  public GameState()
  {
  }

  public GameState(String gameId, Board board, Status status, String message)
  {
    this.gameId = gameId;
    this.board = board;
    this.status = status;
    this.message = message;
  }

  public Status getStatus()
  {
    return status;
  }

  public void setStatus(Status status)
  {
    this.status = status;
  }

  public void setGameId(String gameId)
  {
    this.gameId = gameId;
  }

  public String getGameId()
  {
    return gameId;
  }

  public Board getBoard()
  {
    return board;
  }

  public void setBoard(Board board)
  {
    this.board = board;
  }

  public void setMessage(String message)
  {
    this.message = message;
  }

  public String getMessage()
  {
    return message;
  }
}
