package yurius.game.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import yurius.game.controller.BoardController;
import yurius.game.model.TurnResult;
import yurius.game.model.Player;
import yurius.game.service.rest.GameDoesNotExistException;
import yurius.game.service.rest.WrongUserTurnException;
import yurius.game.service.rest.dto.Board;
import yurius.game.service.rest.dto.GamePlayer;
import yurius.game.service.rest.dto.GameState;
import yurius.game.service.rest.dto.Status;

public class GameService
{

  private static GameService instance = new GameService();

  private Map<String, GameState> games = new HashMap<>();

  private GameState gameWaitingForPlayer;

  public static GameService getInstance()
  {
    return instance;
  }

  private GameService()
  {
  }

  public GamePlayer createOrJoin()
  {
    if (!waitingForOtherPlayer())
    {
      gameWaitingForPlayer = new GameState(
          UUID.randomUUID().toString(),
          Board.createDefault(),
          Status.WAITING_FOR_SECOND_PLAYER,
          "Waiting for second player");
      games.put(gameWaitingForPlayer.getGameId(), gameWaitingForPlayer);
      return new GamePlayer(gameWaitingForPlayer.getGameId(), Player.FIRST);
    }
    else
    {
      GameState game = gameWaitingForPlayer;
      gameWaitingForPlayer = null;
      game.setStatus(Status.FIRST_PLAYER_TURN);
      game.setMessage("Second player joined. First player can take their turn.");
      return new GamePlayer(game.getGameId(), Player.SECOND);
    }
  }

  private boolean waitingForOtherPlayer()
  {
    return gameWaitingForPlayer != null;
  }

  public GameState getGame(String gameId) throws GameDoesNotExistException
  {
    GameState gameState = games.get(gameId);
    if (gameState == null)
      throw new GameDoesNotExistException();
    return gameState;
  }

  public void clearState()
  {
    games.clear();
    gameWaitingForPlayer = null;
  }

  public void makeMove(String gameId, Player player, int houseNumber)
      throws GameDoesNotExistException, WrongUserTurnException
  {
    // TODO IR check status
    GameState game = getGame(gameId);

    if (game.getStatus() == Status.GAME_OVER)
      return;

    if (wrongUserTakingTurn(player, game))
      throw new WrongUserTurnException();

    if (turnWhileWaitingForSecondUser(game))
      throw new WrongUserTurnException();

    BoardController boardController = new BoardController(yurius.game.model.Board.create(game.getBoard()));

    TurnResult turnResult = boardController.takeTurn(player, houseNumber);

    Board board = Board.create(boardController.getBoard());

    game.setBoard(board);
    game.setStatus(turnResult.getStatus().getStatus());
    game.setMessage(turnResult.getMessage());
  }

  private String createMessage(TurnResult turnResult) {
    return null;
  }

  private boolean turnWhileWaitingForSecondUser(GameState game) {
    return game.getStatus() == Status.WAITING_FOR_SECOND_PLAYER;
  }

  private boolean wrongUserTakingTurn(Player player, GameState game) {
    return game.getStatus() == Status.FIRST_PLAYER_TURN && player == Player.SECOND ||
        game.getStatus() == Status.SECOND_PLAYER_TURN && player == Player.FIRST;
  }

}
