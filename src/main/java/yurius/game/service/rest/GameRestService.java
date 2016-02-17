package yurius.game.service.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import yurius.game.service.GameService;
import yurius.game.service.rest.dto.GamePlayer;
import yurius.game.service.rest.dto.GameState;
import yurius.game.service.rest.dto.Move;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class GameRestService
{

  @GET
  @Path("ping")
  @Produces(MediaType.TEXT_PLAIN)
  public String ping()
  {
    return "OK";
  }

  @GET
  @Path("/createOrJoin")
  public GamePlayer createOrJoinGame()
  {
    return GameService.getInstance().createOrJoin();

    //new GameState("gameId", new Board(), Status.FIRST_PLAYER_TURN);
  }

  @GET
  @Path("/{gameId}")
  public GameState get(@PathParam("gameId") String gameId)
  {
    try
    {
      return GameService.getInstance().getGame(gameId);
    }
    // TODO IR move to mapper
    catch (GameDoesNotExistException e)
    {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }

  @POST
  @Path("/{gameId}/makeMove")
  public Response makeMove(
      @PathParam("gameId") String gameId,
      Move move
  )
  {
    try
    {
      GameService.getInstance().makeMove(gameId, move.getPlayer(), move.getHouseNumber());
      return Response.status(Status.OK).build();
    }
    // TODO IR move to mapper
    catch (GameDoesNotExistException e)
    {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
    catch (WrongUserTurnException e)
    {
      throw new WebApplicationException(Status.BAD_REQUEST);
    }
  }


}
