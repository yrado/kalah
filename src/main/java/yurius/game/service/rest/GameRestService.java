package yurius.game.service.rest;

import yurius.game.service.GameService;
import yurius.game.service.rest.dto.GamePlayer;
import yurius.game.service.rest.dto.GameState;
import yurius.game.service.rest.dto.Move;
import yurius.game.service.rest.exceptions.GameDoesNotExistException;
import yurius.game.service.rest.exceptions.WrongUserTurnException;
import yurius.game.storage.GameStorageProvider;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
        // TODO add exception mapper
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
        // TODO add exception mapper
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
