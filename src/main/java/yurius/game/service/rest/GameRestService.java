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

import yurius.game.service.rest.exceptions.GameDoesNotExistException;
import yurius.game.service.rest.exceptions.WrongUserTurnException;
import yurius.game.storage.GameStorageProvider;
import yurius.game.service.GameService;
import yurius.game.model.GamePlayer;
import yurius.game.model.GameState;
import yurius.game.model.Move;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public class GameRestService {

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String ping() {
        return "OK";
    }

    @GET
    @Path("/createOrJoin")
    public GamePlayer createOrJoinGame() {
        return getService().createOrJoin();
    }

    @GET
    @Path("/{gameId}")
    public GameState get(@PathParam("gameId") String gameId) {
        try {
            return getService().getGame(gameId);
        }
        catch (GameDoesNotExistException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        }
    }

    @POST
    @Path("/{gameId}/makeMove")
    public Response makeMove(
            @PathParam("gameId") String gameId,
            Move move
    ) {
        try {
            getService().makeMove(gameId, move.getPlayer(), move.getHouseNumber());
            return Response.status(Status.OK).build();
        } catch (GameDoesNotExistException e) {
            throw new WebApplicationException(Status.NOT_FOUND);
        } catch (WrongUserTurnException e) {
            throw new WebApplicationException(Status.BAD_REQUEST);
        }
    }

    private GameService getService() {
        return new GameService(GameStorageProvider.getInstance());
    }

}
